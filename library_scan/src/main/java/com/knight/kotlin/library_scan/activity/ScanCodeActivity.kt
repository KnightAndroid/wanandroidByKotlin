package com.knight.kotlin.library_scan.activity

import android.content.Intent
import android.os.Build
import android.util.DisplayMetrics
import android.util.Size
import android.view.View
import android.view.WindowManager
import android.widget.RelativeLayout
import androidx.activity.viewModels
import androidx.camera.core.AspectRatio
import androidx.camera.core.Camera
import androidx.camera.core.CameraControl
import androidx.camera.core.CameraInfo
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageCapture
import androidx.camera.core.Preview
import androidx.camera.core.UseCase
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.core.content.ContextCompat
import androidx.core.view.WindowInsetsCompat
import com.google.common.util.concurrent.ListenableFuture
import com.knight.kotlin.library_base.activity.BaseActivity
import com.knight.kotlin.library_base.vm.EmptyViewModel
import com.knight.kotlin.library_scan.R
import com.knight.kotlin.library_scan.databinding.ScancodeActivityBinding
import com.knight.kotlin.library_scan.decode.ScanCodeAnalyzer
import com.knight.kotlin.library_scan.decode.ScanCodeConfig
import com.knight.kotlin.library_scan.entity.ScanCodeEntity
import com.knight.kotlin.library_scan.listener.OnScanCodeListener
import com.knight.kotlin.library_scan.listener.PreviewTouchListener
import com.knight.kotlin.library_scan.widget.BaseScanView
import com.knight.kotlin.library_scan.widget.ScanView
import java.util.concurrent.ExecutionException
import java.util.concurrent.ExecutorService
import java.util.concurrent.LinkedBlockingQueue
import java.util.concurrent.ThreadPoolExecutor
import java.util.concurrent.TimeUnit

/**
 * Author:Knight
 * Time:2022/2/14 15:31
 * Description:ScanCodeActivity
 */
class ScanCodeActivity:BaseActivity<ScancodeActivityBinding,EmptyViewModel>() {

    private val lensFacing = CameraSelector.LENS_FACING_BACK
    private var mImageCapture: ImageCapture? = null
    private var camera: Camera? = null
    private var preview: Preview? = null
    private var imageAnalyzer: ImageAnalysis? = null
    private var cameraControl: CameraControl? = null
    private var mCameraInfo: CameraInfo? = null
    private var cameraExecutor: ExecutorService? = null
    private var baseScanView: BaseScanView? = null
    private var mScanCodeEntity: ScanCodeEntity? = null

    private val RATIO_4_3_VALUE = 4.0 / 3.0
    private val RATIO_16_9_VALUE = 16.0 / 9.0
    private var isOpenLight = false


    override val mViewModel: EmptyViewModel by viewModels()
    override fun ScancodeActivityBinding.initView() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
           val controller =  getWindow().getInsetsController()
            controller?.hide(WindowInsetsCompat.Type.statusBars())
        } else {
            window.setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN
            ) //隐藏状态栏
        }
        initData()
        setOnClickListener(mBinding.scanIvClose,mBinding.scanIvFlash)
    }


    private fun initData() {
        mScanCodeEntity = intent.extras?.getParcelable(ScanCodeConfig.MODEL_KEY)
        //添加扫描布局
        val lp = RelativeLayout.LayoutParams(
            RelativeLayout.LayoutParams.MATCH_PARENT,
            RelativeLayout.LayoutParams.MATCH_PARENT
        )
        baseScanView = ScanView(this)
        baseScanView?.let {
            it.setLayoutParams(lp)
            mBinding.scanRoot.addView(baseScanView)
        }
        cameraExecutor = ThreadPoolExecutor(
            1, 1,
            0L, TimeUnit.MILLISECONDS,
            LinkedBlockingQueue()
        )
        //surface监听
        mBinding.scanPreview.post {
            //设置实例
            bindCameraUseCases()
        }
    }

    private fun bindCameraUseCases() {
        val displayMetrics = DisplayMetrics()
        mBinding.scanPreview.getDisplay().getRealMetrics(displayMetrics)
        val screenAspectRatio =
            aspectRatio(displayMetrics.widthPixels / 2, displayMetrics.heightPixels / 2)
        val width: Int = mBinding.scanPreview.getMeasuredWidth()
        val height: Int
        val size: Size
        height = if (screenAspectRatio == AspectRatio.RATIO_16_9) {
            (width * RATIO_16_9_VALUE).toInt()
        } else {
            (width * RATIO_4_3_VALUE).toInt()
        }
        size = Size(width, height)
        //获取旋转角度
        val rotation: Int = mBinding.scanPreview.getDisplay().getRotation()
        //绑定生命周期
        val cameraSelector = CameraSelector.Builder().requireLensFacing(lensFacing).build()
        val cameraProviderFuture: ListenableFuture<*> = ProcessCameraProvider.getInstance(this)
        cameraProviderFuture.addListener({
            var cameraProvider: ProcessCameraProvider? = null
            try {
                cameraProvider = cameraProviderFuture.get() as ProcessCameraProvider
            } catch (e: ExecutionException) {
                e.printStackTrace()
            } catch (e: InterruptedException) {
                e.printStackTrace()
            }
            mImageCapture = ImageCapture.Builder()
                .setFlashMode(ImageCapture.FLASH_MODE_AUTO)
                .setTargetAspectRatio(AspectRatio.RATIO_16_9)
                .setCaptureMode(ImageCapture.CAPTURE_MODE_MAXIMIZE_QUALITY)
                .build()
            //预览用例
            preview = Preview.Builder()
                .setTargetResolution(size)
                .setTargetRotation(rotation)
                .build()
            //图像分析用例
            imageAnalyzer = ImageAnalysis.Builder()
                .setTargetResolution(size)
                .setTargetRotation(rotation)
                .build()
            mScanCodeEntity?.let {
                ScanCodeAnalyzer(
                    this@ScanCodeActivity,
                    it,
                    object : OnScanCodeListener {
                        override fun onCodeResult(code: String?) {
                            val intent = Intent()
                            intent.putExtra(ScanCodeConfig.CODE_KEY, code)
                            setResult(RESULT_OK, intent)
                            finish()
                            overridePendingTransition(
                                com.knight.kotlin.library_base.R.anim.base_bottom_slient,
                                com.knight.kotlin.library_base.R.anim.base_bottom_out
                            )
                        }
                    })
            }?.let {
                cameraExecutor?.let { it1 ->
                    imageAnalyzer?.setAnalyzer(
                        it1,
                        it
                    )
                }
            }

            //必须在重新绑定用例之前取消之前绑定
            cameraProvider?.unbindAll()
            try {
                camera = cameraProvider?.bindToLifecycle(
                    this@ScanCodeActivity,
                    cameraSelector,
                    preview,
                    mImageCapture as UseCase?,
                    imageAnalyzer as UseCase?
                )
                preview?.setSurfaceProvider(mBinding.scanPreview.getSurfaceProvider())
                cameraControl = camera?.getCameraControl()
                mCameraInfo = camera?.getCameraInfo()
                bindTouchListener()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }, ContextCompat.getMainExecutor(this))

    }


    private fun bindTouchListener() {
        val zoomState = mCameraInfo?.zoomState
        val cameraXPreviewViewTouchListener = PreviewTouchListener(this)
        cameraXPreviewViewTouchListener.setCustomTouchListener(object : PreviewTouchListener.CustomTouchListener {
            override fun zoom(delta: Float) {
                if (zoomState?.value != null) {
                    val currentZoomRatio = zoomState.value?.zoomRatio
                    if (currentZoomRatio != null) {
                        cameraControl?.setZoomRatio(currentZoomRatio * delta)
                    }
                }
            }
        })
        mBinding.scanPreview.setOnTouchListener(cameraXPreviewViewTouchListener)
    }

    private fun aspectRatio(width: Int, height: Int): Int {
        val previewRatio = (Math.max(width, height) / Math.min(width, height)).toDouble()
        return if (Math.abs(previewRatio - RATIO_4_3_VALUE) <= Math.abs(previewRatio - RATIO_16_9_VALUE)) {
            AspectRatio.RATIO_4_3
        } else {
            AspectRatio.RATIO_16_9
        }
    }



    override fun initObserver() {

    }

    override fun initRequestData() {

    }

    private fun flashClick() {
        isOpenLight = !isOpenLight
        cameraControl?.enableTorch(isOpenLight)
        if (isOpenLight) {
            mBinding.scanIvFlash.setBackgroundResource(R.drawable.scan_icon_openflash)
        } else {
            mBinding.scanIvFlash.setBackgroundResource(R.drawable.scan_icon_closeflash)
        }
    }

    override fun onClick(v: View) {
        when(v) {
            mBinding.scanIvClose -> {
                finish()
                overridePendingTransition(com.knight.kotlin.library_base.R.anim.base_bottom_slient, com.knight.kotlin.library_base.R.anim.base_bottom_out)
            }
            mBinding.scanIvFlash -> {
                flashClick()
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        cameraExecutor?.shutdownNow()
        baseScanView?.cancelAnim()
    }

    override fun reLoadData() {

    }

    override fun setThemeColor(isDarkMode: Boolean) {

    }
}
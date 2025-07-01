package com.knight.kotlin.library_share

import android.graphics.Bitmap
import android.os.Bundle
import android.view.Gravity
import android.view.View
import androidx.lifecycle.lifecycleScope
import com.core.library_base.fragment.BaseDialogFragment
import com.core.library_base.ktx.setOnClick
import com.core.library_base.vm.EmptyViewModel

import com.knight.kotlin.library_share.databinding.ShareDialogBinding
import com.knight.kotlin.library_util.FileUtils
import com.knight.kotlin.library_util.ShareSdkUtils
import com.knight.kotlin.library_util.bitmap.BitmapUtils
import com.knight.kotlin.library_util.bitmap.saveToAlbum
import com.knight.kotlin.library_util.image.ImageLoader
import com.knight.kotlin.library_util.toast
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


/**
 * @author created by luguian
 * @organize
 * @Date 2024/8/22 15:49
 * @descript:分享弹窗
 */
class ShareDialog : BaseDialogFragment<ShareDialogBinding, EmptyViewModel>() {

    private lateinit var title: String
    private lateinit var desc: String
    private lateinit var imgUrl: String


    companion object {
        fun newInstance(title: String, desc: String, imgUrl: String): ShareDialog {
            val downLoadDialogFragment = ShareDialog()
            val args = Bundle()
            args.putString("title", title)
            args.putString("desc", desc)
            args.putString("imgUrl", imgUrl)
            downLoadDialogFragment.arguments = args
            return downLoadDialogFragment
        }
    }

    override fun getGravity(): Int = Gravity.CENTER
    override fun cancelOnTouchOutSide(): Boolean {
        return true
    }

    override fun initObserver() {

    }

    override fun initRequestData() {

    }

    override fun reLoadData() {

    }

    override fun ShareDialogBinding.initView() {
        title = arguments?.getString("title") ?: ""
        desc = arguments?.getString("desc") ?: ""
        imgUrl = arguments?.getString("imgUrl") ?: ""

        tvShareTitle.text = title
        tvShareDesc.text = desc
        ImageLoader.getInstance().loadStringPhoto(requireActivity(), imgUrl, ivPhoto)



        tvSaveLocal.setOnClick {
            // 异步处理
            lifecycleScope.launch(Dispatchers.IO) {

                val saveBitmap = generateShareBitmap(rlQrcode, ivPhoto, rlShareDesc)
                // 切换回主线程更新 UI
                withContext(Dispatchers.Main) {
                    saveBitmap?.run {
                        val uri = saveToAlbum(
                            requireActivity(),
                            FileUtils.getDateName("wanandroid") + ".jpg",
                            null
                        )
                        uri?.run {
                            toast(getString(R.string.share_save_photo_success))
                        } ?: run {
                            toast(getString(R.string.share_save_photo_failure))
                        }
                        // 及时释放 Bitmap
                        recycleSafely(this)
                    } ?: run {
                        toast(getString(R.string.share_save_photo_failure))
                    }
                }
            }

        }

        tvWechat.setOnClick {
            val saveBitmap = generateShareBitmap(rlQrcode, ivPhoto, rlShareDesc)
            saveBitmap?.let {
                ShareSdkUtils.shareWxImgToSession(it)
            }
        }

        tvTimeLine.setOnClick {
            val saveBitmap = generateShareBitmap(rlQrcode, ivPhoto, rlShareDesc)
            saveBitmap?.let {
                ShareSdkUtils.shareWxImgToTimeline(it)
            }
        }

    }


    fun generateShareBitmap(
        rlQrcode: View,
        ivPhoto: View,
        rlShareDesc: View
    ): Bitmap? {
        val bitmapQrcode = BitmapUtils.getViewBitmap(rlQrcode)
        val bitmapPhoto = BitmapUtils.getViewBitmap(ivPhoto)
        val bitmapDesc = BitmapUtils.getViewBitmap(rlShareDesc)

        val bitmapList = listOfNotNull(bitmapQrcode, bitmapPhoto, bitmapDesc)

        val puzzleBitmap = BitmapUtils.puzzleBitmap(bitmapList)
        val saveBitmap = puzzleBitmap?.let { BitmapUtils.compressImage(it) }

        // 回收中间过程使用的 Bitmap
        bitmapQrcode?.recycle()
        bitmapPhoto?.recycle()
        bitmapDesc?.recycle()
        if (puzzleBitmap != saveBitmap) {
            puzzleBitmap?.recycle()
        }

        return saveBitmap
    }

    fun recycleSafely(bitmap: Bitmap?) {
        if (bitmap != null && !bitmap.isRecycled) {
            bitmap.recycle()
        }
    }

}
package com.knight.kotlin.library_base.activity

import android.content.Context
import android.content.res.Configuration
import android.graphics.Color
import android.graphics.PixelFormat
import android.os.Bundle
import android.view.Gravity
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.FrameLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.viewbinding.ViewBinding
import com.alibaba.android.arouter.launcher.ARouter
import com.kingja.loadsir.callback.Callback
import com.kingja.loadsir.core.LoadService
import com.kingja.loadsir.core.LoadSir
import com.knight.kotlin.library_base.R
import com.knight.kotlin.library_base.annotation.EventBusRegister
import com.knight.kotlin.library_base.ktx.ClickAction
import com.knight.kotlin.library_base.ktx.subscribeData
import com.knight.kotlin.library_base.loadsir.EmptyCallBack
import com.knight.kotlin.library_base.loadsir.ErrorCallBack
import com.knight.kotlin.library_base.loadsir.LoadCallBack
import com.knight.kotlin.library_base.network.NetworkManager
import com.knight.kotlin.library_base.network.enums.NetworkState
import com.knight.kotlin.library_base.network.interfaces.NetworkMonitor
import com.knight.kotlin.library_base.util.BindingReflex
import com.knight.kotlin.library_base.util.CacheUtils
import com.knight.kotlin.library_base.util.ColorUtils
import com.knight.kotlin.library_base.util.EventBusUtils
import com.knight.kotlin.library_base.util.LanguageFontSizeUtils
import com.knight.kotlin.library_base.util.StatusBarUtils
import com.knight.kotlin.library_base.util.ViewRecreateHelper
import com.knight.kotlin.library_base.view.BaseView
import com.knight.kotlin.library_base.vm.BaseViewModel
import com.knight.kotlin.library_base.widget.loadcircleview.ProgressHud
import com.knight.kotlin.library_base.widget.swapeback.SwipeBackHelper

/**
 * Author:Knight
 * Time:2021/12/15 16:06
 * Description:BaseActivity
 */
abstract class BaseActivity<VB : ViewBinding,VM : BaseViewModel> : AppCompatActivity(),BaseView<VB>,ClickAction {

    protected val mBinding :VB by lazy( mode = LazyThreadSafetyMode.NONE ) {
        BindingReflex.reflexViewBinding(javaClass,layoutInflater)
    }

    abstract val mViewModel:VM

    /**
     * activity?????????????????????
     */
    private var mStatusHelper:ActivityRecreateHelper? = null

    /**
     *
     * ??????????????????
     */
    private lateinit var mLoadService: LoadService<Any>

    /**
     *
     * ????????????
     */
    private var fontScale:Float = 1.0f

    /**
     *
     * ????????????
     */
    protected var themeColor:Int = 0

    /**
     * ??????????????????
     *
     */
    protected var isDarkMode:Boolean = false

    /**
     * ?????????????????????
     *
     */
    protected var isEyeCare:Boolean = false

    /**
     *
     * ???????????????
     */
    private val mSwipeBackHelper:SwipeBackHelper by lazy{SwipeBackHelper(this)}

    val loadingDialog: ProgressHud by lazy{ ProgressHud(this)}

    /**
     *
     * ??????????????????View
     */
    private var tipView: View? = null
    private var mWindowManager: WindowManager? = null
    private var mLayoutParams: WindowManager.LayoutParams? = null

    //??????????????????
    private lateinit var mEyeFragmenLayout: FrameLayout


    /**
     * ??????????????????
     */
    protected abstract fun setThemeColor(isDarkMode:Boolean)

    override fun attachBaseContext(base: Context) {
        fontScale = CacheUtils.getSystemFontSize()
        super.attachBaseContext(LanguageFontSizeUtils.attachBaseContext(base, fontScale))
    }


    override fun onCreate(savedInstanceState:Bundle?) {
        super.onCreate(savedInstanceState)
        setTheme(getActivityTheme())
        setContentView(mBinding.root)
        StatusBarUtils.transparentStatusBar(this)
        //?????????????????????
        mStatusHelper?.onRestoreInstanceStatus(savedInstanceState)
        //ARouter ????????????
        ARouter.getInstance().inject(this)
        //??????EventBus
        if (javaClass.isAnnotationPresent(EventBusRegister::class.java)) EventBusUtils.register(this)
        themeColor = CacheUtils.getThemeColor()
        isDarkMode = CacheUtils.getNormalDark()
        isEyeCare = CacheUtils.getIsEyeCare()
        mBinding.initView()
        setThemeColor(isDarkMode)
        NetworkManager.getInstance().register(this)
        initEye(isEyeCare)
        initTipView()
        subscribeData()
        initObserver()
        initRequestData()

    }




    /**
     *
     * ????????????
     */
    protected fun requestLoading(view: View) {
        if (!::mLoadService.isInitialized) {
            mLoadService = LoadSir.getDefault().register(view, Callback.OnReloadListener {
                mLoadService.showCallback(LoadCallBack::class.java)
                reLoadData()
            })
        }

        mLoadService.showCallback(LoadCallBack::class.java)

    }

    /**
     * ???????????????????????????View
     */
    private fun initTipView() {
        tipView = getLayoutInflater().inflate(R.layout.base_layout_network_tip, null)
        mWindowManager = getSystemService(WINDOW_SERVICE) as WindowManager
        mLayoutParams = WindowManager.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT,
            WindowManager.LayoutParams.TYPE_APPLICATION,
            WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE or WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
            PixelFormat.TRANSLUCENT
        )
        mLayoutParams?.gravity = Gravity.TOP
        mLayoutParams?.x = 0
        mLayoutParams?.y = 0
    }


    /**
     *
     * ????????????
     */
    protected fun requestSuccess() {
        if (::mLoadService.isInitialized) {
            mLoadService.showSuccess()
        }
    }


    /**
     * ?????????????????????
     */
    protected fun requestFailure() {
        if (::mLoadService.isInitialized) {
            mLoadService.showCallback(ErrorCallBack::class.java)
        }
    }


    /**
     * ????????????????????????
     */
    protected fun requestEmptyData() {
        if (::mLoadService.isInitialized) {
            mLoadService.showCallback(EmptyCallBack::class.java)
        }
    }


    open fun getActivityTheme():Int {
        return R.style.base_AppTheme
    }



    @NetworkMonitor
    fun onNetWorkStateChange(networkState: NetworkState) {
        when (networkState) {
            NetworkState.NONE -> {
                if (tipView?.parent == null) {
                    mWindowManager?.addView(tipView, mLayoutParams)
                }
            }
            else ->{
                if (tipView != null && tipView?.parent != null) {
                    mWindowManager?.removeView(tipView)
                }
            }
//            NetworkState.NetworkState.WIFI
//            -> {
//                Toast.makeText(applicationContext, "WIFI??????", Toast.LENGTH_SHORT).show()
//            }
//            NetworkState.CELLULAR -> {
//                Toast.makeText(applicationContext, "????????????", Toast.LENGTH_SHORT).show()
//            }
        }
    }


    private fun initEye(isEyeCare:Boolean) {
        mEyeFragmenLayout = FrameLayout(this)
        openOrCloseEye(isEyeCare)
        val params = WindowManager.LayoutParams()
        params.flags = (WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL
                or WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
                or WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
        params.width = WindowManager.LayoutParams.MATCH_PARENT
        params.height = WindowManager.LayoutParams.MATCH_PARENT
        window.addContentView(mEyeFragmenLayout,params)

    }

    protected fun openOrCloseEye (status:Boolean) {
        if (status) {
            mEyeFragmenLayout?.let {
                it.setBackgroundColor(ColorUtils.getFilterColor(70))
            }
        } else {
            mEyeFragmenLayout?.let {
                it.setBackgroundColor(Color.TRANSPARENT)
            }
        }

    }

    override fun isRecreate(): Boolean = mStatusHelper?.isRecreate ?: false

    override fun onSaveInstanceState(outState: Bundle) {
        if (mStatusHelper == null) {
            //???????????????????????????????????????????????????
            mStatusHelper = ActivityRecreateHelper(outState)
        } else {
            mStatusHelper?.onSaveInstanceState(outState)
        }
        super.onSaveInstanceState(outState)
    }


    override fun dispatchTouchEvent(event: MotionEvent): Boolean {
        return if (mSwipeBackHelper != null && mSwipeBackHelper.dispatchTouchEvent(event)) {
            true
        } else super.dispatchTouchEvent(event)
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        if (mSwipeBackHelper != null) {
            mSwipeBackHelper.onTouchEvent(event)
        }
        return super.onTouchEvent(event)
    }


    /**
     *
     * activity?????????????????????
     */
    private class ActivityRecreateHelper(savedInstanceState:Bundle?=null) :
            ViewRecreateHelper(savedInstanceState)

    override fun applyOverrideConfiguration(overrideConfiguration: Configuration?) {
        // ??????androidX???????????????????????????????????????

        if (overrideConfiguration != null) {
            val uiMode = overrideConfiguration.uiMode
            overrideConfiguration.setTo(baseContext.resources.configuration)
            overrideConfiguration.uiMode = uiMode
        }
        super.applyOverrideConfiguration(overrideConfiguration)
    }

    override fun onDestroy() {
        if (javaClass.isAnnotationPresent(EventBusRegister::class.java)) EventBusUtils.unRegister(this)
        NetworkManager.getInstance().unregister(this)
        if (tipView != null && tipView?.parent != null) {
            mWindowManager?.removeView(tipView)
            tipView = null
        }
        super.onDestroy()
    }

}
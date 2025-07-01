package com.core.library_base.activity

import android.annotation.SuppressLint
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
import androidx.lifecycle.LiveData
import androidx.viewbinding.ViewBinding
import com.core.library_base.ktx.ClickAction
import com.kingja.loadsir.core.LoadService
import com.kingja.loadsir.core.LoadSir
import com.knight.kotlin.library_base.R

import com.core.library_base.loadsir.LoadCallBack
import com.core.library_base.util.BindingReflex
import com.core.library_base.util.CacheUtils
import com.core.library_base.util.LanguageFontSizeUtils
import com.core.library_base.view.BaseView
import com.core.library_base.vm.BaseViewModel
import com.core.library_base.widget.loadcircleview.ProgressHud

import com.core.library_base.widget.swapeback.SwipeBackHelper
import com.wyjson.router.GoRouter


/**
 * Author:Knight
 * Time:2021/12/15 16:06
 * Description:BaseActivity
 */
abstract class BaseActivity<VB : ViewBinding,VM : BaseViewModel> : AppCompatActivity(),
    BaseView<VB>, ClickAction {

    protected val mBinding :VB by lazy( mode = LazyThreadSafetyMode.NONE ) {
        BindingReflex.reflexViewBinding(javaClass,layoutInflater)
    }

    lateinit var  mViewModel:VM

    /**
     * activity页面重建帮助类
     */
    private var mStatusHelper:ActivityRecreateHelper? = null

    /**
     *
     * 全局加载视图
     */
    private lateinit var mLoadService: LoadService<Any>

    /**
     *
     * 字体大小
     */
    private var fontScale:Float = 1.0f

    /**
     *
     * 主题颜色
     */
    protected var themeColor:Int = 0

    /**
     * 是否黑暗模式
     *
     */
    protected var isDarkMode:Boolean = false

    /**
     * 是否是护眼模式
     *
     */
    protected var isEyeCare:Boolean = false

    /**
     *
     * 侧滑帮助类
     */
    private val mSwipeBackHelper: com.core.library_base.widget.swapeback.SwipeBackHelper by lazy{
        com.core.library_base.widget.swapeback.SwipeBackHelper(
            this
        )
    }

    val loadingDialog: ProgressHud by lazy{ ProgressHud(this)}

    /**
     *
     * 断网时弹出的View
     */
    private var tipView: View? = null
    private var mWindowManager: WindowManager? = null
    private var mLayoutParams: WindowManager.LayoutParams? = null

    //护眼模式遮罩
    private lateinit var mEyeFragmentLayout: FrameLayout


    /**
     * 设置主题颜色
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
        mViewModel = createViewModel()
        setStatusBar()
        //处理保存的状态
        mStatusHelper?.onRestoreInstanceStatus(savedInstanceState)
        //ARouter 依赖注入
        //GoRouter.getInstance().injectCheck(this)
        try {
            GoRouter.getInstance().inject(this)
        } catch (e: Exception) {
            e.printStackTrace()
        }

        //注册EventBus
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



    open fun setStatusBar() {
        StatusBarUtils.transparentStatusBar(this)
    }

    /**
     *
     * 默认加载
     */
    protected fun requestLoading(view: View) {
        if (!::mLoadService.isInitialized) {
            mLoadService = LoadSir.getDefault().register(view) {
                mLoadService.showCallback(com.core.library_base.loadsir.LoadCallBack::class.java)
                reLoadData()
            }
        }

        mLoadService.showCallback(com.core.library_base.loadsir.LoadCallBack::class.java)

    }

    /**
     * 初始化网络异常提示View
     */
    @SuppressLint("InflateParams")
    private fun initTipView() {
        tipView = layoutInflater.inflate(R.layout.base_layout_network_tip, null)
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
     * 成功回调
     */
    protected fun requestSuccess() {
        if (::mLoadService.isInitialized) {
            mLoadService.showSuccess()
        }
    }


    /**
     * 请求失败的回调
     */
    protected fun requestFailure() {
        if (::mLoadService.isInitialized) {
            mLoadService.showCallback(ErrorCallBack::class.java)
        }
    }


    /**
     * 空数据请求的回调
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
                CacheUtils.clearIp()
                Appconfig.IP = ""
                if (tipView?.parent == null) {
                    mWindowManager?.addView(tipView, mLayoutParams)
                }
            }
            else ->{
                if (tipView != null && tipView?.parent != null) {
                    mWindowManager?.removeView(tipView)
                }
            }
        }
    }


    private fun initEye(isEyeCare:Boolean) {
        mEyeFragmentLayout = FrameLayout(this)
        openOrCloseEye(isEyeCare)
        val params = WindowManager.LayoutParams()
        params.flags = (WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL
                or WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
                or WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
        params.width = WindowManager.LayoutParams.MATCH_PARENT
        params.height = WindowManager.LayoutParams.MATCH_PARENT
        window.addContentView(mEyeFragmentLayout,params)

    }

    protected fun openOrCloseEye (status:Boolean) {
        if (status) {
            mEyeFragmentLayout.setBackgroundColor(ColorUtils.getFilterColor(70))
        } else {
            mEyeFragmentLayout.setBackgroundColor(Color.TRANSPARENT)
        }

    }

    override fun isRecreate(): Boolean = mStatusHelper?.isRecreate ?: false


    //扩展liveData的observe函数
    protected fun <T : Any> LiveData<T>.observerKt(block: (T) -> Unit) {
        this.observe(this@BaseActivity) {
            block(it)
        }
    }
    override fun onSaveInstanceState(outState: Bundle) {
        if (mStatusHelper == null) {
            //仅当触发重建需要保存状态时创建对象
            mStatusHelper = ActivityRecreateHelper(outState)
        } else {
            mStatusHelper?.onSaveInstanceState(outState)
        }
        super.onSaveInstanceState(outState)
    }


    override fun dispatchTouchEvent(event: MotionEvent): Boolean {
        return if (mSwipeBackHelper.dispatchTouchEvent(event)) {
            true
        } else super.dispatchTouchEvent(event)
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        mSwipeBackHelper.onTouchEvent(event)
        return super.onTouchEvent(event)
    }


    /**
     *
     * activity重建帮助工具类
     */
    private class ActivityRecreateHelper(savedInstanceState:Bundle?=null) :
            ViewRecreateHelper(savedInstanceState)

    override fun applyOverrideConfiguration(overrideConfiguration: Configuration?) {
        // 兼容androidX在部分手机切换语言失败问题

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
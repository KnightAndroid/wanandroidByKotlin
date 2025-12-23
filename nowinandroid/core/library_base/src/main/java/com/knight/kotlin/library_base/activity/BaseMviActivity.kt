package com.knight.kotlin.library_base.activity

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
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.viewbinding.ViewBinding
import com.core.library_base.R
import com.core.library_base.annotation.EventBusRegister
import com.core.library_base.ktx.ClickAction
import com.core.library_base.loadsir.EmptyCallBack
import com.core.library_base.loadsir.ErrorCallBack
import com.core.library_base.network.NetworkManager
import com.core.library_base.network.enums.NetworkState
import com.core.library_base.network.interfaces.NetworkMonitor
import com.core.library_base.util.BindingReflex
import com.core.library_base.util.EventBusUtils
import com.core.library_base.util.ViewRecreateHelper
import com.core.library_base.view.BaseView
import com.core.library_base.vm.BaseMviViewModel
import com.core.library_base.vm.ViewEvent
import com.core.library_base.vm.ViewSideEffect
import com.core.library_base.vm.ViewState
import com.core.library_base.widget.loadcircleview.ProgressHud
import com.core.library_common.util.ColorUtils
import com.kingja.loadsir.core.LoadService
import com.kingja.loadsir.core.LoadSir
import com.knight.kotlin.library_base.ktx.createViewModel
import com.knight.kotlin.library_base.utils.StatusBarUtils
import com.knight.kotlin.library_common.util.CacheUtils
import com.knight.kotlin.library_common.util.LanguageFontSizeUtils
import com.wyjson.router.GoRouter
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

/**
 * 纯 MVI BaseActivity
 */
abstract class BaseMviActivity<
        VB : ViewBinding,
        VM : BaseMviViewModel<EVENT, STATE, EFFECT>,
        EVENT : ViewEvent,
        STATE : ViewState,
        EFFECT : ViewSideEffect
        > : AppCompatActivity(),
    BaseView<VB>,
    ClickAction {

    protected val mBinding: VB by lazy(LazyThreadSafetyMode.NONE) {
        BindingReflex.reflexViewBinding(javaClass, layoutInflater)
    }

    protected lateinit var mViewModel: VM

    /** 页面重建帮助类 */
    private var mStatusHelper: ActivityRecreateHelper? = null

    /** LoadSir */
    private lateinit var mLoadService: LoadService<Any>

    /** 字体缩放 */
    private var fontScale: Float = 1.0f

    /** 主题相关 */
    protected var themeColor: Int = 0
    protected var isDarkMode: Boolean = false
    protected var isEyeCare: Boolean = false

    /** 滑动返回 */
    private val mSwipeBackHelper by lazy {
        com.core.library_base.widget.swapeback.SwipeBackHelper(this)
    }

    /** loading */
    protected val loadingDialog by lazy { ProgressHud(this) }

    /** 网络提示 */
    private var tipView: View? = null
    private var mWindowManager: WindowManager? = null
    private var mLayoutParams: WindowManager.LayoutParams? = null

    /** 护眼模式遮罩 */
    private lateinit var mEyeFragmentLayout: FrameLayout

    //===================== 抽象方法（MVI 核心） =====================//

    /** 设置主题 */
    protected abstract fun setThemeColor(isDarkMode: Boolean)

    /** 发送初始化 Event */
    protected open fun initEvent() {}

    /** 渲染 State */
    protected abstract fun renderState(state: STATE)

    /** 处理 Effect */
    protected abstract fun handleEffect(effect: EFFECT)

    //===================== 生命周期 =====================//

    override fun attachBaseContext(base: Context) {
        fontScale = CacheUtils.getSystemFontSize()
        super.attachBaseContext(LanguageFontSizeUtils.attachBaseContext(base, fontScale))
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setTheme(getActivityTheme())
        setContentView(mBinding.root)

        mViewModel = createViewModel()

        setStatusBar()
        mStatusHelper?.onRestoreInstanceStatus(savedInstanceState)

        injectRouter()
        registerEventBusIfNeed()

        initSystemConfig()
        mBinding.initView()
        setThemeColor(isDarkMode)

        NetworkManager.getInstance().register(this)
        initEye(isEyeCare)
        initTipView()

        collectState()
        collectEffect()
        //发送事件
        initRequestData()
    }

    //===================== MVI 收集 =====================//
    private fun collectState() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                mViewModel.viewState.collectLatest {
                    renderState(it)
                }
            }
        }
    }

    private fun collectEffect() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                mViewModel.effect.collectLatest {
                    handleEffect(it)
                }
            }
        }
    }

    //===================== 基础功能 =====================//

    protected open fun setStatusBar() {
        StatusBarUtils.transparentStatusBar(this)
    }

    protected open fun getActivityTheme(): Int = R.style.base_AppTheme

    private fun injectRouter() {
        try {
            GoRouter.getInstance().inject(this)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun registerEventBusIfNeed() {
        if (javaClass.isAnnotationPresent(EventBusRegister::class.java)) {
            EventBusUtils.register(this)
        }
    }

    private fun initSystemConfig() {
        themeColor = CacheUtils.getThemeColor()
        isDarkMode = CacheUtils.getNormalDark()
        isEyeCare = CacheUtils.getIsEyeCare()
    }

    //===================== LoadSir =====================//

    protected fun requestLoading(view: View) {
        if (!::mLoadService.isInitialized) {
            mLoadService = LoadSir.getDefault().register(view) {
                mLoadService.showCallback(com.core.library_base.loadsir.LoadCallBack::class.java)
                reLoadData()
            }
        }
        mLoadService.showCallback(com.core.library_base.loadsir.LoadCallBack::class.java)
    }

    protected fun requestSuccess() {
        if (::mLoadService.isInitialized) mLoadService.showSuccess()
    }

    protected fun requestFailure() {
        if (::mLoadService.isInitialized) mLoadService.showCallback(ErrorCallBack::class.java)
    }

    protected fun requestEmptyData() {
        if (::mLoadService.isInitialized) mLoadService.showCallback(EmptyCallBack::class.java)
    }

    override fun reLoadData() {}

    //===================== 网络监听 =====================//

    @NetworkMonitor
    fun onNetWorkStateChange(networkState: NetworkState) {
        when (networkState) {
            NetworkState.NONE -> {
                CacheUtils.clearIp()
                if (tipView?.parent == null) {
                    mWindowManager?.addView(tipView, mLayoutParams)
                }
            }
            else -> {
                if (tipView?.parent != null) {
                    mWindowManager?.removeView(tipView)
                }
            }
        }
    }

    //===================== 护眼模式 =====================//

    private fun initEye(isEyeCare: Boolean) {
        mEyeFragmentLayout = FrameLayout(this)
        openOrCloseEye(isEyeCare)
        val params = WindowManager.LayoutParams()
        params.flags = (WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL
                or WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
                or WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
        params.width = WindowManager.LayoutParams.MATCH_PARENT
        params.height = WindowManager.LayoutParams.MATCH_PARENT
        window.addContentView(mEyeFragmentLayout, params)
    }

    protected fun openOrCloseEye(status: Boolean) {
        mEyeFragmentLayout.setBackgroundColor(
            if (status) ColorUtils.getFilterColor(70) else Color.TRANSPARENT
        )
    }

    //===================== 其它 =====================//

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
        ).apply {
            gravity = Gravity.TOP
        }
    }

    override fun dispatchTouchEvent(event: MotionEvent): Boolean {
        return if (mSwipeBackHelper.dispatchTouchEvent(event)) true
        else super.dispatchTouchEvent(event)
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        mSwipeBackHelper.onTouchEvent(event)
        return super.onTouchEvent(event)
    }

    override fun applyOverrideConfiguration(overrideConfiguration: Configuration?) {
        overrideConfiguration?.let {
            val uiMode = it.uiMode
            it.setTo(baseContext.resources.configuration)
            it.uiMode = uiMode
        }
        super.applyOverrideConfiguration(overrideConfiguration)
    }

    override fun onDestroy() {
        if (javaClass.isAnnotationPresent(EventBusRegister::class.java)) {
            EventBusUtils.unRegister(this)
        }
        NetworkManager.getInstance().unregister(this)
        if (tipView?.parent != null) {
            mWindowManager?.removeView(tipView)
            tipView = null
        }
        super.onDestroy()
    }

    override fun isRecreate(): Boolean = mStatusHelper?.isRecreate ?: false

    private class ActivityRecreateHelper(savedInstanceState: Bundle? = null) :
        ViewRecreateHelper(savedInstanceState)
}

package com.knight.kotlin.library_base.activity

import android.content.Context
import android.content.res.Configuration
import android.os.Bundle
import android.view.View
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
import com.knight.kotlin.library_base.network.AutoRegisterNetListener
import com.knight.kotlin.library_base.network.NetworkStateChangeListener
import com.knight.kotlin.library_base.util.BindingReflex
import com.knight.kotlin.library_base.util.CacheUtils
import com.knight.kotlin.library_base.util.EventBusUtils
import com.knight.kotlin.library_base.util.LanguageFontSizeUtils
import com.knight.kotlin.library_base.util.StatusBarUtils
import com.knight.kotlin.library_base.util.ViewRecreateHelper
import com.knight.kotlin.library_base.view.BaseView
import com.knight.kotlin.library_base.vm.BaseViewModel
import com.knight.kotlin.library_base.widget.loadcircleview.ProgressHud

/**
 * Author:Knight
 * Time:2021/12/15 16:06
 * Description:BaseActivity
 */
abstract class BaseActivity<VB : ViewBinding,VM : BaseViewModel> : AppCompatActivity(),BaseView<VB>,NetworkStateChangeListener,ClickAction {

    protected val mBinding :VB by lazy( mode = LazyThreadSafetyMode.NONE ) {
        BindingReflex.reflexViewBinding(javaClass,layoutInflater)
    }

    public abstract val mViewModel:VM

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

    val loadingDialog: ProgressHud by lazy{ ProgressHud(this)}



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
        StatusBarUtils.transparentStatusBar(this)
        //处理保存的状态
        mStatusHelper?.onRestoreInstanceStatus(savedInstanceState)
        //ARouter 依赖注入
        ARouter.getInstance().inject(this)
        //注册EventBus
        if (javaClass.isAnnotationPresent(EventBusRegister::class.java)) EventBusUtils.register(this)
        themeColor = CacheUtils.getThemeColor()
        isDarkMode = CacheUtils.getNormalDark()
        mBinding.initView()
        setThemeColor(isDarkMode)
        initNetworkListener()
        subscribeData()
        initObserver()
        initRequestData()

    }


    /**
     * 初始化网络状态监听
     * @return Unit
     *
     */
    private fun initNetworkListener() {
        lifecycle.addObserver(AutoRegisterNetListener(this))
    }

    /**
     * 网络类型更改回调
     * @param type Int 网络类型
     * @return Unit
     *
     */
    override fun networkTypeChange(type:Int) {

    }

    /**
     * 网络连接状态更改回调
     * @param isConnected Boolean 是否已连接
     * @return Unit
     *
     */
    override fun networkConnectChange(isConnected:Boolean) {


    }


    /**
     *
     * 默认加载
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



    override fun isRecreate(): Boolean = mStatusHelper?.isRecreate ?: false

    override fun onSaveInstanceState(outState: Bundle) {
        if (mStatusHelper == null) {
            //仅当触发重建需要保存状态时创建对象
            mStatusHelper = ActivityRecreateHelper(outState)
        } else {
            mStatusHelper?.onSaveInstanceState(outState)
        }
        super.onSaveInstanceState(outState)
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
        super.onDestroy()
    }

}
package com.knight.kotlin.library_base

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context
import com.kingja.loadsir.core.LoadSir
import com.knight.kotlin.library_base.app.LoadModuleProxy
import com.knight.kotlin.library_base.loadsir.EmptyCallBack
import com.knight.kotlin.library_base.loadsir.ErrorCallBack
import com.knight.kotlin.library_base.loadsir.LoadCallBack
import com.knight.kotlin.library_base.network.NetworkManager
import com.knight.kotlin.library_base.util.ActivityManagerUtils
import com.knight.kotlin.library_base.util.CacheUtils
import com.knight.kotlin.library_base.util.DarkModeUtils
import com.knight.kotlin.library_base.util.HookUtils
import com.tencent.bugly.crashreport.CrashReport
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.cancel
import kotlin.system.measureTimeMillis


/**
 * Author:Knight
 * Time:2021/12/15 16:23
 * Description:BaseApp
 */
open class BaseApp : Application() {

    /**
     *
     * 是否同意了隐私政策
     */
    private var userAgree = false

    /**
     * 协程
     */
    private val mCoroutineScope by lazy(mode = LazyThreadSafetyMode.NONE) { MainScope() }

    private val mLoadModuleProxy by lazy(mode = LazyThreadSafetyMode.NONE) { LoadModuleProxy() }

    companion object {
        @SuppressLint("StaticFieldLeak")
        lateinit var context: Context

        @SuppressLint("StaticFieldLeak")
        lateinit var application: BaseApp

    }


    override fun attachBaseContext(base: Context) {
        context = base
        application = this
        //初始化MMKV
        CacheUtils.init(base)
        userAgree = CacheUtils.getAgreeStatus()
        if (!userAgree) {
            try {
                HookUtils.attachContext()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
        mLoadModuleProxy.onAttachBaseContext(base)
        super.attachBaseContext(base)
    }


    override fun onCreate() {
        super.onCreate()
        //全局监听Activity 生命周期
        registerActivityLifecycleCallbacks(ActivityManagerUtils.getInstance())
        mLoadModuleProxy.onCreate(this)
        //策略初始化安全第三方依赖
        initSafeSdk()
        if (userAgree) {
            initDangrousSdk()
        }


    }


//    /**
//     *
//     * 初始化第三方依赖
//     */
//    private fun initDepends() {
//        //开启一个Default Coroutine 进行初始化不会立即使用的第三方
//        mCoroutineScope.launch (Dispatchers.Default) {
//            mLoadModuleProxy.initByBackTask()
//        }
//        //前台初始化
//        val allTimeMillis = measureTimeMillis {
//            val depends = mLoadModuleProxy.initFrontTask()
//            var dependInfo:String
//            depends.forEach {
//                val dependTimeMillis = measureTimeMillis { dependInfo = it() }
//            }
//        }
//        //这里可以统计初始化所需要时间
//    }

    /**
     * 初始化安全任务
     */
    private fun initSafeSdk() {
        initNetworkStateClient()
        initLoadSir()
        initDarkMode()
        val allTimeMillis = measureTimeMillis {
            val depends = mLoadModuleProxy.initSafeTask()
            var dependInfo: String
            depends.forEach {
                val dependTimeMillis = measureTimeMillis { dependInfo = it() }
            }
        }
    }



    /**
     *
     * 初始化网络状态监听器
     *
     */
    private fun initNetworkStateClient() :String {
        NetworkManager.getInstance().init(this)
        return "NetworkStateClient --->> init complete"
    }

    /**
     * 初始化状态页
     */
    private fun initLoadSir() {
       LoadSir.beginBuilder()
           .addCallback(ErrorCallBack())
           .addCallback(LoadCallBack())
           .addCallback(EmptyCallBack())
           .setDefaultCallback(LoadCallBack::class.java)
           .commit()
    }



    /**
     * 初始化危险sdk
     *
     */
    fun initDangrousSdk() {
        //初始化Provider
        HookUtils.initProvider(this)
        //初始化Bugly
        initBuglySdk()
        mLoadModuleProxy.initDangerousTask()
    }

    /**
     *
     * 注册Bugly
     */
    fun initBuglySdk() {
        CrashReport.initCrashReport(this,"99ea018e83",false)
    }



    /**
     *
     * 初始化深色模式还是普通模式
     */
    fun initDarkMode() {
        DarkModeUtils.darkNormal()
    }

    override fun onTerminate() {
        super.onTerminate()
        mLoadModuleProxy.onTerminate(this)
        mCoroutineScope.cancel()
    }

}
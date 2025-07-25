package com.knight.kotlin.library_base

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context
import android.util.Log
import androidx.work.Configuration
import androidx.work.ExistingWorkPolicy
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import com.core.library_base.app.LoadModuleProxy
import com.core.library_base.loadsir.EmptyCallBack
import com.core.library_base.loadsir.ErrorCallBack
import com.core.library_base.loadsir.LoadCallBack
import com.core.library_base.network.NetworkManager
import com.core.library_base.util.ActivityManagerUtils
import com.core.library_base.util.HookUtils
import com.core.library_common.util.ProcessUtil
import com.kingja.loadsir.core.LoadSir
import com.knight.kotlin.library_base.utils.DarkModeUtils
import com.knight.kotlin.library_common.util.BaiduSoDownloaderUtils
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.cancel
import kotlin.system.measureTimeMillis


/**
 * Author:Knight
 * Time:2021/12/15 16:23
 * Description:BaseApp
 */
open class BaseApp : Application(),Configuration.Provider  {

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
        //在主进程初始化
        if (ProcessUtil.isMainProcess(base)) {
            context = base
            application = this



            //初始化MMKV
            com.knight.kotlin.library_common.util.CacheUtils.init(base)
            userAgree = com.knight.kotlin.library_common.util.CacheUtils.getAgreeStatus()
            if (!userAgree) {
                try {
                    HookUtils.attachContext()
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
            mLoadModuleProxy.onAttachBaseContext(base)
        }

        super.attachBaseContext(base)
    }


    override fun onCreate() {
        super.onCreate()
        if (ProcessUtil.isMainProcess(this)) {
            if (!BaiduSoDownloaderUtils.isSoDownloaded(this)) {
                downloadBaiduSo()
            }
            //全局监听Activity 生命周期
            registerActivityLifecycleCallbacks(ActivityManagerUtils.getInstance())
            mLoadModuleProxy.onCreate(this)
            //策略初始化安全第三方依赖
            initSafeSdk()
            if (userAgree) {
                initDangrousSdk()
            }
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
    @Suppress("ASSIGNED_BUT_NEVER_ACCESSED_VARIABLE")
    private fun initSafeSdk() {
        initNetworkStateClient()
        initLoadSir()
        initDarkMode()
        measureTimeMillis {
            val depends = mLoadModuleProxy.initSafeTask()
            var dependInfo: String
            depends.forEach {
                measureTimeMillis { dependInfo = it() }
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
     *
     * 下载百度地图
     */
    private fun downloadBaiduSo() {
//
//        WorkManager.initialize(this, config)
        val work = OneTimeWorkRequestBuilder<com.knight.kotlin.library_common.workmanager.SoDownloadWorker>()
            .addTag("baidu_so_download_tag")
            .build()

        WorkManager.getInstance(this).enqueueUniqueWork(
            "baidu_so_download_work",
            ExistingWorkPolicy.KEEP,
            work
        )
    }



    /**
     * 初始化危险sdk
     *
     */
    fun initDangrousSdk() {
        //初始化Provider
        HookUtils.initProvider(this)
        mLoadModuleProxy.initDangerousTask()
    }


    /**
     *
     * 初始化深色模式还是普通模式
     */
    private fun initDarkMode() {
        DarkModeUtils.darkNormal()
    }


    override fun onTerminate() {
        super.onTerminate()
        mLoadModuleProxy.onTerminate(this)
        mCoroutineScope.cancel()
    }

    override val workManagerConfiguration: Configuration
        get() = Configuration.Builder()
            .setMinimumLoggingLevel(Log.INFO)
            .build()


}
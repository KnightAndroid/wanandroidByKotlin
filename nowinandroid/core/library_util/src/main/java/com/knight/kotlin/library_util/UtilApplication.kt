package com.knight.kotlin.library_util

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context
import com.core.library_common.app.ApplicationLifecycle
import com.core.library_common.ktx.getApplicationContext
import com.core.library_common.util.ProcessUtil
import com.google.auto.service.AutoService
import com.knight.kotlin.library_common.util.BaiduSoDownloaderUtils
import com.knight.kotlin.library_util.baidu.LocationUtils
import com.knight.kotlin.library_util.toast.ToastUtils.init
import com.tencent.bugly.crashreport.CrashReport
import com.wyjson.router.GoRouter


/**
 * Author:Knight
 * Time:2021/12/20 16:57
 * Description:UtilApplication
 * 工具类Application
 */
@AutoService(ApplicationLifecycle::class)
class UtilApplication: ApplicationLifecycle {


    companion object {
        @SuppressLint("StaticFieldLeak")
        lateinit var mUtilApplication : UtilApplication
    }

    override fun onAttachBaseContext(context: Context) {
        mUtilApplication = this
    }

    override fun onCreate(application: Application) {

    }

    override fun onTerminate(application: Application) {

    }

    override fun initSafeTask(): MutableList<() -> String> {

        val list = mutableListOf<() -> String>()
        //在主进程初始化
        if (ProcessUtil.isMainProcess(getApplicationContext())) {
            list.add{initARouter()}
            list.add{initToast()}
        }
        return list
    }

    /**
     *
     * 初始化危险类sdk
     * 初始化Bugly
     */
    override fun initDangerousTask() {
        val strategy = CrashReport.UserStrategy(getApplicationContext())
        strategy.deviceID = PhoneUtils.getDeviceUUID(getApplicationContext())
        strategy.deviceModel = PhoneUtils.getPhoneModel()
        strategy.appVersion = SystemUtils.getAppVersionName(getApplicationContext())
        strategy.appPackageName = getApplicationContext().packageName
        CrashReport.initCrashReport(getApplicationContext(),"99ea018e83",false,strategy)
        //同意ShareSdk分享
        //MobSDK.submitPolicyGrantResult(true)
        if (BaiduSoDownloaderUtils.isSoDownloaded(getApplicationContext())) {
            LocationUtils.init(getApplicationContext())
        }





    }






    /**
     *
     * 注册阿里ARoute 初始化
     */
    private fun initARouter():String {
        //正式环境需要关闭ARouter
        if (BuildConfig.TYPE != "MASTER") {
            GoRouter.openDebug()
//            ARouter.openLog() //打印日志
//            ARouter.openDebug() //开启调试模式(如果在InstantRun模式下运行，必须开启调试模式！线上版本需要关闭,否则有安全风险)
        }
        GoRouter.autoLoadRouteModule(getApplicationContext())
        return "GoRouter --->> init complete"
    }

    /**
     *
     * 初始化Toast
     */
    private fun initToast():String {
        //初始化Toast
        //setInterceptor(ToastInterceptor())
        init(getApplicationContext())
        return  "Toast -->> init Toast"
    }


}
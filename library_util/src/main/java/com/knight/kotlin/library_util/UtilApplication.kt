package com.knight.kotlin.library_util

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context
import com.alibaba.android.arouter.launcher.ARouter
import com.google.auto.service.AutoService
import com.knight.kotlin.library_base.BaseApp
import com.knight.kotlin.library_base.BuildConfig
import com.knight.kotlin.library_base.app.ApplicationLifecycle
import com.knight.kotlin.library_base.network.NetworkStateClient
import com.knight.kotlin.library_base.util.ProcessUtil
import com.knight.kotlin.library_util.toast.ToastInterceptor
import com.knight.kotlin.library_util.toast.ToastUtils.init
import com.knight.kotlin.library_util.toast.ToastUtils.setInterceptor


/**
 * Author:Knight
 * Time:2021/12/20 16:57
 * Description:UtilApplication
 * 工具类Application
 */
@AutoService(ApplicationLifecycle::class)
class UtilApplication:ApplicationLifecycle {


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
        if (ProcessUtil.isMainProcess(BaseApp.context)) {
            list.add{initNetworkStateClient()}
            list.add{initARouter()}
            list.add{initToast()}
        }
        return list
    }

    override fun initDangerousTask() {
    }





    /**
     *
     * 初始化网络状态监听器
     *
     */
    private fun initNetworkStateClient() :String {
        NetworkStateClient.register()
        return "NetworkStateClient --->> init complete"
    }
    /**
     *
     * 注册阿里ARoute 初始化
     */
    private fun initARouter():String {
        //正式环境需要关闭ARouter
        if (BuildConfig.TYPE != "MASTER") {
            ARouter.openLog() //打印日志
            ARouter.openDebug() //开启调试模式(如果在InstantRun模式下运行，必须开启调试模式！线上版本需要关闭,否则有安全风险)
        }
        ARouter.init(BaseApp.application)
        return "ARouter --->> init complete"
    }

    /**
     *
     * 初始化Toast
     */
    private fun initToast():String {
        //初始化Toast
        //初始化Toast
        setInterceptor(ToastInterceptor())
        init(BaseApp.application)
        return  "Toast -->> init Toast"
    }


}
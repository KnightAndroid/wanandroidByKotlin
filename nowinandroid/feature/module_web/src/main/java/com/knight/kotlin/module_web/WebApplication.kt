package com.knight.kotlin.module_web

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context
import com.core.library_common.app.ApplicationLifecycle
import com.core.library_common.util.ProcessUtil
import com.google.auto.service.AutoService
import com.knight.kotlin.library_base.BaseApp
import com.peakmain.webview.H5Utils
import com.peakmain.webview.PkWebViewInit
import com.peakmain.webview.annotation.CacheMode
import com.peakmain.webview.sealed.LoadingWebViewState


/**
 * Author:Knight
 * Time:2022/2/21 18:06
 * Description:WebApplication
 */
@AutoService(ApplicationLifecycle::class)
class WebApplication: ApplicationLifecycle {
    companion object {
        @SuppressLint("StaticFieldLeak")
        lateinit var mWebApplication: WebApplication
    }

    override fun onAttachBaseContext(context: Context) {
        mWebApplication = this
    }

    override fun onCreate(application: Application) {

    }

    override fun onTerminate(application: Application) {

    }

    override fun initSafeTask(): MutableList<() -> String> {
        val list = mutableListOf<() -> String>()

        if (ProcessUtil.isMainProcess(BaseApp.context)){
            list.add{initWeb()}
        }



        return list
    }

    override fun initDangerousTask() {

    }



    fun initWeb() :String{
        PkWebViewInit.Builder(BaseApp.application)
            //.setLoadingView(ReplaceLoadingConfigImpl())
            //设置全局拦截url回调
            //.setHandleUrlParamsCallback(HandlerUrlParamsImpl())
            .setLoadingWebViewState(LoadingWebViewState.HorizontalProgressBarLoadingStyle)
            //.registerEntities(OnlineServiceHandle::class.java, PageActionHandle::class.java)
            .setUserAgent("Android")
            .setNoCacheUrl(arrayOf("signIn/signIn"))
            .build()
        H5Utils().setWebViewCacheMode(CacheMode.LOAD_DEFAULT)
        return "Web --->> init complete"
    }
}
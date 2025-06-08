package com.knight.kotlin.library_permiss

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context
import com.google.auto.service.AutoService
import com.knight.kotlin.library_base.BaseApp
import com.knight.kotlin.library_base.app.ApplicationLifecycle
import com.knight.kotlin.library_base.util.ProcessUtil
import com.knight.kotlin.library_permiss.listener.OnPermissionInterceptor
import com.knight.kotlin.library_permiss.listener.OnPermissionPageCallback
import com.knight.kotlin.library_util.BuildConfig
import com.knight.kotlin.library_util.UtilApplication
import com.wyjson.router.GoRouter

/**
 * @Description
 * @Author knight
 * @Time 2025/6/8 23:18
 * 
 */
@AutoService(ApplicationLifecycle::class)
class PermissionApplication :ApplicationLifecycle{

    companion object {
        @SuppressLint("StaticFieldLeak")
        lateinit var mPermissionApplication : PermissionApplication
    }
    override fun onAttachBaseContext(context: Context) {
        mPermissionApplication = this
    }

    override fun onCreate(application: Application) {

    }

    override fun onTerminate(application: Application) {

    }

    override fun initSafeTask(): MutableList<() -> String> {
        val list = mutableListOf<() -> String>()
        //在主进程初始化
        if (ProcessUtil.isMainProcess(BaseApp.context)) {
           list.add{initPermission()}
        }
        return list
    }

    override fun initDangerousTask() {

    }

    /**
     *
     * 注册阿里ARoute 初始化
     */
    private fun initPermission():String {
       XXPermissions.setPermissionInterceptor(PermissionInterceptor())
        return "Permission --->> init complete"
    }
}
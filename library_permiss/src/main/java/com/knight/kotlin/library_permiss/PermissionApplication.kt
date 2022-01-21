package com.knight.kotlin.library_permiss

import android.app.Application
import android.content.Context
import com.google.auto.service.AutoService
import com.knight.kotlin.library_base.BaseApp
import com.knight.kotlin.library_base.app.ApplicationLifecycle
import com.knight.kotlin.library_base.util.ProcessUtil

/**
 * Author:Knight
 * Time:2022/1/21 17:23
 * Description:PermissionApplication
 */
@AutoService(ApplicationLifecycle::class)
class PermissionApplication: ApplicationLifecycle {

    override fun onAttachBaseContext(context: Context) {

    }

    override fun onCreate(application: Application) {

    }

    override fun onTerminate(application: Application) {

    }

    override fun initSafeTask(): MutableList<() -> String> {
        val list = mutableListOf<() -> String>()
        if (ProcessUtil.isMainProcess(BaseApp.context)){
            list.add{initPermissionInterceptor()}
        }
        return list
    }

    override fun initDangerousTask() {

    }



    /**
     * 初始化网络状态监听器
     */
    private fun initPermissionInterceptor() :String {
        XXPermissions.setInterceptor(PermissionInterceptor())
        return "Permission --->> init complete"
    }





}
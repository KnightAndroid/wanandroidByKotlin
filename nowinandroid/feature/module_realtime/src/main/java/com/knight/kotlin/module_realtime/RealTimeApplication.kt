package com.knight.kotlin.module_realtime

import android.app.Application
import android.content.Context
import com.core.library_common.app.ApplicationLifecycle
import com.google.auto.service.AutoService


/**
 * @author created by luguian
 * @organize
 * @Date 2025/3/11 15:13
 * @descript:百度热搜
 */
@AutoService(ApplicationLifecycle::class)
class RealTimeApplication : ApplicationLifecycle {
    override fun onAttachBaseContext(context: Context) {}

    override fun onCreate(application: Application) {}

    override fun onTerminate(application: Application) {}
    override fun initSafeTask(): MutableList<() -> String>  = mutableListOf()

    override fun initDangerousTask() {

    }

}
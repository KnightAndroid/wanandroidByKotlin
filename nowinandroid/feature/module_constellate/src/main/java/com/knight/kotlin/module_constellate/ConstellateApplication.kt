package com.knight.kotlin.module_constellate

import android.app.Application
import android.content.Context
import com.google.auto.service.AutoService
import com.knight.kotlin.library_base.app.ApplicationLifecycle


/**
 * @author created by luguian
 * @organize
 * @Date 2025/6/18 14:47
 * @descript:星座application
 */
@AutoService(ApplicationLifecycle::class)
class ConstellateApplication:ApplicationLifecycle {
    override fun onAttachBaseContext(context: Context) {}

    override fun onCreate(application: Application) {}

    override fun onTerminate(application: Application) {}
    override fun initSafeTask(): MutableList<() -> String>  = mutableListOf()

    override fun initDangerousTask() {

    }
}
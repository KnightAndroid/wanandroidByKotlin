package com.knight.kotlin.module_constellate

import android.app.Application
import android.content.Context
import com.core.library_common.app.ApplicationLifecycle
import com.google.auto.service.AutoService


/**
 * @author created by luguian
 * @organize
 * @Date 2025/6/18 14:47
 * @descript:星座application
 */
@AutoService(ApplicationLifecycle::class)
class ConstellateApplication: ApplicationLifecycle {
    override fun onAttachBaseContext(context: Context) {}

    override fun onCreate(application: Application) {}

    override fun onTerminate(application: Application) {}
    override fun initSafeTask(): MutableList<() -> String>  = mutableListOf()

    override fun initDangerousTask() {

    }
}
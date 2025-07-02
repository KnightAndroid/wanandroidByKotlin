package com.knight.kotlin.module_navigate

import android.app.Application
import android.content.Context
import com.core.library_base.app.ApplicationLifecycle
import com.google.auto.service.AutoService

/**
 * Author:Knight
 * Time:2021/12/23 17:42
 * Description:NavigateApplication
 */
@AutoService(ApplicationLifecycle::class)
class NavigateApplication : ApplicationLifecycle {
    override fun onAttachBaseContext(context: Context) {
    }

    override fun onCreate(application: Application) {
    }

    override fun onTerminate(application: Application) {
    }

    override fun initSafeTask():  MutableList<() -> String> = mutableListOf()

    override fun initDangerousTask() {
    }

}
package com.knight.kotlin.module_home

import android.app.Application
import android.content.Context
import com.core.library_common.app.ApplicationLifecycle
import com.google.auto.service.AutoService

/**
 * Author:Knight
 * Time:2021/12/23 15:02
 * Description:HomeApplication
 * Home组件的伪Application
 */

@AutoService(ApplicationLifecycle::class)
class HomeApplication : ApplicationLifecycle {
    override fun onAttachBaseContext(context: Context) {}

    override fun onCreate(application: Application) {}

    override fun onTerminate(application: Application) {}

    override fun initSafeTask(): MutableList<() -> String>  = mutableListOf()

    override fun initDangerousTask() {

    }

}
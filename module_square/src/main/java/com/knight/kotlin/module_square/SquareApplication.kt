package com.knight.kotlin.module_square

import android.app.Application
import android.content.Context
import com.google.auto.service.AutoService
import com.knight.kotlin.library_base.app.ApplicationLifecycle

/**
 * Author:Knight
 * Time:2021/12/23 16:43
 * Description:SquareApplication
 */
@AutoService(ApplicationLifecycle::class)
class SquareApplication : ApplicationLifecycle {
    override fun onAttachBaseContext(context: Context) {

    }

    override fun onCreate(application: Application) {

    }

    override fun onTerminate(application: Application) {

    }

    override fun initFrontTask(): MutableList<() -> String> = mutableListOf()

    override fun initByBackTask() {
    }
}
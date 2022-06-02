package com.knight.kotlin.module_utils

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context
import com.google.auto.service.AutoService
import com.knight.kotlin.library_base.app.ApplicationLifecycle

/**
 * Author:Knight
 * Time:2022/6/2 14:22
 * Description:UtilsApplication
 */
@AutoService(ApplicationLifecycle::class)
class UtilsApplication : ApplicationLifecycle {
    companion object {
        @SuppressLint("StaticFieldLeak")
        lateinit var mUtilsApplication: UtilsApplication
    }

    override fun onAttachBaseContext(context: Context) {
        mUtilsApplication = this
    }

    override fun onCreate(application: Application) {

    }

    override fun onTerminate(application: Application) {

    }

    override fun initSafeTask(): MutableList<() -> String> {
        val list = mutableListOf<() -> String>()
        return list
    }

    override fun initDangerousTask() {

    }
}
package com.knight.kotlin.module_square

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context
import com.core.library_base.app.ApplicationLifecycle
import com.google.auto.service.AutoService


/**
 * Author:Knight
 * Time:2022/4/28 15:45
 * Description:SquareApplication
 */
@AutoService(ApplicationLifecycle::class)
class SquareApplication : ApplicationLifecycle {
    companion object {
        @SuppressLint("StaticFieldLeak")
        lateinit var mSquareApplication: SquareApplication
    }

    override fun onAttachBaseContext(context: Context) {
        mSquareApplication = this
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
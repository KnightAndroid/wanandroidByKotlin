package com.knight.kotlin.module_welcome

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context
import com.google.auto.service.AutoService
import com.knight.kotlin.library_base.app.ApplicationLifecycle

/**
 * Author:Knight
 * Time:2021/12/21 11:42
 * Description:WelcomeApp
 */
@AutoService(ApplicationLifecycle::class)
class WelcomeApplication:ApplicationLifecycle {

    companion object {
        @SuppressLint("StaticFieldLeak")
        lateinit var mWelcomeApplication: WelcomeApplication
    }

    override fun onAttachBaseContext(context: Context) {
        mWelcomeApplication = this
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
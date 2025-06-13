package com.knight.kotlin.module_eye_daily

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context
import com.google.auto.service.AutoService
import com.knight.kotlin.library_base.app.ApplicationLifecycle

/**
 * Author:Knight
 * Time:2024/4/28 17:09
 * Description:EyeDailyApplication
 */
@AutoService(ApplicationLifecycle::class)
class EyeDailyApplication : ApplicationLifecycle {

    companion object {
        @SuppressLint("staticFieldLeak")
        lateinit var mEyeDailyApplication : EyeDailyApplication
    }



    override fun onAttachBaseContext(context: Context) {
        mEyeDailyApplication = this
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
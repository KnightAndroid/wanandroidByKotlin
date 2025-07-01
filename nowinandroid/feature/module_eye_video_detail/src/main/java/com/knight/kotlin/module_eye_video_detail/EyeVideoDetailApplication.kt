package com.knight.kotlin.module_eye_video_detail

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context
import com.core.library_base.app.ApplicationLifecycle
import com.google.auto.service.AutoService

@AutoService(ApplicationLifecycle::class)
class EyeVideoDetailApplication : ApplicationLifecycle {

    companion object {
        @SuppressLint("staticFieldLeak")
        lateinit var mEyeVideoDetailApplication: EyeVideoDetailApplication
    }


    override fun onAttachBaseContext(context: Context) {
        mEyeVideoDetailApplication = this
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
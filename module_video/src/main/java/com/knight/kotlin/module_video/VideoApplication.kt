package com.knight.kotlin.module_video

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context
import com.google.auto.service.AutoService
import com.knight.kotlin.library_base.app.ApplicationLifecycle

/**
 * Author:Knight
 * Time:2024/2/26 10:28
 * Description:VideoApplication
 */
@AutoService(ApplicationLifecycle::class)
class VideoApplication : ApplicationLifecycle{


    companion object {
        @SuppressLint("StaticFieldLeak")
        lateinit var mVideoApplication : VideoApplication
    }

    override fun onAttachBaseContext(context: Context) {
        mVideoApplication = this
    }

    override fun onCreate(application: Application) {

    }

    override fun onTerminate(application: Application) {

    }

    override fun initSafeTask(): MutableList<() -> String> {
        val list = mutableListOf<()-> String>()
        return list
    }

    override fun initDangerousTask() {

    }
}
package com.knight.kotlin.module_video

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context
import com.core.library_common.app.ApplicationLifecycle
import com.core.library_common.ktx.getApplicationContext
import com.google.auto.service.AutoService
import com.knight.kotlin.module_video.utils.precache.PreloadManager


/**
 * Author:Knight
 * Time:2024/2/26 10:28
 * Description:VideoApplication
 */
@AutoService(ApplicationLifecycle::class)
class VideoApplication : ApplicationLifecycle {


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
        list.add{initVideoCache()}
        return list
    }

    override fun initDangerousTask() {

    }

    fun initVideoCache() :String {
        getApplicationContext()?.let {
            PreloadManager(it)
        }
        return  "VideoCache -->> init VideoCache"
    }
}
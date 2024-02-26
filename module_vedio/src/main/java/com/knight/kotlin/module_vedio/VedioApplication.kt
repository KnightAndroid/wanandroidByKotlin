package com.knight.kotlin.module_vedio

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context
import com.google.auto.service.AutoService
import com.knight.kotlin.library_base.app.ApplicationLifecycle

/**
 * Author:Knight
 * Time:2024/2/26 10:28
 * Description:VedioApplication
 */
@AutoService(ApplicationLifecycle::class)
class VedioApplication : ApplicationLifecycle{


    companion object {
        @SuppressLint("StaticFieldLeak")
        lateinit var mVedioApplication : VedioApplication
    }

    override fun onAttachBaseContext(context: Context) {
        mVedioApplication = this
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
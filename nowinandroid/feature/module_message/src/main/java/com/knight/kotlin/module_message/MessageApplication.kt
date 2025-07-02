package com.knight.kotlin.module_message

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context
import com.core.library_base.app.ApplicationLifecycle
import com.google.auto.service.AutoService

/**
 * Author:Knight
 * Time:2023/5/16 10:41
 * Description:MessageApplication
 */

@AutoService(ApplicationLifecycle::class)
class MessageApplication : ApplicationLifecycle {
    companion object {
        @SuppressLint("StaticFieldLeak")
        lateinit var mMessageApplication: MessageApplication
    }



    override fun onAttachBaseContext(context: Context) {
        mMessageApplication = this
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
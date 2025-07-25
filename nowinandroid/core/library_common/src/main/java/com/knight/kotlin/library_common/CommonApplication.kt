package com.knight.kotlin.library_common

import android.app.Application
import android.content.Context
import com.core.library_common.app.ApplicationLifecycle
import com.google.auto.service.AutoService

/**
 * Author:Knight
 * Time:2021/12/24 14:50
 * Description:CommonApplication
 */
@AutoService(ApplicationLifecycle::class)
class CommonApplication: ApplicationLifecycle {


    override fun onAttachBaseContext(context: Context) {

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
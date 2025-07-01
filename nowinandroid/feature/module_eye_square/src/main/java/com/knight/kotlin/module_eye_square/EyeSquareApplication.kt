package com.knight.kotlin.module_eye_square

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context
import com.core.library_base.app.ApplicationLifecycle
import com.google.auto.service.AutoService


/**
 * @author created by luguian
 * @organize
 * @Date 2025/2/11 11:23
 * @descript:开眼社区/广场应用类
 */
@AutoService(ApplicationLifecycle::class)
class EyeSquareApplication : ApplicationLifecycle {

    companion object {
        @SuppressLint("staticFieldLeak")
        lateinit var mEyeSquareApplication : EyeSquareApplication
    }



    override fun onAttachBaseContext(context: Context) {
        mEyeSquareApplication = this
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
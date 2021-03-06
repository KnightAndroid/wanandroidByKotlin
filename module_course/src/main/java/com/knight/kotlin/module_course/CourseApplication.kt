package com.knight.kotlin.module_course

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context
import com.google.auto.service.AutoService
import com.knight.kotlin.library_base.app.ApplicationLifecycle

/**
 * Author:Knight
 * Time:2022/6/2 15:50
 * Description:CourseApplication
 */
@AutoService(ApplicationLifecycle::class)
class CourseApplication : ApplicationLifecycle {
    companion object {
        @SuppressLint("StaticFieldLeak")
        lateinit var mCourseApplication: CourseApplication
    }

    override fun onAttachBaseContext(context: Context) {
        mCourseApplication = this
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
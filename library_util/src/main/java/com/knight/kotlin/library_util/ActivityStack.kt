package com.knight.kotlin.library_util

import android.app.Activity
import android.app.Application
import android.os.Bundle

/**
 * Author:Knight
 * Time:2021/12/20 14:18
 * Description:ActivityStack
 */
class ActivityStack :Application.ActivityLifecycleCallbacks {

    companion object {

        /**
         * 前台Activity 对象
         *
         */
        private var mForegroundActivity :Activity?=null
        fun register(application: Application):ActivityStack {
            val lifecycle = ActivityStack()
            application.registerActivityLifecycleCallbacks(lifecycle)
            return lifecycle

        }

        fun getForegroundActivity() :Activity? {
            return mForegroundActivity
        }
    }





    override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {
    }

    override fun onActivityStarted(activity: Activity) {
    }

    override fun onActivityResumed(activity: Activity) {
        mForegroundActivity = activity
    }

    override fun onActivityPaused(activity: Activity) {
        if (mForegroundActivity != activity) {
            return
        }
        mForegroundActivity = null
    }

    override fun onActivityStopped(activity: Activity) {

    }

    override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle) {

    }

    override fun onActivityDestroyed(activity: Activity) {

    }
}
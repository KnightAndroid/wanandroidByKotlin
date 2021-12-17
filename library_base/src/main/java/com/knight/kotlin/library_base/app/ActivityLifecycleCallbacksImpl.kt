package com.knight.kotlin.library_base.app

import android.app.Activity
import android.app.Application
import android.os.Bundle
import com.knight.kotlin.library_util.ActivityManager

/**
 * Author:Knight
 * Time:2021/12/15 17:48
 * Description:ActivityLifecycleCallbacksImpl
 */
class ActivityLifecycleCallbacksImpl : Application.ActivityLifecycleCallbacks{
    override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {
        ActivityManager.addActivityToStack(activity)
    }

    override fun onActivityStarted(activity: Activity) {
    }

    override fun onActivityResumed(activity: Activity) {
    }

    override fun onActivityPaused(activity: Activity) {
    }

    override fun onActivityStopped(activity: Activity) {
    }

    override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle) {
    }

    override fun onActivityDestroyed(activity: Activity) {
        ActivityManager.popActivityToStack(activity)
    }
}
package com.knight.kotlin.library_permiss

import android.app.Activity
import android.app.Application.ActivityLifecycleCallbacks
import android.app.Dialog
import android.os.Build
import android.os.Bundle
import android.widget.PopupWindow


/**
 * @Description
 * @Author knight
 * @Time 2025/6/8 22:45
 *
 */

object WindowLifecycleManager {
    /**
     * 将 Activity 和 Dialog 的生命周期绑定在一起
     */
    fun bindDialogLifecycle(activity: Activity, dialog: Dialog) {
        val windowLifecycleCallbacks: WindowLifecycleCallbacks = object : WindowLifecycleCallbacks(activity) {
            override fun onWindowDismiss() {
                if (!dialog.isShowing) {
                    return
                }
                dialog.dismiss()
            }
        }
        registerWindowLifecycleCallbacks(activity, windowLifecycleCallbacks)
    }

    /**
     * 将 Activity 和 PopupWindow 的生命周期绑定在一起
     */
    fun bindPopupWindowLifecycle(activity: Activity, popupWindow: PopupWindow) {
        val windowLifecycleCallbacks: WindowLifecycleCallbacks = object : WindowLifecycleCallbacks(activity) {
            override fun onWindowDismiss() {
                if (!popupWindow.isShowing) {
                    return
                }
                popupWindow.dismiss()
            }
        }
        registerWindowLifecycleCallbacks(activity, windowLifecycleCallbacks)
    }

    /**
     * 注册窗口回调
     */
    private fun registerWindowLifecycleCallbacks(activity: Activity, callbacks: WindowLifecycleCallbacks) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            activity.registerActivityLifecycleCallbacks(callbacks)
        } else {
            activity.application.registerActivityLifecycleCallbacks(callbacks)
        }
    }

    /**
     * 反注册窗口回调
     */
    private fun unregisterWindowLifecycleCallbacks(activity: Activity, callbacks: WindowLifecycleCallbacks) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            activity.unregisterActivityLifecycleCallbacks(callbacks)
        } else {
            activity.application.unregisterActivityLifecycleCallbacks(callbacks)
        }
    }

    /**
     * 窗口生命周期回调
     */

    abstract class WindowLifecycleCallbacks(activity: Activity) : ActivityLifecycleCallbacks {

        private var mActivity: Activity?

        init {
            mActivity = activity
        }

        abstract fun onWindowDismiss()

        override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {
            // default implementation ignored
        }

        override fun onActivityStarted(activity: Activity) {
            // default implementation ignored
        }

        override fun onActivityResumed(activity: Activity) {
            // default implementation ignored
        }

        override fun onActivityPaused(activity: Activity) {
            // default implementation ignored
        }

        override fun onActivityStopped(activity: Activity) {
            // default implementation ignored
        }

        override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle) {
            // default implementation ignored
        }

        override fun onActivityDestroyed(activity: Activity) {
            if (activity !== mActivity) {
                return
            }
            // 释放 Activity 对象
            mActivity = null
            // 反注册窗口监听
            unregisterWindowLifecycleCallbacks(activity, this)
            // 通知外层销毁窗口
            onWindowDismiss()
        }
    }
}
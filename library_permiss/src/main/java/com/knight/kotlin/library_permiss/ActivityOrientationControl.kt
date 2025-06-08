package com.knight.kotlin.library_permiss

import android.app.Activity
import android.content.pm.ActivityInfo
import android.content.res.Configuration
import android.view.Display
import android.view.Surface
import android.view.WindowManager


/**
 * @Description
 * @Author knight
 * @Time 2025/6/8 16:36
 *
 */

object ActivityOrientationControl {

    /** 存放 Activity 屏幕方向集合  */
    val ACTIVITY_ORIENTATION_MAP: MutableMap<Int, Int> = HashMap()

    /** 私有化构造函数  */
    private fun ActivityOrientationControl() {}

    /**
     * 锁定 Activity 方向
     */
    @Synchronized
    fun lockActivityOrientation(activity: Activity) {
        // 如果当前没有锁定屏幕方向就获取当前屏幕方向并进行锁定
        val sourceScreenOrientation: Int = activity.getRequestedOrientation()
        if (sourceScreenOrientation != ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED) {
            return
        }

        val targetScreenOrientation: Int
        // 锁定当前 Activity 方向
        try {
            // 兼容问题：在 Android 8.0 的手机上可以固定 Activity 的方向，但是这个 Activity 不能是透明的，否则就会抛出异常
            // 复现场景：只需要给 Activity 主题设置 <item name="android:windowIsTranslucent">true</item> 属性即可
            when (activity.getResources().getConfiguration().orientation) {
                Configuration.ORIENTATION_LANDSCAPE -> {
                    targetScreenOrientation =
                        if (isActivityReverse(activity)) ActivityInfo.SCREEN_ORIENTATION_REVERSE_LANDSCAPE else ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
                    activity.setRequestedOrientation(targetScreenOrientation)
                    ACTIVITY_ORIENTATION_MAP[getIntKeyByActivity(activity)] =
                        targetScreenOrientation
                }

                Configuration.ORIENTATION_PORTRAIT -> {
                    targetScreenOrientation =
                        if (isActivityReverse(activity)) ActivityInfo.SCREEN_ORIENTATION_REVERSE_PORTRAIT else ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
                    activity.setRequestedOrientation(targetScreenOrientation)
                    ACTIVITY_ORIENTATION_MAP[getIntKeyByActivity(activity)] =
                        targetScreenOrientation
                }

                else -> {}
            }
        } catch (e: IllegalStateException) {
            // java.lang.IllegalStateException: Only fullscreen activities can request orientation
            e.printStackTrace()
        }
    }

    /**
     * 解锁 Activity 方向
     */
    @Synchronized
    fun unlockActivityOrientation(activity: Activity) {
        // 如果当前 Activity 没有锁定，就直接返回
        if (activity.getRequestedOrientation() === ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED) {
            return
        }
        val targetScreenOrientation = ACTIVITY_ORIENTATION_MAP[getIntKeyByActivity(activity)]
            ?: return
        // 判断 Activity 之前是不是设置的屏幕自适应（这个判断可能永远为 false，但是为了代码的严谨性，还是要做一下判断）
        if (targetScreenOrientation === ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED) {
            return
        }
        // 为什么这里不用跟上面一样 try catch ？因为这里是把 Activity 方向取消固定，只有设置横屏或竖屏的时候才可能触发 crash
        activity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED)
    }

    /**
     * 判断 Activity 是否反方向旋转了
     */
    @Suppress("deprecation")
    private fun isActivityReverse(activity: Activity): Boolean {
        var display: Display? = null
        if (AndroidVersionTools.isAndroid11()) {
            display = activity.getDisplay()
        } else {
            val windowManager: WindowManager = activity.getWindowManager()
            if (windowManager != null) {
                display = windowManager.defaultDisplay
            }
        }

        if (display == null) {
            return false
        }

        // 获取 Activity 旋转的角度
        val activityRotation = display.rotation
        return when (activityRotation) {
            Surface.ROTATION_180, Surface.ROTATION_270 -> true
            Surface.ROTATION_0, Surface.ROTATION_90 -> false
            else -> false
        }
    }

    /**
     * 通过 Activity 获得一个 int 值的 key
     */
    private fun getIntKeyByActivity(activity: Activity): Int {
        // 这里取 Activity 的 hashCode 作为 key 值，这样就不会出现重复
        return activity.hashCode()
    }
}
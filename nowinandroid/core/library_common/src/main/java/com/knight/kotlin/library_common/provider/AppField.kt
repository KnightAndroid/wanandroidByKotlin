package com.knight.kotlin.library_common.provider

import android.app.Application
import java.lang.reflect.Field
import java.lang.reflect.InvocationTargetException

/**
 * Author:Knight
 * Time:2021/12/16 13:51
 * Description:AppField
 */
class AppField {
    companion object {
        val INSTANCE = AppField()
    }

    fun getApplicationByReflect(): Application? {
        try {
            val activityThreadClass = Class.forName("android.app.ActivityThread")
            val thread: Any? = getActivityThread()
            val app = activityThreadClass.getMethod("getApplication").invoke(thread) ?: return null
            return app as Application
        } catch (e: InvocationTargetException) {
            e.printStackTrace()
        } catch (e: NoSuchMethodException) {
            e.printStackTrace()
        } catch (e: IllegalAccessException) {
            e.printStackTrace()
        } catch (e: ClassNotFoundException) {
            e.printStackTrace()
        }
        return null
    }

    private fun getActivityThread(): Any? {
        var activityThread: Any? = getActivityThreadInActivityThreadStaticField()
        if (activityThread != null) {
            return activityThread
        }
        activityThread = getActivityThreadInActivityThreadStaticMethod()
        return activityThread ?: getActivityThreadInLoadedApkField()
    }


    private fun getActivityThreadInActivityThreadStaticField(): Any? {
        return try {
            val activityThreadClass = Class.forName("android.app.ActivityThread")
            val currentActivityThreadField: Field =
                activityThreadClass.getDeclaredField("sCurrentActivityThread")
            currentActivityThreadField.setAccessible(true)
            currentActivityThreadField.get(null)
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    private fun getActivityThreadInActivityThreadStaticMethod(): Any? {
        return try {
            val activityThreadClass = Class.forName("android.app.ActivityThread")
            activityThreadClass.getMethod("currentActivityThread").invoke(null)
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    private fun getActivityThreadInLoadedApkField(): Any? {
        return try {
            val mLoadedApkField = Application::class.java.getDeclaredField("mLoadedApk")
            mLoadedApkField.isAccessible = true
            val mLoadedApk = mLoadedApkField[getApplicationByReflect()]
            val mActivityThreadField = mLoadedApk.javaClass.getDeclaredField("mActivityThread")
            mActivityThreadField.isAccessible = true
            mActivityThreadField[mLoadedApk]
        } catch (e: Exception) {
            null
        }
    }


}
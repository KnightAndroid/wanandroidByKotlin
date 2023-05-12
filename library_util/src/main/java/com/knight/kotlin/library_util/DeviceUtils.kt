package com.knight.kotlin.library_util

import android.os.Build
import java.util.Locale
import java.util.TimeZone

/**
 * Author:Knight
 * Time:2023/5/10 16:35
 * Description:DeviceUtils
 */
object DeviceUtils {

    /**
     *
     * 获取系统版本
     */
    fun getSystemVersion():String {
        return Build.VERSION.RELEASE
    }

    /**
     *
     * 获取安卓SDK版本
     */
    fun getAndroidSdkVersion() :Int {
        return Build.VERSION.SDK_INT
    }

    /**
     *
     * 返回时区
     */
    fun getCountry():String {
        return Locale.getDefault().country
    }

    /**
     *
     * 返回时区名
     */
    fun getTimeZone() :String {
        return TimeZone.getDefault().id
    }

}
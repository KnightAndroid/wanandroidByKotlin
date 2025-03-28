package com.knight.kotlin.library_util

import java.util.TimeZone


/**
 * @author created by luguian
 * @organize
 * @Date 2025/3/28 15:24
 * @descript:时间帮助类
 */
object TimeUtils {

    /**
     * 获取返回时区的ID，例如"Asia/Shanghai"。
     */
    fun getDefaultTimeZoneId(): String {
        val tz: TimeZone = TimeZone.getDefault()
        return tz.getID()
    }

    /**
     *
     * 获取时区
     */
    fun getDefaultTimeZone(): TimeZone {
        return TimeZone.getDefault()
    }
}
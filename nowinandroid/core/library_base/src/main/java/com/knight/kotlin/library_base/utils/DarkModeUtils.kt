package com.knight.kotlin.library_base.utils

import androidx.appcompat.app.AppCompatDelegate
import com.core.library_common.util.CacheUtils
import java.util.Calendar

/**
 * Author:Knight
 * Time:2022/5/23 11:42
 * Description:DarkModeUtils
 */
object DarkModeUtils {

    /**
     *
     * 判断是什么模式
     */
    fun darkNormal() {
        if (CacheUtils.getAutoNightMode()) {
            val nightStartHour: Int = Integer.valueOf(CacheUtils.getStartNightModeHour())
            val nightStartMinute: Int = Integer.valueOf(CacheUtils.getStartNightModeMinuter())
            val dayStartHour: Int = Integer.valueOf(CacheUtils.getStartDayModeHour())
            val dayStartMinuter: Int = Integer.valueOf(CacheUtils.getStartDayModeMinuter())
            val calendar = Calendar.getInstance()
            val currentHour = calendar[Calendar.HOUR_OF_DAY]
            val currentMinute = calendar[Calendar.MINUTE]
            val nightValue = nightStartHour * 60 + nightStartMinute
            val dayValue = dayStartHour * 60 + dayStartMinuter
            val currentValue = currentHour * 60 + currentMinute
            if (currentValue >= nightValue || currentValue <= dayValue) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                CacheUtils.setNightModeStatus(true)
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                CacheUtils.setNightModeStatus(false)
            }
        } else {
            CacheUtils.setNightModeStatus(false)
            if (CacheUtils.getFollowSystem()) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM)
            } else {
                if (CacheUtils.getNormalDark()) {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                } else {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                }
            }
        }
    }
}
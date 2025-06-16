package com.knight.kotlin.library_util

import android.os.Build
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.Date

/**
 * Author:Knight
 * Time:2024/3/20 14:45
 * Description:TimeAgoUtils
 */
object TimeAgoUtils {

    private const val ONE_MINUTE = 60000L
    private const val ONE_HOUR = 3600000L
    private const val ONE_DAY = 86400000L
    private const val ONE_WEEK = 604800000L

    private const val ONE_SECOND_AGO = "秒前"
    private const val ONE_MINUTE_AGO = "分钟前"
    private const val ONE_HOUR_AGO = "小时前"
    private const val ONE_DAY_AGO = "天前"
    private const val ONE_MONTH_AGO = "月前"
    private const val ONE_YEAR_AGO = "年前"

    fun format(createTime:String) : String {
        val delta=  if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val time = LocalDateTime.parse(createTime, DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss"))
            val compareTime = getTimestampOfDateTime(time)
            val now = LocalDateTime.now()
            val rightTime = getTimestampOfDateTime(now)
            rightTime - compareTime
        } else {
            val format = SimpleDateFormat("yyyy-MM-dd HH:m:s")
            val date: Date = format.parse(createTime)
            Date().getTime() - date.getTime()
        }
        return toShowAgo(delta)

    }


    private fun toShowAgo(delta:Long):String {
        if (delta < 1L * ONE_MINUTE) {
            val seconds = toSeconds(delta)
            return (if(seconds <= 0) 1 else seconds).toString().plus(ONE_SECOND_AGO)
        }
        if (delta < 45L * ONE_MINUTE) {
            val minutes = toMinutes(delta)
            return (if(minutes <= 0)  1 else minutes).toString().plus(ONE_MINUTE_AGO)
        }
        if (delta < 24L * ONE_HOUR) {
            val hours = toHours(delta);
            return (if(hours <= 0)  1 else hours).toString().plus(ONE_HOUR_AGO)
        }
        if (delta < 48L * ONE_HOUR) {
            return "昨天"
        }
        if (delta < 30L * ONE_DAY) {
            val days = toDays(delta)
            return (if(days <= 0) 1 else days).toString().plus(ONE_DAY_AGO)
        }
        if (delta < 12L * 4L * ONE_WEEK) {
            val months = toMonths(delta)
            return (if(months <= 0) 1 else months).toString().plus(ONE_MONTH_AGO)
        } else {
            val years = toYears(delta)
            return (if(years <= 0) 1 else years).toString().plus(ONE_YEAR_AGO)
        }

    }

    private fun toSeconds(date : Long) :Long {
        return date / 1000L
    }

    private fun toMinutes(date : Long) :Long {
        return toSeconds(date) / 60L
    }

    private fun toHours(date : Long):Long{
        return toMinutes(date) / 60L
    }

    private fun toDays(date : Long) : Long {
        return toHours(date) / 24L
    }

    private fun toMonths(date : Long) : Long {
        return toDays(date) / 30L
    }

    private fun toYears(date : Long) :Long {
        return toMonths(date) / 365L
    }

    /**
     * 返回传入时间的毫秒数
     *
     *
     *
     * @param localDateTime
     * @return
     */
    private fun getTimestampOfDateTime(localDateTime : LocalDateTime) : Long {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val zone = ZoneId.systemDefault()
            val instant = localDateTime.atZone(zone).toInstant()
            return instant.toEpochMilli()
        } else {
            return 0L
        }

    }
}

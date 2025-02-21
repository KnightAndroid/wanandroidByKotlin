package com.knight.kotlin.library_util

import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

/**
 * Author:Knight
 * Time:2021/12/28 15:43
 * Description:DateUtils
 */
object DateUtils {


    /**
     *
     * 判断时间是否是今天
     * @param time
     * @return
     */
    fun isToday(time: String?): Boolean {
        val format = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
        var date: Date? = null
        date = try {
            format.parse(time)
        } catch (e: ParseException) {
            e.printStackTrace()
            return false
        }
        val c1: Calendar = Calendar.getInstance()
        c1.time = date
        val year1: Int = c1.get(Calendar.YEAR)
        val month1: Int = c1.get(Calendar.MONTH) + 1
        val day1: Int = c1.get(Calendar.DAY_OF_MONTH)
        val c2: Calendar = Calendar.getInstance()
        c2.time = Date()
        val year2: Int = c2.get(Calendar.YEAR)
        val month2: Int = c2.get(Calendar.MONTH) + 1
        val day2: Int = c2.get(Calendar.DAY_OF_MONTH)
        return year1 == year2 && month1 == month2 && day1 == day2
    }

    /**
     *
     * 时间戳转成yyyy-MM-dd HH:mm:ss
     * @param date
     * @return
     */
    fun ConvertStringTime(date: Long): String? {
        //要转换的时间格式
        val sdf = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
        val dateTemp: Date
        return try {
            dateTemp = sdf.parse(sdf.format(date))
            sdf.format(dateTemp)
        } catch (e: ParseException) {
            e.printStackTrace()
            null
        }
    }

    /**
     *
     * 时间戳转成yyyy-MM-dd
     * @param date
     * @return
     */
    fun ConvertYearMonthDayTime(date: Date?): String {
        val calendar = Calendar.getInstance()
        calendar.time = date
        val year = calendar[Calendar.YEAR]
        val month = calendar[Calendar.MONTH]
        val day = calendar[Calendar.DAY_OF_MONTH]
        val hour = calendar[Calendar.HOUR_OF_DAY]
        val minuter = calendar[Calendar.MINUTE]
        val second = calendar[Calendar.SECOND]
        return "$year-$month-$day $hour:$minuter:$second"
    }

    /**
     *
     * 将时间转换成上午，中午，下午，晚上
     * @return
     */
    fun convertTime(): String {
        val date = Date()
        val df = SimpleDateFormat("HH")
        val str = df.format(date)
        val a = str.toInt()
        if (a in 0..6) {
            return "凌晨"
        }
        if (a in 7..12) {
            return "上午好"
        }
        if (a in 13..13) {
            return "中午好"
        }
        if (a in 14..18) {
            return "下午好"
        }
        return if (a in 19..24) {
            "晚上好"
        } else "您好"
    }

    /**
     * 时间格式转成分秒
     *
     * @param milliseconds
     * @return
     */
    fun formatDateMsByMS(milliseconds: Long): String {
        val simpleDateFormat = SimpleDateFormat("mm:ss")
        return simpleDateFormat.format(Date(milliseconds))
    }

    /**
     * 时间格式转成年月日
     *
     *
     * @param milliseconds
     * @return
     */
    fun formatDateMsByYMD(milliseconds: Long): String {
        val simpleDateFormat = SimpleDateFormat("yyyy/MM/dd")
        return simpleDateFormat.format(Date(milliseconds))
    }

    /**
     *
     * 时间格式转成年月日 时分
     *
     * @param milliseconds
     * @return
     */
    fun formatDateMsByYMDHM(milliseconds: Long): String {
        val simpleDateFormat = SimpleDateFormat("yyyy/MM/dd HH:mm")
        return simpleDateFormat.format(Date(milliseconds))
    }


    /**
     *
     * 时间格式转成年月日 时分秒
     *
     * @param milliseconds
     * @return
     */
    fun formatDateMsByYMDHMS(milliseconds: Long): String {
        val simpleDateFormat = SimpleDateFormat("yyyy/MM/dd HH:mm:ss")
        return simpleDateFormat.format(Date(milliseconds))
    }

    /**
     * 将时间格式转成整形
     *
     */
    fun convertToTimestamp(dateString: String) : Long? {
        val formatter = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
        return try {
            // 解析日期字符串并返回时间戳
            val date: Date = formatter.parse(dateString)
            date?.time
        } catch (e: Exception) {
            // 如果日期格式不匹配，返回 null
            e.printStackTrace()
            null
        }
    }

    /**
     *
     * 获取当天月日
     */
    fun getCurrentDateFormatted(): String {
        val calendar = Calendar.getInstance()  // 获取当前日期
        val dateFormat = SimpleDateFormat("M月d日", Locale.getDefault())  // 格式化为 "1月1日"
        return dateFormat.format(calendar.time)  // 格式化当前时间并返回
    }

}
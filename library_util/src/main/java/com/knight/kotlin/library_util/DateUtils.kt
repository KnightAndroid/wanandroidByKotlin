package com.knight.kotlin.library_util

import android.os.Build
import androidx.annotation.RequiresApi
import com.knight.kotlin.library_base.util.CacheUtils
import com.knight.kotlin.library_base.util.LanguageFontSizeUtils
import java.text.ParseException
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import java.util.Calendar
import java.util.Date
import java.util.Locale
import java.util.TimeZone


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
    fun convertToTimestamp(dateString: String): Long? {
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
     * 根据传入的Date String 转换目标时间输出
     */
    fun formatDate(input: String, inputPattern: String, outputPattern: String): String {
        return try {
            val inputFormat = SimpleDateFormat(inputPattern, Locale.getDefault())
            val outputFormat = SimpleDateFormat(outputPattern, Locale.getDefault())
            val date = inputFormat.parse(input)
            outputFormat.format(date!!)
        } catch (e: Exception) {
            e.printStackTrace()
            ""
        }
    }

    /**
     *
     * 获取当天月日 自行传入格式
     */
    fun getCurrentDateFormatted(format: String? = "M月d日"): String {
        val calendar = Calendar.getInstance()  // 获取当前日期
        val dateFormat = SimpleDateFormat(format, Locale.getDefault())  // 格式化为 "1月1日"
        return dateFormat.format(calendar.time)  // 格式化当前时间并返回
    }

    /**
     *
     * 获取14天后的日期
     */
    fun getTwoWeekDaysLater(): String {
        val calendar = Calendar.getInstance()
        calendar.add(Calendar.DAY_OF_MONTH, 14) // 添加 14 天
        val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        return dateFormat.format(calendar.time)
    }


    /**
     *
     * 将日期yyyy-MM-dd改成MM/dd
     */
    fun formatDateToMMdd(dateString: String): String {
        val inputFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val outputFormat = SimpleDateFormat("MM/dd", Locale.getDefault())

        return try {
            val date: Date = inputFormat.parse(dateString) as Date
            outputFormat.format(date)
        } catch (e: Exception) {
            // 处理日期解析错误，例如无效的日期字符串
            "Invalid Date"
        }
    }

    /**
     * 获取一周的第几天
     */
    fun getDayOfWeek(dateString: String): String {
        val inputFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        return try {
            val date: Date = inputFormat.parse(dateString) as Date
            val calendar = Calendar.getInstance()
            calendar.time = date
            val dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK)
            getDayOfWeekName(dayOfWeek)
        } catch (e: Exception) {
            "Invalid Date"
        }
    }

    /**
     *
     * 将一周的第几天转成周几
     */
    fun getDayOfWeekName(dayOfWeek: Int): String {
        if (CacheUtils.getLanguageMode() == "English") {
            return when (dayOfWeek) {
                Calendar.SUNDAY -> "Sunday"
                Calendar.MONDAY -> "Monday"
                Calendar.TUESDAY -> "Tuesday"
                Calendar.WEDNESDAY -> "Wednesday"
                Calendar.THURSDAY -> "Thursday"
                Calendar.FRIDAY -> "Friday"
                Calendar.SATURDAY -> "Saturday"
                else -> "Invalid Day"
            }
        } else {
            return when (dayOfWeek) {
                Calendar.SUNDAY -> "周日"
                Calendar.MONDAY -> "周一"
                Calendar.TUESDAY -> "周二"
                Calendar.WEDNESDAY -> "周三"
                Calendar.THURSDAY -> "周四"
                Calendar.FRIDAY -> "周五"
                Calendar.SATURDAY -> "周六"
                else -> "无效"
            }
        }


    }

    /**
     *
     * 判断是否在白天还是晚上
     */
    fun isDaytime(): Boolean {
        val calendar = Calendar.getInstance()
        val hour = calendar.get(Calendar.HOUR_OF_DAY)
        return hour in 6..18 // 假设 6:00 到 18:00 为白天
    }

    /**
     *
     * 将20250227110000 转成时分  20250227170000
     */
    fun formatTimeToHourMinuter(timeStr: String): String {
        if (timeStr.length != 14) {
            return "Invalid time string" // 或者抛出异常，根据你的需求
        }
        val hour = timeStr.substring(8, 10)
        val minute = timeStr.substring(10, 12)
        return "$hour:$minute"
    }

    /**
     *
     * 获取月-日
     */
    fun getMonthDay(dateString: String?): String {
        val date = LocalDate.parse(dateString)
        val formatter = DateTimeFormatter.ofPattern("MM/dd")
        return date.format(formatter)
    }


    /**
     *
     * 获取是星期几
     */
    fun getWeekday(): String {
        return if (LanguageFontSizeUtils.isChinese()) {
            getCNWeekday()
        } else {
            getEnglishWeekday()
        }
    }


    /**
     *
     * 获取中文星期几
     */
    fun getCNWeekday(): String {
        val calendar = Calendar.getInstance()
        val dayOfWeek = calendar[Calendar.DAY_OF_WEEK]

        // Calendar.DAY_OF_WEEK 的值从 1（星期日）到 7（星期六）
        return when (dayOfWeek) {
            Calendar.SUNDAY -> "星期日"
            Calendar.MONDAY -> "星期一"
            Calendar.TUESDAY -> "星期二"
            Calendar.WEDNESDAY -> "星期三"
            Calendar.THURSDAY -> "星期四"
            Calendar.FRIDAY -> "星期五"
            Calendar.SATURDAY -> "星期六"
            else -> ""
        }
    }


    // 可选：获取英文星期几
    fun getEnglishWeekday(): String {
        val calendar = Calendar.getInstance()
        return calendar.getDisplayName(Calendar.DAY_OF_WEEK, Calendar.LONG, Locale.ENGLISH)
    }


    /**
     *
     * 将年月日和 时分 拼接起来 转成时间戳
     */
    fun getTimestamp(dateStr: String, timeStr: String, zoneId: String = "Asia/Shanghai"): Long {
        val dateTimeStr = "$dateStr $timeStr"
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val formatter =
                DateTimeFormatter.ofPattern("yyyyMMdd HH:mm", Locale.getDefault())
            val localDateTime = LocalDateTime.parse(dateTimeStr, formatter)
            val zonedDateTime = localDateTime.atZone(ZoneId.of(zoneId))
            return zonedDateTime.toInstant().toEpochMilli()
        } else {
            try {
                val sdf = SimpleDateFormat("yyyyMMdd HH:mm", Locale.getDefault())
                sdf.timeZone = TimeZone.getTimeZone(zoneId)
                val date: Date = sdf.parse(dateTimeStr)
                val calendar = Calendar.getInstance(TimeZone.getTimeZone(zoneId))
                calendar.time = date
                return calendar.timeInMillis
            } catch (e: ParseException) {
                throw ParseException("Invalid date time format", e.errorOffset)
            }
        }


    }

    /**
     *
     * 转成时间戳
     */
    fun getTimestampCompat(year: Int, month: Int, day: Int, hour: Int, minute: Int, zoneId: TimeZone): Long {
        val calendar = Calendar.getInstance(zoneId).apply {
            set(year, month - 1, day, hour, minute, 0)
            set(Calendar.MILLISECOND, 0)
        }
        return calendar.timeInMillis
    }

    /**
     *
     * 返回时间戳
     */
    @RequiresApi(Build.VERSION_CODES.O)
    fun getTimeStampByZonedDateTime(dateTime: ZonedDateTime, zoneId:ZoneId):Long {
        return dateTime.withZoneSameInstant(zoneId).toInstant().toEpochMilli()
    }
}
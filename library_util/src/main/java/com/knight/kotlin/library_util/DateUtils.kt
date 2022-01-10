package com.knight.kotlin.library_util

import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date

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
}
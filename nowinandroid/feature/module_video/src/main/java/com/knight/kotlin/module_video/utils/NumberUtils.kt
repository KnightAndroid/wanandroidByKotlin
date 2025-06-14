package com.knight.kotlin.module_video.utils

import java.text.DecimalFormat

/**
 * Author:Knight
 * Time:2024/3/18 17:10
 * Description:NumberUtils
 */
object NumberUtils {


    /**
     * 数字上万过滤
     * @return
     */
    @JvmStatic
    fun numberFilter(number: Long): String {
        return if (number in 10000..999999) {  //数字上万，小于百万，保留一位小数点
            val df2 = DecimalFormat("##.#")
            val format = df2.format((number.toFloat() / 10000).toDouble())
            format + "万"
        } else if (number in 1000000..99999998) {  //百万到千万不保留小数点
            (number/10000).toString() + "万"
        } else if (number > 99999999) { //上亿
            val df2 = DecimalFormat("##.#")
            val format = df2.format((number.toFloat() / 100000000).toDouble())
            format + "亿"
        } else {
            number.toString() + ""
        }
    }
}
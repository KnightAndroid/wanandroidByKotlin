package com.knight.kotlin.library_network.log.printer

import com.knight.kotlin.library_network.log.LogLevel
import com.knight.kotlin.library_network.log.formatter.Formatter

/**
 * Author:Knight
 * Time:2021/12/22 11:00
 * Description:Printer
 */
interface Printer {

    val formatter: Formatter // 用于格式化日志

    /**
     * 打印日志
     */
    fun printLog(logLevel: LogLevel, tag: String, msg: String)
}
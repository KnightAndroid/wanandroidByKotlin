package com.knight.kotlin.library_network.log.bean


import com.knight.kotlin.library_network.log.LogLevel
import com.knight.kotlin.library_network.log.printer.Printer

/**
 * Author:Knight
 * Time:2021/12/22 10:43
 * Description:JSONConfig
 */
data class JSONConfig(val logLevel: LogLevel = LogLevel.INFO, val tag:String, val printers:MutableSet<Printer>)
package com.knight.kotlin.library_network.log.printer

import android.util.Log
import com.knight.kotlin.library_network.log.LogLevel
import com.knight.kotlin.library_network.log.formatter.BorderFormatter
import com.knight.kotlin.library_network.log.formatter.Formatter

/**
 * Author:Knight
 * Time:2021/12/22 11:04
 * Description:LogcatPrinter
 */
class LogcatPrinter(override val formatter: Formatter = BorderFormatter):Printer {

    override fun printLog(logLevel: LogLevel, tag: String, msg: String) {

        when(logLevel) {

            LogLevel.ERROR -> Log.e(tag, msg)

            LogLevel.WARN  -> Log.w(tag, msg)

            LogLevel.INFO  -> Log.i(tag, msg)

            LogLevel.DEBUG -> Log.d(tag, msg)
        }
    }

    override fun equals(other: Any?): Boolean {

        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as LogcatPrinter

        if (formatter != other.formatter) return false

        return true
    }

    override fun hashCode() = formatter.hashCode()
}
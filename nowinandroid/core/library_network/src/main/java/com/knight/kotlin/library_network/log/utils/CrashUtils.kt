package com.knight.kotlin.library_network.log.utils


import com.knight.kotlin.library_network.log.L
import com.knight.kotlin.library_network.log.LogLevel
import com.knight.kotlin.library_network.log.printer.Printer


/**
 * Author:Knight
 * Time:2021/12/22 11:01
 * Description:CrashUtils
 */
object CrashUtils {

    private val DEFAULT_UNCAUGHT_EXCEPTION_HANDLER: Thread.UncaughtExceptionHandler = Thread.getDefaultUncaughtExceptionHandler()

    fun init(tag:String? = null, extraInfo:String? = null, printer: Printer, onCrashListener: OnCrashListener?) {

        Thread.setDefaultUncaughtExceptionHandler(
            getUncaughtExceptionHandler(tag, extraInfo, printer, onCrashListener)
        )
    }

    private fun getUncaughtExceptionHandler(
        tag:String?,
        extraInfo:String?,
        printer: Printer,
        onCrashListener: OnCrashListener?
    ): Thread.UncaughtExceptionHandler {
        return Thread.UncaughtExceptionHandler { t, e ->

            val sb = StringBuilder()
            sb.append(extraInfo?:"").append(getFullStackTrace(e))

            val crashInfo = sb.toString()

            L.print(LogLevel.ERROR,tag,crashInfo,printer)

            onCrashListener?.onCrash(crashInfo, e)

            DEFAULT_UNCAUGHT_EXCEPTION_HANDLER.uncaughtException(t, e)
        }
    }

    interface OnCrashListener {
        fun onCrash(crashInfo: String, e: Throwable)
    }
}
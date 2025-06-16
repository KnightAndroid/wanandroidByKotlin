package com.knight.kotlin.library_network.log.handler

import com.knight.kotlin.library_network.log.L
import com.knight.kotlin.library_network.log.bean.JSONConfig
import com.knight.kotlin.library_network.log.formatter.Formatter
import com.knight.kotlin.library_network.log.parser.Parser

import java.io.PrintWriter
import java.io.StringWriter

/**
 * Author:Knight
 * Time:2021/12/22 10:42
 * Description:ThrowableHandler
 */
class ThrowableHandler:BaseHandler(), Parser<Throwable> {

    override fun handle(obj: Any, jsonConfig: JSONConfig): Boolean {

        if (obj is Throwable) {

            jsonConfig.printers.map {
                val s = L.getMethodNames(it.formatter)
                it.printLog(jsonConfig.logLevel,jsonConfig.tag,String.format(s, parseString(obj,it.formatter)))
            }
            return true
        }

        return false
    }

    override fun parseString(throwable: Throwable,formatter: Formatter): String {
        val sw = StringWriter(256)
        val pw = PrintWriter(sw, false)
        throwable.printStackTrace(pw)
        pw.flush()
        var message = sw.toString()
        message = message.replace("\n", "\n${formatter.spliter()}")

        return message
    }

}
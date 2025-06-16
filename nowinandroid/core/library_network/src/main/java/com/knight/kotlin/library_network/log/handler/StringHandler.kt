package com.knight.kotlin.library_network.log.handler

import com.knight.kotlin.library_network.log.L
import com.knight.kotlin.library_network.log.LogLevel
import com.knight.kotlin.library_network.log.bean.JSONConfig
import com.knight.kotlin.library_network.log.extension.formatJSON
import com.knight.kotlin.library_network.log.formatter.Formatter
import com.knight.kotlin.library_network.log.parser.Parser
import com.knight.kotlin.library_network.log.printer.Printer
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject


/**
 * Author:Knight
 * Time:2021/12/22 10:41
 * Description:StringHandler
 */
class StringHandler:BaseHandler(), Parser<String> {

    companion object {
        private const val MAX_STRING_LENGTH = 4000
    }

    override fun handle(obj: Any, jsonConfig: JSONConfig): Boolean {

        if (obj is String) {

            var json = obj.trim { it <= ' ' }

            jsonConfig.printers.map {
                val s = L.getMethodNames(it.formatter)
                val logString = String.format(s, parseString(json,it.formatter))
                printLongLog(jsonConfig.logLevel,jsonConfig.tag,logString,it)
            }
            return true
        }

        return false
    }

    /**
     * 打印超过 4000 行的日志
     */
    private fun printLongLog(logLevel: LogLevel, tag: String, logString: String, printer: Printer) {

        if (logString.length > MAX_STRING_LENGTH) {

            var i = 0

            while (i < logString.length) {

                if (i + MAX_STRING_LENGTH < logString.length) {
                    if (i==0) {
                        printer.printLog(logLevel, tag, logString.substring(i, i + MAX_STRING_LENGTH))
                    } else {
                        printer.printLog(logLevel, "", logString.substring(i, i + MAX_STRING_LENGTH))
                    }
                } else
                    printer.printLog(logLevel, "", logString.substring(i, logString.length))

                i += MAX_STRING_LENGTH
            }
        } else
            printer.printLog(logLevel, tag, logString)
    }

    override fun parseString(json: String,formatter: Formatter): String {
        var message = ""

        try {
            if (json.startsWith("{")) {
                val jsonObject = JSONObject(json)
                message = jsonObject.formatJSON().run {
                    replace("\n", "\n${formatter.spliter()}")
                }
            } else if (json.startsWith("[")) {
                val jsonArray = JSONArray(json)
                message = jsonArray.formatJSON().run {
                    replace("\n", "\n${formatter.spliter()}")
                }
            } else { // 普通的字符串
                message = json.replace("\n", "\n${formatter.spliter()}")
            }
        } catch (e: JSONException) {
            L.e("Invalid Json: $json")
            message = ""
        }

        return message
    }

}
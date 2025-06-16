package com.knight.kotlin.library_network.log.handler

import android.net.Uri
import com.knight.kotlin.library_network.log.L
import com.knight.kotlin.library_network.log.LoggerPrinter
import com.knight.kotlin.library_network.log.bean.JSONConfig
import com.knight.kotlin.library_network.log.extension.formatJSON
import com.knight.kotlin.library_network.log.formatter.Formatter
import com.knight.kotlin.library_network.log.parser.Parser
import com.knight.kotlin.library_network.log.utils.toJavaClass
import org.json.JSONObject



/**
 * Author:Knight
 * Time:2021/12/22 10:42
 * Description:UriHandler
 */
class UriHandler:BaseHandler(), Parser<Uri> {

    override fun handle(obj: Any, jsonConfig: JSONConfig): Boolean {

        if (obj is Uri) {

            jsonConfig.printers.map {
                val s = L.getMethodNames(it.formatter)
                it.printLog(jsonConfig.logLevel,jsonConfig.tag,String.format(s, parseString(obj,it.formatter)))
            }
            return true
        }

        return false
    }

    override fun parseString(uri: Uri,formatter: Formatter): String {

        var msg = uri.toJavaClass() + LoggerPrinter.BR + formatter.spliter()

        return msg + JSONObject().apply {

            put("Scheme", uri.scheme)
            put("Host", uri.host)
            put("Port", uri.port)
            put("Path", uri.path)
            put("Query", uri.query)
            put("Fragment", uri.fragment)
        }
            .formatJSON()
            .let {
                it.replace("\n", "\n${formatter.spliter()}")
            }
    }

}
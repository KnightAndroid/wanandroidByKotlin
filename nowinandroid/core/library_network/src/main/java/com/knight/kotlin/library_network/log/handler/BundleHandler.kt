package com.knight.kotlin.library_network.log.handler

import android.os.Bundle
import com.knight.kotlin.library_network.log.bean.JSONConfig
import com.knight.kotlin.library_network.log.L
import com.knight.kotlin.library_network.log.LoggerPrinter
import com.knight.kotlin.library_network.log.extension.formatJSON
import com.knight.kotlin.library_network.log.extension.parseBundle
import com.knight.kotlin.library_network.log.formatter.Formatter
import com.knight.kotlin.library_network.log.parser.Parser
import com.knight.kotlin.library_network.log.utils.toJavaClass
import org.json.JSONObject



/**
 * Author:Knight
 * Time:2021/12/22 10:39
 * Description:BundleHandler
 */
class BundleHandler:BaseHandler(), Parser<Bundle> {

    override fun handle(obj: Any, jsonConfig: JSONConfig): Boolean {

        if (obj is Bundle) {

            jsonConfig.printers.map {
                val s = L.getMethodNames(it.formatter)
                it.printLog(jsonConfig.logLevel, jsonConfig.tag, String.format(s, parseString(obj,it.formatter)))
            }

            return true
        }

        return false
    }

    override fun parseString(bundle: Bundle,formatter: Formatter): String {

        var msg = bundle.toJavaClass() + LoggerPrinter.BR + formatter.spliter()

        return msg + JSONObject().parseBundle(bundle)
            .formatJSON()
            .let {
                it.replace("\n", "\n${formatter.spliter()}")
            }
    }
}
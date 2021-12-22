package com.knight.kotlin.library_network.log.handler

import android.content.Intent
import android.os.Bundle
import com.knight.kotlin.library_network.log.L
import com.knight.kotlin.library_network.log.LoggerPrinter
import com.knight.kotlin.library_network.log.bean.JSONConfig
import com.knight.kotlin.library_network.log.extension.formatJSON
import com.knight.kotlin.library_network.log.extension.parseBundle
import com.knight.kotlin.library_network.log.formatter.Formatter
import com.knight.kotlin.library_network.log.parser.Parser
import com.knight.kotlin.library_network.log.utils.toJavaClass
import org.json.JSONObject


/**
 * Author:Knight
 * Time:2021/12/22 10:40
 * Description:IntentHandler
 */
class IntentHandler:BaseHandler(), Parser<Intent> {

    override fun handle(obj: Any, jsonConfig: JSONConfig): Boolean {

        if (obj is Intent) {

            jsonConfig.printers.map {
                val s = L.getMethodNames(it.formatter)
                it.printLog(jsonConfig.logLevel,jsonConfig.tag,String.format(s, parseString(obj,it.formatter)))
            }
            return true
        }

        return false
    }

    override fun parseString(intent: Intent,formatter: Formatter): String {

        var msg = intent.toJavaClass()+ LoggerPrinter.BR + formatter.spliter()

        return msg + JSONObject().apply {

            put("Scheme", intent.scheme)
            put("Action", intent.action)
            put("DataString", intent.dataString)
            put("Type", intent.type)
            put("Package", intent.`package`)
            put("ComponentInfo", intent.component)
            put("Categories", intent.categories)

            intent.extras?.let {
                put("Extras", JSONObject(parseBundleString(it)))
            }
        }
            .formatJSON()
            .let {
                it.replace("\n", "\n${formatter.spliter()}")
            }
    }

    private fun parseBundleString(extras: Bundle) = JSONObject().parseBundle(extras).formatJSON()
}
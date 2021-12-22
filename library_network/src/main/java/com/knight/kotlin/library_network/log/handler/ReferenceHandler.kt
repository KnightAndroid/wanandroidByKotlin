package com.knight.kotlin.library_network.log.handler

import com.knight.kotlin.library_network.log.L
import com.knight.kotlin.library_network.log.LoggerPrinter
import com.knight.kotlin.library_network.log.bean.JSONConfig
import com.knight.kotlin.library_network.log.extension.formatJSON
import com.knight.kotlin.library_network.log.formatter.Formatter
import com.knight.kotlin.library_network.log.parser.Parser
import com.knight.kotlin.library_network.log.utils.isPrimitiveType
import com.knight.kotlin.library_network.log.utils.toJavaClass
import org.json.JSONObject

import java.lang.ref.Reference

/**
 * Author:Knight
 * Time:2021/12/22 10:41
 * Description:ReferenceHandler
 */
class ReferenceHandler:BaseHandler(), Parser<Reference<*>> {

    override fun handle(obj: Any, jsonConfig: JSONConfig): Boolean {

        if (obj is Reference<*>) {

            jsonConfig.printers.map {
                val s = L.getMethodNames(it.formatter)
                it.printLog(jsonConfig.logLevel,jsonConfig.tag,String.format(s, parseString(obj,it.formatter)))
            }
            return true
        }

        return false
    }

    override fun parseString(reference: Reference<*>,formatter: Formatter): String {
        val actual = reference.get()

        var msg = reference.javaClass.canonicalName + "<" + actual?.toJavaClass() + ">"+ LoggerPrinter.BR + formatter.spliter()

        val isPrimitiveType = isPrimitiveType(actual)

        msg += if (isPrimitiveType) {

            "{$actual}"
        } else {

            L.getConverter()?.takeIf { actual != null }?.let {

                JSONObject(it.toJson(actual!!))
                    .formatJSON()
                    .let {
                        it.replace("\n", "\n${formatter.spliter()}")
                    }

            }?:""
        }

        return msg
    }
}
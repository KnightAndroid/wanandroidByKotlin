package com.knight.kotlin.library_network.log.handler

import com.knight.kotlin.library_network.log.L
import com.knight.kotlin.library_network.log.LoggerPrinter
import com.knight.kotlin.library_network.log.bean.JSONConfig
import com.knight.kotlin.library_network.log.extension.formatJSON
import com.knight.kotlin.library_network.log.extension.parseToJSONArray
import com.knight.kotlin.library_network.log.formatter.Formatter
import com.knight.kotlin.library_network.log.parser.Parser
import com.knight.kotlin.library_network.log.utils.isPrimitiveType
import org.json.JSONArray

/**
 * Author:Knight
 * Time:2021/12/22 10:40
 * Description:CollectionHandler
 */
class CollectionHandler:BaseHandler(), Parser<Collection<*>> {

    override fun handle(obj: Any, jsonConfig: JSONConfig): Boolean {

        if (obj is Collection<*>) {

            val value = obj.firstOrNull()
            val isPrimitiveType = isPrimitiveType(value)

            if (isPrimitiveType) {
                val simpleName = obj.javaClass
                var msg = "%s size = %d" + LoggerPrinter.BR

                jsonConfig.printers.map {
                    msg = String.format(msg, simpleName, obj.size) + it.formatter.spliter()

                    val s = L.getMethodNames(it.formatter)
                    it.printLog(jsonConfig.logLevel,jsonConfig.tag,String.format(s, msg + obj.toString()))
                }
                return true
            }

            jsonConfig.printers.map {
                val s = L.getMethodNames(it.formatter)
                it.printLog(jsonConfig.logLevel,jsonConfig.tag, String.format(s, parseString(obj,it.formatter)))
            }
            return true
        }

        return false
    }

    override fun parseString(collection: Collection<*>,formatter: Formatter): String {

        val jsonArray = JSONArray()

        val simpleName = collection.javaClass

        var msg = "%s size = %d" + LoggerPrinter.BR
        msg = String.format(msg, simpleName, collection.size) + formatter.spliter()

        return msg + collection.parseToJSONArray(jsonArray)
            .formatJSON()
            .let {
                it.replace("\n", "\n${formatter.spliter()}")
            }
    }

}
package com.knight.kotlin.library_network.log.handler

import com.knight.kotlin.library_network.log.L
import com.knight.kotlin.library_network.log.LoggerPrinter
import com.knight.kotlin.library_network.log.bean.JSONConfig
import com.knight.kotlin.library_network.log.extension.formatJSON
import com.knight.kotlin.library_network.log.utils.toJavaClass
import org.json.JSONObject

/**
 * Author:Knight
 * Time:2021/12/22 10:41
 * Description:ObjectHandler
 */
class ObjectHandler:BaseHandler() {

    override fun handle(obj: Any, jsonConfig: JSONConfig): Boolean {

        if (L.getConverter()!=null) {

            jsonConfig.printers.map {

                val formatter = it.formatter

                var msg = obj.toJavaClass() + LoggerPrinter.BR + formatter.spliter()

                val message = L.getConverter()!!.toJson(obj).run {
                    JSONObject(this)
                }
                    .formatJSON()
                    .run {
                        replace("\n", "\n${formatter.spliter()}")
                    }

                val s = L.getMethodNames(formatter)
                it.printLog(jsonConfig.logLevel,jsonConfig.tag,String.format(s, msg + message))
            }
            return true
        }

        return false
    }
}
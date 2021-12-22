package com.knight.kotlin.library_network.log.extension

import android.os.Bundle
import com.knight.kotlin.library_network.log.L
import com.knight.kotlin.library_network.log.LoggerPrinter
import com.knight.kotlin.library_network.log.utils.isPrimitiveType
import org.json.JSONException
import org.json.JSONObject

/**
 * Author:Knight
 * Time:2021/12/22 10:57
 * Description:JSONObject+Extension
 */

fun JSONObject.formatJSON(): String = this.toString(LoggerPrinter.JSON_INDENT)

/**
 * 解析 bundle ，并存储到 JSONObject
 */
fun JSONObject.parseBundle(bundle: Bundle):JSONObject {

    bundle.keySet().map {

        val value = bundle.get(it)

        value?.run {
            val isPrimitiveType = isPrimitiveType(this)

            try {
                if (isPrimitiveType) {
                    put(it, bundle.get(it))
                } else {
                    put(it, JSONObject(L.getConverter()?.toJson(this)?:"{}"))
                }
            } catch (e: JSONException) {
                L.e("Invalid Json")
            }
        }
    }

    return this
}

/**
 * 解析 map ，并存储到 JSONObject
 */
fun JSONObject.parseMap(map: Map<*, *>):JSONObject {

    val keys = map.keys
    val values = map.values
    val value = values.firstOrNull()
    val isPrimitiveType = isPrimitiveType(value)

    keys.map {

        it?.let {

            try {
                if (isPrimitiveType) {
                    put(it.toString(), map[it])
                } else {
                    put(it.toString(), JSONObject(L.getConverter()?.toJson(map[it]?:"{}")?:"{}"))
                }
            } catch (e: JSONException) {
                L.e("Invalid Json")
            }
        }
    }

    return this
}
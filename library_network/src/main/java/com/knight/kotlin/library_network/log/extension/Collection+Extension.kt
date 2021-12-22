package com.knight.kotlin.library_network.log.extension

import com.knight.kotlin.library_network.log.L
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject

/**
 * Author:Knight
 * Time:2021/12/22 10:55
 * Description:Collection+Extension
 */
/**
 * 解析 collection ，并存储到 JSONArray
 */
fun Collection<*>.parseToJSONArray(jsonArray: JSONArray): JSONArray {

    this.map {

        it?.let {
            val objStr = L.getConverter()?.toJson(it)
            objStr?.run {
                try {
                    val jsonObject = JSONObject(this)
                    jsonArray.put(jsonObject)
                } catch (e: JSONException) {
                    L.e("Invalid Json")
                }
            }
        }
    }

    return jsonArray
}
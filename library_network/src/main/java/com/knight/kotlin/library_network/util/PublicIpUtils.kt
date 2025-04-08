package com.knight.kotlin.library_network.util

import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import org.json.JSONObject
import java.io.IOException


/**
 * @author created by luguian
 * @organize
 * @Date 2025/4/8 10:26
 * @descript:获取公网ip方法
 */
object PublicIpUtils {

    private val client = OkHttpClient()

    fun getPublicIP(callback: (String?, IOException?) -> Unit) {
        val request = Request.Builder()
            .url("https://jsonip.com/")
            .build()

        client.newCall(request).enqueue(object : okhttp3.Callback {
            override fun onFailure(call: okhttp3.Call, e: IOException) {
                callback(null, e)
            }

            override fun onResponse(call: okhttp3.Call, response: Response) {
                response.use {
                    if (!response.isSuccessful) {
                        callback(null, IOException("Unexpected code $response"))
                        return
                    }

                    try {
                        val json = JSONObject(response.body?.string())
                        val ip = json.getString("ip")
                        callback(ip, null)
                    } catch (e: Exception) {
                        callback(null, IOException("Failed to parse JSON: ${e.message}"))
                    }
                }
            }
        })
    }
}
package com.knight.kotlin.library_common.util

import android.content.Context
import android.os.Build
import android.util.Log
import java.io.File
import java.io.FileOutputStream
import java.net.HttpURLConnection
import java.net.URL


/**
 * @author created by luguian
 * @organize
 * @Date 2025/6/25 17:18
 * @descript:下载百度so类
 */
object BaiduSoDownloaderUtils {

    private const val TAG = "BaiduSoDownloader"

    // 下载项：soName -> v7aUrl, v8aUrl
    private val soFiles = listOf(
        Triple(
            "libBaiduMapSDK_map_for_navi_v7_6_4.so",
            "https://gitee.com/MengSuiXinSuoYuan/baidu-so/releases/download/Baidu_map_v7a/libBaiduMapSDK_map_for_navi_v7_6_4.so",
            "https://gitee.com/MengSuiXinSuoYuan/baidu-so/releases/download/Baidu_map_v8a/libBaiduMapSDK_map_for_navi_v7_6_4.so"
        ),
        Triple(
            "liblocSDK8b.so",
            "https://gitee.com/MengSuiXinSuoYuan/baidu-so/releases/download/Baidu_location_v7a/liblocSDK8b.so",
            "https://gitee.com/MengSuiXinSuoYuan/baidu-so/releases/download/Baidu_location_v8a/liblocSDK8b.so"
        )
    )

    interface OnSoLoadCallback {
        fun onSuccess()
        fun onFailure(e: Throwable)
    }

    fun load(context: Context, callback: OnSoLoadCallback?) {
        Thread {
            try {
                val abi = Build.SUPPORTED_ABIS.firstOrNull() ?: "armeabi-v7a"
                val isV8a = abi == "arm64-v8a"
                val soDir = File(context.filesDir, "baidu_so/$abi").apply { mkdirs() }

                for ((soName, v7aUrl, v8aUrl) in soFiles) {
                    val soFile = File(soDir, soName)
                    val url = if (isV8a) v8aUrl else v7aUrl

                    if (!soFile.exists()) {
                        Log.i(TAG, "$soName 不存在，开始下载...")
                        downloadSo(url, soFile)
                    } else {
                        Log.i(TAG, "$soName 已存在，跳过下载")
                    }

                    System.load(soFile.absolutePath)
                    Log.i(TAG, "已加载: ${soFile.name}")
                }

                callback?.onSuccess()
            } catch (e: Throwable) {
                Log.e(TAG, "SO 加载失败", e)
                callback?.onFailure(e)
            }
        }.start()
    }

    @Throws(Exception::class)
    private fun downloadSo(urlStr: String, destFile: File) {
        val url = URL(urlStr)
        val conn = url.openConnection() as HttpURLConnection
        conn.connectTimeout = 10000
        conn.readTimeout = 20000
        conn.requestMethod = "GET"
        conn.setRequestProperty("User-Agent", "Mozilla/5.0 (Android)")
        conn.setRequestProperty("Referer", "https://gitee.com/")
        conn.connect()

        if (conn.responseCode != 200) {
            throw RuntimeException("HTTP 错误码: ${conn.responseCode}")
        }

        conn.inputStream.use { input ->
            FileOutputStream(destFile).use { output ->
                val buffer = ByteArray(4096)
                var len: Int
                while (input.read(buffer).also { len = it } != -1) {
                    output.write(buffer, 0, len)
                }
            }
        }

        Log.i(TAG, "下载完成并保存: ${destFile.absolutePath}")
    }
}
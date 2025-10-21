package com.knight.kotlin.library_common.util

import android.content.Context
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import java.io.BufferedInputStream
import java.io.File
import java.io.FileInputStream
import java.io.FileNotFoundException
import java.io.FileOutputStream
import java.io.IOException
import java.net.HttpURLConnection
import java.net.URL
import java.util.concurrent.atomic.AtomicLong
import java.util.zip.ZipEntry
import java.util.zip.ZipInputStream


/**
 * @author created by luguian
 * @organize
 * @Date 2025/6/25 17:18
 * @descript:下载百度so类
 */
/**
 * @author
 * @descript: 下载并解压百度 so 库
 */
object BaiduSoDownloaderUtils {

    private const val TAG = "BaiduSoDownloader"

    private val soFiles = listOf(
//        Triple(
//            "libc++_shared.so",
//            "https://gitee.com/MengSuiXinSuoYuan/baidu-so/releases/download/Baidu_libc++_shared_v7a/libc%20%20_shared.zip",
//            "https://gitee.com/MengSuiXinSuoYuan/baidu-so/releases/download/Baidu_libc++_shared_v8a/libc%20%20_shared.zip"
//        ),
//        Triple(
//            "libtiny_magic.so",
//            "https://gitee.com/MengSuiXinSuoYuan/baidu-so/releases/download/Baidu_libtiny_magic_v7a/libtiny_magic.zip",
//            "https://gitee.com/MengSuiXinSuoYuan/baidu-so/releases/download/Baidu_libtiny_magic_v8a/libtiny_magic.zip"
//        ),
//
//        Triple(
//            "libBaiduMapSDK_base_v7_6_4.so",
//            "https://gitee.com/MengSuiXinSuoYuan/baidu-so/releases/download/Baidu_libBaiduMapSDK_v7a/libBaiduMapSDK_base_v7_6_4.zip",
//            "https://gitee.com/MengSuiXinSuoYuan/baidu-so/releases/download/Baidu_libBaiduMapSDK_v8a/libBaiduMapSDK_base_v7_6_4.zip"
//        ),



        Triple(
            "libBaiduMapSDK_map_for_navi_v7_6_6.so",
            "https://gitee.com/MengSuiXinSuoYuan/baidu-so/releases/download/Baidu_map_navi_v7a/libBaiduMapSDK_map_for_navi_v7_6_6.zip",
            "https://gitee.com/MengSuiXinSuoYuan/baidu-so/releases/download/Baidu_map_navi_v8a/libBaiduMapSDK_map_for_navi_v7_6_6.zip"
        ),
//        Triple(
//            "liblocSDK8b.so",
//            "https://gitee.com/MengSuiXinSuoYuan/baidu-so/releases/download/Baidu_location_v7a/liblocSDK8b.zip",
//            "https://gitee.com/MengSuiXinSuoYuan/baidu-so/releases/download/Baidu_location_v8a/liblocSDK8b.zip"
//        ),


//        Triple(
//            "libLaneSeg.so",
//            "https://gitee.com/MengSuiXinSuoYuan/baidu-so/releases/download/Baidu_libLaneSeg_v7a/libLaneSeg.zip",
//            "https://gitee.com/MengSuiXinSuoYuan/baidu-so/releases/download/Baidu_libLaneSeg_v8a/libLaneSeg.zip"
//        ),
        Triple(
            "libapp_BaiduNaviApplib.so",
            "https://gitee.com/MengSuiXinSuoYuan/baidu-so/releases/download/Baidu_map_navi_applib_v7a/libapp_BaiduNaviApplib.zip",
            "https://gitee.com/MengSuiXinSuoYuan/baidu-so/releases/download/Baidu_map_navi_applib_v8a/libapp_BaiduNaviApplib.zip"
        ),

//        Triple(
//            "libNative.so",
//            "https://gitee.com/MengSuiXinSuoYuan/baidu-so/releases/download/Baidu_libNative_v7a/libNative.zip",
//            "https://gitee.com/MengSuiXinSuoYuan/baidu-so/releases/download/Baidu_libNative_v8a/libNative.zip"
//        )





    )

    interface OnSoLoadCallback {
        fun onProgress(totalProgress: Int,text:String)   // 总进度回调（0~100）
        fun onSuccess()
        fun onFailure(e: Throwable)
    }

    @RequiresApi(Build.VERSION_CODES.N)
    fun load(context: Context, callback: OnSoLoadCallback?) {
        Thread {
            try {
                val abi = Build.SUPPORTED_ABIS.firstOrNull() ?: "armeabi-v7a"
                val isV8a = abi == "arm64-v8a"
                val soDir = File(context.filesDir, "baidu_so/$abi").apply { mkdirs() }

                val zipUrls = soFiles.map { if (isV8a) it.third else it.second }
                val totalBytes = zipUrls.sumOf { getRemoteFileLength(it) }
                val downloadedBytes = AtomicLong(0)

                var successLoadCount = 0

                for ((soName, v7aUrl, v8aUrl) in soFiles) {
                    val soFile = File(soDir, soName)
                    if (!soFile.exists()) {
                        val zipUrl = if (isV8a) v8aUrl else v7aUrl
                        val zipFile = File.createTempFile("so_temp_", ".zip", context.cacheDir)

                        Log.i(TAG, "开始下载: $zipUrl")

                        downloadWithTotalProgress(
                            urlStr = zipUrl,
                            destFile = zipFile,
                            soName = soName,
                            totalBytes = totalBytes,
                            downloadedBytes = downloadedBytes,
                            onTotalProgress = { progress ->
                                callback?.onProgress(progress,"正在下载"+soName)
                            }
                        )

                        unzip(zipFile, soDir)
                        zipFile.delete()
                    } else {
                        Log.i(TAG, "$soName 已存在，跳过下载")
                    }

                    if (soFile.exists()) {
                        try {
                            System.load(soFile.absolutePath)
                            Log.i(TAG, "成功加载: $soName")
                            successLoadCount++
                        } catch (e: UnsatisfiedLinkError) {
                            throw RuntimeException("加载失败: $soName", e)
                        }
                    } else {
                        throw FileNotFoundException("未找到解压后的 $soName")
                    }
                }

                if (successLoadCount == soFiles.size) {
                    callback?.onProgress(100,"加载完成") // 确保最终 100%
                    callback?.onSuccess()
                } else {
                    throw RuntimeException("部分 so 加载失败 ($successLoadCount/${soFiles.size})")
                }
            } catch (e: Throwable) {
                Log.e(TAG, "SO 加载失败", e)
                callback?.onFailure(e)
            }
        }.start()
    }

    @RequiresApi(Build.VERSION_CODES.N)
    private fun getRemoteFileLength(urlStr: String): Long {
        val conn = URL(urlStr).openConnection() as HttpURLConnection
        conn.requestMethod = "HEAD"
        conn.connectTimeout = 5000
        conn.readTimeout = 5000
        val size = conn.contentLengthLong
        conn.disconnect()
        return size
    }

    @Throws(IOException::class)
    private fun downloadWithTotalProgress(
        urlStr: String,
        destFile: File,
        soName: String,
        totalBytes: Long,
        downloadedBytes: AtomicLong,
        onTotalProgress: (progress: Int) -> Unit
    ) {
        val url = URL(urlStr)
        val conn = url.openConnection() as HttpURLConnection
        conn.connectTimeout = 10000
        conn.readTimeout = 20000
        conn.requestMethod = "GET"
        conn.setRequestProperty("User-Agent", "Mozilla/5.0 (Android)")
        conn.setRequestProperty("Referer", "https://gitee.com/")
        conn.connect()

        if (conn.responseCode != 200) {
            throw IOException("HTTP 错误码: ${conn.responseCode}")
        }

        val buffer = ByteArray(4096)
        var lastProgress = 0

        conn.inputStream.use { input ->
            FileOutputStream(destFile).use { output ->
                var len: Int
                while (input.read(buffer).also { len = it } != -1) {
                    output.write(buffer, 0, len)
                    val current = downloadedBytes.addAndGet(len.toLong())
                    val progress = (current * 100 / totalBytes).toInt()
                    if (progress != lastProgress) {
                        lastProgress = progress
                        onTotalProgress(progress)
                    }
                }
            }
        }

        Log.i(TAG, "下载完成: ${destFile.name}")
    }

    @Throws(IOException::class)
    private fun unzip(zipFile: File, targetDir: File) {
        ZipInputStream(BufferedInputStream(FileInputStream(zipFile))).use { zipInput ->
            var entry: ZipEntry?
            while (zipInput.nextEntry.also { entry = it } != null) {
                val outFile = File(targetDir, entry!!.name)
                if (entry!!.isDirectory) {
                    outFile.mkdirs()
                } else {
                    outFile.parentFile?.mkdirs()
                    FileOutputStream(outFile).use { out -> zipInput.copyTo(out) }
                }
            }
        }
    }


    fun isSoDownloaded(context: Context): Boolean {
        val abi = Build.SUPPORTED_ABIS.firstOrNull() ?: "armeabi-v7a"
        val soDir = File(context.filesDir, "baidu_so/$abi")

        for ((soName, _, _) in soFiles) {
            val soFile = File(soDir, soName)
            if (!soFile.exists()) return false
        }
        return true
    }
}
package com.knight.kotlin.library_base.utils

import android.content.Context
import android.os.Handler
import android.os.Message
import android.widget.ProgressBar
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.io.InputStream
import java.net.HttpURLConnection
import java.net.MalformedURLException
import java.net.URL

/**
 * Author:Knight
 * Time:2022/1/12 11:04
 * Description:DownLoadManagerUtils
 */
object DownLoadManagerUtils {

    /**
     *
     * 下载文件
     * @param path
     * @param pb
     * @param mHandler
     * @param context
     * @return
     */
    fun downloadApk(path: String?, pb: ProgressBar, mHandler: Handler, context: Context): File {
        val apkFile: File? = context.getExternalFilesDir("Wanandroid_APK")
        if (apkFile?.exists() == false) {
            apkFile.mkdir()
        }
        val file = File(apkFile?.absolutePath + File.separator.toString() + getApkName())
        if (!file.exists()) {
            try {
                file.createNewFile()
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
        try {
            val url = URL(path)
            val conn: HttpURLConnection = url.openConnection() as HttpURLConnection
            conn.connectTimeout = 5000
            conn.requestMethod = "GET"
            if (conn.responseCode === 200) {
                val max: Int = conn.contentLength
                pb.max = max
                var count = 0
                val inputStream: InputStream = conn.inputStream
                //保存文件
                val fos = FileOutputStream(file)
                val buffer = ByteArray(1024)
                while (true) {
                    val len: Int = inputStream.read(buffer)
                    if (len == -1) {
                        inputStream.close()
                        fos.close()
                        break
                    }
                    fos.write(buffer, 0, len)
                    count += len
                    pb.progress = count
                    val message = Message()
                    message.what = 2
                    message.arg1 = pb.getMax()
                    message.arg2 = count
                    mHandler.sendMessage(message)
                }
            }
        } catch (e: MalformedURLException) {
            e.printStackTrace()
        } catch (e: IOException) {
            e.printStackTrace()
        }
        return file
    }





    private fun getApkName(): String {
        return "wanandroid.apk"
    }

}
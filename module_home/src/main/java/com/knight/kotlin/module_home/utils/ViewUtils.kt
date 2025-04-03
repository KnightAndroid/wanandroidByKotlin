package com.knight.kotlin.module_home.utils

import android.content.Context
import android.graphics.BitmapFactory
import android.net.Uri
import java.io.InputStream


/**
 * @author created by luguian
 * @organize
 * @Date 2025/4/3 9:08
 * @descript:图像工具
 */
object ViewUtils {

    fun loadImageDimensions(context: Context, imageUri: Uri, callback: (width: Int, height: Int) -> Unit) {
        try {
            val inputStream: InputStream? = context.contentResolver.openInputStream(imageUri)
            if (inputStream != null) {
                val options = BitmapFactory.Options().apply {
                    inJustDecodeBounds = true // 只解码图片的尺寸信息，不加载像素数据
                }
                BitmapFactory.decodeStream(inputStream, null, options)
                inputStream.close()

                val width = options.outWidth
                val height = options.outHeight

                if (width > 0 && height > 0) {
                    callback(width, height)
                } else {
                    // 处理无法获取尺寸的情况，例如图片损坏或格式不支持
                    println("无法获取图片尺寸")
                }
            } else {
                println("无法打开图片流")
            }
        } catch (e: Exception) {
            e.printStackTrace()
            println("加载图片时发生错误：${e.message}")
        }
    }


}
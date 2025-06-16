package com.knight.kotlin.library_util

import android.content.Context
import android.os.Environment
import android.text.TextUtils
import java.io.File


/**
 * @author created by luguian
 * @organize
 * @Date 2024/8/23 9:33
 * @descript:文件工具类
 */
object FileUtils {

    /**
     * 返回带日期的名称
     *
     * @return String
     */
    fun getDateName(): String {
        return getDateName(null)
    }

    /**
     * 返回带日期的名称
     *
     * @param prefix 文件名前缀(会自动拼接 _ )
     * @return String
     */
    fun getDateName(prefix: String?): String {
        val dateStr = System.currentTimeMillis().toString()
        return if (!TextUtils.isEmpty(prefix)) {
            prefix + "_" + dateStr
        } else {
            dateStr
        }
    }


    /**
     * 获取Cache目录
     *
     * @param context context
     * @return File
     */
    fun getCacheDir(context: Context): File? {
        return context.externalCacheDir
    }

    /**
     * 获取Cache目录 图片
     *
     * @param context context
     * @return File
     */
    fun getCachePhotoDir(context: Context): File {
        val dir = Environment.DIRECTORY_PICTURES
        return File(getCacheDir(context), dir)
    }

    /**
     * 获取Cache目录 图片压缩
     *
     * @param context context
     * @return File
     */
    fun getCachePhotoCompressDir(context: Context): File {
        val dir = "Compress"
        return File(getCachePhotoDir(context), dir)
    }
}
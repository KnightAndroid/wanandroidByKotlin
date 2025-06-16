package com.knight.kotlin.library_util

import android.content.Context
import android.os.Environment
import com.knight.kotlin.library_common.util.FormetUtils
import java.io.File

/**
 * Author:Knight
 * Time:2022/3/31 10:03
 * Description:CacheFileUtils
 */
object CacheFileUtils {
    /**
     *
     * 获取缓存总大小
     * @param context
     * @return
     */
    fun getToalCacheSize(context: Context): String {
        var cacheSize: Long = CacheFileUtils.getFoldSize(context.cacheDir)
        if (Environment.getExternalStorageState() === Environment.MEDIA_MOUNTED) {
            cacheSize += context.externalCacheDir?.let {
                CacheFileUtils.getFoldSize(it)
            } ?: kotlin.run {
                0
            }
        }
        return FormetUtils.formetFileSize(cacheSize)
    }


    /**
     *
     * 返回文件的大小
     * getCacheDir()方法用于获取/data/data//cache目录
     * getFilesDir()方法用于获取/data/data//files目录
     * Context.getExternalFilesDir() --> SDCard/Android/data/你的应用的包名/files/目录
     * Context.getExternalCacheDir() --> SDCard/Android/data/你的应用包名/cache/目录，一般存放临时缓存数据
     * @param file
     * @return
     */
    private fun getFoldSize(file: File): Long {
        var size = 0L
        try {
            val files = file.listFiles()
            for (i in files.indices) {
                //如果下面还有文件
                size += if (files[i].isDirectory) {
                    getFoldSize(files[i])
                } else {
                    files[i].length()
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return size
    }

    /**
     *
     * 清理缓存
     * @param context
     */
    fun cleadAllCache(context: Context) {
        CacheFileUtils.deleteDir(context.cacheDir)
        if (Environment.getExternalStorageState() === Environment.MEDIA_MOUNTED) {
            CacheFileUtils.deleteDir(context.externalCacheDir)
        }
    }

    /**
     *
     * 删除目录
     * @param dir
     * @return
     */
    private fun deleteDir(dir: File?): Boolean {
        if (dir != null && dir.isDirectory) {
            val children = dir.list()
            for (i in children.indices) {
                val success = deleteDir(File(dir, children[i]))
                if (!success) {
                    return false
                }
            }
        }
        return dir!!.delete()
    }


}
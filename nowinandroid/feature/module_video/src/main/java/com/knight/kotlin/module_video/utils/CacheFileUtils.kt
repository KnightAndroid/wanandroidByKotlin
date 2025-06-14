package com.knight.kotlin.module_video.utils

import android.content.Context
import java.io.File
import java.io.IOException


/**
 * Author:Knight
 * Time:2024/4/7 10:05
 * Description:CacheFileUtils 视频文件缓存
 */
object CacheFileUtils {
    fun getVideoCacheDir(context: Context): File {
        return File(context.externalCacheDir, "video-cache")
    }

    @Throws(IOException::class)
    fun cleanVideoCacheDir(context: Context) {
        val videoCacheDir = getVideoCacheDir(context)
        cleanDirectory(videoCacheDir)
    }

    @Throws(IOException::class)
    private fun cleanDirectory(file: File) {
        if (!file.exists()) {
            return
        }
        val contentFiles = file.listFiles()
        if (contentFiles != null) {
            for (contentFile in contentFiles) {
                delete(contentFile)
            }
        }
    }

    @Throws(IOException::class)
    private fun delete(file: File) {
        if (file.isFile && file.exists()) {
            deleteOrThrow(file)
        } else {
            cleanDirectory(file)
            deleteOrThrow(file)
        }
    }

    @Throws(IOException::class)
    private fun deleteOrThrow(file: File) {
        if (file.exists()) {
            val isDeleted = file.delete()
            if (!isDeleted) {
                throw IOException(String.format("File %s can't be deleted", file.absolutePath))
            }
        }
    }



}
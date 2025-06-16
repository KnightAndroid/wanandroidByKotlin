package com.knight.kotlin.library_common.util

import java.text.DecimalFormat

/**
 * Author:Knight
 * Time:2022/1/12 14:33
 * Description:FormetUtils
 */
object FormetUtils {

    /**
     *
     * 格式化长度
     * @param files
     * @return
     */
    fun formetFileSize(files: Long): String {
        val df = DecimalFormat("#.00")
        var fileSizeString = ""
        val wrongSize = "0B"
        if (files == 0L) {
            return wrongSize
        }
        fileSizeString = when {
            files < 1024 -> {
                df.format(files.toDouble()).toString() + "B"
            }
            files < 1048576 -> {
                df.format(files.toDouble() / 1024).toString() + "KB"
            }
            files < 1073741824 -> {
                df.format(files.toDouble() / 1048576).toString() + "MB"
            }
            else -> {
                df.format(files.toDouble() / 1073741824).toString() + "GB"
            }
        }
        return fileSizeString
    }

}
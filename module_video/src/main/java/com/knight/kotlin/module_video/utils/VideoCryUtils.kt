package com.knight.kotlin.module_video.utils

import com.knight.library.cryption.AesUtils

/**
 * Author:Knight
 * Time:2024/3/4 17:30
 * Description:VideoCryUtils
 */
object VideoCryUtils {

    /**
     * 移除前缀并且解密
     * @param cipherUrl
     * @return
     */
    fun removePrefixToDecry(cipherUrl:String) :String {
        val  prefixToRemove = "ftp://" // 要移除的前缀
        if (cipherUrl.startsWith(prefixToRemove)) {
            val startIndex = prefixToRemove.length
            val result = cipherUrl.substring (startIndex)
            return AesUtils.decrypt(result) ?: ""
        } else {
            return cipherUrl
        }

    }
}
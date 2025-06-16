package com.knight.library.cryption

/**
 * Author:Knight
 * Time:2024/1/26 16:50
 * Description:SignatureUtils
 */
object SignatureUtils {

    init {
        System.loadLibrary("md5")
    }

    /**
     * native 方法签名参数
     * @return
     */
    external fun signatureParams(params:String): String

}
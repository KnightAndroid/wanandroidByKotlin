package com.knight.library.cryption

import java.io.IOException
import java.io.UnsupportedEncodingException
import java.security.InvalidKeyException
import java.security.NoSuchAlgorithmException
import javax.crypto.BadPaddingException
import javax.crypto.IllegalBlockSizeException
import javax.crypto.NoSuchPaddingException

/**
 * Author:Knight
 * Time:2024/1/26 16:08
 * Description:AesUtils
 */
object AesUtils {

    init {
        System.loadLibrary("ctyption")
    }
    /**
     * base64编码
     * @param buf 待编码字节
     * @return 结果
     */
    @Throws(UnsupportedEncodingException::class)
    external fun string2Base64(buf: ByteArray?): String?


    /**
     * base64解码
     * @param hexString 待解码字符串
     * @return 结果
     */
    external fun base642Byte(hexString: String?): ByteArray?

    /**
     * AES加密
     * @param src 待加密字符串
     * @return 结果
     */
    @Throws(
        UnsupportedEncodingException::class,
        IllegalBlockSizeException::class,
        InvalidKeyException::class,
        BadPaddingException::class,
        NoSuchAlgorithmException::class,
        NoSuchPaddingException::class
    )
    external fun encrypt(src: String?): String?

    /**
     * AES解密
     * @param encrypted 待解密串
     * @return 结果
     */
    @Throws(
        IOException::class,
        IllegalBlockSizeException::class,
        InvalidKeyException::class,
        BadPaddingException::class,
        NoSuchAlgorithmException::class,
        NoSuchPaddingException::class
    )
    external fun decrypt(encrypted: String?): String?

    /**
     * 获取native 层密钥
     * @return 密钥
     */
    external fun getAESKey(): String?

    /**
     * 设置native层密钥
     * @param key 密钥
     */
    external fun setAESKey(key: String?)

}
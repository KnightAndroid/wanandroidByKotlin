package com.knight.kotlin.library_network.util

import android.util.Base64
import java.util.Arrays
import javax.crypto.Cipher
import javax.crypto.spec.IvParameterSpec
import javax.crypto.spec.SecretKeySpec


/**
 * @author created by luguian
 * @organize
 * @Date 2024/12/9 17:20
 * @descript:AES帮助类
 */
object AesUtils {
    private const val LENGTH = 32
    private val KEY_BYTE by lazy {
        Base64.decode(
            "DmA1G5g5jq27L6OWbxapKln3CJ7HlyNBB6yOClNAMN6=", Base64.DEFAULT
        )
    }

    fun encrypt(encryptString: String): String {
        var inputBytes = encryptString.toByteArray(Charsets.UTF_8)
        val alignNumber = LENGTH - inputBytes.size.rem(LENGTH)
        repeat(alignNumber) {
            inputBytes += alignNumber.toByte()
        }
        return try {
            val cipher = Cipher.getInstance("AES/CBC/NoPadding")
            val secretKeySpec = SecretKeySpec(KEY_BYTE, "AES")
            val ivParameterSpec = IvParameterSpec(KEY_BYTE, 0, 16)
            cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec, ivParameterSpec)
            Base64.encodeToString(cipher.doFinal(inputBytes), 0)
        } catch (exception: Exception) {
            exception.printStackTrace()
            throw Exception("")
        }
    }

    fun decrypt(decryptString: String): String {
        return try {
            val cipher = Cipher.getInstance("AES/CBC/NoPadding")
            val secretKeySpec = SecretKeySpec(KEY_BYTE, "AES")
            val ivParameterSpec = IvParameterSpec(Arrays.copyOfRange(KEY_BYTE, 0, 16))
            cipher.init(Cipher.DECRYPT_MODE, secretKeySpec, ivParameterSpec)
            val result = cipher.doFinal(Base64.decode(decryptString, Base64.DEFAULT))
            val lastByte = result.last()
            if (lastByte < 1 || lastByte > 32) {
                Arrays.copyOfRange(result, 0, result.size).toString(Charsets.UTF_8)
            } else {
                Arrays.copyOfRange(result, 0, result.size - lastByte).toString(Charsets.UTF_8)
            }
        } catch (exception: Exception) {
            exception.printStackTrace()
            throw exception
        }
    }
}
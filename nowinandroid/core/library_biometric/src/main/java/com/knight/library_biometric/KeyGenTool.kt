package com.knight.library_biometric

import android.content.Context
import android.os.Build
import android.security.keystore.KeyGenParameterSpec
import android.security.keystore.KeyProperties
import androidx.annotation.RequiresApi
import java.security.KeyStore
import javax.crypto.Cipher
import javax.crypto.KeyGenerator
import javax.crypto.SecretKey
import javax.crypto.spec.IvParameterSpec

/**
 *
 * @ProjectName:    wanandroid
 * @Package:        com.knight.library_biometric
 * @ClassName:      KeyGenTool
 * @Description:    java类作用描述
 * @Author:         knight
 * @CreateDate:     2022/3/24 10:21 上午
 * @UpdateUser:     更新者
 * @UpdateDate:     2022/3/24 10:21 上午
 * @UpdateRemark:   更新说明
 * @Version:        1.0
 */

class KeyGenTool constructor(mContext: Context) {
    private val ANDROID_KEY_STORE = "AndroidKeyStore"
    private val KEY_ALGORITHM = KeyProperties.KEY_ALGORITHM_AES
    private val KEY_BLOCK_MODE = KeyProperties.BLOCK_MODE_CBC
    private val KEY_PADDING = KeyProperties.ENCRYPTION_PADDING_PKCS7
    private val TRANSFORMATION = "$KEY_ALGORITHM/$KEY_BLOCK_MODE/$KEY_PADDING"
    private var KEY_ALIAS: String? = null

    init {
        KEY_ALIAS = mContext.packageName + "fingerprint"
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    fun getEncryptCipher(): Cipher? {
        var cipher: Cipher? = null
        try {
            cipher = Cipher.getInstance(TRANSFORMATION)
            cipher.init(Cipher.ENCRYPT_MODE, getKey())
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return cipher
    }

    /**
     * 获取解密的cipher
     * 加密cipher的一些参数
     * 包括initialize vector(AES加密中 以CBC模式加密需要一个初始的数据块，解密时同样需要这个初始块)
     * @return
     */
    @RequiresApi(api = Build.VERSION_CODES.M)
    fun getDecryptCipher(initializeVector: ByteArray?): Cipher? {
        var cipher: Cipher? = null
        try {
            cipher = Cipher.getInstance(TRANSFORMATION)
            val ivParameterSpec = IvParameterSpec(initializeVector)
            cipher.init(Cipher.DECRYPT_MODE, getKey(), ivParameterSpec)
        } catch (e: java.lang.Exception) {
            e.printStackTrace()
        }
        return cipher
    }

    /**
     * 获取key，首先从秘钥库中根据别名获取key，如果秘钥库中不存在，则创建一个key，并存入秘钥库中
     * @return
     * @throws Exception
     */
    @RequiresApi(api = Build.VERSION_CODES.M)
    @Throws(java.lang.Exception::class)
    private fun getKey(): SecretKey? {
        var secretKey: SecretKey? = null
        val keyStore = KeyStore.getInstance(ANDROID_KEY_STORE)
        keyStore.load(null)
        if (keyStore.isKeyEntry(KEY_ALIAS)) {
            val secretKeyEntry = keyStore.getEntry(KEY_ALIAS, null) as KeyStore.SecretKeyEntry
            secretKey = secretKeyEntry.secretKey
        } else {
            secretKey = createKey()
        }
        return secretKey
    }

    /**
     * 在Android中，key的创建之后必须存储在秘钥库才能使用
     * @return
     * @throws Exception
     */
    @RequiresApi(api = Build.VERSION_CODES.M)
    @Throws(java.lang.Exception::class)
    private fun createKey(): SecretKey? {
        //在创建KeyGenerator的时候，第二个参数指定provider为AndroidKeyStore，这样创建的key就会被存放在这个秘钥库中
        val keyGenerator: KeyGenerator =
            KeyGenerator.getInstance(KEY_ALGORITHM, ANDROID_KEY_STORE)
        val spec = KeyGenParameterSpec.Builder(
            KEY_ALIAS!!,
            KeyProperties.PURPOSE_ENCRYPT or KeyProperties.PURPOSE_DECRYPT
        )
            .setBlockModes(KEY_BLOCK_MODE)
            .setEncryptionPaddings(KEY_PADDING) //这个设置为true，表示这个key必须是通过了用户认证才可以使用
            .setUserAuthenticationRequired(true)
            .build()
        keyGenerator.init(spec)
        return keyGenerator.generateKey()
    }

}
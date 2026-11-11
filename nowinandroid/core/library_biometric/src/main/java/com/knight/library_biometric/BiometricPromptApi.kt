package com.knight.library_biometric

import android.os.CancellationSignal
import android.util.Base64
import androidx.biometric.BiometricManager
import androidx.biometric.BiometricPrompt
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentActivity
import com.knight.kotlin.library_common.util.CacheUtils
import javax.crypto.Cipher

/**
 * 基于 androidx.biometric 的统一实现，兼容所有 API 级别。
 * 替代原先分别针对 FingerprintManager (API 23-29) 和
 * android.hardware.biometrics.BiometricPrompt (API 28+) 的两套实现。
 */
internal class BiometricPromptApi(
    private val builder: BiometricPromptManager.Builder
) : IBiometricPromptImpl {

    private val mActivity: FragmentActivity by lazy {
        builder.mActivity.get() as FragmentActivity
    }
    private var mBiometricPrompt: BiometricPrompt? = null
    private var mManagerIdentifyCallback: BiometricPromptManager.OnBiometricIdentifyCallback? = null
    private lateinit var mCancellationSignal: CancellationSignal

    init {
        mBiometricPrompt = BiometricPrompt(
            mActivity,
            ContextCompat.getMainExecutor(mActivity),
            object : BiometricPrompt.AuthenticationCallback() {
                override fun onAuthenticationError(
                    errorCode: Int,
                    errString: CharSequence
                ) {
                    super.onAuthenticationError(errorCode, errString)
                    when (errorCode) {
                        // 用户点击了 negative button（"使用密码"按钮）
                        BiometricPrompt.ERROR_NEGATIVE_BUTTON ->
                            mManagerIdentifyCallback?.onUsePassword()
                        // 用户主动取消或系统取消
                        BiometricPrompt.ERROR_USER_CANCELED,
                        BiometricPrompt.ERROR_CANCELED ->
                            mManagerIdentifyCallback?.onCancel()
                        // 其他错误
                        else ->
                            mManagerIdentifyCallback?.onError(
                                errorCode,
                                errString.toString()
                            )
                    }
                }

                override fun onAuthenticationFailed() {
                    super.onAuthenticationFailed()
                    mManagerIdentifyCallback?.onFailed()
                }

                override fun onAuthenticationSucceeded(
                    result: BiometricPrompt.AuthenticationResult
                ) {
                    super.onAuthenticationSucceeded(result)
                    mManagerIdentifyCallback?.onSucceeded(
                        result.cryptoObject?.cipher
                    )
                }
            }
        )
    }

    override fun authenticate(
        loginFlg: Boolean,
        cancel: CancellationSignal,
        callback: BiometricPromptManager.OnBiometricIdentifyCallback
    ) {
        mManagerIdentifyCallback = callback
        mCancellationSignal = cancel

        val promptInfo = buildPromptInfo()

        val keyGenTool = KeyGenTool(mActivity)
        val cryptoObject: BiometricPrompt.CryptoObject? = try {
            if (loginFlg) {
                // 解密模式
                val ivStr: String = CacheUtils.getCliperIv()
                val iv = Base64.decode(ivStr, Base64.URL_SAFE)
                keyGenTool.getDecryptCipher(iv)?.let { BiometricPrompt.CryptoObject(it) }
            } else {
                // 加密模式
                keyGenTool.getEncryptCipher()?.let { BiometricPrompt.CryptoObject(it) }
            }
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }

        try {
            if (cryptoObject != null) {
                mBiometricPrompt?.authenticate(promptInfo, cryptoObject)
            } else {
                mBiometricPrompt?.authenticate(promptInfo)
            }
        } catch (e: Exception) {
            e.printStackTrace()
            mManagerIdentifyCallback?.onError(-1, e.message)
        }
    }

    private fun buildPromptInfo(): BiometricPrompt.PromptInfo {
        // 注意：setNegativeButtonText 和 setDeviceCredentialAllowed(true) /
        // setAllowedAuthenticators(DEVICE_CREDENTIAL) 不能同时使用，否则会在运行时崩溃。
        // 这里保持和原有 BiometricPromptApiP 一致的行为：使用 negativeButton，
        // 限定为 BIOMETRIC_STRONG（指纹/面容等强生物识别）。
        val negativeText = builder.mNegativeText
            ?: mActivity.getString(R.string.biometric_not_open_touchverify)

        return BiometricPrompt.PromptInfo.Builder()
            .setTitle(builder.mTitle ?: "")
            .setDescription(builder.mDesc ?: "")
            .setNegativeButtonText(negativeText)
            .setAllowedAuthenticators(BiometricManager.Authenticators.BIOMETRIC_STRONG)
            .build()
    }
}

package com.knight.library_biometric

import android.hardware.biometrics.BiometricPrompt
import android.os.Build
import android.os.CancellationSignal
import android.util.Base64
import androidx.annotation.RequiresApi
import androidx.fragment.app.FragmentActivity
import com.knight.kotlin.library_common.util.CacheUtils

/**
 *
 * @ProjectName:    wanandroid
 * @Package:        com.knight.library_biometric.utils
 * @ClassName:      BiometricPromptApiP
 * @Description:    java类作用描述
 * @Author:         knight
 * @CreateDate:     2022/3/24 10:26 上午
 * @UpdateUser:     更新者
 * @UpdateDate:     2022/3/24 10:26 上午
 * @UpdateRemark:   更新说明
 * @Version:        1.0
 */

@RequiresApi(Build.VERSION_CODES.P)
class BiometricPromptApiP constructor(builder:BiometricPromptManager.Builder) : IBiometricPromptImpl{
    private val mActivity = builder.mActivity.get() as FragmentActivity
    private var mBiometricPrompt: BiometricPrompt? = null
    private var mManagerIdentifyCallback: BiometricPromptManager.OnBiometricIdentifyCallback? = null
    private lateinit var mCancellationSignal: CancellationSignal

    init {
        mBiometricPrompt = BiometricPrompt.Builder(mActivity)
            .setTitle(builder.mTitle ?: "")
            .setDescription(builder.mDesc ?: "")
            .setSubtitle("")
            .setNegativeButton(
                builder.mNegativeText!!,
                mActivity.mainExecutor
            ) { dialogInterface, i ->

                mManagerIdentifyCallback?.onUsePassword()
                mCancellationSignal.cancel()
            }
            .build()
    }

    override fun authenticate(
        loginFlg: Boolean,
        cancel: CancellationSignal,
        callback: BiometricPromptManager.OnBiometricIdentifyCallback
    ) {
        mManagerIdentifyCallback = callback
        mCancellationSignal = cancel
        mCancellationSignal = CancellationSignal()
        mCancellationSignal.setOnCancelListener(CancellationSignal.OnCancelListener { })

        val mKeyGenTool = KeyGenTool(mActivity)
        val mCryptoObject: BiometricPrompt.CryptoObject
        if (loginFlg) {
            //解密
            try {
                /**
                 * 可通过服务器保存iv,然后在使用之前从服务器获取
                 */
                //保存用于做AES-CBC
                val ivStr: String = CacheUtils.getCliperIv()
                val iv = Base64.decode(ivStr, Base64.URL_SAFE)
                mCryptoObject = BiometricPrompt.CryptoObject(mKeyGenTool.getDecryptCipher(iv)!!)
                mBiometricPrompt?.authenticate(
                    mCryptoObject,
                    mCancellationSignal,
                    mActivity.mainExecutor,
                    BiometricPromptCallbackImpl()
                )
            } catch (e: Exception) {
                e.printStackTrace()
            }
        } else {
            //加密
            try {
                mCryptoObject = BiometricPrompt.CryptoObject(mKeyGenTool.getEncryptCipher()!!)
                mBiometricPrompt!!.authenticate(
                    mCryptoObject,
                    mCancellationSignal,
                    mActivity.mainExecutor,
                    BiometricPromptCallbackImpl()
                )
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }

    }

    @RequiresApi(Build.VERSION_CODES.P)
    private inner class BiometricPromptCallbackImpl : BiometricPrompt.AuthenticationCallback() {
        override fun onAuthenticationError(errorCode: Int, errString: CharSequence) {
            super.onAuthenticationError(errorCode, errString)
            mCancellationSignal.cancel()
        }

        override fun onAuthenticationHelp(helpCode: Int, helpString: CharSequence) {
            super.onAuthenticationHelp(helpCode, helpString)
        }

        override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult) {
            super.onAuthenticationSucceeded(result)
            mManagerIdentifyCallback?.onSucceeded(result.cryptoObject.cipher)
            mCancellationSignal.cancel()
        }

        override fun onAuthenticationFailed() {
            super.onAuthenticationFailed()
        }
    }


}
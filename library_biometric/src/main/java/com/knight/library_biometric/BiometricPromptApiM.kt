package com.knight.library_biometric

import android.content.Context
import android.hardware.fingerprint.FingerprintManager
import android.os.Build
import android.os.CancellationSignal
import android.util.Base64
import androidx.annotation.RequiresApi
import androidx.fragment.app.FragmentActivity
import com.knight.kotlin.library_base.util.CacheUtils
import com.knight.library_biometric.dialog.BiometricPromptDialog

/**
 *
 * @ProjectName:    wanandroid
 * @Package:        com.knight.library_biometric
 * @ClassName:      BiometricPromptApiM
 * @Description:    java类作用描述
 * @Author:         knight
 * @CreateDate:     2022/3/24 10:26 上午
 * @UpdateUser:     更新者
 * @UpdateDate:     2022/3/24 10:26 上午
 * @UpdateRemark:   更新说明
 * @Version:        1.0
 */

@RequiresApi(Build.VERSION_CODES.M)
class BiometricPromptApiM constructor(activity:FragmentActivity) : IBiometricPromptImpl {
    private val mActivity: FragmentActivity = activity
    private var mDialog: BiometricPromptDialog? = null
    private var mFingerprintManager: FingerprintManager
    private var mCancellationSignal: CancellationSignal? = null
    private var mManagerIdentifyCallback: BiometricPromptManager.OnBiometricIdentifyCallback? = null
    private val mFmAuthCallback: FingerprintManager.AuthenticationCallback =
        FingerprintManageCallbackImpl()

    init {
        mFingerprintManager = getFingerprintManager(activity)
    }

    @RequiresApi(Build.VERSION_CODES.M)
    override fun authenticate(
        loginFlg: Boolean,
        cancel: CancellationSignal,
        callback: BiometricPromptManager.OnBiometricIdentifyCallback
    ) {
        //指纹识别的回调
        mManagerIdentifyCallback = callback

        mDialog = BiometricPromptDialog.newInstance()
        mDialog?.setOnBiometricPromptDialogActionCallback(object :
            BiometricPromptDialog.OnBiometricPromptDialogActionCallback {
            override fun onDialogDismiss() {
                //当dialog消失的时候，包括点击userPassword、点击cancel、和识别成功之后
                mCancellationSignal?.let {
                    if(!it.isCanceled) {
                        it.cancel()
                    }
                }
                mDialog = null
            }

            override fun onCancel() {
                //点击cancel键
                mManagerIdentifyCallback?.let {
                    it.onCancel()
                }
            }
        })
        mDialog?.showAllowingStateLoss(mActivity?.supportFragmentManager, "BiometricPromptApiM")

        mCancellationSignal = cancel
        if (mCancellationSignal == null) {
            mCancellationSignal = CancellationSignal()
        }
        mCancellationSignal?.setOnCancelListener(CancellationSignal.OnCancelListener { mDialog?.dismiss() })

        val mKeyGenTool = KeyGenTool(mActivity)
        val mCryptoObject: FingerprintManager.CryptoObject
        if (loginFlg) {
            //解密
            try {
                /**
                 * 可通过服务器保存iv,然后在使用之前从服务器获取
                 */
                val ivStr: String = CacheUtils.getCliperIv()
                val iv = Base64.decode(ivStr, Base64.URL_SAFE)
                mCryptoObject = FingerprintManager.CryptoObject(mKeyGenTool.getDecryptCipher(iv)!!)
                getFingerprintManager(mActivity).authenticate(
                    mCryptoObject, mCancellationSignal,
                    0, mFmAuthCallback, null
                )
            } catch (e: Exception) {
                e.printStackTrace()
            }
        } else {
            //加密
            try {
                mCryptoObject = FingerprintManager.CryptoObject(mKeyGenTool.getEncryptCipher()!!)
                getFingerprintManager(mActivity).authenticate(
                    mCryptoObject, mCancellationSignal,
                    0, mFmAuthCallback, null
                )
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private inner class FingerprintManageCallbackImpl : FingerprintManager.AuthenticationCallback() {
        @RequiresApi(api = Build.VERSION_CODES.M)
        override fun onAuthenticationError(errorCode: Int, errString: CharSequence) {
            super.onAuthenticationError(errorCode, errString)
            mDialog?.setState(BiometricPromptDialog.STATE_ERROR)
            mManagerIdentifyCallback?.onError(errorCode, errString.toString())
        }

        @RequiresApi(api = Build.VERSION_CODES.M)
        override fun onAuthenticationFailed() {
            super.onAuthenticationFailed()
            mDialog?.setState(BiometricPromptDialog.STATE_FAILED)
            mManagerIdentifyCallback?.onFailed()
        }

        override fun onAuthenticationHelp(helpCode: Int, helpString: CharSequence) {
            super.onAuthenticationHelp(helpCode, helpString)
        }

        @RequiresApi(api = Build.VERSION_CODES.M)
        override fun onAuthenticationSucceeded(result: FingerprintManager.AuthenticationResult) {
            super.onAuthenticationSucceeded(result)
            mDialog?.setState(BiometricPromptDialog.STATE_SUCCEED)
            mManagerIdentifyCallback?.onSucceeded(result.cryptoObject.cipher)
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private fun getFingerprintManager(context: Context): FingerprintManager {
        if (mFingerprintManager == null) {
            mFingerprintManager = context.getSystemService(FingerprintManager::class.java)
        }
        return mFingerprintManager
    }
}
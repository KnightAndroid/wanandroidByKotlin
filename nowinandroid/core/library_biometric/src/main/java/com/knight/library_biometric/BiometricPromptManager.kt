package com.knight.library_biometric

import android.os.CancellationSignal
import androidx.fragment.app.FragmentActivity
import com.knight.library_biometric.utils.BiometricUtils
import java.lang.ref.WeakReference
import javax.crypto.Cipher

/**
 * 生物识别（指纹/面容等）管理入口。
 * 内部统一使用 androidx.biometric 实现，兼容所有 API 级别。
 */
class BiometricPromptManager constructor(builder: Builder) {
    private var mImpl: IBiometricPromptImpl? = null

    init {
        // 统一使用 androidx.biometric 的兼容实现，替代原本
        // 针对 API 23-29 (FingerprintManager) 和 API 28+
        // (android.hardware.biometrics.BiometricPrompt) 的两套原生方案
        mImpl = BiometricPromptApi(builder)
    }

    interface OnBiometricIdentifyCallback {
        /**
         * 使用密码
         */
        fun onUsePassword()

        /**
         * 成功
         * @param cipher
         */
        fun onSucceeded(cipher: Cipher?)

        /**
         * 失败
         */
        fun onFailed()

        /**
         * 错误
         * @param code
         * @param reason
         */
        fun onError(code: Int, reason: String?)

        /**
         * 取消
         */
        fun onCancel()
    }

    fun authenticate(loginFlg: Boolean, callback: OnBiometricIdentifyCallback) {
        mImpl?.authenticate(loginFlg, CancellationSignal(), callback)
    }

    fun authenticate(
        cancel: CancellationSignal,
        callback: OnBiometricIdentifyCallback
    ) {
        // TODO: 2019/1/7
        //        mImpl.authenticate(cancel, callback);
    }

    class Builder constructor(activty: WeakReference<FragmentActivity>) {
        //标题
        var mTitle: String? = null

        //标题描述
        var mDesc: String? = null

        //取消按钮描述
        var mNegativeText: String? = null

        //activity
        var mActivity: WeakReference<FragmentActivity> = activty
        fun setTitle(title: String?): Builder {
            mTitle = title
            return this
        }

        fun setDesc(desc: String?): Builder {
            mDesc = desc
            return this
        }

        fun setNegativeText(negativeText: String?): Builder {
            mNegativeText = negativeText
            return this
        }

        fun build(): BiometricPromptManager {
            return BiometricPromptManager(this)
        }
    }
}

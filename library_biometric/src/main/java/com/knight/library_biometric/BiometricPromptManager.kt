package com.knight.library_biometric

import android.os.CancellationSignal
import androidx.fragment.app.FragmentActivity
import com.knight.library_biometric.utils.BiometricUtils
import javax.crypto.Cipher

/**
 *
 * @ProjectName:    wanandroid
 * @Package:        com.knight.library_biometric
 * @ClassName:      BiometricPromptManager
 * @Description:    java类作用描述
 * @Author:         knight
 * @CreateDate:     2022/3/24 10:27 上午
 * @UpdateUser:     更新者
 * @UpdateDate:     2022/3/24 10:27 上午
 * @UpdateRemark:   更新说明
 * @Version:        1.0
 */

class BiometricPromptManager constructor(builder: Builder) {
    private var mImpl: IBiometricPromptImpl? = null
    private var mActivity: FragmentActivity? = null

    init {
        mActivity = builder.mActivity
        if (BiometricUtils.isAboveApiP()) {
            mActivity?.let {
                mImpl = BiometricPromptApiP(it, builder)
            }
        } else if (BiometricUtils.isAboveApiM()) {
            mActivity?.let {
                mImpl = BiometricPromptApiM(it)
            }

        }

    }
    interface OnBiometricIdentifyCallback {
        /**
         *
         * 使用密码
         */
        fun onUsePassword()

        /**
         *
         * 成功
         * @param cipher
         */
        fun onSucceeded(cipher: Cipher?)

        /**
         *
         * 失败
         */
        fun onFailed()

        /**
         *
         * 错误
         * @param code
         * @param reason
         */
        fun onError(code: Int, reason: String?)

        /**
         *
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


    class Builder {
        //标题
        var mTitle: String? = null

        //标题描述
        var mDesc: String? = null

        //取消按钮描述
        var mNegativeText: String? = null

        //activity
        var mActivity: FragmentActivity? = null
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

        fun setActivity(mActivity: FragmentActivity): Builder {
            this.mActivity = mActivity
            return this
        }



        fun build(): BiometricPromptManager {
            return BiometricPromptManager(this)
        }
    }
}
package com.knight.library_biometric.control

import androidx.fragment.app.FragmentActivity
import com.knight.library_biometric.BiometricPromptManager
import com.knight.library_biometric.R
import com.knight.library_biometric.utils.BiometricUtils
import javax.crypto.Cipher

/**
 *
 * @ProjectName:    wanandroid
 * @Package:        com.knight.library_biometric
 * @ClassName:      BiometricControl
 * @Description:    java类作用描述
 * @Author:         knight
 * @CreateDate:     2022/3/23 4:43 下午
 * @UpdateUser:     更新者
 * @UpdateDate:     2022/3/23 4:43 下午
 * @UpdateRemark:   更新说明
 * @Version:        1.0
 */

class BiometricControl {
    interface BiometricStatusCallback {
        /**
         *
         * 取消或者使用密码
         */
        fun onUsePassword()

        /**
         *
         * 验证成功
         */
        fun onVerifySuccess(cipher: Cipher?)

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
        fun error(code: Int, reason: String?)

        /**
         *
         * 取消
         */
        fun onCancel()
    }

    companion object {
        /**
         *
         * 开启指纹解锁
         */
        fun openBlomtric(
            activity: FragmentActivity,
            mBiometricStatusCallback: BiometricStatusCallback
        ) {
            if (BiometricUtils.isBiometricPromptEnable(activity)) {
                BiometricPromptManager.Builder()
                    .setActivity(activity)
                    .setTitle(activity.getString(R.string.biometric_touch_verify_finger))
                    .setDesc(activity.getString(R.string.biometric_touch_sensor))
                    .setNegativeText(activity.getString(R.string.biometric_not_open_touchverify))
                    .build()
                    .authenticate(false, object : BiometricPromptManager.OnBiometricIdentifyCallback {
                        override fun onUsePassword() {
                            mBiometricStatusCallback.onUsePassword()
                        }

                        override fun onSucceeded(cipher: Cipher?) {
                            mBiometricStatusCallback.onVerifySuccess(cipher)
                        }

                        override fun onFailed() {
                            mBiometricStatusCallback.onFailed()
                        }

                        override fun onError(code: Int, reason: String?) {
                            mBiometricStatusCallback.error(code, reason)
                        }

                        override fun onCancel() {
                            mBiometricStatusCallback.onCancel()
                        }
                    })
            } else {
                mBiometricStatusCallback.onFailed()
            }
        }

        /**
         *
         * 通过指纹识别进行登录
         * @param activity
         * @param mBiometricStatusCallback
         */
        fun loginBlomtric(
            activity: FragmentActivity,
            mBiometricStatusCallback: BiometricStatusCallback
        ) {
            if (BiometricUtils.isBiometricPromptEnable(activity)) {
                BiometricPromptManager.Builder()
                    .setActivity(activity)
                    .setTitle(activity.getString(R.string.biometric_touch_verify_finger))
                    .setDesc(activity.getString(R.string.biometric_touch_sensor))
                    .setNegativeText(activity.getString(R.string.biometric_use_password_login))
                    .build()
                    .authenticate(true, object : BiometricPromptManager.OnBiometricIdentifyCallback {
                        override fun onUsePassword() {
                            mBiometricStatusCallback.onUsePassword()
                        }

                        override fun onSucceeded(cipher: Cipher?) {
                            mBiometricStatusCallback.onVerifySuccess(cipher)
                        }

                        override fun onFailed() {
                            mBiometricStatusCallback.onFailed()
                        }

                        override fun onError(code: Int, reason: String?) {
                            mBiometricStatusCallback.error(code, reason)
                        }

                        override fun onCancel() {
                            mBiometricStatusCallback.onCancel()
                        }
                    })
            } else {
                mBiometricStatusCallback.onFailed()
            }
        }



    }
}
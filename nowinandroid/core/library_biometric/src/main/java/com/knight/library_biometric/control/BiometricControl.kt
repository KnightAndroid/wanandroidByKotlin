package com.knight.library_biometric.control

import androidx.fragment.app.FragmentActivity
import com.knight.library_biometric.BiometricPromptManager
import com.knight.library_biometric.R
import com.knight.library_biometric.listener.BiometricStatusCallback
import com.knight.library_biometric.utils.BiometricUtils
import java.lang.ref.WeakReference
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



    companion object {
        private var mBiometricStatusCallback: BiometricStatusCallback?=null

        fun setStatusCallback(mBiometricStatusCallback: BiometricStatusCallback): Companion {
            this.mBiometricStatusCallback = mBiometricStatusCallback
            return BiometricControl
        }
        /**
         *
         * 开启指纹解锁
         */
        fun openBlomtric(
            activity: FragmentActivity,
        ) {
            if (BiometricUtils.isBiometricPromptEnable(activity)) {
                BiometricPromptManager.Builder(WeakReference<FragmentActivity>(activity))
                    .setTitle(activity.getString(R.string.biometric_touch_verify_finger))
                    .setDesc(activity.getString(R.string.biometric_touch_sensor))
                    .setNegativeText(activity.getString(R.string.biometric_not_open_touchverify))
                    .build()
                    .authenticate(false, object : BiometricPromptManager.OnBiometricIdentifyCallback {
                        override fun onUsePassword() {
                            mBiometricStatusCallback?.onUsePassword()
                        }

                        override fun onSucceeded(cipher: Cipher?) {
                            mBiometricStatusCallback?.onVerifySuccess(cipher)
                        }

                        override fun onFailed() {
                            mBiometricStatusCallback?.onFailed()
                        }

                        override fun onError(code: Int, reason: String?) {
                            mBiometricStatusCallback?.error(code, reason)
                        }

                        override fun onCancel() {
                            mBiometricStatusCallback?.onCancel()
                        }
                    })
            } else {
                mBiometricStatusCallback?.onFailed()
            }
        }

        fun setunListener(){
            mBiometricStatusCallback = null
        }

        /**
         *
         * 通过指纹识别进行登录
         * @param activity
         * @param mBiometricStatusCallback
         */
        fun loginBlomtric(
            activity: FragmentActivity,
        ) {
            if (BiometricUtils.isBiometricPromptEnable(activity)) {
                BiometricPromptManager.Builder(WeakReference<FragmentActivity>(activity))
                    .setTitle(activity.getString(R.string.biometric_touch_verify_finger))
                    .setDesc(activity.getString(R.string.biometric_touch_sensor))
                    .setNegativeText(activity.getString(R.string.biometric_use_password_login))
                    .build()
                    .authenticate(true, object : BiometricPromptManager.OnBiometricIdentifyCallback {
                        override fun onUsePassword() {
                            mBiometricStatusCallback?.onUsePassword()
                        }

                        override fun onSucceeded(cipher: Cipher?) {
                            mBiometricStatusCallback?.onVerifySuccess(cipher)
                        }

                        override fun onFailed() {
                            mBiometricStatusCallback?.onFailed()
                        }

                        override fun onError(code: Int, reason: String?) {
                            mBiometricStatusCallback?.error(code, reason)
                        }

                        override fun onCancel() {
                            mBiometricStatusCallback?.onCancel()
                        }
                    })
            } else {
                mBiometricStatusCallback?.onFailed()
            }
        }



    }
}
package com.knight.library_biometric

import android.os.CancellationSignal

/**
 *
 * @ProjectName:    wanandroid
 * @Package:        com.knight.library_biometric
 * @ClassName:      IBiometricPromptImpl
 * @Description:    java类作用描述
 * @Author:         knight
 * @CreateDate:     2022/3/24 10:27 上午
 * @UpdateUser:     更新者
 * @UpdateDate:     2022/3/24 10:27 上午
 * @UpdateRemark:   更新说明
 * @Version:        1.0
 */

interface IBiometricPromptImpl {
    fun authenticate(
        loginFlg: Boolean, cancel: CancellationSignal,
        callback: BiometricPromptManager.OnBiometricIdentifyCallback
    )
}
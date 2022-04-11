package com.knight.library_biometric.listener

import javax.crypto.Cipher

/**
 * Author:Knight
 * Time:2022/4/8 14:24
 * Description:BiometricStatusCallback
 */
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
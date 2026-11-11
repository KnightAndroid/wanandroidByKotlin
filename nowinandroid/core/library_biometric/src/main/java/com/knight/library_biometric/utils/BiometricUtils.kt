package com.knight.library_biometric.utils

import android.app.KeyguardManager
import android.content.Context
import android.os.Build
import androidx.biometric.BiometricManager
import androidx.fragment.app.FragmentActivity

/**
 * 生物识别工具类。
 * 使用 androidx.biometric.BiometricManager 替代原有的 FingerprintManager，
 * 后者从 Android 12 (API 31) 起已被系统移除，导致在 compileSdk 31+ 下
 * 出现 "Unresolved reference" 编译错误。
 */
object BiometricUtils {

    fun isAboveApiP(): Boolean {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.P
    }

    fun isAboveApiM(): Boolean {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.M
    }

    /**
     * 设备是否支持生物识别，且已登记指纹，且已设置锁屏。
     */
    fun isBiometricPromptEnable(activity: FragmentActivity): Boolean {
        return canAuthenticate(activity) && isKeyguardSecure(activity)
    }

    /**
     * 使用 androidx.biometric.BiometricManager 检测是否可以进行生物识别。
     * 这是跨所有 API 级别的官方兼容方案。
     */
    fun canAuthenticate(activity: FragmentActivity): Boolean {
        return try {
            val result = BiometricManager.from(activity).canAuthenticate(
                BiometricManager.Authenticators.BIOMETRIC_STRONG
            )
            result == BiometricManager.BIOMETRIC_SUCCESS
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }

    /**
     * 是否存在至少一个已登记的指纹。
     * androidx.biometric 内部已经封装了这个检测，canAuthenticate 返回 SUCCESS
     * 就意味着硬件存在且有已登记的生物特征。
     */
    fun hasEnrolledFingerprints(activity: FragmentActivity): Boolean {
        return canAuthenticate(activity)
    }

    /**
     * 硬件是否支持生物识别。
     * androidx.biometric 的 canAuthenticate 会同时检测硬件、已登记特征和安全设置。
     */
    fun isHardwareDetected(activity: FragmentActivity): Boolean {
        return try {
            val result = BiometricManager.from(activity).canAuthenticate(
                BiometricManager.Authenticators.BIOMETRIC_STRONG
            )
            // BIOMETRIC_ERROR_NONE_ENROLLED 表示硬件存在但未登记，此时硬件是存在的
            result != BiometricManager.BIOMETRIC_ERROR_NO_HARDWARE &&
                    result != BiometricManager.BIOMETRIC_ERROR_HW_UNAVAILABLE
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }

    /**
     * 系统是否设置了锁屏。
     */
    fun isKeyguardSecure(activity: Context): Boolean {
        val keyguardManager =
            activity.getSystemService(Context.KEYGUARD_SERVICE) as KeyguardManager
        return keyguardManager.isKeyguardSecure
    }
}

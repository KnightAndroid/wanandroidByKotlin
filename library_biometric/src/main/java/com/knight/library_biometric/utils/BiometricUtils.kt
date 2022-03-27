package com.knight.library_biometric.utils

import android.app.Activity
import android.app.KeyguardManager
import android.content.Context
import android.hardware.fingerprint.FingerprintManager
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.fragment.app.FragmentActivity

/**
 *
 * @ProjectName:    wanandroid
 * @Package:        com.knight.library_biometric.utils
 * @ClassName:      BiometricUtils
 * @Description:    java类作用描述
 * @Author:         knight
 * @CreateDate:     2022/3/24 10:15 上午
 * @UpdateUser:     更新者
 * @UpdateDate:     2022/3/24 10:15 上午
 * @UpdateRemark:   更新说明
 * @Version:        1.0
 */

object BiometricUtils {
    fun isAboveApiP(): Boolean {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.P
    }

    fun isAboveApiM(): Boolean {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.M
    }

    /**
     * Whether the device support biometric.
     * @return
     */
    fun isBiometricPromptEnable(activity: FragmentActivity): Boolean {
        return (isAboveApiM()
                && isHardwareDetected(activity)
                && hasEnrolledFingerprints(activity)
                && isKeyguardSecure(activity))
    }

    /**
     * 确定是否至少有一个指纹登记
     * @param activity
     * @return
     */
    fun hasEnrolledFingerprints(activity: Activity): Boolean {
        return if (isAboveApiP()) {
            //TODO 这是Api23的判断方法，也许以后有针对Api28的判断方法
            val manager = activity.getSystemService(
                FingerprintManager::class.java
            )
            manager != null && manager.hasEnrolledFingerprints()
        } else if (isAboveApiM()) {
            AbovehasEnrolledFingerprints(activity)
        } else {
            false
        }
    }

    /**
     *
     * 用来判断系统硬件是否支持指纹识别
     * @param activity
     * @return
     */
    fun isHardwareDetected(activity: Activity): Boolean {
        return if (isAboveApiP()) {
            //TODO 这是Api23的判断方法，也许以后有针对Api28的判断方法
            val fm = activity.getSystemService(
                FingerprintManager::class.java
            )
            fm != null && fm.isHardwareDetected
        } else if (isAboveApiM()) {
            AboveisHardwareDetected(activity)
        } else {
            false
        }
    }

    /**
     *
     * 这个方法是判断系统有没有设置锁屏
     * @param activity
     * @return
     */
    fun isKeyguardSecure(activity: Activity): Boolean {
        val keyguardManager = activity.getSystemService(Context.KEYGUARD_SERVICE) as KeyguardManager
        return keyguardManager.isKeyguardSecure
    }

    /**
     *
     * 高于23判断硬件是否支持
     * @param activity
     * @return
     */
    @RequiresApi(api = Build.VERSION_CODES.M)
    fun AboveisHardwareDetected(activity: Activity): Boolean {
        return activity.getSystemService(FingerprintManager::class.java).isHardwareDetected
    }

    /**
     *
     * 高于23判断
     * @param activity
     * @return
     */
    @RequiresApi(api = Build.VERSION_CODES.M)
    fun AbovehasEnrolledFingerprints(activity: Activity): Boolean {
        return activity.getSystemService(FingerprintManager::class.java).hasEnrolledFingerprints()
    }
}
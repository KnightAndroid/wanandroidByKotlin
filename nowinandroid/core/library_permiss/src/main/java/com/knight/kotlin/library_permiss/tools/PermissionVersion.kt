package com.knight.kotlin.library_permiss.tools

import android.content.Context
import android.os.Build


/**
 * @author created by luguian
 * @organize
 * @Date 2025/7/11 9:51
 * @descript:Android 版本工具
 */
object PermissionVersion {
    const val ANDROID_15: Int = Build.VERSION_CODES.VANILLA_ICE_CREAM
    const val ANDROID_14: Int = Build.VERSION_CODES.UPSIDE_DOWN_CAKE
    const val ANDROID_13: Int = Build.VERSION_CODES.TIRAMISU
    const val ANDROID_12_L: Int = Build.VERSION_CODES.S_V2
    const val ANDROID_12: Int = Build.VERSION_CODES.S
    const val ANDROID_11: Int = Build.VERSION_CODES.R
    const val ANDROID_10: Int = Build.VERSION_CODES.Q
    const val ANDROID_9: Int = Build.VERSION_CODES.P
    const val ANDROID_8_1: Int = Build.VERSION_CODES.O_MR1
    const val ANDROID_8: Int = Build.VERSION_CODES.O
    const val ANDROID_7_1: Int = Build.VERSION_CODES.N_MR1
    const val ANDROID_7: Int = Build.VERSION_CODES.N
    const val ANDROID_6: Int = Build.VERSION_CODES.M
    const val ANDROID_5_1: Int = Build.VERSION_CODES.LOLLIPOP_MR1
    const val ANDROID_5: Int = Build.VERSION_CODES.LOLLIPOP
    const val ANDROID_4_4: Int = Build.VERSION_CODES.KITKAT
    const val ANDROID_4_3: Int = Build.VERSION_CODES.JELLY_BEAN_MR2
    const val ANDROID_4_2: Int = Build.VERSION_CODES.JELLY_BEAN_MR1
    const val ANDROID_4_1: Int = Build.VERSION_CODES.JELLY_BEAN
    const val ANDROID_4_0: Int = Build.VERSION_CODES.ICE_CREAM_SANDWICH
    const val ANDROID_3_2: Int = Build.VERSION_CODES.HONEYCOMB_MR2
    const val ANDROID_3_1: Int = Build.VERSION_CODES.HONEYCOMB_MR1
    const val ANDROID_3_0: Int = Build.VERSION_CODES.HONEYCOMB
    const val ANDROID_2_3_3: Int = Build.VERSION_CODES.GINGERBREAD_MR1
    const val ANDROID_2_3: Int = Build.VERSION_CODES.GINGERBREAD
    const val ANDROID_2_2: Int = Build.VERSION_CODES.FROYO
    const val ANDROID_2_1: Int = Build.VERSION_CODES.ECLAIR_MR1
    const val ANDROID_2_0_1: Int = Build.VERSION_CODES.ECLAIR_0_1
    const val ANDROID_2_0: Int = Build.VERSION_CODES.ECLAIR

    /**
     * 获取当前 Android 版本
     */
    fun getCurrentVersion(): Int {
        return Build.VERSION.SDK_INT
    }

    /**
     * 获取 TargetSdk 版本
     */
    fun getTargetVersion(context: Context): Int {
        return context.applicationInfo.targetSdkVersion
    }

    /**
     * 是否是 Android 15 及以上版本
     */
    fun isAndroid15(): Boolean {
        return Build.VERSION.SDK_INT >= ANDROID_15
    }

    /**
     * 是否是 Android 14 及以上版本
     */
    fun isAndroid14(): Boolean {
        return Build.VERSION.SDK_INT >= ANDROID_14
    }

    /**
     * 是否是 Android 13 及以上版本
     */
    fun isAndroid13(): Boolean {
        return Build.VERSION.SDK_INT >= ANDROID_13
    }

    /**
     * 是否是 Android 12 及以上版本
     */
    fun isAndroid12(): Boolean {
        return Build.VERSION.SDK_INT >= ANDROID_12
    }

    /**
     * 是否是 Android 11 及以上版本
     */
    fun isAndroid11(): Boolean {
        return Build.VERSION.SDK_INT >= ANDROID_11
    }

    /**
     * 是否是 Android 10 及以上版本
     */
    fun isAndroid10(): Boolean {
        return Build.VERSION.SDK_INT >= ANDROID_10
    }

    /**
     * 是否是 Android 9.0 及以上版本
     */
    fun isAndroid9(): Boolean {
        return Build.VERSION.SDK_INT >= ANDROID_9
    }

    /**
     * 是否是 Android 8.1 及以上版本
     */
    fun isAndroid8_1(): Boolean {
        return Build.VERSION.SDK_INT >= ANDROID_8_1
    }

    /**
     * 是否是 Android 8.0 及以上版本
     */
    fun isAndroid8(): Boolean {
        return Build.VERSION.SDK_INT >= ANDROID_8
    }

    /**
     * 是否是 Android 7.1 及以上版本
     */
    fun isAndroid7_1(): Boolean {
        return Build.VERSION.SDK_INT >= ANDROID_7_1
    }

    /**
     * 是否是 Android 7.0 及以上版本
     */
    fun isAndroid7(): Boolean {
        return Build.VERSION.SDK_INT >= ANDROID_7
    }

    /**
     * 是否是 Android 6.0 及以上版本
     */
    fun isAndroid6(): Boolean {
        return Build.VERSION.SDK_INT >= ANDROID_6
    }

    /**
     * 是否是 Android 5.1 及以上版本
     */
    fun isAndroid5_1(): Boolean {
        return Build.VERSION.SDK_INT >= ANDROID_5_1
    }

    /**
     * 是否是 Android 5.0 及以上版本
     */
    fun isAndroid5(): Boolean {
        return Build.VERSION.SDK_INT >= ANDROID_5
    }

    /**
     * 是否是 Android 4.4 及以上版本
     */
    fun isAndroid4_4(): Boolean {
        return Build.VERSION.SDK_INT >= ANDROID_4_4
    }

    /**
     * 是否是 Android 4.3 及以上版本
     */
    fun isAndroid4_3(): Boolean {
        return Build.VERSION.SDK_INT >= ANDROID_4_3
    }
}
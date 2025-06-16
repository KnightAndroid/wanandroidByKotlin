package com.knight.kotlin.library_scan.annoation

import androidx.annotation.IntDef

/**
 * Author:Knight
 * Time:2022/2/11 18:01
 * Description:ScanStyle
 */
@kotlin.annotation.Retention(AnnotationRetention.SOURCE)
@IntDef(ScanStyle.NONE,ScanStyle.HALF_SCREEN,ScanStyle.FULL_SCREEN,ScanStyle.CUSTOMIZE)
annotation class ScanStyle {
    companion object {
        const val NONE = -1
        const val HALF_SCREEN = 1001
        const val FULL_SCREEN = 1002
        const val CUSTOMIZE = 1003
    }
}
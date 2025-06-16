package com.knight.kotlin.library_scan.annoation

import androidx.annotation.IntDef

/**
 * Author:Knight
 * Time:2022/2/11 17:38
 * Description:ScanMode
 */
@kotlin.annotation.Retention(AnnotationRetention.SOURCE)
@IntDef(ScanMode.RESTART,ScanMode.REVERSE)
annotation class ScanMode {
    companion object {
        const val RESTART = 1 //重复运动
        const val REVERSE = 2 //往返运动
    }
}
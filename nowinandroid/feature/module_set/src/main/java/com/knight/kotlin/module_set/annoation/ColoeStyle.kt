package com.knight.kotlin.module_set.annoation

import androidx.annotation.IntDef
import java.lang.annotation.Retention
import java.lang.annotation.RetentionPolicy

/**
 * Author:Knight
 * Time:2022/5/25 11:10
 * Description:ColoeStyle
 */
@Retention(RetentionPolicy.SOURCE)
@IntDef(ColorStyle.THEMECOLOR, ColorStyle.TEXTCOLOR, ColorStyle.BGCOLOR)
annotation class ColorStyle {
    companion object {
        const val THEMECOLOR = 5001
        const val TEXTCOLOR = 5002
        const val BGCOLOR = 5003
    }
}
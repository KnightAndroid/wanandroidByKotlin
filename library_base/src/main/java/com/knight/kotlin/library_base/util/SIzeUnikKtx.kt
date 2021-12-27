package com.knight.kotlin.library_base.util

import android.content.Context
import android.content.res.Resources

/**
 * Author:Knight
 * Time:2021/12/24 16:26
 * Description:SIzeUnikKtx
 * 尺寸单位换算扩展鼠性
 */

fun Int.dp2px():Float{
    return (0.5f + this * Resources.getSystem().displayMetrics.density)
}

fun Context.dp2px(dpValue: Float): Int {
    val scale = resources.displayMetrics.density
    return (dpValue * scale + 0.5f).toInt()
}

/**
 * px 转 dp
 */
fun Context.px2dp(pxValue: Float): Int {
    val scale = resources.displayMetrics.density
    return (pxValue / scale + 0.5f).toInt()
}

/**
 * sp 转 px
 */
fun Context.sp2px(spValue: Float): Int {
    val scale = resources.displayMetrics.scaledDensity
    return (spValue * scale + 0.5f).toInt()
}

/**
 * px 转 sp
 */
fun Context.px2sp(pxValue: Float): Int {
    val scale = resources.displayMetrics.scaledDensity
    return (pxValue / scale + 0.5f).toInt()
}

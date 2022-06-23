package com.knight.kotlin.library_base.util

import android.content.Context
import android.content.res.Resources

/**
 * Author:Knight
 * Time:2021/12/24 16:26
 * Description:SIzeUnikKtx
 * 尺寸单位换算扩展鼠性
 */

fun Int.dp2px():Int{
    return (0.5f + this * Resources.getSystem().displayMetrics.density).toInt()
}


fun Context.dp2px(dpValue: Float): Int {
    val scale = resources.displayMetrics.density
    return (dpValue * scale + 0.5f).toInt()
}

/**
 * px 转 dp
 */
fun Int.px2dp(): Int {
    val scale = Resources.getSystem().displayMetrics.density
    return (this / scale + 0.5f).toInt()
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
fun Int.px2sp(): Int {
    val scale = Resources.getSystem().displayMetrics.scaledDensity
    return (this / scale + 0.5f).toInt()
}

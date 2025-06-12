package com.knight.kotlin.library_base.util

import android.content.Context
import android.content.res.Resources
import android.os.Build
import android.util.TypedValue

/**
 * Author:Knight
 * Time:2021/12/24 16:26
 * Description:SIzeUnikKtx
 * 尺寸单位换算扩展鼠性
 */

fun Int.dp2px():Int{
    return (0.5f + this * Resources.getSystem().displayMetrics.density).toInt()
}

fun Float.dp2px(): Float {
    return (0.5f + this * Resources.getSystem().displayMetrics.density)
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
    return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP,spValue,resources.displayMetrics).toInt()
}


/**
 * sp 转 px
 */
fun Int.sp2px(): Int {
    val scale = Resources.getSystem().displayMetrics.density
    return  (this * scale + 0.5f).toInt()
}

/**
 * sp 转 px
 */
fun Float.sp2px(): Float{
    val scale = Resources.getSystem().displayMetrics.density
    return  (this * scale + 0.5f)
}

/**
 * px 转 sp
 */
fun Int.px2sp(): Int {
    return if (Build.VERSION.SDK_INT >= 34) {
        TypedValue.deriveDimension(TypedValue.COMPLEX_UNIT_SP,this.toFloat(),Resources.getSystem().displayMetrics).toInt()
    } else {
        val scale = Resources.getSystem().displayMetrics.scaledDensity
        (this / scale + 0.5f).toInt()
    }
}

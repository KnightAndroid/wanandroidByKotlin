package com.knight.kotlin.library_util.toast.style

import android.content.Context
import android.graphics.drawable.Drawable
import android.graphics.drawable.GradientDrawable
import android.util.TypedValue


/**
 * Author:Knight
 * Time:2021/12/17 14:05
 * Description:WhiteToastStyle
 */
class WhiteToastStyle : BlackToastStyle() {
    override fun getTextColor(): Int {
        return 0XBB000000.toInt()
    }

    override fun getBackgroundDrawable(context: Context): Drawable {
        val drawable = GradientDrawable()
        // 设置颜色
        drawable.setColor(0XFFEAEAEA.toInt())
        // 设置圆角
        drawable.cornerRadius =
            TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP,
                8f,
                context.resources.displayMetrics
            )
        return drawable
    }
}
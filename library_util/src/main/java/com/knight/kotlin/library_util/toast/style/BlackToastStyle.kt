package com.knight.kotlin.library_util.toast.style

import android.content.Context
import android.graphics.drawable.Drawable
import android.graphics.drawable.GradientDrawable
import android.os.Build
import android.util.TypedValue
import android.view.Gravity
import android.view.ViewGroup
import android.widget.TextView
import com.knight.kotlin.library_util.toast.callback.IToastStyle


/**
 * Author:Knight
 * Time:2021/12/17 11:20
 * Description:BlackToastStyle
 */
open class BlackToastStyle : IToastStyle<TextView> {
    override fun createView(context: Context): TextView {
        val textView = TextView(context)
        textView.id = android.R.id.message
        textView.gravity = getTextGravity()
        textView.setTextColor(getTextColor())
        textView.setTextSize(TypedValue.COMPLEX_UNIT_PX, getTextSize(context))
        val horizontalPadding = getHorizontalPadding(context)
        val verticalPadding = getVerticalPadding(context)

        //适配布局反方向特性
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            textView.setPaddingRelative(horizontalPadding, verticalPadding, horizontalPadding, verticalPadding);
        } else {
            textView.setPadding(horizontalPadding, verticalPadding, horizontalPadding, verticalPadding);
        }
        textView.layoutParams =
            ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )
        val background = getBackgroundDrawable(context)
        // 设置背景
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            textView.background = background;
        } else {
            textView.setBackgroundDrawable(background);
        }

        //设置Z轴阴影
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            textView.z = getTranslationZ(context)
        }
        return textView
    }

    protected open fun getTextGravity(): Int {
        return Gravity.CENTER
    }


    open fun getTextColor(): Int {
        return 0XEEFFFFFF.toInt()
    }


    protected open fun getTextSize(context: Context): Float {
        return TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_SP,
            14f,
            context.resources.displayMetrics
        )
    }

    protected open fun getHorizontalPadding(context: Context): Int {
        return TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP,
            24f,
            context.resources.displayMetrics
        ).toInt()
    }

    protected open fun getVerticalPadding(context:Context) :Int {
        return TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP,
            16f,
            context.resources.displayMetrics
        ).toInt()
    }

    protected open fun getBackgroundDrawable(context:Context): Drawable {
        val drawable = GradientDrawable()
        // 设置颜色
        drawable.setColor(0X88000000.toInt())
        drawable.cornerRadius = (TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 10f, context.resources.displayMetrics))
        return drawable
    }

    protected open fun getTranslationZ(context: Context): Float {
        return TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP,
            3f,
            context.resources.displayMetrics
        )
    }

}
package com.knight.kotlin.library_base.widget.loadcircleview

import android.content.Context
import android.graphics.drawable.GradientDrawable
import android.os.Build
import android.util.AttributeSet
import android.widget.LinearLayout
import com.knight.kotlin.library_base.R
import com.knight.kotlin.library_base.util.dp2px

/**
 * Author:Knight
 * Time:2022/3/28 11:25
 * Description:BackgroundLayout
 */
class BackgroundLayout : LinearLayout {


    private var mCornerRadius = 0f
    private var mBackgroundColor = 0
    @JvmOverloads
    constructor(context: Context, attributeSet: AttributeSet? = null, defAttrStyle: Int = 0)
            : super(context, attributeSet,defAttrStyle) {
        init()
    }

    private fun init() {
        val color = context.resources.getColor(R.color.base_progress_color)
        initBackground(color, mCornerRadius)
    }


    private fun initBackground(color: Int, cornerRadius: Float) {
        val drawable = GradientDrawable()
        drawable.shape = GradientDrawable.RECTANGLE
        drawable.setColor(color)
        drawable.cornerRadius = cornerRadius
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            background = drawable
        } else {
            setBackgroundDrawable(drawable)
        }
    }

    fun setCornerRadius(radius: Int) {
        mCornerRadius = radius.dp2px().toFloat()
        initBackground(mBackgroundColor, mCornerRadius)
    }

    fun setBaseColor(color: Int) {
        mBackgroundColor = color
        initBackground(mBackgroundColor, mCornerRadius)
    }



}
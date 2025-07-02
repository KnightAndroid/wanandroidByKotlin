package com.core.library_base.widget.loadcircleview

import android.content.Context
import android.graphics.drawable.GradientDrawable
import android.util.AttributeSet
import android.widget.LinearLayout
import androidx.core.content.ContextCompat
import com.core.library_base.R
import com.core.library_base.util.dp2px


/**
 * Author:Knight
 * Time:2022/3/28 11:25
 * Description:BackgroundLayout
 */
class BackgroundLayout @JvmOverloads constructor(
    context: Context,
    attributeSet: AttributeSet? = null,
    defAttrStyle: Int = 0
) : LinearLayout(context, attributeSet, defAttrStyle) {


    private var mCornerRadius = 0f
    private var mBackgroundColor = 0

    init {
        init()
    }

    private fun init() {
        val color = ContextCompat.getColor(context,R.color.base_progress_color)
        initBackground(color, mCornerRadius)
    }


    private fun initBackground(color: Int, cornerRadius: Float) {
        val drawable = GradientDrawable()
        drawable.shape = GradientDrawable.RECTANGLE
        drawable.setColor(color)
        drawable.cornerRadius = cornerRadius
        background = drawable
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
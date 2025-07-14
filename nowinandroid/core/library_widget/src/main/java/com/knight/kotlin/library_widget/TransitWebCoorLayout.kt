package com.knight.kotlin.library_widget

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.util.AttributeSet
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.content.ContextCompat
import com.core.library_common.util.CacheUtils
import com.core.library_common.util.ColorUtils

/**
 * Author:Knight
 * Time:2022/3/10 10:07
 * Description:TransitWebCoorLayout
 */
class TransitWebCoorLayout:CoordinatorLayout {

    private var darkTheme = false
    private var bgColor = Color.TRANSPARENT

    @JvmOverloads
    constructor(context: Context, attributeSet: AttributeSet? = null, defAttrStyle: Int = 0)
            : super(context, attributeSet, defAttrStyle) {
         darkTheme = CacheUtils.getNormalDark()
        if (darkTheme) {
            bgColor = ColorUtils.alphaColor(
                ContextCompat.getColor(
                    getContext(),
                    R.color.widget_night_color
                ), 0.6f)
        }
    }

    override fun dispatchDraw(canvas: Canvas) {
        super.dispatchDraw(canvas)
        if (darkTheme) {
            canvas.drawColor(bgColor)
        }
    }


}
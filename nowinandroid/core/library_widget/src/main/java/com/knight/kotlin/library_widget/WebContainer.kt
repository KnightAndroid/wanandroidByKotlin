package com.knight.kotlin.library_widget

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.util.AttributeSet
import android.widget.LinearLayout
import androidx.core.content.ContextCompat
import com.core.library_common.util.CacheUtils
import com.core.library_common.util.ColorUtils

/**
 * Author:Knight
 * Time:2022/1/5 15:33
 * Description:WebContainer
 */
class WebContainer:LinearLayout {

    /**
     * 是否暗黑模式
      */
    private var darkTheme:Boolean = false

    private var bgColor: Int = Color.TRANSPARENT
    @JvmOverloads
    constructor(context: Context, attributeSet: AttributeSet? = null, defAttrStyle: Int = 0)
            : super(context, attributeSet, defAttrStyle) {
          darkTheme = CacheUtils.getNormalDark()
        if (darkTheme) {
            bgColor = ColorUtils.alphaColor(ContextCompat.getColor(getContext(),R.color.widget_night_color),0.6f)
        }
    }

    override fun dispatchDraw(canvas: Canvas) {
        super.dispatchDraw(canvas)
        if (darkTheme) {
            canvas.drawColor(bgColor)
        }
    }
}
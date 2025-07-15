package com.knight.kotlin.module_eye_daily.view

import android.content.Context
import android.graphics.Canvas
import android.util.AttributeSet
import com.core.library_common.ktx.screenWidth
import com.core.library_common.util.dp2px
import com.youth.banner.indicator.BaseIndicator
import kotlin.math.max

/**
 * Author:Knight
 * Time:2024/5/7 18:15
 * Description:MyCustomBannerIndicator
 */
class MyCustomBannerIndicator @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) :  BaseIndicator(context, attrs, defStyleAttr) {
    var mNormalRadius = 0
    var mSelectedRadius = 0
    var maxRadius = 0

    var indicatorWidth = 0
    init {
        mNormalRadius = config.getNormalWidth() / 2
        mSelectedRadius = config.getSelectedWidth() / 2
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        val count: Int = config.getIndicatorSize()
        if (count <= 1) {
            return
        }
        mNormalRadius = config.getNormalWidth() / 2
        mSelectedRadius = config.getSelectedWidth() / 2
        //考虑当 选中和默认 的大小不一样的情况
        maxRadius = max(mSelectedRadius.toDouble(), mNormalRadius.toDouble()).toInt()
        //间距*（总数-1）+选中宽度+默认宽度*（总数-1）
        indicatorWidth =
            (count - 1) * config.getIndicatorSpace() + config.getSelectedWidth() + config.getNormalWidth() * (count - 1)
//        setMeasuredDimension(
//            getWidth(),
//            height
//        )
        setMeasuredDimension(context.screenWidth,26.dp2px())
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        val count: Int = config.getIndicatorSize()
        if (count <= 1) {
            return
        }
        var left :Float = ((width - indicatorWidth) / 2).toFloat()
        for (i in 0 until count) {
            mPaint.setColor(if (config.getCurrentPosition() == i) config.getSelectedColor() else config.getNormalColor())
            val indicatorWidth: Int =
                if (config.getCurrentPosition() == i) config.getSelectedWidth() else config.getNormalWidth()
            val radius = if (config.getCurrentPosition() == i) mSelectedRadius else mNormalRadius
            canvas.drawCircle(left + radius, (height / 2).toFloat(), radius.toFloat(), mPaint)
            left += (indicatorWidth + config.getIndicatorSpace()).toFloat()
        }
    }

}
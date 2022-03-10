package com.knight.kotlin.library_widget.pagetransformer

import android.content.Context
import android.util.AttributeSet
import androidx.cardview.widget.CardView

/**
 * Author:Knight
 * Time:2022/3/8 10:30
 * Description:AspectRatioCardView
 */
class AspectRatioCardView :CardView {
    private val ratio = 1.2f
    @JvmOverloads
    constructor(context: Context, attributeSet: AttributeSet? = null, defAttrStyle: Int = 0)
            : super(context, attributeSet, defAttrStyle) {

    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        if (ratio > 0) {
            val ratioHeight = (measuredWidth * ratio).toInt()
            setMeasuredDimension(measuredWidth, ratioHeight)
            val lp = layoutParams
            //   LayoutParams lp = (LayoutParams) getLayoutParams();
            // lp.setMargins(30,0,30,0);
            lp.height = ratioHeight
            layoutParams = lp
        }
    }
}
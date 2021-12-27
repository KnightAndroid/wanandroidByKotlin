package com.knight.kotlin.library_widget

import android.content.Context
import android.util.AttributeSet
import android.widget.TextView

/**
 * Author:Knight
 * Time:2021/12/24 16:51
 * Description:MarqueeTextView
 */
class MarqueeTextView:TextView {
    @JvmOverloads
    constructor(context: Context, attributeSet: AttributeSet? = null, defAttrStyle: Int = 0)
            : super(context, attributeSet, defAttrStyle) {

    }

    override fun isFocused(): Boolean {
        return true
    }
}
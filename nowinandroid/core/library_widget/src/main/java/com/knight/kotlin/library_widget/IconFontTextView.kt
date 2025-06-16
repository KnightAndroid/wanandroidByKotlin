package com.knight.kotlin.library_widget

import android.content.Context
import android.graphics.Typeface
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatTextView

/**
 * Author:Knight
 * Time:2024/3/18 16:27
 * Description:IconFontTextView
 */
class IconFontTextView : AppCompatTextView {
    constructor(context: Context) : super(context) {}
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {}

    companion object {
        /** 所有IconFontTextView公用typeface  */
        private var typeface: Typeface? = null
    }

    init {
        Companion.typeface = Typeface.createFromAsset(context.assets, "iconfont.ttf")
        typeface = Companion.typeface
    }
}
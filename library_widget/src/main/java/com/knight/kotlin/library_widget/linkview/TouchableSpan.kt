package com.knight.kotlin.library_widget.linkview

import android.graphics.Color
import android.text.TextPaint
import android.text.style.ClickableSpan


/**
 * Author:Knight
 * Time:2024/3/18 15:43
 * Description:TouchableSpan
 */
abstract class TouchableSpan : ClickableSpan {

    private var isPressed = false
    private var normalTextColor = 0
    private var pressedTextColor = 0
    private var isUnderLineEnabled = false


    constructor(
        normalTextColor: Int,
        pressedTextColor: Int,
        isUnderLineEnabled: Boolean
    ) {
        this.normalTextColor = normalTextColor
        this.pressedTextColor = pressedTextColor
        this.isUnderLineEnabled = isUnderLineEnabled
    }

    open fun setPressed(isSelected: Boolean) {
        isPressed = isSelected
    }

    override fun updateDrawState(textPaint: TextPaint) {
        super.updateDrawState(textPaint)
        val textColor = if (isPressed) pressedTextColor else normalTextColor
        textPaint.color = textColor
        textPaint.bgColor = Color.TRANSPARENT
        textPaint.isUnderlineText = isUnderLineEnabled
    }
}
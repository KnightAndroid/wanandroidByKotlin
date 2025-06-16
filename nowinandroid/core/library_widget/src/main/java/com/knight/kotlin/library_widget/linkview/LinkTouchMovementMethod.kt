package com.knight.kotlin.library_widget.linkview

import android.text.Layout
import android.text.Selection
import android.text.Spannable
import android.text.method.LinkMovementMethod
import android.view.MotionEvent
import android.view.ViewGroup
import android.widget.TextView


/**
 * Author:Knight
 * Time:2024/3/18 15:40
 * Description:LinkTouchMovementMethod
 */
class LinkTouchMovementMethod : LinkMovementMethod(){
    private var pressedSpan: TouchableSpan? = null

    override fun onTouchEvent(
        textView: TextView,
        spannable: Spannable,
        event: MotionEvent
    ): Boolean {
        val action = event.action
        val isConsume = super.onTouchEvent(textView, spannable, event)
        if (action == MotionEvent.ACTION_DOWN) {
            pressedSpan = getPressedSpan(textView, spannable, event)
            if (pressedSpan != null) {
                pressedSpan?.setPressed(true)
                Selection.setSelection(
                    spannable, spannable.getSpanStart(pressedSpan),
                    spannable.getSpanEnd(pressedSpan)
                )
            }
        } else if (action == MotionEvent.ACTION_MOVE) {
            val touchedSpan: TouchableSpan? = getPressedSpan(textView, spannable, event)
            if (pressedSpan != null && touchedSpan !== pressedSpan) {
                pressedSpan?.setPressed(false)
                pressedSpan = null
                Selection.removeSelection(spannable)
            }
        } else {
            if (pressedSpan != null) {
                pressedSpan?.setPressed(false)
                textView.isClickable = false
            }
            pressedSpan = null
            Selection.removeSelection(spannable)
            // 解决clickable与textview 本身click点击事件的冲突。
            if (!isConsume && event.action == MotionEvent.ACTION_UP) {
                val parent = textView.parent
                if (parent is ViewGroup) {
                    // 获取被点击控件的父容器，让父容器执行点击；
                    parent.performClick()
                }
            }
        }
        return isConsume
    }

    private fun getPressedSpan(
        textView: TextView,
        spannable: Spannable,
        event: MotionEvent
    ): TouchableSpan? {
        var x = event.x.toInt()
        var y = event.y.toInt()
        x -= textView.totalPaddingLeft
        y -= textView.totalPaddingTop
        x += textView.scrollX
        y += textView.scrollY
        val layout: Layout = textView.layout
        val verticalLine: Int = layout.getLineForVertical(y)
        val horizontalOffset: Int = layout.getOffsetForHorizontal(verticalLine, x.toFloat())
        val link: Array<TouchableSpan> = spannable.getSpans(
            horizontalOffset, horizontalOffset,
            TouchableSpan::class.java
        )
        var touchedSpan: TouchableSpan? = null
        if (link.size > 0) {
            touchedSpan = link[0]
        }
        return touchedSpan
    }
}
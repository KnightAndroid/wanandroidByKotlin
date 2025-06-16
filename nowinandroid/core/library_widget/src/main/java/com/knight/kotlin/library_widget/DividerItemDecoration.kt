package com.knight.kotlin.library_widget

import android.R
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Rect
import android.util.TypedValue
import android.view.View
import androidx.recyclerview.widget.RecyclerView


/**
 * @Description
 * @Author knight
 * @Time 2025/4/17 23:39
 *
 */

class DividerItemDecoration(context: Context) : RecyclerView.ItemDecoration() {
    private val dividerHeight: Float
    private val mPaint = Paint(Paint.ANTI_ALIAS_FLAG)

    init {
        mPaint.color = Color.parseColor("#EDEDED")
        dividerHeight = TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP,
            0.5f,
            context.resources.displayMetrics
        )
    }

    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        super.getItemOffsets(outRect, view, parent, state)
        outRect.bottom = dividerHeight.toInt()
    }

    override fun onDraw(c: Canvas, parent: RecyclerView, state: RecyclerView.State) {
        val childCount = parent.childCount
        val left = parent.paddingLeft
        val right = parent.width - parent.paddingRight

        for (i in 0 until childCount - 1) {
            val view = parent.getChildAt(i)
            val top = view.bottom.toFloat()
            val bottom = view.bottom + dividerHeight
            c.drawRect(left.toFloat(), top, right.toFloat(), bottom, mPaint)
        }
    }
}
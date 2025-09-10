package com.knight.kotlin.library_widget

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView


/**
 * @author created by luguian
 * @organize
 * @Date 2024/8/8 18:06
 * @descript:布局分割线
 */
class RecyclerItemDecoration : RecyclerView.ItemDecoration {
    private var left: Int

    private var top: Int

    private var right: Int

    private var bottom: Int

    private var dividerColor = Color.TRANSPARENT

    private var dividerHeight = 0

    private var dividerMarginHeight = 0

    private var mPaint: Paint? = null

    constructor(left: Int, top: Int, right: Int, bottom: Int) {
        this.left = left
        this.top = top
        this.right = right
        this.bottom = bottom
        mPaint = Paint()
        mPaint!!.color = dividerColor
    }

    constructor(
        left: Int, top: Int, right: Int, bottom: Int,
        dividerColor: Int, dividerHeight: Int, dividerMarginHeight: Int
    ) {
        this.left = left
        this.top = top
        this.right = right
        this.bottom = bottom
        this.dividerColor = dividerColor
        this.dividerHeight = dividerHeight
        this.dividerMarginHeight = dividerMarginHeight
        mPaint = Paint()
        mPaint!!.color = dividerColor
    }

    override fun getItemOffsets(
        outRect: Rect, view: View,
        parent: RecyclerView, state: RecyclerView.State
    ) {
        super.getItemOffsets(outRect, view, parent, state)
        if (left == 0 && top == 0 && right == 0 && bottom == 0) {
            return
        }

        outRect[left, top, right] = bottom
    }

    override fun onDraw(
        c: Canvas, parent: RecyclerView,
        state: RecyclerView.State
    ) {
        super.onDraw(c, parent, state)
    }

    override fun onDrawOver(
        c: Canvas, parent: RecyclerView,
        state: RecyclerView.State
    ) {
        super.onDrawOver(c, parent, state)
        if (mPaint == null) {
            return
        }

        val left = parent.paddingLeft //横线的左端必须是paddngleft,如果用left则横线过长（不显示）
        val right = parent.width - parent.paddingRight //同上，getLeft()是控件左端距离屏幕左端的长度，right是控件右端距离屏幕左端的长度
        // 获取RecyclerView的child view的个数
        val childCount = parent.childCount
        // 遍历每个Item，分别获取它们的位置信息，然后再绘制对应的分割线
        for (i in 0 until childCount) {
            // 获取每个Item
            val child = parent.getChildAt(i)
            val params = child.layoutParams as RecyclerView.LayoutParams
            //val left = child.left
            // 需要加上 margin的高度
            val top = child.bottom + dividerMarginHeight  + params.bottomMargin
            // val right = child.right
            val bottom = top + dividerHeight
            // 绘制分割线
            c.drawRect(left.toFloat(), top.toFloat(), right.toFloat(), bottom.toFloat(), mPaint!!)
        }
    }
}
package com.knight.kotlin.library_widget

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView


/**
 * Author:Knight
 * Time:2024/2/26 17:21
 * Description:SpacesItemDecoration 网格间距
 */
class SpacesItemDecoration(spacing: Int) : RecyclerView.ItemDecoration() {

    private var leftRight = 0 // 间距大小
    private var topBottom = 0 //暂时和左右间隔一样，后期有需要再做调整
    init {
        this.leftRight = spacing
        this.topBottom = spacing
    }
    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        val layoutManager = parent.layoutManager as GridLayoutManager?
        val lp = view.layoutParams as GridLayoutManager.LayoutParams
        val childPosition = parent.getChildAdapterPosition(view)
        val spanCount = layoutManager!!.spanCount

        if (layoutManager!!.orientation == GridLayoutManager.VERTICAL) {
            //判断是否在第一排
            if (layoutManager!!.spanSizeLookup.getSpanGroupIndex(childPosition, spanCount) == 0) { //第一排的需要上面
                outRect.top = topBottom
            }
            outRect.bottom = topBottom
            //这里忽略和合并项的问题，只考虑占满和单一的问题
            if (lp.spanSize == spanCount) { //占满
                outRect.left = leftRight
                outRect.right = leftRight
            } else {
                outRect.left = (((spanCount - lp.spanIndex).toFloat()) / spanCount * leftRight).toInt()
                outRect.right = ((leftRight * (spanCount + 1) / spanCount) - outRect.left).toInt()
            }
        } else {
            if (layoutManager!!.spanSizeLookup.getSpanGroupIndex(childPosition, spanCount) == 0) { //第一排的需要left
                outRect.left = leftRight
            }
            outRect.right = leftRight
            //这里忽略和合并项的问题，只考虑占满和单一的问题
            if (lp.spanSize == spanCount) { //占满
                outRect.top = topBottom
                outRect.bottom = topBottom
            } else {
                outRect.top = (((spanCount - lp.spanIndex).toFloat()) / spanCount * topBottom).toInt()
                outRect.bottom = ((topBottom * (spanCount + 1) / spanCount) - outRect.top).toInt()
            }
        }


    }

}
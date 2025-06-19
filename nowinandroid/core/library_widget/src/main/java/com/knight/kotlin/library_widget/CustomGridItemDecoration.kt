package com.knight.kotlin.library_widget

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView


/**
 * @author created by luguian
 * @organize
 * @Date 2025/6/19 15:56
 * @descript:计算网格行与行之间的间隔
 */
class CustomGridItemDecoration(
    private val spanCount: Int,
    private val horizontalSpacing: Int,
    private val verticalSpacing: Int,
    private val totalRows: Int
) : RecyclerView.ItemDecoration() {

    override fun getItemOffsets(
        outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State
    ) {
        val position = parent.getChildAdapterPosition(view)
        val column = position % spanCount
        val row = position / spanCount

        // 左右固定间距
        outRect.left = horizontalSpacing
        outRect.right = horizontalSpacing

        // 上下间距：包括顶部和底部
        when (row) {
            0 -> outRect.top = verticalSpacing
            totalRows - 1 -> {
                outRect.top = verticalSpacing   // ← 加这一行
                outRect.bottom = verticalSpacing
            }
            else -> outRect.top = verticalSpacing
        }
    }
}
package com.knight.kotlin.library_widget

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView


/**
 * Author:Knight
 * Time:2024/2/26 17:21
 * Description:SpacesItemDecoration
 */
class SpacesItemDecoration(spacing: Int) : RecyclerView.ItemDecoration() {

    private var spacing = 0 // 间距大小

    init {
        this.spacing = spacing
    }
    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        val position = parent.getChildAdapterPosition(view)
        val spanCount = (parent.layoutManager as GridLayoutManager).spanCount
        val column = position % spanCount

        outRect.left = spacing - column * spacing / spanCount
        outRect.right = (column + 1) * spacing / spanCount
        if (position >= spanCount) {
            outRect.top = spacing
        }
    }
}
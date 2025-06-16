package com.knight.kotlin.library_widget

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView


/**
 * @author created by luguian
 * @organize
 * @Date 2025/3/20 16:49
 * @descript:解决Viewpager 嵌套Recycleview Margin失效
 */
class MarginItemVpDecoration (private val spaceHeight: Int) : RecyclerView.ItemDecoration() {
    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
        with(outRect) {
            if (parent.getChildAdapterPosition(view) == 0) {
                top = spaceHeight
            }
            left = spaceHeight
            right = spaceHeight
            val itemCount = parent.adapter?.itemCount ?: 0
            if (itemCount== parent.getChildAdapterPosition(view)) {
                bottom = spaceHeight
            }

        }
    }
}
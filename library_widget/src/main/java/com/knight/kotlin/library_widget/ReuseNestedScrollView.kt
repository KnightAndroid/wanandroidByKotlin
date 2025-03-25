package com.knight.kotlin.library_widget

import android.content.Context
import android.util.AttributeSet
import android.view.View
import androidx.core.widget.NestedScrollView


/**
 * @author created by luguian
 * @organize
 * @Date 2025/3/25 15:43
 * @descript:
 */
class ReuseNestedScrollView(context: Context,attrs: AttributeSet) : NestedScrollView(context, attrs) {
    // 使用NestedScrollView嵌套RecyclerView，会导致RecyclerView复用机制失效，RecyclerView会将所有数据一次性全部加载。
    // 解决方法：重写measureChildWithMargins，让NestedScrollView测量RecyclerView时 不使用MeasureSpec.UNSPECIFIED模式即可。
    override fun measureChildWithMargins(child: View, parentWidthMeasureSpec: Int, widthUsed: Int, parentHeightMeasureSpec: Int, heightUsed: Int) {
        child.measure(parentWidthMeasureSpec, parentHeightMeasureSpec)
    }
}
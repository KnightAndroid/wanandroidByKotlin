package com.knight.kotlin.library_widget

import android.content.Context
import android.util.AttributeSet
import android.view.View
import androidx.core.widget.NestedScrollView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView


/**
 * @author created by luguian
 * @organize
 * @Date 2024/8/2 16:17
 * @descript:
 */
class CustomNestedScrollView : NestedScrollView {

    @JvmOverloads
    constructor(context: Context, attributeSet: AttributeSet? = null, defAttrStyle: Int =  0)
            : super(context, attributeSet, defAttrStyle) {

    }


    /* NestedScrollView 在一下两种情况中将拦截scroll/fling事件：
  (1) RecyclerView已经滑动到顶部，用户手指继续向下滑动
  (2) NestedScrollView已经滑动到底部，用户手指继续向上滑动*/
    override fun onNestedPreScroll(target: View, dx: Int, dy: Int, consumed: IntArray) {
        val rv = target as RecyclerView
        if ((dy < 0 && isRvScrolledToTop(rv)) || (dy > 0 && !isNsvScrolledToBottom(this))) {
            // 滑动NestedScrollView并且标记滑动距离，
            // 这样RecyclerView就可以知道有多少滑动距离是不用去处理的
            scrollBy(0, dy)
            // consumed[0]表示横向滑动， consumed[1]表示纵向滑动
            consumed[1] = dy
            return
        }
        super.onNestedPreScroll(target, dx, dy, consumed)

        // 当scrollY还没到最大值时，先滑动ScrollView dy距离
//        if (scrollY + dy < remainingHeight) {
//            val consumedDy = min(dy, remainingHeight - scrollY)
//            scrollBy(0, consumedDy)
//            consumed[1] = consumedDy
//        }
    }

//    override fun onNestedPreFling(target: View, velX: Float, velY: Float): Boolean {
//        val rv = target as RecyclerView
//        if ((velY < 0 && isRvScrolledToTop(rv)) || (velY > 0 && !isNsvScrolledToBottom(this))) {
//            // 处理NestedScrollView的fling，并return true，
//            //同样的RecyclerView也会收到通知，不用处理这次的Fling事件了
//            fling(velY.toInt())
//            return true
//        }
//        return super.onNestedPreFling(target, velX, velY)
//    }

    /**
     * 判断NestedScrollView是否滑动到底部。
     *
     * @return NestedScrollView 滑动到底部的时候return true
     * 即RecyclerView完全可见的时候return true
     */
    private fun isNsvScrolledToBottom(nsv: NestedScrollView): Boolean {
        return !nsv.canScrollVertically(1)
    }

    /**
     * 判断RecyclerView是否滑动到顶部
     *
     * @return RecyclerView 滑动到顶部的的时候return true，
     * 即RecyclerView的第一个item完全可见的时候return true。
     */
    private fun isRvScrolledToTop(rv: RecyclerView): Boolean {
        val lm = rv.layoutManager as LinearLayoutManager?
        return (lm!!.findFirstVisibleItemPosition() == 0
                && lm.findViewByPosition(0)!!.top == 0)
    }

}
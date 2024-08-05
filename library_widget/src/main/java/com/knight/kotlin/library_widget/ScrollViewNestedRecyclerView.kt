package com.knight.kotlin.library_widget

import android.content.Context
import android.os.Build
import android.util.AttributeSet
import android.view.View
import android.view.ViewGroup
import android.widget.AbsListView
import androidx.core.view.NestedScrollingChild2
import androidx.core.view.ViewCompat
import androidx.core.widget.NestedScrollView
import androidx.recyclerview.widget.RecyclerView


/**
 * @author created by luguian
 * @organize
 * @Date 2024/8/5 10:02
 * @descript: Scrollview 嵌套 Recycleview 解决复用问题
 */
class ScrollViewNestedRecyclerView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : NestedScrollView(context, attrs, defStyleAttr) {

    private val TAG: String = ScrollViewNestedRecyclerView::class.java.simpleName
    private var topView: View? = null
    private var recyclerView: RecyclerView? = null

    private var topHeight = 0 //recycleview 上面的view高度
    private var scrollY = 0


    override fun onFinishInflate() {
        super.onFinishInflate()
        findHeadView()
        recyclerView = findRecyclerView()
        check(!(topView == null || recyclerView == null)) { "topView or recyclerView must not null" }
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        //先测量recycler view的高度，和 ScrollViewNestedRecyclerView的高度一样，防止recycler view 自动布局把所有的item加载出来，失去复用
        recyclerView!!.measure(
            widthMeasureSpec,
            MeasureSpec.makeMeasureSpec(if (height == 0) MeasureSpec.getSize(heightMeasureSpec) else height, MeasureSpec.EXACTLY)
        )
        val rcLp = recyclerView!!.layoutParams
        rcLp.height = recyclerView!!.measuredHeight
        recyclerView!!.layoutParams = rcLp


        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        topHeight = topView!!.measuredHeight
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        topHeight = topView!!.measuredHeight
    }


    override fun onNestedScroll(target: View, dxConsumed: Int, dyConsumed: Int, dxUnconsumed: Int, dyUnconsumed: Int, type: Int) {
        if (target is NestedScrollingChild2) {
            //防止到顶部后一直调用
            if (dyConsumed == 0 && dyUnconsumed < 0 && type == ViewCompat.TYPE_NON_TOUCH && scrollY == 0) {
                (target as NestedScrollingChild2).stopNestedScroll(type)
                return
            }
        }
        super.onNestedScroll(target, dxConsumed, dyConsumed, dxUnconsumed, dyUnconsumed, type)
    }

    override fun onNestedPreScroll(target: View, dx: Int, dy: Int, consumed: IntArray, type: Int) {
        if (target is NestedScrollingChild2) {
            //防止到顶部后一直调用
            if (dy < 0 && type == ViewCompat.TYPE_NON_TOUCH && scrollY == 0) {
                (target as NestedScrollingChild2).stopNestedScroll(type)
                return
            }
            if (scrollY < topHeight) {
                if (dy > 0) { //上滑
                    consumed[0] = dx
                    consumed[1] = dy
                    scrollBy(0, dy)
                } else { //下滑
                    if (!canScrollUp(target)) { //到达顶部
                        if (scrollY == 0) {
                            consumed[0] = dx
                            consumed[1] = 0
                            super.onNestedPreScroll(target, dx, dy, consumed, type)
                        } else {
                            consumed[0] = dx
                            consumed[1] = dy
                            scrollBy(0, dy)
                        }
                    }
                }
            } else {
                super.onNestedPreScroll(target, dx, dy, consumed, type)
            }
        } else {
            super.onNestedPreScroll(target, dx, dy, consumed, type)
        }
    }

    /**
     * 判断targetView是否还可以向上滚动
     * @param targetView
     * @return
     */
    private fun canScrollUp(targetView: View?): Boolean {
        if (targetView == null) {
            return false
        }
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
            if (targetView is AbsListView) {
                val absListView = targetView
                return (absListView.childCount > 0
                        && (absListView.firstVisiblePosition > 0 || absListView.getChildAt(0)
                    .top < absListView.paddingTop))
            } else {
                return targetView.canScrollVertically(-1) || targetView.scrollY > 0
            }
        } else {
            return targetView.canScrollVertically(-1)
        }
    }


    override fun onScrollChanged(l: Int, t: Int, oldl: Int, oldt: Int) {
        super.onScrollChanged(l, t, oldl, oldt)
        scrollY = t
    }


    // 添加一个方法来查找RecyclerView
    fun findRecyclerView(): RecyclerView? {
        for (i in 0 until childCount) {
            val child = getChildAt(i)
            if (child is RecyclerView) {
                return child
            } else if (child is ViewGroup) {
                val recyclerView = findRecyclerView(child)
                if (recyclerView != null) {
                    return recyclerView
                }
            }
        }
        return null
    }


    private fun findRecyclerView(viewGroup: ViewGroup): RecyclerView? {
        if (viewGroup is RecyclerView && viewGroup.javaClass == RecyclerView::class.java) {
            return viewGroup
        }
        for (i in 0 until viewGroup.childCount) {
            val child = viewGroup.getChildAt(i)
                        if (child is RecyclerView) {
                return child
            } else if (child is ViewGroup) {
                val recyclerView = findRecyclerView(child)
                if (recyclerView != null) {
                    return recyclerView
                }
            }
        }
        return null
    }


    private fun findHeadView() {
        val childView = getChildAt(0) as ViewGroup
        if (childView.childCount == 2) {
            topView = childView.getChildAt(0)
        } else {
            throw IllegalStateException("$TAG is designed for nested scrolling and can only have two direct child")
        }
    }


}
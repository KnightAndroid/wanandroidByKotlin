package com.knight.kotlin.library_widget

import android.animation.ObjectAnimator
import android.content.Context
import android.util.AttributeSet
import android.view.View
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.view.ViewCompat

/**
 * Author:Knight
 * Time:2022/3/7 16:59
 * Description:BottomNavigationBehavior
 */
class BottomNavigationBehavior:CoordinatorLayout.Behavior<View> {

    private var outAnimator: ObjectAnimator? = null
    private  var inAnimator:ObjectAnimator? = null
    @JvmOverloads
    constructor(context: Context, attributeSet: AttributeSet? = null)
            : super(context, attributeSet) {

    }

    // 垂直滑动
    override fun onStartNestedScroll(
        coordinatorLayout: CoordinatorLayout,
        child: View,
        directTargetChild: View,
        target: View,
        nestedScrollAxes: Int
    ): Boolean {
        return nestedScrollAxes == ViewCompat.SCROLL_AXIS_VERTICAL
    }

    override fun onNestedPreScroll(
        coordinatorLayout: CoordinatorLayout,
        child: View,
        target: View,
        dx: Int,
        dy: Int,
        consumed: IntArray
    ) {
        if (dy > 0) { // 上滑隐藏
            outAnimator?.let {
                if (!it.isRunning && child.translationY <= 0) {
                    it.start()
                }
            } ?:run{
                outAnimator =
                    ObjectAnimator.ofFloat(child, "translationY", 0f, child.height.toFloat())
                outAnimator?.setDuration(200)
            }


        } else if (dy < 0) { // 下滑显示
            inAnimator?.let {
                if (!it.isRunning && child.translationY >= child.height) {
                    it.start()
                }
            } ?: run {
                inAnimator =
                    ObjectAnimator.ofFloat(child, "translationY", child.height.toFloat(), 0f)
                inAnimator?.setDuration(200)
            }
        }
    }

}
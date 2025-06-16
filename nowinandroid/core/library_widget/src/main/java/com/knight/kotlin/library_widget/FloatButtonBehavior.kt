package com.knight.kotlin.library_widget

import android.content.Context
import android.util.AttributeSet
import android.view.View
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.view.ViewCompat
import com.google.android.material.floatingactionbutton.FloatingActionButton

/**
 *
 * @ProjectName:    wanandroid
 * @Package:        com.knight.kotlin.library_widget
 * @ClassName:      FloatButtonBehavior
 * @Description:    java类作用描述
 * @Author:         knight
 * @CreateDate:     2022/3/19 11:08 上午
 * @UpdateUser:     更新者
 * @UpdateDate:     2022/3/19 11:08 上午
 * @UpdateRemark:   更新说明
 * @Version:        1.0
 */

class FloatButtonBehavior :FloatingActionButton.Behavior {

    @JvmOverloads
    constructor(context: Context, attributeSet: AttributeSet? = null)
            : super(context, attributeSet) {
    }

    override fun onStartNestedScroll(
        coordinatorLayout: CoordinatorLayout,
        child: FloatingActionButton,
        directTargetChild: View,
        target: View,
        axes: Int,
        type: Int
    ): Boolean {
        return axes == ViewCompat.SCROLL_AXIS_VERTICAL || super.onStartNestedScroll(
            coordinatorLayout,
            child, directTargetChild, target, axes
        )
    }

    override fun onNestedScroll(
        coordinatorLayout: CoordinatorLayout,
        child: FloatingActionButton,
        target: View,
        dxConsumed: Int,
        dyConsumed: Int,
        dxUnconsumed: Int,
        dyUnconsumed: Int,
        type: Int
    ) {
        super.onNestedScroll(
            coordinatorLayout, child,
            target, dxConsumed, dyConsumed, dxUnconsumed, dyUnconsumed, type
        )
        if (dyConsumed > 0 && child.visibility == View.VISIBLE) {
            child.visibility = View.INVISIBLE
        } else if (dyConsumed < 0 && child.visibility != View.VISIBLE) {
            child.show()
        }
    }


}
package com.knight.kotlin.library_widget

import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import androidx.core.widget.NestedScrollView
import kotlin.math.abs


/**
 * @author created by luguian
 * @organize
 * @Date 2025/3/6 9:14
 * @descript: 拦截垂直滑动 横向滑动交给子组件
 */
class IncepterVerticalNestScrollView : NestedScrollView {
    private var mInitialX = 0f
    private var mInitialY = 0f

    constructor(context: Context?) : super(context!!)

    constructor(context: Context?, attrs: AttributeSet?) : super(context!!, attrs)

    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(context!!, attrs, defStyleAttr)

    override fun onInterceptTouchEvent(ev: MotionEvent): Boolean {
        when (ev.action) {
            MotionEvent.ACTION_DOWN -> {
                mInitialX = ev.x
                mInitialY = ev.y
            }

            MotionEvent.ACTION_MOVE -> {
                val dx = abs((ev.x - mInitialX).toDouble()).toFloat()
                val dy = abs((ev.y - mInitialY).toDouble()).toFloat()
                if (dx > dy) {
                    return false // 横向滑动，不拦截事件
                }
            }
        }
        return super.onInterceptTouchEvent(ev) // 默认行为
    }
}

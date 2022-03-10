package com.knight.kotlin.library_widget.pagetransformer

import android.content.Context
import android.util.AttributeSet
import android.view.GestureDetector.SimpleOnGestureListener
import android.view.MotionEvent
import android.view.View
import android.view.View.OnClickListener
import android.view.ViewConfiguration
import android.widget.FrameLayout
import androidx.core.view.GestureDetectorCompat
import androidx.core.view.ViewCompat
import androidx.customview.widget.ViewDragHelper
import com.knight.kotlin.library_widget.R

/**
 * Author:Knight
 * Time:2022/3/7 18:08
 * Description:DragLayout
 */
class DragLayout :FrameLayout {

    private var bottomDragVisibleHeight // 滑动可见的高度
            = 0
    private var bototmExtraIndicatorHeight // 底部指示器的高度
            = 0
    private var dragTopDest = 0 // 顶部View滑动的目标位置

    private val DECELERATE_THRESHOLD = 120
    private val DRAG_SWITCH_DISTANCE_THRESHOLD = 100
    private val DRAG_SWITCH_VEL_THRESHOLD = 800

    private val MIN_SCALE_RATIO = 0.5f
    private val MAX_SCALE_RATIO = 1.0f

    private val STATE_CLOSE = 1
    private val STATE_EXPANDED = 2
    private var downState = 0//按下时的状态

    private var mDragHelper: ViewDragHelper? = null
    private var moveDetector: GestureDetectorCompat? = null
    private var mTouchSlop = 5 // 判定为滑动的阈值，单位是像素

    private var originX:Int = 0
    private var originY = 0 // 初始状态下，topView的坐标
    private var bottomView: View? = null
    private var topView:View? = null // FrameLayout的两个子View:View? = null

    private var gotoDetailListener: GotoDetailListener? = null

    interface GotoDetailListener {
        fun gotoDetail()
    }


    fun setGotoDetailListener(gotoDetailListener: GotoDetailListener) {
        this.gotoDetailListener = gotoDetailListener
    }
    @JvmOverloads
    constructor(context: Context, attributeSet: AttributeSet? = null, defAttrStyle: Int = 0)
            : super(context, attributeSet, defAttrStyle) {
        val a = context.obtainStyledAttributes(attributeSet, R.styleable.app, 0, 0)
        bottomDragVisibleHeight =
            a.getDimension(R.styleable.app_bottomDragVisibleHeight, 0f).toInt()
        bototmExtraIndicatorHeight =
            a.getDimension(R.styleable.app_bototmExtraIndicatorHeight, 0f).toInt()
        a.recycle()

        mDragHelper = ViewDragHelper
            .create(this, 10f, DragHelperCallback())
        mDragHelper?.setEdgeTrackingEnabled(ViewDragHelper.EDGE_TOP)
        moveDetector = GestureDetectorCompat(
            context,
            MoveDetector()
        )
        moveDetector?.setIsLongpressEnabled(false) // 不处理长按事件


        // 滑动的距离阈值由系统提供

        // 滑动的距离阈值由系统提供
        val configuration = ViewConfiguration.get(getContext())
        mTouchSlop = configuration.scaledTouchSlop
    }

    override fun onFinishInflate() {
        super.onFinishInflate()
        bottomView = getChildAt(0)
        topView = getChildAt(1)
        topView?.setOnClickListener(OnClickListener {
            // 点击回调
            val state: Int = getCurrentState()
            if (state == STATE_CLOSE) {
                // 点击时为初始状态，需要展开
                if (mDragHelper?.smoothSlideViewTo(topView!!, originX, dragTopDest) == true) {
                    ViewCompat.postInvalidateOnAnimation(this@DragLayout)
                }
            } else {
                // 点击时为展开状态，直接进入详情页
                gotoDetailActivity()
            }
        })
    }

    // 跳转到下一页
    private fun gotoDetailActivity() {
        gotoDetailListener?.let {
            it.gotoDetail()
        }
    }

    private inner class DragHelperCallback : ViewDragHelper.Callback() {
        override fun onViewPositionChanged(
            changedView: View,
            left: Int,
            top: Int,
            dx: Int,
            dy: Int
        ) {
            if (changedView === topView) {
                processLinkageView()
            }
        }

        override fun tryCaptureView(child: View, pointerId: Int): Boolean {
            return if (child === topView) {
                true
            } else false
        }

        override fun clampViewPositionVertical(child: View, top: Int, dy: Int): Int {
            val currentTop = child.top
            if (top > child.top) {
                // 往下拉的时候，阻力最小
                return currentTop + (top - currentTop) / 2
            }
            val result: Int
            result = if (currentTop > DECELERATE_THRESHOLD * 3) {
                currentTop + (top - currentTop) / 2
            } else if (currentTop >DECELERATE_THRESHOLD * 2) {
                currentTop + (top - currentTop) / 4
            } else if (currentTop > 0) {
                currentTop + (top - currentTop) / 8
            } else if (currentTop > -DECELERATE_THRESHOLD) {
                currentTop + (top - currentTop) / 16
            } else if (currentTop > -DECELERATE_THRESHOLD * 2) {
                currentTop + (top - currentTop) / 32
            } else if (currentTop > -DECELERATE_THRESHOLD * 3) {
                currentTop + (top - currentTop) / 48
            } else {
                currentTop + (top - currentTop) / 64
            }
            return result
        }

        override fun clampViewPositionHorizontal(child: View, left: Int, dx: Int): Int {
            return child.left
        }

        override fun getViewHorizontalDragRange(child: View): Int {
            return 600
        }

        override fun getViewVerticalDragRange(child: View): Int {
            return 600
        }

        override fun onViewReleased(releasedChild: View, xvel: Float, yvel: Float) {
            var finalY: Int = originY
            if (downState == STATE_CLOSE) {
                // 按下的时候，状态为：初始状态
                if (originY - releasedChild.top > DRAG_SWITCH_DISTANCE_THRESHOLD || yvel < -DRAG_SWITCH_VEL_THRESHOLD) {
                    finalY = dragTopDest
                }
            } else {
                // 按下的时候，状态为：展开状态
                val gotoBottom =
                    releasedChild.top - dragTopDest > DRAG_SWITCH_DISTANCE_THRESHOLD || yvel > DRAG_SWITCH_VEL_THRESHOLD
                if (!gotoBottom) {
                    finalY = dragTopDest

                    // 如果按下时已经展开，又向上拖动了，就进入详情页
                    if (dragTopDest - releasedChild.top > mTouchSlop) {
                        gotoDetailActivity()
                        postResetPosition()
                        return
                    }
                }
            }
            if (mDragHelper?.smoothSlideViewTo(releasedChild, originX, finalY) == true) {
                ViewCompat.postInvalidateOnAnimation(this@DragLayout)
            }
        }
    }

    private fun postResetPosition() {
        postDelayed({ topView?.offsetTopAndBottom(dragTopDest - topView?.top!!) }, 500)
    }

    /**
     * 顶层ImageView位置变动，需要对底层的view进行缩放显示
     */
    private fun processLinkageView() {
        if (topView?.top!! > originY) {
            bottomView?.alpha = 0f
        } else {
            var alpha = (originY - topView!!.top) * 0.01f
            if (alpha > 1) {
                alpha = 1f
            }
            bottomView?.alpha = alpha
            val maxDistance = originY - dragTopDest
            val currentDistance = topView?.top?.minus(dragTopDest)
            var scaleRatio = 1f
            val distanceRatio = currentDistance?.toFloat()?.div(maxDistance)
            if (currentDistance != null) {
                if (currentDistance > 0) {
                    scaleRatio =
                        MIN_SCALE_RATIO + (MAX_SCALE_RATIO - MIN_SCALE_RATIO) * (1 - distanceRatio!!)
                }
            }
            bottomView?.scaleX = scaleRatio
            bottomView?.scaleY = scaleRatio
        }
    }

    inner class MoveDetector : SimpleOnGestureListener() {
        override fun onScroll(
            e1: MotionEvent, e2: MotionEvent, dx: Float,
            dy: Float
        ): Boolean {
            // 拖动了，touch不往下传递
            return Math.abs(dy) + Math.abs(dx) > mTouchSlop
        }
    }

    override fun computeScroll() {
        if (mDragHelper!!.continueSettling(true)) {
            ViewCompat.postInvalidateOnAnimation(this)
        }
    }

    /**
     * 获取当前状态
     */
    private fun getCurrentState(): Int {
        val state: Int
        if (Math.abs(topView?.top?.minus(dragTopDest) ?: 0) <= mTouchSlop) {
            state = STATE_EXPANDED
        } else {
            state = STATE_CLOSE
        }
        return state
    }


    override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
        if (!changed) {
            return
        }
        super.onLayout(changed, left, top, right, bottom)
        originX = topView?.x?.toInt() ?: 0
        originY = topView?.y?.toInt() ?: 0
        dragTopDest = (bottomView?.bottom?.minus(bottomDragVisibleHeight) ?: 0) - topView!!.measuredHeight
    }

    /* touch事件的拦截与处理都交给mDraghelper来处理 */
    override fun onInterceptTouchEvent(ev: MotionEvent): Boolean {
        // 1. detector和mDragHelper判断是否需要拦截
        val yScroll = moveDetector!!.onTouchEvent(ev)
        var shouldIntercept = false
        try {
            shouldIntercept = mDragHelper!!.shouldInterceptTouchEvent(ev)
        } catch (e: Exception) {
        }

        // 2. 触点按下的时候直接交给mDragHelper
        val action = ev.actionMasked
        if (action == MotionEvent.ACTION_DOWN) {
            downState = getCurrentState()
            mDragHelper!!.processTouchEvent(ev)
        }
        return shouldIntercept && yScroll
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)

        // bottomMarginTop高度的计算，还是需要有一个清晰的数学模型才可以。
        // 实现的效果，是topView.top和bottomView.bottom展开前、与展开后都整体居中
        val bottomMarginTop =
            (bottomDragVisibleHeight + topView?.measuredHeight!! / 2 - bottomView!!.measuredHeight / 2) / 2 - bototmExtraIndicatorHeight
        val lp1 = bottomView?.layoutParams as LayoutParams
        lp1.setMargins(0, bottomMarginTop, 0, 0)
        bottomView?.layoutParams = lp1
    }

    override fun onTouchEvent(e: MotionEvent): Boolean {
        // 统一交给mDragHelper处理，由DragHelperCallback实现拖动效果
        try {
            mDragHelper?.processTouchEvent(e)
        } catch (ex: Exception) {
            ex.printStackTrace()
        }
        return true
    }


}
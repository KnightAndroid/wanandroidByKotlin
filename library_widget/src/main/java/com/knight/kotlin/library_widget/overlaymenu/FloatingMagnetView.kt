package com.knight.kotlin.library_widget.overlaymenu

import android.content.Context
import android.content.res.Configuration
import android.os.Handler
import android.os.Looper
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.ViewConfiguration
import android.view.ViewGroup
import android.widget.FrameLayout
import com.knight.kotlin.library_base.util.StatusBarUtils
import kotlin.math.abs
import kotlin.math.max
import kotlin.math.min


/**
 * @author created by luguian
 * @organize
 * @Date 2025/4/23 14:53
 * @descript:
 */
open class FloatingMagnetView @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) :
    FrameLayout(context, attrs, defStyleAttr) {
    private var mOriginalRawX = 0f
    private var mOriginalRawY = 0f
    private var mOriginalX = 0f
    private var mOriginalY = 0f
    private var mMagnetViewListener: MagnetViewListener? = null
    private var mLastTouchDownTime: Long = 0
    protected var mMoveAnimator: MoveAnimator? = null
    protected var mScreenWidth: Int = 0
    private var mScreenHeight = 0
    private var mStatusBarHeight = 0
    private var isNearestLeft = true
    private var mPortraitY = 0f
    private var dragEnable = true
    private var autoMoveToEdge = true

    fun setMagnetViewListener(magnetViewListener: MagnetViewListener?) {
        this.mMagnetViewListener = magnetViewListener
    }

    private fun init() {
        mMoveAnimator = MoveAnimator()
        mStatusBarHeight = StatusBarUtils.getStatusBarHeight(context)
        setClickable(true)
        //        updateSize();
    }

    /**
     * @param dragEnable 是否可拖动
     */
    fun updateDragState(dragEnable: Boolean) {
        this.dragEnable = dragEnable
    }

    /**
     * @param autoMoveToEdge 是否自动到边缘
     */
    fun setAutoMoveToEdge(autoMoveToEdge: Boolean) {
        this.autoMoveToEdge = autoMoveToEdge
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        if (event == null) {
            return false
        }
        when (event.action) {
            MotionEvent.ACTION_DOWN -> {}
            MotionEvent.ACTION_MOVE -> updateViewPosition(event)
            MotionEvent.ACTION_UP -> {
                clearPortraitY()
                if (autoMoveToEdge) {
                    moveToEdge()
                }
                if (isOnClickEvent()) {
                    dealClickEvent()
                }
            }
        }
        return true
    }

    protected fun dealClickEvent() {

        mMagnetViewListener?.onClick(this)

    }

    protected fun isOnClickEvent(): Boolean {
        return (System.currentTimeMillis() - mLastTouchDownTime) > TOUCH_TIME_THRESHOLD
    }

    private fun updateViewPosition(event: MotionEvent) {
        //dragEnable
        if (!dragEnable) return
        //占满width或height时不用变
        val params: LayoutParams = getLayoutParams() as LayoutParams
        //限制不可超出屏幕宽度
        var desX = mOriginalX + event.rawX - mOriginalRawX
        if (params.width === FrameLayout.LayoutParams.WRAP_CONTENT) {
            if (desX < 0) {
                desX = MARGIN_EDGE.toFloat()
            }
            if (desX > mScreenWidth) {
                desX = (mScreenWidth - MARGIN_EDGE).toFloat()
            }
            setX(desX)
        }
        // 限制不可超出屏幕高度
        var desY = mOriginalY + event.rawY - mOriginalRawY
        if (params.height === FrameLayout.LayoutParams.WRAP_CONTENT) {
            if (desY < mStatusBarHeight) {
                desY = mStatusBarHeight.toFloat()
            }
            if (desY > mScreenHeight - getHeight()) {
                desY = (mScreenHeight - getHeight()).toFloat()
            }
            setY(desY)
        }
    }

    private fun changeOriginalTouchParams(event: MotionEvent) {
        mOriginalX = getX()
        mOriginalY = getY()
        mOriginalRawX = event.rawX
        mOriginalRawY = event.rawY
        mLastTouchDownTime = System.currentTimeMillis()
    }

    protected fun updateSize() {
        val viewGroup = getParent() as ViewGroup?
        if (viewGroup != null) {
            mScreenWidth = viewGroup.width - getWidth()
            mScreenHeight = viewGroup.height
        }
        //        mScreenWidth = (SystemUtils.getScreenWidth(getContext()) - this.getWidth());
//        mScreenHeight = SystemUtils.getScreenHeight(getContext());
    }

    fun moveToEdge() {
        //dragEnable
        if (!dragEnable) return
        moveToEdge(isNearestLeft(), false)
    }

    fun moveToEdge(isLeft: Boolean, isLandscape: Boolean) {
        val moveDistance = (if (isLeft) MARGIN_EDGE else mScreenWidth - MARGIN_EDGE).toFloat()
        var y: Float = getY()
        if (!isLandscape && mPortraitY != 0f) {
            y = mPortraitY
            clearPortraitY()
        }
        mMoveAnimator!!.start(moveDistance, min(max(0.0, y.toDouble()), (mScreenHeight - getHeight()).toDouble()).toFloat())
    }

    private fun clearPortraitY() {
        mPortraitY = 0f
    }

    protected fun isNearestLeft(): Boolean {
        val middle = mScreenWidth / 2
        isNearestLeft = getX() < middle
        return isNearestLeft
    }

    fun onRemove() {
        mMagnetViewListener?.onRemove(this)
    }

    protected inner class MoveAnimator : Runnable {
        private val handler: Handler = Handler(Looper.getMainLooper())
        private var destinationX = 0f
        private var destinationY = 0f
        private var startingTime: Long = 0

        fun start(x: Float, y: Float) {
            this.destinationX = x
            this.destinationY = y
            startingTime = System.currentTimeMillis()
            handler.post(this)
        }

        override fun run() {
            if (getRootView() == null || getRootView().getParent() == null) {
                return
            }
            val progress = min(1.0, ((System.currentTimeMillis() - startingTime) / 400f).toDouble()).toFloat()
            val deltaX: Float = (destinationX - getX()) * progress
            val deltaY: Float = (destinationY - getY()) * progress
            move(deltaX, deltaY)
            if (progress < 1) {
                handler.post(this)
            }
        }

        internal fun stop() {
            handler.removeCallbacks(this)
        }
    }

    private fun move(deltaX: Float, deltaY: Float) {
        setX(getX() + deltaX)
        setY(getY() + deltaY)
    }

    protected override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        if (getParent() != null) {
            val isLandscape = newConfig.orientation === Configuration.ORIENTATION_LANDSCAPE
            markPortraitY(isLandscape)
            (getParent() as ViewGroup).post {
                updateSize()
                moveToEdge(isNearestLeft, isLandscape)
            }
        }
    }

    private fun markPortraitY(isLandscape: Boolean) {
        if (isLandscape) {
            mPortraitY = getY()
        }
    }

    private var touchDownX = 0f

    init {
        init()
    }

    private fun initTouchDown(ev: MotionEvent) {
        changeOriginalTouchParams(ev)
        updateSize()
        mMoveAnimator!!.stop()
    }

    override fun onInterceptTouchEvent(ev: MotionEvent): Boolean {
        var intercepted = false
        when (ev.actionMasked) {
            MotionEvent.ACTION_DOWN -> {
                intercepted = false
                touchDownX = ev.x
                initTouchDown(ev)
            }

            MotionEvent.ACTION_MOVE -> intercepted = abs((touchDownX - ev.x).toDouble()) >= ViewConfiguration.get(getContext()).scaledTouchSlop
            MotionEvent.ACTION_UP -> intercepted = false
        }
        return intercepted
    }

    companion object {
        const val MARGIN_EDGE: Int = 13
        private const val TOUCH_TIME_THRESHOLD = 150
    }
}
package com.knight.kotlin.library_widget.weatherview

import android.animation.ValueAnimator.AnimatorUpdateListener
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.LinearGradient
import android.graphics.Paint
import android.graphics.PointF
import android.graphics.Rect
import android.graphics.RectF
import android.graphics.Region
import android.graphics.Shader
import android.os.Build
import android.text.TextPaint
import android.util.AttributeSet
import android.view.GestureDetector.SimpleOnGestureListener
import android.view.MotionEvent
import android.view.View
import android.view.ViewConfiguration
import android.widget.OverScroller
import androidx.core.view.GestureDetectorCompat
import androidx.core.view.ViewCompat
import androidx.interpolator.view.animation.FastOutLinearInInterpolator
import java.util.Collections
import kotlin.math.abs
import kotlin.math.ceil
import kotlin.math.floor
import kotlin.math.min


/**
 * @Description
 * @Author knight
 * @Time 2025/3/5 22:24
 *
 */

class ScrollBarChartView(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) :
    View(context, attrs, defStyleAttr) {
    private var mChartPaint: Paint? = null
    private var mBound: Rect? = null

    private var textStart = 0f
    private var mHeight = 0
    private var mWidth = 0

    private var verticalList: List<Float> = ArrayList()
    private var horizontalList: List<String> = ArrayList()

    private val verticalWidth = 100f
    private var chartWidth = 0f //表的总宽度，除过外间距
    private val outSpace = verticalWidth // 柱子与纵轴的距离
    private var startChart = verticalWidth //柱子开始的横坐标

    private var interval = 0f //柱子之间的间隔
    private var barWidth = 0f //柱子的宽度

    private val bottomHeight = 100f //底部横坐标高度

    private var maxValue = "2" //默认最大值
    private var middleValue = "1"

    private val paddingTop = 20

    private var noDataPaint: Paint? = null
    private var textXpaint: TextPaint? = null
    private var linePaint: Paint? = null

    private val noDataColor = "#66FF6933"
    private val textColor = "#FFBEBEBE"
    private val lineColor = "#E4E5E6"
    private val chartColor = "#FF6933"
    private val yBgColor = "#66FF6933"
    private val mDuriation = 3000

    private var textYpaint: TextPaint? = null

    private var mContext: Context?

    private var mAnimator: ChartAnimator? = null
    private var mGestureDetector: GestureDetectorCompat? = null
    private var mScroller: OverScroller? = null
    private var yBackgroundPaint: Paint? = null

    enum class Direction {
        NONE, LEFT, RIGHT, VERTICAL
    }

    //正常滑动方向
    private var mCurrentScrollDirection = Direction.NONE

    //快速滑动方向
    private var mCurrentFlingDirection = Direction.NONE

    private val mHorizontalFlingEnabled = true

    private val mCurrentOrigin = PointF(0f, 0f)

    private val mScrollDuration = 250

    //滑动速度
    private val mXScrollingSpeed = 1f
    private var mMinimumFlingVelocity = 0


    ///////////////////////////////////////////////////////////////////////////
    // 滑动相关
    ///////////////////////////////////////////////////////////////////////////
    private val mGestureListener: SimpleOnGestureListener = object : SimpleOnGestureListener() {
        //手指按下
        override fun onDown(e: MotionEvent): Boolean {
            goToNearestBar()
            return true
        }

        //有效的滑动
        override fun onScroll(
            e1: MotionEvent?,
            e2: MotionEvent,
            distanceX: Float,
            distanceY: Float
        ): Boolean {
            when (mCurrentScrollDirection) {
                Direction.NONE ->                     // 只允许在一个方向上滑动
                    if (abs(distanceX.toDouble()) > abs(distanceY.toDouble())) {
                        if (distanceX > 0) {
                            mCurrentScrollDirection = Direction.LEFT
                        } else {
                            mCurrentScrollDirection = Direction.RIGHT
                        }
                    } else {
                        mCurrentScrollDirection = Direction.VERTICAL
                    }

                Direction.LEFT ->                     // Change direction if there was enough change.
                    if (abs(distanceX.toDouble()) > abs(distanceY.toDouble()) && (distanceX < 0)) {
                        mCurrentScrollDirection = Direction.RIGHT
                    }

                Direction.RIGHT ->                     // Change direction if there was enough change.
                    if (abs(distanceX.toDouble()) > abs(distanceY.toDouble()) && (distanceX > 0)) {
                        mCurrentScrollDirection = Direction.LEFT
                    }
                else ->{}
            }


            // 重新计算滑动后的起点
            when (mCurrentScrollDirection) {
                Direction.LEFT, Direction.RIGHT -> {
                    mCurrentOrigin.x -= distanceX * mXScrollingSpeed
                    ViewCompat.postInvalidateOnAnimation(this@ScrollBarChartView)
                }
                else->{}
            }

            return true
        }

        //快速滑动
        override fun onFling(
            e1: MotionEvent?,
            e2: MotionEvent,
            velocityX: Float,
            velocityY: Float
        ): Boolean {
            if ((mCurrentFlingDirection == Direction.LEFT && !mHorizontalFlingEnabled) ||
                (mCurrentFlingDirection == Direction.RIGHT && !mHorizontalFlingEnabled)
            ) {
                return true
            }

            mCurrentFlingDirection = mCurrentScrollDirection

            mScroller!!.forceFinished(true)

            when (mCurrentFlingDirection) {
                Direction.LEFT, Direction.RIGHT -> mScroller!!.fling(
                    mCurrentOrigin.x.toInt(),
                    mCurrentOrigin.y.toInt(),
                    (velocityX * mXScrollingSpeed).toInt(),
                    0,
                    Int.MIN_VALUE,
                    Int.MAX_VALUE,
                    0,
                    0
                )

                Direction.VERTICAL -> {}
                else ->{}
            }

            ViewCompat.postInvalidateOnAnimation(this@ScrollBarChartView)
            return true
        }


        //单击事件
        override fun onSingleTapConfirmed(e: MotionEvent): Boolean {
            return super.onSingleTapConfirmed(e)
        }

        //长按
        override fun onLongPress(e: MotionEvent) {
            super.onLongPress(e)
        }
    }


    override fun computeScroll() {
        super.computeScroll()


        if (mScroller!!.isFinished) { //当前滚动是否结束
            if (mCurrentFlingDirection != Direction.NONE) {
                goToNearestBar()
            }
        } else {
            if (mCurrentFlingDirection != Direction.NONE && forceFinishScroll()) { //惯性滑动时保证最左边条目展示正确
                goToNearestBar()
            } else if (mScroller!!.computeScrollOffset()) { //滑动是否结束 记录最新的滑动的点 惯性滑动处理
                mCurrentOrigin.y = mScroller!!.currY.toFloat()
                mCurrentOrigin.x = mScroller!!.currX.toFloat()
                ViewCompat.postInvalidateOnAnimation(this)
            }
        }
    }


    /**
     * Check if scrolling should be stopped.
     *
     * @return true if scrolling should be stopped before reaching the end of animation.
     */
    private fun forceFinishScroll(): Boolean {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
            mScroller!!.currVelocity <= mMinimumFlingVelocity
        } else {
            false
        }
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        //将view的OnTouchEvent事件交给手势监听器处理
        val `val` = mGestureDetector!!.onTouchEvent(event)

        // 正常滑动结束后 处理最左边的条目
        if (event.action == MotionEvent.ACTION_UP && mCurrentFlingDirection == Direction.NONE) {
            if (mCurrentScrollDirection == Direction.RIGHT || mCurrentScrollDirection == Direction.LEFT) {
                goToNearestBar()
            }
            mCurrentScrollDirection = Direction.NONE
        }
        return `val`
    }

    private fun goToNearestBar() {
        //让最左边的条目 显示出来

//        var leftBar = (mCurrentOrigin.x / (barWidth + interval)).toDouble()
//
//        leftBar = if (mCurrentFlingDirection != Direction.NONE) {
//            // 跳到最近一个bar
//            Math.round(leftBar).toDouble()
//        } else if (mCurrentScrollDirection == Direction.LEFT) {
//            // 跳到上一个bar
//            floor(leftBar)
//        } else if (mCurrentScrollDirection == Direction.RIGHT) {
//            // 跳到下一个bar
//            ceil(leftBar)
//        } else {
//            // 跳到最近一个bar
//            Math.round(leftBar).toDouble()
//        }
//
//        val nearestOrigin = (mCurrentOrigin.x - leftBar * (barWidth + interval)).toInt()
//
//        if (nearestOrigin != 0) {
//            // 停止当前动画
//            mScroller!!.forceFinished(true)
//            //开始滚动
//            mScroller!!.startScroll(
//                mCurrentOrigin.x.toInt(),
//                mCurrentOrigin.y.toInt(),
//                -nearestOrigin,
//                0,
//                (abs(nearestOrigin.toDouble()) / (barWidth + interval) * mScrollDuration).toInt()
//            )
//            ViewCompat.postInvalidateOnAnimation(this@ScrollBarChartView)
//        }
        //重新设置滚动方向.
        mCurrentFlingDirection = Direction.NONE
        mCurrentScrollDirection = mCurrentFlingDirection
    }

    constructor(context: Context?) : this(context, null) {
        this.mContext = context
        init()
    }

    constructor(context: Context?, attrs: AttributeSet?) : this(context, attrs, 0) {
        this.mContext = context
        init()
    }

    init {
        this.mContext = context
        init()
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)

        //宽度的模式
        val mWidthModle = MeasureSpec.getMode(widthMeasureSpec)
        //宽度大小
        val mWidthSize = MeasureSpec.getSize(widthMeasureSpec)

        val mHeightModle = MeasureSpec.getMode(heightMeasureSpec)
        val mHeightSize = MeasureSpec.getSize(heightMeasureSpec)
        //如果明确大小,直接设置大小
        if (mWidthModle == MeasureSpec.EXACTLY) {
            mWidth = mWidthSize
        } else {
            //计算宽度,可以根据实际情况进行计算
            mWidth = (paddingLeft + paddingRight)
            //如果为AT_MOST, 不允许超过默认宽度的大小
            if (mWidthModle == MeasureSpec.AT_MOST) {
                mWidth = min(mWidth.toDouble(), mWidthSize.toDouble()).toInt()
            }
        }
        if (mHeightModle == MeasureSpec.EXACTLY) {
            mHeight = mHeightSize
        } else {
            mHeight = (getPaddingTop() + paddingBottom)
            if (mHeightModle == MeasureSpec.AT_MOST) {
                mHeight = min(mHeight.toDouble(), mHeightSize.toDouble()).toInt()
            }
        }
        //设置测量完成的宽高
        setMeasuredDimension(mWidth, mHeight)
    }

    override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
        super.onLayout(changed, left, top, right, bottom)
        mWidth = width
        mHeight = height - paddingTop

        chartWidth = mWidth - outSpace
        //每个柱子宽度
        barWidth = 100f
        interval = 30f

        //柱子开始的横坐标
        startChart = outSpace

        //////////////////////////////////////////

        //横坐标
        textStart = startChart + (barWidth / 2f)
    }

    private fun init() {
        //初始化手势

        mGestureDetector = GestureDetectorCompat(mContext!!, mGestureListener)

        // 解决长按屏幕后无法拖动的现象 但是 长按 用不了
        mGestureDetector!!.setIsLongpressEnabled(false)


        mScroller = OverScroller(mContext, FastOutLinearInInterpolator())


        mMinimumFlingVelocity = ViewConfiguration.get(mContext!!).scaledMinimumFlingVelocity


        //初始化动画
        mAnimator = ChartAnimator(AnimatorUpdateListener { postInvalidate() })

        mBound = Rect()


        //柱子画笔
        mChartPaint = Paint()
        mChartPaint!!.isAntiAlias = true
        mChartPaint!!.color = Color.parseColor(chartColor)


        //线画笔
        linePaint = Paint()
        linePaint!!.isAntiAlias = true
        linePaint!!.color = Color.parseColor(lineColor)

        //x纵坐标 画笔
        textXpaint = TextPaint()
        textXpaint!!.isAntiAlias = true
        textXpaint!!.textSize = 27f
        textXpaint!!.textAlign = Paint.Align.CENTER
        textXpaint!!.color = Color.parseColor(textColor)

        //Y纵坐标 画笔
        textYpaint = TextPaint()
        textYpaint!!.isAntiAlias = true
        textYpaint!!.textSize = 28f
        textYpaint!!.textAlign = Paint.Align.LEFT
        textYpaint!!.color = Color.parseColor(textColor)

        //Y轴背景
        yBackgroundPaint = Paint()
        yBackgroundPaint!!.color = Color.parseColor(yBgColor)

        //无数据时的画笔
        noDataPaint = Paint()
        noDataPaint!!.isAntiAlias = true
        noDataPaint!!.color = Color.parseColor(noDataColor)
        noDataPaint!!.style = Paint.Style.FILL
    }


    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)


        val lineInterval = (mHeight - bottomHeight) / 4f //横线之间的间距  纵向
        val textHeight = mHeight + paddingTop - bottomHeight //横坐标高度

        //控制图表滑动左右边界
        if (mCurrentOrigin.x < width - (verticalList.size * barWidth + (verticalList.size - 1) * interval + outSpace)) mCurrentOrigin.x =
            width - (verticalList.size * barWidth + (verticalList.size - 1) * interval + outSpace)

        if (mCurrentOrigin.x > 0) mCurrentOrigin.x = 0f


        //画线
        drawLine(canvas, lineInterval, textHeight)

        //画纵坐标
        drawYtext(canvas, lineInterval, textHeight)

        //画横坐标
        val textTempStart = textStart

        drawXtext(canvas, textTempStart)

        val chartTempStart = startChart

        val size = (mHeight - bottomHeight) / 100f //比例

        //画柱子
        drawBar(canvas, chartTempStart, size)
    }

    /**
     * 画柱子
     *
     * @param canvas
     * @param chartTempStart
     * @param size
     */
    private fun drawBar(canvas: Canvas, chartTempStart: Float, size: Float) {
        var chartTempStart = chartTempStart
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            canvas.save()
            canvas.clipRect(outSpace - 5f, 0f, mWidth.toFloat(), height.toFloat())

            canvas.restore()
        } else {
            canvas.clipRect(outSpace - 10f, 0f, mWidth.toFloat(), height.toFloat(), Region.Op.REPLACE)

        }
        for (i in verticalList.indices) {
            //每个数据点所占的Y轴高度

            val barHeight = verticalList[i] / maxValue.toFloat() * 100f * size

            val realBarHeight: Float = barHeight * (mAnimator?.getPhaseY() ?: 0f)

            //画柱状图 矩形
            val rectF = RectF()
            rectF.left = chartTempStart + mCurrentOrigin.x
            rectF.top = (mHeight - bottomHeight + paddingTop) - realBarHeight
            rectF.right = chartTempStart + barWidth + mCurrentOrigin.x
            rectF.bottom = mHeight + paddingTop - bottomHeight

//            val lg = LinearGradient(
//                0f,
//                top,
//                0f,
//                bottom.toFloat(),
//                gradientStartColor,
//                gradientEndColor,
//                Shader.TileMode.CLAMP
//            )
//            mChartPaint!!.setShader(lg)
//

            canvas.drawRect(rectF, mChartPaint!!)

            chartTempStart += (barWidth + interval / 4 * 3)
        }
    }

    /**
     * 画x轴
     *
     * @param canvas
     * @param textTempStart
     */
    private fun drawXtext(canvas: Canvas, textTempStart: Float) {
        var textTempStart = textTempStart
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            canvas.save()
            canvas.clipRect(
                outSpace,
                height - bottomHeight,
                mWidth.toFloat(),
                height.toFloat()
            )
            canvas.restore()
        } else {

            canvas.clipRect(
                outSpace,
                height - bottomHeight,
                mWidth.toFloat(),
                height.toFloat(),
                Region.Op.REPLACE
            )

        }

        for (i in verticalList.indices) {
            textXpaint!!.getTextBounds(horizontalList[i], 0, horizontalList[i].length, mBound)
            canvas.drawText(
                horizontalList[i],
                textTempStart + mCurrentOrigin.x,
                mHeight + paddingTop - 60f + mBound!!.height() / 2f,
                textXpaint!!
            )
            //                canvas.drawLine(textTempStart, mHeight + paddingTop - bottomHeight, textTempStart, 0, linePaint);
            textTempStart += (barWidth + interval / 4 * 3)
        }
    }

    /**
     * 画Y轴
     *
     * @param canvas
     * @param lineInterval
     * @param textHeight
     */
    private fun drawYtext(canvas: Canvas, lineInterval: Float, textHeight: Float) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            canvas.save()
            canvas.clipRect(0f, 0f, outSpace, mHeight.toFloat())
            canvas.restore()
        } else {
            canvas.clipRect(0f, 0f, outSpace, mHeight.toFloat(),Region.Op.REPLACE)
        }

        canvas.drawText("0", 0f, textHeight, textYpaint!!)

        canvas.drawText(middleValue, 0f, textHeight - lineInterval * 2f + 10f, textYpaint!!)
        canvas.drawText(maxValue, 0f, textHeight - lineInterval * 4f + 10f, textYpaint!!)
    }

    /**
     * 画线
     *
     * @param canvas
     * @param lineInterval
     * @param textHeight
     */
    private fun drawLine(canvas: Canvas, lineInterval: Float, textHeight: Float) {
        canvas.drawLine(outSpace - 10f, textHeight, mWidth.toFloat(), textHeight, linePaint!!)
        canvas.drawLine(
            outSpace - 10f, textHeight - lineInterval, mWidth.toFloat(), textHeight - lineInterval,
            linePaint!!
        )
        canvas.drawLine(
            outSpace - 10f,
            textHeight - lineInterval * 2f,
            mWidth.toFloat(),
            textHeight - lineInterval * 2f,
            linePaint!!
        )
        canvas.drawLine(
            outSpace - 10f,
            textHeight - lineInterval * 3f,
            mWidth.toFloat(),
            textHeight - lineInterval * 3f,
            linePaint!!
        )
        canvas.drawLine(
            outSpace - 10f,
            textHeight - lineInterval * 4f,
            mWidth.toFloat(),
            textHeight - lineInterval * 4f,
            linePaint!!
        )
    }


    /**
     * 重新指定起始位置
     *
     * @param verticalList
     */
    private fun measureWidthShort(verticalList: List<Float>) {
        startChart = outSpace
        textStart = startChart + barWidth / 2f
    }


    /**
     * 设置纵轴数据
     *
     * @param verticalList
     */
    fun setVerticalList(verticalList: List<Float>?) {
        if (verticalList != null) {
            this.verticalList = verticalList
        } else {
            maxValue = "2"
            middleValue = "1"
            invalidate()
            return
        }


        measureWidthShort(verticalList)

        if (Collections.max(verticalList) > 2) {
            var tempMax = Math.round(Collections.max(verticalList))
            while (tempMax % 10 != 0) {
                tempMax++
            }
            val middle = tempMax / 2
            maxValue = tempMax.toString()
            middleValue = middle.toString()
        } else {
            maxValue = "2"
            middleValue = "1"
        }

        mAnimator?.animateY(mDuriation)
    }

    /**
     * 设置横轴数据
     *
     * @param horizontalList
     */
    fun setHorizontalList(horizontalList: List<String>?) {
        if (horizontalList != null) this.horizontalList = horizontalList
    }

    companion object {
        private const val TAG = "ScrollBar"
    }
}
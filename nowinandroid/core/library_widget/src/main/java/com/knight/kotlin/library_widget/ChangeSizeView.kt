package com.knight.kotlin.library_widget

import android.content.Context
import android.content.res.TypedArray
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Point
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import com.core.library_base.util.dp2px

/**
 * Author:Knight
 * Time:2022/6/23 12:07
 * Description:ChangeSizeView
 */
class ChangeSizeView : View {

    private val defaultLineColor = Color.rgb(33, 33, 33)
    private var defaultLineWidth = 0
    private val defaultMax = 5
    private val defaultCircleColor = Color.WHITE
    private var defaultCircleRadius = 0
    // 当前所在位置
    private var currentProgress = 0
    // 默认位置
    private var defaultPosition = 1
    // 一共有多少格
    private var max = 6
    // 线条颜色
    private var lineColor = Color.BLACK
    // 线条粗细
    private var lineWidth = 0
    //字体颜色
    private var textColor = Color.BLACK
    //字体大小
    private var smallSize = 14
    private var standerSize = 16
    private var bigSize = 28
    // 圆半径
    private var circleRadius = 0
    private var circleColor = Color.WHITE
    // 一段的宽度，根据总宽度和总格数计算得来
    private var itemWidth = 0
    // 控件的宽高
    private var sizeHeight = 0
    private var sizeWidth = 0
    // 画笔
    private lateinit var mLinePaint: Paint
    private lateinit var mTextPaint: Paint
    private lateinit var mText1Paint: Paint
    private lateinit var mText2Paint: Paint
    private lateinit var mCirclePaint: Paint
    // 滑动过程中x坐标
    private var currentX = 0f
    // 有效数据点
    private val points = mutableListOf<Point>()

    private var circleY = 0f
    private var textScaleX = 0f
    private var text1ScaleX = 0f
    private var text2ScaleX = 0f


    @JvmOverloads
    constructor(context: Context, attributeSet: AttributeSet? = null, defAttrStyle: Int = 0)
            : super(context, attributeSet, defAttrStyle) {
        init(context,attributeSet)
    }



    private fun init(context:Context,attrs:AttributeSet?) {
        defaultLineWidth = 2.dp2px()
        defaultCircleRadius = 35.dp2px()
        lineWidth = 1.dp2px()
        val typedArray = context.obtainStyledAttributes(attrs, R.styleable.FontSizeView)
        val N = typedArray.indexCount
        for (i in 0 until N) {
            initCustomAttr(typedArray.getIndex(i), typedArray)
        }
        typedArray.recycle()
        // 初始化画笔
        mLinePaint = Paint(Paint.ANTI_ALIAS_FLAG)
        mLinePaint.color = lineColor
        mLinePaint.style = Paint.Style.FILL_AND_STROKE
        mLinePaint.strokeWidth = lineWidth.toFloat()
        //初始化文字画笔
        mTextPaint = Paint(Paint.ANTI_ALIAS_FLAG)
        mTextPaint.color = textColor
        mTextPaint.style = Paint.Style.FILL_AND_STROKE
        mTextPaint.textSize = smallSize.dp2px().toFloat()
        textScaleX = mTextPaint.measureText("A")

        //初始化文字画笔1
        mText1Paint = Paint(Paint.ANTI_ALIAS_FLAG)
        mText1Paint.color = textColor
        mText1Paint.style = Paint.Style.FILL_AND_STROKE
        mText1Paint.textSize = bigSize.dp2px().toFloat()
        text1ScaleX = mText1Paint.measureText("A")

        //初始化文字画笔2
        mText2Paint = Paint(Paint.ANTI_ALIAS_FLAG)
        mText2Paint.color = textColor
        mText2Paint.style = Paint.Style.FILL_AND_STROKE
        mText2Paint.textSize = standerSize.dp2px().toFloat()
        text2ScaleX = mText2Paint.measureText("标准")

        mCirclePaint = Paint(Paint.ANTI_ALIAS_FLAG)
        mCirclePaint.color = circleColor
        mCirclePaint.style = Paint.Style.FILL

        //设置阴影效果
        setLayerType(LAYER_TYPE_SOFTWARE, null)
        mCirclePaint.setShadowLayer(2f,0f,0f, Color.rgb(33, 33, 33))
    }

    private fun initCustomAttr(attr: Int, typedArray: TypedArray) {
        if (attr == R.styleable.FontSizeView_lineColor) {
            lineColor = typedArray.getColor(attr, defaultLineColor)
        } else if (attr == R.styleable.FontSizeView_circleColor) {
            circleColor = typedArray.getColor(attr, defaultCircleColor)
        } else if (attr == R.styleable.FontSizeView_lineWidth) {
            lineWidth = typedArray.getDimensionPixelSize(attr, defaultLineWidth)
        } else if (attr == R.styleable.FontSizeView_circleRadius) {
            circleRadius = typedArray.getDimensionPixelSize(attr, defaultCircleRadius)
        } else if (attr == R.styleable.FontSizeView_totalCount) {
            max = typedArray.getInteger(attr, defaultMax)
        } else if (attr == R.styleable.FontSizeView_textFontColor) {
            textColor = typedArray.getColor(attr, textColor)
        } else if (attr == R.styleable.FontSizeView_smallSize) {
            smallSize = typedArray.getInteger(attr, smallSize)
        } else if (attr == R.styleable.FontSizeView_standerSize) {
            standerSize = typedArray.getInteger(attr, standerSize)
        } else if (attr == R.styleable.FontSizeView_bigSize) {
            bigSize = typedArray.getInteger(attr, bigSize)
        } else if (attr == R.styleable.FontSizeView_defaultPosition) {
            defaultPosition = typedArray.getInteger(attr, defaultPosition)
        }
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        sizeHeight = h
        sizeWidth = w - 20.dp2px()
        circleY = (sizeHeight / 2).toFloat()
        // 横线宽度是总宽度-2个圆的半径
        itemWidth = (sizeWidth - 2 * circleRadius) / max
        var startX: Int
        // 把可点击点保存起来
        for (i in 0..max) {
            startX = circleRadius + i * itemWidth + 10.dp2px()
            points.add(Point(startX, sizeHeight / 2))
        }
        //初始刻度
        currentX = points[defaultPosition].x.toFloat()
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        //画字
        canvas.drawText("A", points[0].x - textScaleX / 2, (sizeHeight / 2 - 50).toFloat(), mTextPaint)

        //画字
        canvas.drawText("标准", points[1].x - text2ScaleX / 2, (sizeHeight / 2 - 50).toFloat(), mText2Paint)

        //画字
        canvas.drawText("A", points[points.size - 1].x - text1ScaleX / 2, (sizeHeight / 2 - 50).toFloat(), mText1Paint)

        // 先画中间的横线
        canvas.drawLine(points[0].x.toFloat(), (sizeHeight / 2).toFloat(), points[points.size - 1].x.toFloat(), (sizeHeight / 2).toFloat(), mLinePaint)
        // 绘制刻度
        for (point in points) {
            canvas.drawLine((point.x + 1).toFloat(), (sizeHeight / 2 - 20).toFloat(), (point.x + 1).toFloat(), (sizeHeight / 2 + 20).toFloat(), mLinePaint)
        }

        // 画圆
        if (currentX < circleRadius) {
            currentX = circleRadius.toFloat()
        }
        if (currentX > sizeWidth - circleRadius) {
            currentX = sizeWidth.toFloat()
        }

        // 实体圆
        canvas.drawCircle(currentX + 1, circleY, circleRadius.toFloat(), mCirclePaint)
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        currentX = event.x
        when (event.action) {
            MotionEvent.ACTION_DOWN -> invalidate()
            MotionEvent.ACTION_MOVE -> invalidate()
            MotionEvent.ACTION_UP -> {
                //回到最近的一个刻度点
                val targetPoint: Point? = getNearestPoint(currentX)
                if (targetPoint != null) {
                    // 最终
                    currentX = points[currentProgress].x.toFloat()
                    invalidate()
                }
                onChangeCallbackListener?.let {
                    it.onChangeListener(currentProgress)
                }
            }
        }
        return true
    }

    /**
     * 获取最近的刻度
     */
    private fun getNearestPoint(x: Float): Point? {
        for (i in points.indices) {
            val point = points[i]
            if (Math.abs(point.x - x) < itemWidth / 2) {
                currentProgress = i
                return point
            }
        }
        return null
    }


    interface OnChangeCallbackListener {
        fun onChangeListener(position:Int)
    }


    private var onChangeCallbackListener:OnChangeCallbackListener?=null

    fun setChangeCallbackListener(listener:OnChangeCallbackListener) {
        this.onChangeCallbackListener = listener

    }

    /**
     *
     * 设置默认位置
     * @param position
     */
    fun setDefaultPosition(position: Int) {
        defaultPosition = position
        onChangeCallbackListener?.let {
            it.onChangeListener(defaultPosition)
        }
        invalidate()
    }
}
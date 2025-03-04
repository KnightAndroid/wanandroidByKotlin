package com.knight.kotlin.library_widget.weatherview

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.LinearGradient
import android.graphics.Paint
import android.graphics.RectF
import android.graphics.Shader
import android.util.AttributeSet
import android.view.View
import com.knight.kotlin.library_base.util.dp2px
import com.knight.kotlin.library_widget.R
import java.util.Calendar


/**
 * @Description
 * @Author knight
 * @Time 2025/3/4 20:55
 * 降雨量柱线图
 */

class BarChartView @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null) :
    View(context, attrs) {
    private val mContext = context
    private var mLinePaint: Paint? = null //画线的画笔
    private var mTextPaint: Paint? = null //文字的画笔
    private var mChartPaint: Paint? = null //柱形图的画笔

    private var mStartX = 0 //x坐标开始值
    private var mStartY = 0 //Y坐标结束的值
    private var mStopX = 0 //x坐标结束的值
    private var mStopY = 0 //y坐标结束的值
    private var barWidth = 0 //每条柱形图的柱宽度
    private var totalBarNum = 6 //柱形图中的柱数量
    private var max = -1f //最大值，用于计算比例
    private var deltaY = 0 //刻度值间距
    private var deltaX = 0 //柱形图之间的间距
    private var currentVerticalLineProgress = 0 //每条柱图的当前比例
    var numPerUnit: Float = 0f //刻度值
    var mWidth: Int = 0 //测量后屏幕的宽度
    var mHeight: Int = 0 //测量后屏幕的高度
    private var horizentalLineNum = 2 //刻度线的数量值
    private var totalBarWidth = 0 //柱形图总宽度
    private var datas: List<VvalueAndHunit>? = null
    private var unit: String? = "单位" //单位
    private var lineColor = Color.parseColor("#333333") //线条颜色
    private var chartColor = Color.parseColor("#666666") //图表颜色
    private var textColor = Color.parseColor("#666666")
    private var gradientStartColor = Color.parseColor("#ffa666")
    private var gradientEndColor = Color.parseColor("#e57a2e")

    init {
        init(attrs)
        initDefaultData()
    }


    /**
     * 初始化默认数据
     */
    private fun initDefaultData() {
        val mDatas = ArrayList<VvalueAndHunit>()
        val mCalendar: Calendar = Calendar.getInstance()
        val today: Int = mCalendar.get(Calendar.DAY_OF_MONTH)
        val defaultList = ArrayList<Int>()
        for (i in 0..6) {
            defaultList.add(1000)
        }
        for (j in 0..6) {
            val vah: VvalueAndHunit = VvalueAndHunit()
            vah.value = defaultList[j].toFloat()
            vah.setUnit((today - (6 - j)).toString() + "")
            mDatas.add(vah)
            this.datas = mDatas
        }
    }

    private fun init(attrs: AttributeSet?) {
        val typedArray = mContext.obtainStyledAttributes(attrs, R.styleable.BarChartView)

        barWidth = typedArray.getInteger(R.styleable.BarChartView_barWidth, 10)

        max = typedArray.getInteger(R.styleable.BarChartView_maxValue, 1000).toFloat()

        horizentalLineNum = typedArray.getInteger(R.styleable.BarChartView_horizentalLineNum, 2)

        unit = typedArray.getString(R.styleable.BarChartView_unit)

        lineColor =
            typedArray.getColor(R.styleable.BarChartView_barlineColor, Color.parseColor("#333333"))


        chartColor =
            typedArray.getColor(R.styleable.BarChartView_chartColor, Color.parseColor("#666666"))


        textColor =
            typedArray.getColor(R.styleable.BarChartView_bartextColor, Color.parseColor("#666666"))
        gradientStartColor = typedArray.getColor(
            R.styleable.BarChartView_gradientStartColor,
            Color.parseColor("#ffa666")
        )
        gradientEndColor = typedArray.getColor(
            R.styleable.BarChartView_gradientEndColor,
            Color.parseColor("#e57a2e")
        )

        mLinePaint = Paint(Paint.ANTI_ALIAS_FLAG)
        mLinePaint!!.color = lineColor
        mLinePaint!!.style = Paint.Style.FILL

        mChartPaint = Paint(Paint.ANTI_ALIAS_FLAG)
        mChartPaint!!.color = chartColor
        mChartPaint!!.style = Paint.Style.FILL
        mTextPaint = Paint()
        mTextPaint!!.flags = Paint.ANTI_ALIAS_FLAG
        mTextPaint!!.color = textColor
        mTextPaint!!.textSize = 16.dp2px().toFloat()
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        val widthSpecMode = MeasureSpec.getMode(widthMeasureSpec)
        val heightSpecMode = MeasureSpec.getMode(heightMeasureSpec)
        val widthSpecSize = MeasureSpec.getSize(widthMeasureSpec)
        val heightSpecSize = MeasureSpec.getSize(heightMeasureSpec)
        if (widthSpecMode == MeasureSpec.AT_MOST && heightSpecMode == MeasureSpec.AT_MOST) {
            //如果高度和宽度都是warp_content,宽度就是输入的最大值，max值，也就是柱形图的最大值，
            // 高度为每条柱形图的宽度加上间距再乘以柱形图条数再加上开始Y值后得到的值
            setMeasuredDimension(mStartX + 10 + totalBarNum * (barWidth + 2 * 10), max.toInt())
        } else if (widthSpecMode == MeasureSpec.AT_MOST) {
            //如果宽度为wrap_content，高度为match_parent或者精确值的时候
            setMeasuredDimension(max.toInt(), heightSpecSize)
        } else if (heightSpecMode == MeasureSpec.AT_MOST) {
            //如果高度为wrap_content,宽度为match_parent或者精确值的时候
            setMeasuredDimension(widthSpecSize, mStartY + 10 + totalBarNum * (barWidth + 2 * 10))
        }
    }

    override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
        super.onLayout(changed, left, top, right, bottom)
        //测量后的宽度
        mWidth = getMeasuredWidth()
        totalBarWidth = mWidth * 4 / 5
        //测量后的高度
        mHeight = getMeasuredHeight()
        //计算结束Y的值
        //x坐标开始值为0
        mStartX = 0 * barWidth
        // y坐标开始值为50
        mStartY = 0 * barWidth
        mStopX = mWidth - 2 * barWidth
        mStopY = mHeight - 2 * barWidth
        deltaY = (mStopY - (mStartY + 7 * barWidth / 5)) / horizentalLineNum
        deltaX = (totalBarWidth - mStartX - barWidth * totalBarNum) / totalBarNum
        numPerUnit = (max.toInt() / horizentalLineNum).toFloat()
        currentVerticalLineProgress = mStopY
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        drawScaleLine(canvas)
        drawBar(canvas)
    }

    /**
     * 画刻度线刻度值
     *
     * @param canvas
     */
    private fun drawScaleLine(canvas: Canvas) {
        canvas.drawLine(
            (mStartX - barWidth).toFloat(),
            (mStartY + 1).toFloat(),
            mWidth.toFloat(),
            (mStartY + 1).toFloat(),
            mLinePaint!!
        )
        canvas.drawLine(
            (mStartX - barWidth).toFloat(),
            (mStopY - (mStopY - mStartY) * 1 / 3).toFloat(),
            mWidth.toFloat(),
            (mStopY - (mStopY - mStartY) * 1 / 3).toFloat(),
            mLinePaint!!
        )
        canvas.drawLine(
            (mStartX - barWidth).toFloat(),
            (mStopY - (mStopY - mStartY) * 2 / 3).toFloat(),
            mWidth.toFloat(),
            (mStopY - (mStopY - mStartY) * 2 / 3).toFloat(),
            mLinePaint!!
        )
        canvas.drawLine(
            (mStartX - barWidth).toFloat(),
            mStopY.toFloat(),
            mWidth.toFloat(),
            mStopY.toFloat(),
            mLinePaint!!
        )
        canvas.drawText(
            "0", (mWidth * 19 / 20).toFloat(), (mStopY - 5).toFloat(),
            mTextPaint!!
        )
        if (max < 10) {
            canvas.drawText(
                Math.round(max).toString() + "",
                (mWidth * 18 / 20 - 5.dp2px()).toFloat(),
                (mStartY + 20.dp2px()).toFloat(),
                mTextPaint!!
            )
        } else {
            canvas.drawText(
                Math.round(max).toString() + "",
                (mWidth * 18 / 20 - 5.dp2px()).toFloat(),
                (mStartY + 20.dp2px().toFloat()),
                mTextPaint!!
            )
        }
    }

    /**
     * 画柱形图
     *
     * @param canvas
     */
    private fun drawBar(canvas: Canvas) {
        for (i in 0 until totalBarNum) {
            val top = mStopY - datas!![i].value / max * mStopY
            val left = mStartX + deltaX + i * (barWidth + deltaX)
            val right = mStartX + deltaX + i * (barWidth + deltaX) + barWidth
            val bottom = mStopY

            val lg = LinearGradient(
                0f,
                top,
                0f,
                bottom.toFloat(),
                gradientStartColor,
                gradientEndColor,
                Shader.TileMode.CLAMP
            )
            mChartPaint!!.setShader(lg)
            val roundRectF = RectF(left.toFloat(), top, right.toFloat(), bottom.toFloat())
            canvas.drawRoundRect(roundRectF, 8f, 8f, mChartPaint!!)
            val textX = mStartX + deltaX + i * (barWidth + deltaX)
            val textY = this.mStopY + (barWidth * 3 / 2)
            val name = datas!![i].getUnit()
            if (i == totalBarNum - 1) {
                canvas.drawText("今天", textX.toFloat(), textY.toFloat(), mTextPaint!!)
            } else {
                canvas.drawText(name!!, textX.toFloat(), textY.toFloat(), mTextPaint!!)
            }
        }
    }

    fun setBarWidth(width: Int) {
        this.barWidth = width

        mTextPaint!!.textSize = (barWidth * 1.2).toFloat()
    }

    /**
     * 设置最大值
     *
     * @param max
     */
    fun setMax(max: Float) {
        this.max = max
    }

    /**
     * 设置单位
     *
     * @param unit
     */
    fun setUnit(unit: String?) {
        this.unit = unit
    }

    fun setHorizentalLineNum(num: Int) {
        this.horizentalLineNum = num
    }

    fun setDatas(datas: List<VvalueAndHunit>) {
        this.datas = datas
        this.totalBarNum = datas.size
        invalidate()
    }

    inner class VvalueAndHunit {
        var value: Float = 0f //柱形图柱值
        private var yUnit: String? = null //柱形图横坐标名

        constructor(xValue: Float, yUnit: String?) {
            this.value = xValue
            this.yUnit = yUnit
        }

        constructor()

        fun setUnit(yUnit: String?) {
            this.yUnit = yUnit
        }

        fun getUnit(): String? {
            return yUnit
        }
    }
}
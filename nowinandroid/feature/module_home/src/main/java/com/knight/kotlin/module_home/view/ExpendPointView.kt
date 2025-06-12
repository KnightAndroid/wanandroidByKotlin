package com.knight.kotlin.module_home.view

import android.annotation.SuppressLint
import android.app.Service
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.os.Vibrator
import android.util.AttributeSet
import android.view.View
import kotlin.math.max
import kotlin.math.pow

/**
 * Author:Knight
 * Time:2024/4/18 10:49
 * Description:ExpendPointView 首页下滑三个点变化view
 */
class ExpendPointView@JvmOverloads constructor(context: Context,attrs:AttributeSet?=null, defStyleAttr: Int = 0)  : View(context, attrs,defStyleAttr){


    private val mPaint: Paint
    private val mBgPaint: Paint
    private var percent = 0f
    private var isExpanded = false
    private var BgAlpha = 255

    /** 以下为可调参数，请根据实际情况进行调整  */
    private val firstPercent = 0.08f //开始出现三个圆的页面下拉偏移量

    private val secondPercent = 0.21f //开始渐变的页面下拉偏移量

    private val maxRadius = 170f //最大的圆半径

    private val minRadius = 8f //最小的圆半径

    private val maxDistance = 55f //两圆之间的最大间距

    private val radiusRate = 0.03f //中心圆的缩放速度，值越大缩放越快

    private val pointAlphaRate = 5f //圆点渐变速度，值越大渐变越快

    private val bgAlphaRate = 1f //背景色渐变速度，值越大渐变越快

    private var vibrator: Vibrator // 振动器




    init {

        vibrator = context.getSystemService(Service.VIBRATOR_SERVICE) as Vibrator
        //绘制圆点的画笔
        //绘制圆点的画笔
        mPaint = Paint()
        mPaint.setAntiAlias(true)
        mPaint.setColor(Color.parseColor("#686868"))
        //绘制背景的画笔
        //绘制背景的画笔
        mBgPaint = Paint()
        mBgPaint.setAntiAlias(true)
        mBgPaint.setColor(Color.parseColor("#ededed"))
    }

    /**
     * 设置百分比
     * @param percent 百分比
     */
    fun setPercent(percent: Float) {
        this.percent = percent
        invalidate()
    }

    @SuppressLint("MissingPermission")
    override fun onDraw(canvas: Canvas) {
        //绘制背景
        canvas.drawRect(0f, 0f, measuredWidth.toFloat(), measuredHeight.toFloat(), mBgPaint)
        super.onDraw(canvas)
        val centerX = (width / 2).toFloat()
        val centerY = (height / 2).toFloat()
        if (percent > 0 && percent < firstPercent) {
            //绘制中心点，不断变大
            mPaint.setAlpha(255)
            mBgPaint.setAlpha(255)
            val curRadius: Float = percent * maxRadius
            canvas.drawCircle(centerX, centerY, curRadius, mPaint)
            setExpandStatus(false)
        } else if (percent >= firstPercent && percent < secondPercent) {
            //中心点缩小，并出现三个点，逐渐分离
            mPaint.setAlpha(255)
            mBgPaint.setAlpha(255)
            vibrator.cancel()
            vibrator.vibrate(10)
            val rate: Float =
                (percent - firstPercent) / (secondPercent - firstPercent)
            var curRadius: Float =
                firstPercent * maxRadius - maxRadius * rate * radiusRate
            curRadius =
                max(curRadius.toDouble(), minRadius.toDouble()).toFloat() //计算中心圆的半径
            val offsetX: Float = maxDistance * rate //计算每两个圆之间的间距
            canvas.drawCircle(centerX, centerY, curRadius, mPaint)
            canvas.drawCircle(centerX - offsetX, centerY, minRadius, mPaint)
            canvas.drawCircle(centerX + offsetX, centerY, minRadius, mPaint)
            setExpandStatus(false)
        } else if (percent >= secondPercent && percent < 1f) {
            //三个点等距，并逐渐透明至消失
            val rate: Float =
                (percent - secondPercent) / (1f - secondPercent)
            val pointRate: Float = pointAlphaRate * rate
            val bgRate: Float = bgAlphaRate * rate.pow(3.0f)
            var pointAlpha: Int =
                (255 * (1f - percent + secondPercent) * (1f - pointRate)).toInt()
            BgAlpha = (255 * (1f - percent + secondPercent) * (1f - bgRate)).toInt()
            pointAlpha = max(pointAlpha.toDouble(), 0.0).toInt()
            BgAlpha = max(BgAlpha.toDouble(), 0.0).toInt()
            mPaint.setAlpha(pointAlpha)
            mBgPaint.setAlpha(BgAlpha)
            canvas.drawCircle(centerX, centerY, minRadius, mPaint)
            canvas.drawCircle(centerX - 54.9f, centerY, minRadius, mPaint)
            canvas.drawCircle(centerX + 54.9f, centerY, minRadius, mPaint)
            setExpandStatus(true)
        }
    }

    /**
     * 设置圆点动画完全执行的状态
     * @param isExpanded 状态值，true为执行完动画，进入渐变
     */
    fun setExpandStatus(isExpanded: Boolean) {
        this.isExpanded = isExpanded
    }

    /**
     * 获取背景的透明度
     * @return 透明度
     */
    fun getBackgroundAlpha(): Int {
        return BgAlpha
    }




}
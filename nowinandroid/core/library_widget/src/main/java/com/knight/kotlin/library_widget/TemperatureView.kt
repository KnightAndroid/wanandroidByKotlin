package com.knight.kotlin.library_widget

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View
import com.core.library_common.util.CacheUtils
import com.core.library_common.dp2px


/**
 * @author created by luguian
 * @organize
 * @Date 2025/2/24 14:10
 * @descript:温度曲线图
 */
class TemperatureView @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null) :
    View(context, attrs) {
    private var maxTemp = 0
    private var minTemp = 0

    private var temperatureDay = 0
    private var temperatureNight = 0

    //白天圆点
    private var dayPointPaint: Paint? = null
    //晚上圆点
    private var nightPointPaint: Paint? = null
    //白色圆点
    private var whitePointPaint:Paint? = null


    private var linePaint: Paint? = null
    private var textPaint: Paint? = null
    private var lineColor = 0
    private var dayPointColor = 0
    private var nightPointColor = 0
    private var textColor = 0

    private var radius = 6
    private var textSize = 26

    private var xPointDay = 0
    private var yPointDay = 0
    private var xPointNight = 0
    private var yPointNight = 0
    private var mWidth = 0

    init {
        init(context, attrs)
    }

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : this(context, attrs)

    private fun init(context: Context, attrs: AttributeSet?) {
        initAttrs(context, attrs)

        initPaint(context, attrs)
    }

    private fun initAttrs(context: Context, attrs: AttributeSet?) {
        lineColor = 0xff93a122.toInt()
        textColor = if (CacheUtils.getNormalDark()) Color.parseColor("#8D8D8E") else Color.parseColor("#565657")
        dayPointColor = Color.parseColor("#102BF8")
        nightPointColor = Color.parseColor("#ED106A")
    }

    private fun initPaint(context: Context, attrs: AttributeSet?) {
        textSize = 15.dp2px()

        dayPointPaint = Paint()

        nightPointPaint = Paint()

        whitePointPaint = Paint()
        whitePointPaint?.style = Paint.Style.STROKE
        whitePointPaint?.strokeWidth = 6f
        whitePointPaint?.color = Color.parseColor("#ffffff")
        linePaint = Paint()
        textPaint = Paint()

        linePaint!!.color = lineColor
        dayPointPaint!!.color = dayPointColor
        dayPointPaint!!.isAntiAlias = true
        nightPointPaint!!.color = nightPointColor
        nightPointPaint!!.isAntiAlias = true


        textPaint!!.color = textColor
        textPaint!!.textSize = textSize.toFloat()
        textPaint!!.isAntiAlias = true
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        drawPoint(canvas)

        drawText(canvas)
    }

    fun setTextSize(textSize: Int) {
        this.textSize = textSize
        textPaint!!.textSize = textSize.toFloat()
        invalidate()
    }

    private fun drawPoint(canvas: Canvas) {
        val height = height - textSize * 4
        val x = width / 2
        val y = (height - height * (temperatureDay - minTemp) * 1.0f / (maxTemp - minTemp)).toInt() + textSize * 2
        val x2 = width / 2
        val y2 = (height - height * (temperatureNight - minTemp) * 1.0f / (maxTemp - minTemp)).toInt() + textSize * 2
        xPointDay = x
        yPointDay = y
        xPointNight = x2
        yPointNight = y2
        mWidth = width
        canvas.drawCircle(x.toFloat(), y.toFloat(), radius.toFloat() - whitePointPaint!!.strokeWidth / 2, dayPointPaint!!)

        canvas.drawCircle(x.toFloat(), y.toFloat(), radius.toFloat(), whitePointPaint!!)

        canvas.drawCircle(x2.toFloat(), y2.toFloat(), radius.toFloat() - whitePointPaint!!.strokeWidth / 2, nightPointPaint!!)

        canvas.drawCircle(x2.toFloat(), y2.toFloat(), radius.toFloat(), whitePointPaint!!)
    }

    private fun drawText(canvas: Canvas) {
        val height = height - textSize * 4
        val yDay = (height - height * (temperatureDay - minTemp) * 1.0f / (maxTemp - minTemp)).toInt() + textSize * 2
        val yNight = (height - height * (temperatureNight - minTemp) * 1.0f / (maxTemp - minTemp)).toInt() + textSize * 2
        val dayTemp = "$temperatureDay°"
        val nightTemp = "$temperatureNight°"
        val widDay = textPaint!!.measureText(dayTemp)
        val widNight = textPaint!!.measureText(nightTemp)
        val hei = textPaint!!.descent() - textPaint!!.ascent()
        canvas.drawText(dayTemp, width / 2.0f - widDay / 2, yDay - radius - hei / 2, textPaint!!)
        canvas.drawText(nightTemp, width / 2.0f - widNight / 2, yNight + radius + hei, textPaint!!)
    }

    fun getRadius(): Int {
        return radius
    }

    fun setRadius(radius: Int) {
        this.radius = radius
        invalidate()
    }

    fun getMaxTemp(): Int {
        return maxTemp
    }

    fun setMaxTemp(maxTemp: Int) {
        this.maxTemp = maxTemp
    }

    fun getMinTemp(): Int {
        return minTemp
    }

    fun setMinTemp(minTemp: Int) {
        this.minTemp = minTemp
    }

    fun getxPointDay(): Int {
        return xPointDay
    }

    fun getyPointDay(): Int {
        return yPointDay
    }

    fun setxPointDay(xPointDay: Int) {
        this.xPointDay = xPointDay
    }

    fun setyPointDay(yPointDay: Int) {
        this.yPointDay = yPointDay
    }

    fun getxPointNight(): Int {
        return xPointNight
    }

    fun setxPointNight(xPointNight: Int) {
        this.xPointNight = xPointNight
    }

    fun getyPointNight(): Int {
        return yPointNight
    }

    fun setyPointNight(yPointNight: Int) {
        this.yPointNight = yPointNight
    }

    fun getmWidth(): Int {
        return mWidth
    }

    fun getTemperatureDay(): Int {
        return temperatureDay
    }

    fun setTemperatureDay(temperatureDay: Int) {
        this.temperatureDay = temperatureDay
    }

    fun getLineColor(): Int {
        return lineColor
    }

    fun setLineColor(lineColor: Int) {
        this.lineColor = lineColor
    }

    fun getPointColor(): Int {
        return dayPointColor
    }

    fun setPointColor(pointColor: Int) {
        this.dayPointColor = pointColor
    }

    fun getTextColor(): Int {
        return textColor
    }

    fun setTextColor(textColor: Int) {
        this.textColor = textColor
    }

    fun getTemperatureNight(): Int {
        return temperatureNight
    }

    fun setTemperatureNight(temperatureNight: Int) {
        this.temperatureNight = temperatureNight
    }
}
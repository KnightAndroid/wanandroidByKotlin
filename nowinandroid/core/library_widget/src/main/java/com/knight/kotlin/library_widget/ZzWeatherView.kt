package com.knight.kotlin.library_widget


import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Path
import android.util.AttributeSet
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.HorizontalScrollView
import android.widget.LinearLayout
import com.knight.kotlin.library_base.entity.WeatherEveryDay
import com.knight.kotlin.library_base.enum.AirLevel
import com.knight.kotlin.library_util.DateUtils
import com.knight.kotlin.library_widget.utils.WeatherPicUtil
import kotlin.math.max
import kotlin.math.min


/**
 * @author created by luguian
 * @organize
 * @Date 2025/2/24 16:07
 * @descript:天气折线图
 */
class ZzWeatherView @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null) :
    HorizontalScrollView(context, attrs) {
    private var mData: List<WeatherEveryDay>? = null
    private var dayPaint: Paint? = null
    private var nightPaint: Paint? = null

    protected lateinit var pathDay: Path
    protected lateinit var pathNight: Path

    private var lineType = LINE_TYPE_CURVE
    private var lineWidth = 6f

    private var dayLineColor = Color.parseColor("#1616D5")
    private var nightLineColor = Color.parseColor("#ED106A")

    private var columnNumber = 6

    private var weatherItemClickListener: OnWeatherItemClickListener? = null

    init {
        init(context, attrs)
    }

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : this(context, attrs)

    private fun init(context: Context, attrs: AttributeSet?) {
        dayPaint = Paint()
        dayPaint!!.color = dayLineColor
        dayPaint!!.isAntiAlias = true
        dayPaint!!.strokeWidth = lineWidth
        dayPaint!!.style = Paint.Style.STROKE

        nightPaint = Paint()
        nightPaint!!.color = nightLineColor
        nightPaint!!.isAntiAlias = true
        nightPaint!!.strokeWidth = lineWidth
        nightPaint!!.style = Paint.Style.STROKE

        pathDay = Path()
        pathNight = Path()
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        if (childCount > 0) {
            val root = getChildAt(0) as ViewGroup

            if (root.childCount > 0) {
                val prevDx = 0f
                val prevDy = 0f
                val curDx = 0f
                val curDy = 0f
                val prevDx1 = 0f
                val prevDy1 = 0f
                val curDx1 = 0f
                val curDy1 = 0f
                val intensity = 0.16f

                val c = root.getChildAt(0) as WeatherItemView
                val dX = c.getTempX()
                val dY = c.getTempY()
                val nX = c.getTempX()
                val nY = c.getTempY()


                val tv = c.findViewById<View>(R.id.ttv_day) as TemperatureView

                tv.setRadius(10)

                pathDay.reset()
                pathNight.reset()

                pathDay.moveTo((dX + tv.getxPointDay()).toFloat(), (dY + tv.getyPointDay()).toFloat())
                pathNight.moveTo((nX + tv.getxPointNight().toFloat()), (nY + tv.getyPointNight()).toFloat())

                if (lineType == LINE_TYPE_CURVE) {
                    val lineSize = root.childCount
                    //曲线
                    var prePreviousPointX = Float.NaN
                    var prePreviousPointY = Float.NaN
                    var previousPointX = Float.NaN
                    var previousPointY = Float.NaN
                    var currentPointX = Float.NaN
                    var currentPointY = Float.NaN
                    var nextPointX = Float.NaN
                    var nextPointY = Float.NaN

                    var prePreviousPointX1 = Float.NaN
                    var prePreviousPointY1 = Float.NaN
                    var previousPointX1 = Float.NaN
                    var previousPointY1 = Float.NaN
                    var currentPointX1 = Float.NaN
                    var currentPointY1 = Float.NaN
                    var nextPointX1 = Float.NaN
                    var nextPointY1 = Float.NaN

                    for (i in 0 until lineSize) {
                        //Day

                        if (java.lang.Float.isNaN(currentPointX)) {
                            val curWI = root.getChildAt(i) as WeatherItemView
                            val dayX = curWI.getTempX() + curWI.width * i
                            val dayY = curWI.getTempY()
                            val nightX = curWI.getTempX() + curWI.width * i
                            val nightY = curWI.getTempY()
                            val tempV = curWI.findViewById<View>(R.id.ttv_day) as TemperatureView







                            tempV.setRadius(10)


                            //day2
                            currentPointX = (dayX + tempV.getxPointDay()).toFloat()
                            currentPointY = (dayY + tempV.getyPointDay()).toFloat()
                            //night2
                            val x2 = (nightX + tempV.getxPointNight())
                            val y2 = (nightY + tempV.getyPointNight())
                        }
                        if (java.lang.Float.isNaN(previousPointX)) {
                            if (i > 0) {
                                val preWI = root.getChildAt(max((i - 1).toDouble(), 0.0).toInt()) as WeatherItemView

                                val dayX0 = preWI.getTempX() + preWI.width * (i - 1)
                                val dayY0 = preWI.getTempY()
                                val nightX0 = preWI.getTempX() + preWI.width * (i - 1)
                                val nightY0 = preWI.getTempY()
                                val tempV0 = preWI.findViewById<View>(R.id.ttv_day) as TemperatureView
                                tempV0.setRadius(10)


                                //day1
                                previousPointX = (dayX0 + tempV0.getxPointDay()).toFloat()
                                previousPointY = (dayY0 + tempV0.getyPointDay()).toFloat()


                                //night1
                                val x02 = (nightX0 + tempV0.getxPointNight())
                                val y02 = (nightY0 + tempV0.getyPointNight())
                            } else {
                                previousPointX = currentPointX
                                previousPointY = currentPointY
                            }
                        }

                        if (java.lang.Float.isNaN(prePreviousPointX)) {
                            if (i > 1) {
                                val preWI = root.getChildAt(max((i - 2).toDouble(), 0.0).toInt()) as WeatherItemView

                                val dayX0 = preWI.getTempX() + preWI.width * (i - 2)
                                val dayY0 = preWI.getTempY()
                                val nightX0 = preWI.getTempX() + preWI.width * (i - 2)
                                val nightY0 = preWI.getTempY()
                                val tempV0 = preWI.findViewById<View>(R.id.ttv_day) as TemperatureView
                                tempV0.setRadius(10)


                                //pre pre day
                                prePreviousPointX = (dayX0 + tempV0.getxPointDay()).toFloat()
                                prePreviousPointY = (dayY0 + tempV0.getyPointDay()).toFloat()
                            } else {
                                prePreviousPointX = previousPointX
                                prePreviousPointY = previousPointY
                            }
                        }


                        // nextPoint is always new one or it is equal currentPoint.
                        if (i < lineSize - 1) {
                            val nextWI = root.getChildAt(min((root.childCount - 1).toDouble(), (i + 1).toDouble()).toInt()) as WeatherItemView


                            val dayX1 = nextWI.getTempX() + nextWI.width * (i + 1)
                            val dayY1 = nextWI.getTempY()
                            val nightX1 = nextWI.getTempX() + nextWI.width * (i + 1)
                            val nightY1 = nextWI.getTempY()


                            val tempV1 = nextWI.findViewById<View>(R.id.ttv_day) as TemperatureView

                            tempV1.setRadius(10)
                            //day3
                            nextPointX = (dayX1 + tempV1.getxPointDay()).toFloat()
                            nextPointY = (dayY1 + tempV1.getyPointDay()).toFloat()
                            //night3
                            val x22 = (nightX1 + tempV1.getxPointNight())
                            val y22 = (nightY1 + tempV1.getyPointNight())
                        } else {
                            nextPointX = currentPointX
                            nextPointY = currentPointY
                        }

                        /*****************************Night */
                        if (java.lang.Float.isNaN(currentPointX1)) {
                            val curWI = root.getChildAt(i) as WeatherItemView
                            val nightX = curWI.getTempX() + curWI.width * i
                            val nightY = curWI.getTempY()
                            val tempV = curWI.findViewById<View>(R.id.ttv_day) as TemperatureView
                            tempV.setRadius(10)


                            //night2
                            currentPointX1 = (nightX + tempV.getxPointNight()).toFloat()
                            currentPointY1 = (nightY + tempV.getyPointNight()).toFloat()
                        }
                        if (java.lang.Float.isNaN(previousPointX1)) {
                            if (i > 0) {
                                val preWI = root.getChildAt(max((i - 1).toDouble(), 0.0).toInt()) as WeatherItemView

                                val nightX0 = preWI.getTempX() + preWI.width * (i - 1)
                                val nightY0 = preWI.getTempY()
                                val tempV0 = preWI.findViewById<View>(R.id.ttv_day) as TemperatureView
                                tempV0.setRadius(10)


                                //night
                                previousPointX1 = (nightX0 + tempV0.getxPointNight()).toFloat()
                                previousPointY1 = (nightY0 + tempV0.getyPointNight()).toFloat()
                            } else {
                                previousPointX1 = currentPointX1
                                previousPointY1 = currentPointY1
                            }
                        }

                        if (java.lang.Float.isNaN(prePreviousPointX1)) {
                            if (i > 1) {
                                val preWI = root.getChildAt(max((i - 2).toDouble(), 0.0).toInt()) as WeatherItemView

                                val nightX0 = preWI.getTempX() + preWI.width * (i - 2)
                                val nightY0 = preWI.getTempY()
                                val tempV0 = preWI.findViewById<View>(R.id.ttv_day) as TemperatureView
                                tempV0.setRadius(10)


                                //pre pre day
                                prePreviousPointX1 = (nightX0 + tempV0.getxPointNight()).toFloat()
                                prePreviousPointY1 = (nightY0 + tempV0.getyPointNight()).toFloat()
                            } else {
                                prePreviousPointX1 = previousPointX1
                                prePreviousPointY1 = previousPointY1
                            }
                        }


                        // nextPoint is always new one or it is equal currentPoint.
                        if (i < lineSize - 1) {
                            val nextWI = root.getChildAt(min((root.childCount - 1).toDouble(), (i + 1).toDouble()).toInt()) as WeatherItemView
                            val nightX1 = nextWI.getTempX() + nextWI.width * (i + 1)
                            val nightY1 = nextWI.getTempY()

                            val tempV1 = nextWI.findViewById<View>(R.id.ttv_day) as TemperatureView

                            tempV1.setRadius(10)
                            //night3
                            nextPointX1 = (nightX1 + tempV1.getxPointNight()).toFloat()
                            nextPointY1 = (nightY1 + tempV1.getyPointNight()).toFloat()
                        } else {
                            nextPointX1 = currentPointX1
                            nextPointY1 = currentPointY1
                        }


                        //Day and Night
                        if (i == 0) {
                            // Move to start point.
                            pathDay.moveTo(currentPointX, currentPointY)
                            pathNight.moveTo(currentPointX1, currentPointY1)
                        } else {
                            // Calculate control points.
                            val firstDiffX = (currentPointX - prePreviousPointX)
                            val firstDiffY = (currentPointY - prePreviousPointY)
                            val secondDiffX = (nextPointX - previousPointX)
                            val secondDiffY = (nextPointY - previousPointY)
                            val firstControlPointX = previousPointX + (intensity * firstDiffX)
                            val firstControlPointY = previousPointY + (intensity * firstDiffY)
                            val secondControlPointX = currentPointX - (intensity * secondDiffX)
                            val secondControlPointY = currentPointY - (intensity * secondDiffY)
                            pathDay.cubicTo(
                                firstControlPointX, firstControlPointY, secondControlPointX, secondControlPointY,
                                currentPointX, currentPointY
                            )

                            val firstDiffX1 = (currentPointX1 - prePreviousPointX1)
                            val firstDiffY1 = (currentPointY1 - prePreviousPointY1)
                            val secondDiffX1 = (nextPointX1 - previousPointX1)
                            val secondDiffY1 = (nextPointY1 - previousPointY1)
                            val firstControlPointX1 = previousPointX1 + (intensity * firstDiffX1)
                            val firstControlPointY1 = previousPointY1 + (intensity * firstDiffY1)
                            val secondControlPointX1 = currentPointX1 - (intensity * secondDiffX1)
                            val secondControlPointY1 = currentPointY1 - (intensity * secondDiffY1)
                            pathNight.cubicTo(
                                firstControlPointX1, firstControlPointY1, secondControlPointX1, secondControlPointY1,
                                currentPointX1, currentPointY1
                            )
                        }


                        // Shift values by one back to prevent recalculation of values that have
                        // been already calculated.
                        prePreviousPointX = previousPointX
                        prePreviousPointY = previousPointY
                        previousPointX = currentPointX
                        previousPointY = currentPointY
                        currentPointX = nextPointX
                        currentPointY = nextPointY

                        prePreviousPointX1 = previousPointX1
                        prePreviousPointY1 = previousPointY1
                        previousPointX1 = currentPointX1
                        previousPointY1 = currentPointY1
                        currentPointX1 = nextPointX1
                        currentPointY1 = nextPointY1
                    }

                    canvas.drawPath(pathDay, dayPaint!!)
                    canvas.drawPath(pathNight, nightPaint!!)
                } else {
                    //折线
                    for (i in 0 until root.childCount - 1) {
                        val child = root.getChildAt(i) as WeatherItemView
                        val child1 = root.getChildAt(i + 1) as WeatherItemView
                        val dayX = child.getTempX() + child.width * i
                        val dayY = child.getTempY()
                        val nightX = child.getTempX() + child.width * i
                        val nightY = child.getTempY()
                        val dayX1 = child1.getTempX() + child1.width * (i + 1)
                        val dayY1 = child1.getTempY()
                        val nightX1 = child1.getTempX() + child1.width * (i + 1)
                        val nightY1 = child1.getTempY()

                        val tempV = child.findViewById<View>(R.id.ttv_day) as TemperatureView
                        val tempV1 = child1.findViewById<View>(R.id.ttv_day) as TemperatureView
                        tempV.setRadius(10)
                        tempV1.setRadius(10)

                        canvas.drawLine(
                            (dayX + tempV.getxPointDay()).toFloat(),
                            (dayY + tempV.getyPointDay()).toFloat(),
                            (dayX1 + tempV1.getxPointDay()).toFloat(),
                            (dayY1 + tempV1.getyPointDay()).toFloat(),
                            dayPaint!!
                        )
                        canvas.drawLine(
                            (nightX + tempV.getxPointNight()).toFloat(),
                            (nightY + tempV.getyPointNight()).toFloat(),
                            (nightX1 + tempV1.getxPointNight()).toFloat(),
                            (nightY1 + tempV1.getyPointNight()).toFloat(),
                            nightPaint!!
                        )
                    }
                }
            }
        }
    }


    fun getLineWidth(): Float {
        return lineWidth
    }

    /**
     * 设置线宽，单位px
     *
     * @param lineWidth 线宽
     */
    fun setLineWidth(lineWidth: Float) {
        this.lineWidth = lineWidth
        dayPaint!!.strokeWidth = lineWidth
        nightPaint!!.strokeWidth = lineWidth
        invalidate()
    }

    /**
     * 设置白天线的颜色
     *
     * @param color 颜色值
     */
    fun setDayLineColor(color: Int) {
        this.dayLineColor = color
        dayPaint!!.color = dayLineColor
        invalidate()
    }

    /**
     * 设置晚上线的颜色
     *
     * @param color 颜色值
     */
    fun setNightLineColor(color: Int) {
        this.nightLineColor = color
        nightPaint!!.color = nightLineColor
        invalidate()
    }

    /**
     * 设置白天和晚上线的颜色
     *
     * @param dayColor   白天颜色值
     * @param nightColor 晚上颜色值
     */
    fun setDayAndNightLineColor(dayColor: Int, nightColor: Int) {
        this.dayLineColor = dayColor
        this.nightLineColor = nightColor
        dayPaint!!.color = dayLineColor
        nightPaint!!.color = nightLineColor
        invalidate()
    }

    fun getData(): List<WeatherEveryDay>? {
        return mData
    }

    /**
     * 设置点击监听
     *
     * @param weatherItemClickListener 回调
     */
    fun setOnWeatherItemClickListener(weatherItemClickListener: OnWeatherItemClickListener?) {
        this.weatherItemClickListener = weatherItemClickListener
    }

    /**
     * 填充数据
     *
     * @param data 天气数据
     */
    fun setData(data: List<WeatherEveryDay>?) {
        this.mData = data
        val screenWidth = getScreenWidth()
        val maxDay = getMaxDayTemp(data)
        val maxNight = getMaxNightTemp(data)
        val minDay = getMinDayTemp(data)
        val minNight = getMinNightTemp(data)
        val max = max(maxDay.toDouble(), maxNight.toDouble()).toInt()
        val min = min(minDay.toDouble(), minNight.toDouble()).toInt()
        removeAllViews()
        val llRoot = LinearLayout(context)
        llRoot.layoutParams = ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        llRoot.orientation = LinearLayout.HORIZONTAL
        for (i in data!!.indices) {
            val model: WeatherEveryDay = data[i]
            val itemView = WeatherItemView(context)
            itemView.setMaxTemp(max)
            itemView.setMinTemp(min)
            itemView.setDate(DateUtils.formatDateToMMdd(model.time))
            if (i == 1) {
                itemView.setTodayShadowBackground()
            }
            itemView.setWeek(DateUtils.getDayOfWeek(model.time))
            itemView.setDayTemp(model.maxDegree.toInt())
            itemView.setDayWeather(model.dayWeather)

            if (model.dayWeather != null) {
                    itemView.setDayImg(WeatherPicUtil.getDayWeatherPic(model.dayWeather))
            }

            itemView.setNightWeather(model.nightWeather)
            itemView.setNightTemp(model.minDegree.toInt())
            i
                if (model.nightWeather != null) {
                    itemView.setNightImg(WeatherPicUtil.getNightWeatherPic(model.nightWeather))
                }

            itemView.setWindOri(model.nightWindDirection)
            itemView.setWindLevel(model.nightWindPower)
            itemView.setAirLevel(getAirLevelByAqiLevel(model.aqiLevel))
            itemView.layoutParams = LinearLayout.LayoutParams(screenWidth / columnNumber, ViewGroup.LayoutParams.WRAP_CONTENT)
            itemView.isClickable = true
            val finalI = i
            itemView.setOnClickListener {
                if (weatherItemClickListener != null) {
                    weatherItemClickListener!!.onItemClick(itemView, finalI, data[finalI])
                }
            }
            llRoot.addView(itemView)
        }
        addView(llRoot)
        invalidate()
    }


    /**
     *
     * 根据等级转换空气质量level
     */
    fun getAirLevelByAqiLevel(aqiLevel:Int) : AirLevel {
        return when(aqiLevel) {
            1 -> AirLevel.EXCELLENT
            2 -> AirLevel.GOOD
            3 -> AirLevel.LIGHT
            4 -> AirLevel.MIDDLE
            5 -> AirLevel.HIGH
            6 -> AirLevel.POISONOUS
            else -> AirLevel.GOOD
        }
    }

    /**
     * 设置一屏幕显示几列(最少3列)
     *
     * @param num 列数
     * @throws Exception 异常
     */
    @Throws(Exception::class)
    fun setColumnNumber(num: Int) {
        if (num > 2) {
            this.columnNumber = num
            setData(this.mData)
        } else {
            throw Exception("ColumnNumber should lager than 2")
        }
    }

    private fun getScreenWidth(): Int {
        val wm = context
            .getSystemService(Context.WINDOW_SERVICE) as WindowManager
        return wm.defaultDisplay.width
    }

    private fun getMinDayTemp(list: List<WeatherEveryDay>?): Int {
        return list?.minByOrNull { it.maxDegree.toInt() }?.maxDegree?.toInt() ?: 0
    }

    private fun getMinNightTemp(list: List<WeatherEveryDay>?): Int {
        return list?.minByOrNull { it.minDegree.toInt() }?.minDegree?.toInt() ?: 0
    }


    private fun getMaxNightTemp(list: List<WeatherEveryDay>?): Int {
        return list?.maxByOrNull { it.minDegree.toInt() }?.minDegree?.toInt() ?: 0
    }

    private fun getMaxDayTemp(list: List<WeatherEveryDay>?): Int {
        return list?.maxByOrNull { it.maxDegree.toInt() }?.maxDegree?.toInt() ?: 0
    }

    fun getLineType(): Int {
        return lineType
    }

    fun setLineType(lineType: Int) {
        this.lineType = lineType
        invalidate()
    }

//    private class DayTempComparator : Comparator<WeatherCustomEntity> {
//        override fun compare(o1: WeatherCustomEntity, o2: WeatherCustomEntity): Int {
//            return if (o1.dayTemp === o2.dayTemp ) {
//                0
//            } else if (o1.dayTemp  > o2.dayTemp ) {
//                1
//            } else {
//                -1
//            }
//        }
//    }
//
//    private class NightTempComparator : Comparator<WeatherCustomEntity> {
//        override fun compare(o1: WeatherCustomEntity, o2: WeatherCustomEntity): Int {
//            return if (o1.nightTemp === o2.nightTemp) {
//                0
//            } else if (o1.nightTemp > o2.nightTemp) {
//                1
//            } else {
//                -1
//            }
//        }
//    }

    interface OnWeatherItemClickListener {
        fun onItemClick(itemView: WeatherItemView?, position: Int, weatherModel: WeatherEveryDay?)
    }

    companion object {
        /**
         * 曲线
         */
        const val LINE_TYPE_CURVE: Int = 1

        /**
         * 折线
         */
        const val LINE_TYPE_DISCOUNT: Int = 2
    }
}
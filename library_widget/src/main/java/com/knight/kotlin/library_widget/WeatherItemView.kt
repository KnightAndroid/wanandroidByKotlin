package com.knight.kotlin.library_widget


import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import com.knight.kotlin.library_base.enum.AirLevel


/**
 * @author created by luguian
 * @organize
 * @Date 2025/2/24 15:24
 * @descript:
 */
class WeatherItemView @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null) :
    LinearLayout(context, attrs) {
    private var rootView: View? = null
    private var tvWeek: TextView? = null
    private var tvDate: TextView? = null
    private var tvDayWeather: TextView? = null
    private var tvNightWeather: TextView? = null
    private var ttvTemp: TemperatureView? = null
    private var tvWindOri: TextView? = null
    private var tvWindLevel: TextView? = null
    private var tvAirLevel: TextView? = null
    private var ivDayWeather: ImageView? = null
    private var ivNightWeather: ImageView? = null

    init {
        init(context, attrs)
    }

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : this(context, attrs)

    private fun init(context: Context, attrs: AttributeSet?) {
        rootView = LayoutInflater.from(context).inflate(R.layout.weather_item, null)
        tvWeek = rootView!!.findViewById<View>(R.id.tv_week) as TextView
        tvDate = rootView!!.findViewById<View>(R.id.tv_date) as TextView
        tvDayWeather = rootView!!.findViewById<View>(R.id.tv_day_weather) as TextView
        tvNightWeather = rootView!!.findViewById<View>(R.id.tv_night_weather) as TextView
        ttvTemp = rootView!!.findViewById<View>(R.id.ttv_day) as TemperatureView
        tvWindOri = rootView!!.findViewById<View>(R.id.tv_wind_ori) as TextView
        tvWindLevel = rootView!!.findViewById<View>(R.id.tv_wind_level) as TextView
        ivDayWeather = rootView!!.findViewById<View>(R.id.iv_day_weather) as ImageView
        ivNightWeather = rootView!!.findViewById<View>(R.id.iv_night_weather) as ImageView
        tvAirLevel = rootView!!.findViewById<View>(R.id.tv_air_level) as TextView
        rootView!!.layoutParams = LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        addView(rootView)
    }

    fun setWeek(week: String?) {
        if (tvWeek != null) {
            tvWeek!!.text = week
        }
    }

    fun setDate(date: String?) {
        if (tvDate != null) {
            tvDate!!.text = date
        }
    }

    fun getTempX(): Int {
        if (ttvTemp != null) {
            return ttvTemp!!.x.toInt()
        }
        return 0
    }

    fun getTempY(): Int {
        if (ttvTemp != null) {
            return ttvTemp!!.y.toInt()
        }
        return 0
    }

    fun setDayWeather(dayWeather: String?) {
        if (tvDayWeather != null) {
            tvDayWeather!!.text = dayWeather
        }
    }

    fun setNightWeather(nightWeather: String?) {
        if (tvNightWeather != null) {
            tvNightWeather!!.text = nightWeather
        }
    }

    fun setWindOri(windOri: String?) {
        if (tvWindOri != null) {
            tvWindOri!!.text = windOri
        }
    }

    fun setWindLevel(windLevel: String?) {
        if (tvWindLevel != null) {
            tvWindLevel!!.text = windLevel
        }
    }

    fun setAirLevel(airLevel: AirLevel?) {
        if (tvAirLevel != null) {
            when (airLevel) {
                AirLevel.EXCELLENT -> {
                    tvAirLevel!!.setBackgroundResource(R.drawable.widget_best_level_shape)
                    tvAirLevel!!.text = "优"
                }

                AirLevel.GOOD -> {
                    tvAirLevel!!.setBackgroundResource(R.drawable.widget_good_level_shape)
                    tvAirLevel!!.text = "良好"
                }

                AirLevel.LIGHT -> {
                    tvAirLevel!!.text = "轻度"
                    tvAirLevel!!.setBackgroundResource(R.drawable.widget_small_level_shape)
                }

                AirLevel.MIDDLE -> {
                    tvAirLevel!!.setBackgroundResource(R.drawable.widget_mid_level_shape)
                    tvAirLevel!!.text = "中度"
                }

                AirLevel.HIGH -> {
                    tvAirLevel!!.setBackgroundResource(R.drawable.widget_big_level_shape)
                    tvAirLevel!!.text = "重度"
                }

                AirLevel.POISONOUS -> {
                    tvAirLevel!!.setBackgroundResource(R.drawable.widget_poison_level_shape)
                    tvAirLevel!!.text = "有毒"
                }

                else -> {
                    tvAirLevel!!.setBackgroundResource(R.drawable.widget_best_level_shape)
                    tvAirLevel!!.text = "优"
                }
            }
        }
    }

    fun setDayTemp(dayTemp: Int) {
        if (ttvTemp != null) {
            ttvTemp!!.setTemperatureDay(dayTemp)
        }
    }

    fun setNightTemp(nightTemp: Int) {
        if (ttvTemp != null) {
            ttvTemp!!.setTemperatureNight(nightTemp)
        }
    }

    fun setDayImg(resId: Int) {
        if (ivDayWeather != null) {
            ivDayWeather!!.setImageResource(resId)
        }
    }

    fun setNightImg(resId: Int) {
        if (ivNightWeather != null) {
            ivNightWeather!!.setImageResource(resId)
        }
    }

    fun setMaxTemp(max: Int) {
        if (ttvTemp != null) {
            ttvTemp!!.setMaxTemp(max)
        }
    }

    fun setMinTemp(min: Int) {
        if (ttvTemp != null) {
            ttvTemp!!.setMinTemp(min)
        }
    }
}
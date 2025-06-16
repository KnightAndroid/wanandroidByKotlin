package com.knight.kotlin.library_widget.utils

import android.content.Context
import androidx.annotation.ColorInt
import com.knight.kotlin.library_widget.R
import com.knight.kotlin.library_widget.weatherview.WeatherView


/**
 * @author created by luguian
 * @organize
 * @Date 2025/4/9 10:28
 * @descript:天气工具类
 */
object WeatherUtils {


    /**
     *
     * 根据天气返回背景图片
     */
    fun getBackgroundByWeather(weather:String) : Int {
        return when (weather) {
            "晴" -> WeatherView.WEATHER_KIND_CLEAR
            "多云" -> WeatherView.WEATHER_KIND_CLOUD
            "阴" -> WeatherView.WEATHER_KIND_CLOUDY
            "打雷" -> WeatherView.WEATHER_KIND_THUNDER
            "雷阵雨" -> WeatherView.WEATHER_KIND_THUNDERSTORM
            "雨夹雪" -> WeatherView.WEATHER_KIND_SLEET
            "雨" -> WeatherView.WEATHER_KIND_RAINY
            "阵雨" -> WeatherView.WEATHER_KIND_RAINY
            "小雨" -> WeatherView.WEATHER_KIND_RAINY
            "中雨" -> WeatherView.WEATHER_KIND_RAINY
            "大雨" -> WeatherView.WEATHER_KIND_RAINY
            "暴雨","大暴雨","特大暴雨","降雨" -> WeatherView.WEATHER_KIND_RAINY
            "雷雨" -> WeatherView.WEATHER_KIND_RAINY
            "小雪","降雪","下雪","雪" -> WeatherView.WEATHER_KIND_SNOW
            "大雪" -> WeatherView.WEATHER_KIND_SNOW
            "中雪" -> WeatherView.WEATHER_KIND_SNOW
            "暴雪" -> WeatherView.WEATHER_KIND_SNOW
            "冰雹" -> WeatherView.WEATHER_KIND_HAIL
            "雾"   -> WeatherView.WEATHER_KIND_FOG
            "薄雾" -> WeatherView.WEATHER_KIND_HAZE
            else -> WeatherView.WEATHER_KIND_WIND
        }
    }

    /**
     *
     * 返回空气质量等级
     */
    fun getAqiToName(context: Context, aqiLevel: Int): String? {
        return context.resources.getStringArray(R.array.widget_air_quality_levels).getOrNull(aqiLevel)
    }

    /**
     *
     * 返回进度条颜色
     */
    @ColorInt
    fun getColor(context: Context,aqiLevel: Int): Int {
        return context.resources.getIntArray(R.array.widget_air_quality_level_colors).getOrNull(aqiLevel) ?: android.graphics.Color.TRANSPARENT
    }


}
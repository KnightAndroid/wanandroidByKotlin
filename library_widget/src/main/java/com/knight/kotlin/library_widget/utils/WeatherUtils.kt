package com.knight.kotlin.library_widget.utils

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
            "阵雨" -> WeatherView.WEATHER_KIND_RAINY
            "小雨" -> WeatherView.WEATHER_KIND_RAINY
            "中雨" -> WeatherView.WEATHER_KIND_RAINY
            "大雨" -> WeatherView.WEATHER_KIND_RAINY
            "暴雨" -> WeatherView.WEATHER_KIND_RAINY
            "小雪" -> WeatherView.WEATHER_KIND_SNOW
            "大雪" -> WeatherView.WEATHER_KIND_SNOW
            "中雪" -> WeatherView.WEATHER_KIND_SNOW
            "暴雪" -> WeatherView.WEATHER_KIND_SNOW
            "冰雹" -> WeatherView.WEATHER_KIND_HAIL
            "雾"   -> WeatherView.WEATHER_KIND_FOG
            "薄雾" -> WeatherView.WEATHER_KIND_HAZE
            else -> WeatherView.WEATHER_KIND_WIND
        }
    }


}
package com.knight.kotlin.library_widget.utils


/**
 * @author created by luguian
 * @organize
 * @Date 2025/2/24 14:34
 * @descript:天气小图标匹配
 */
object WeatherPicUtil {

    /**
     * 根据白天天气名称解析白天天气图片
     *
     * @param weatherName 天气名称
     * @return 天气图片资源
     */
    fun getDayWeatherPic(weatherName: String?): Int {
        when (weatherName) {
            "晴" -> return com.knight.kotlin.library_widget.R.drawable.widget_weather_icon_w0
            "多云" -> return com.knight.kotlin.library_widget.R.drawable.widget_weather_icon_w1
            "阴" -> return com.knight.kotlin.library_widget.R.drawable.widget_weather_icon_w2
            "雷阵雨" -> return com.knight.kotlin.library_widget.R.drawable.widget_weather_icon_w4
            "雨夹雪" -> return com.knight.kotlin.library_widget.R.drawable.widget_weather_icon_w6
            "小雨" -> return com.knight.kotlin.library_widget.R.drawable.widget_weather_icon_w7
            "中雨" -> return com.knight.kotlin.library_widget.R.drawable.widget_weather_icon_w8
            "大雨" -> return com.knight.kotlin.library_widget.R.drawable.widget_weather_icon_w9
            "暴雨" -> return com.knight.kotlin.library_widget.R.drawable.widget_weather_icon_w10
            "大雪" -> return com.knight.kotlin.library_widget.R.drawable.widget_weather_icon_w17
            "中雪" -> return com.knight.kotlin.library_widget.R.drawable.widget_weather_icon_w16
            "冰雹" -> return com.knight.kotlin.library_widget.R.drawable.widget_weather_icon_w15
        }
        return com.knight.kotlin.library_widget.R.drawable.widget_weather_icon_w0
    }


    /**
     * 根据夜间天气名称解析夜间天气图片
     *
     * @param weatherName 天气名称
     * @return 天气图片资源
     */
    fun getNightWeatherPic(weatherName: String?): Int {
        when (weatherName) {
            "晴" -> return com.knight.kotlin.library_widget.R.drawable.widget_weather_icon_w30
            "多云" -> return com.knight.kotlin.library_widget.R.drawable.widget_weather_icon_w31
            "阴" -> return com.knight.kotlin.library_widget.R.drawable.widget_weather_icon_w2
            "雷阵雨" -> return com.knight.kotlin.library_widget.R.drawable.widget_weather_icon_w4
            "雨夹雪" -> return com.knight.kotlin.library_widget.R.drawable.widget_weather_icon_w6
            "小雨" -> return com.knight.kotlin.library_widget.R.drawable.widget_weather_icon_w7
            "中雨" -> return com.knight.kotlin.library_widget.R.drawable.widget_weather_icon_w8
            "大雨" -> return com.knight.kotlin.library_widget.R.drawable.widget_weather_icon_w9
            "暴雨" -> return com.knight.kotlin.library_widget.R.drawable.widget_weather_icon_w10
            "大雪" -> return com.knight.kotlin.library_widget.R.drawable.widget_weather_icon_w17
            "中雪" -> return com.knight.kotlin.library_widget.R.drawable.widget_weather_icon_w16
            "冰雹" -> return com.knight.kotlin.library_widget.R.drawable.widget_weather_icon_w15
        }
        return com.knight.kotlin.library_widget.R.drawable.widget_weather_icon_w30
    }

}
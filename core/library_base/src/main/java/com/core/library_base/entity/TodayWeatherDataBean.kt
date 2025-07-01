package com.core.library_base.entity

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

/**
 * @Description 当日天气实体
 * @Author knight
 * @Time 2025/2/20 20:19
 * {"data":{"observe":{"degree":"16","humidity":"80","precipitation":"0","pressure":"1013","update_time":"202502202005","weather":"晴","weather_bg_pag":"","weather_code":"00","weather_color":null,"weather_first":"","weather_pag":"","weather_short":"晴","weather_url":"","wind_direction":"2","wind_direction_name":"东风","wind_power":"4-5"}},"message":"OK","status":200}
 */

@Parcelize
data class TodayWeatherDataBean (
    var degree:String,//温度
    val humidity:String,//湿度
    val precipitation:String,//降水
    val pressure:String,//气压
    val update_time:String,
    val weather:String,//天气
    val weather_bg_pag:String,
    val weather_code:String,
    val weather_color:List<String?>,
    val weather_first:String,
    val weather_pag:String,
    val weather_short:String,
    val weather_url:String,
    val wind_direction:String,//风向
    val wind_direction_name:String,
    val wind_power:String, //风向登记
): Parcelable
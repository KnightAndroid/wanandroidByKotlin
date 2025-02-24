package com.knight.kotlin.library_base.entity

import android.os.Parcelable
import com.knight.kotlin.library_base.enum.AirLevel
import kotlinx.parcelize.Parcelize


/**
 * @author created by luguian
 * @organize
 * @Date 2025/2/24 16:03
 * @descript:自己组装模型给view渲染
 */
@Parcelize
data class WeatherCustomEntity (
    /**
     * 白天温度
     */
    var dayTemp: Int = 0,
    /**
     * 晚上温度
     */
    var nightTemp: Int = 0,
    /**
     * 白天天气
     */
    var dayWeather: String? = null,
    /**
     * 晚上天气
     */
    var nightWeather: String? = null,
    /**
     * 日期
     */
    var date: String? = null,
    /**
     * 星期
     */
    var week: String? = null,
    /**
     * 是否今天
     */
    var isToday: Boolean = false,
    /**
     * 白天天气图片
     */
    var dayPic: Int = 0,
    /**
     * 晚上天气图片
     */
    var nightPic: Int = 0,
    /**
     * 风向
     */
    var windOrientation: String? = null,
    /**
     * 风级
     */
    var windLevel: String? = null,
    /**
     * 空气级别
     */
    var airLevel: AirLevel? = null
) : Parcelable
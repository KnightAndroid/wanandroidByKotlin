package com.knight.kotlin.module_home.entity

import android.os.Parcelable
import kotlinx.parcelize.Parcelize


/**
 * @author created by luguian
 * @organize
 * @Date 2025/3/27 14:28
 * @descript:每日一图实体模型
 */
@Parcelize
data class WeatherNewBean(
    val images: List<WeatherNewWeatherNewImage>,
    val tooltips: WeatherNewTooltips
): Parcelable
@Parcelize
data class WeatherNewWeatherNewImage(
    val bot: Int,
    val copyright: String,
    val copyrightlink: String,
    val drk: Int,
    val enddate: String,
    val fullstartdate: String,
    val hsh: String,
    val quiz: String,
    val startdate: String,
    val title: String,
    val top: Int,
    val url: String,
    val urlbase: String,
    val wp: Boolean
): Parcelable
@Parcelize
data class WeatherNewTooltips(
    val loading: String,
    val next: String,
    val previous: String,
    val walle: String,
    val walls: String
): Parcelable
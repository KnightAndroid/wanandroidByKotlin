package com.knight.kotlin.module_home.entity

import android.os.Parcelable
import kotlinx.parcelize.Parcelize


/**
 * @author created by luguian
 * @organize
 * @Date 2025/3/6 10:38
 * @descript:
 */
@Parcelize
data class RainFallBean(
    val current_weather: CurrentWeather,
    val current_weather_units: CurrentWeatherUnits,
    val daily: Daily,
    val daily_units: DailyUnits,
    val elevation: Double,
    val generationtime_ms: Double,
    val latitude: Double,
    val longitude: Double,
    val timezone: String,
    val timezone_abbreviation: String,
    val utc_offset_seconds: Int
): Parcelable



@Parcelize
data class CurrentWeather(
    val interval: Int,
    val is_day: Int,
    val temperature: Double,
    val time: String,
    val weathercode: Int,
    val winddirection: Int,
    val windspeed: Double
): Parcelable


@Parcelize
data class CurrentWeatherUnits(
    val interval: String,
    val is_day: String,
    val temperature: String,
    val time: String,
    val weathercode: String,
    val winddirection: String,
    val windspeed: String
): Parcelable


@Parcelize
data class Daily(
    val precipitation_sum: List<Float>,
    val time: List<String>
): Parcelable


@Parcelize
data class DailyUnits(
    val precipitation_sum: String,
    val time: String
): Parcelable
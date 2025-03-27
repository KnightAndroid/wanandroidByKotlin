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
open class RainBaseFallBean(
    open val current_weather: CurrentWeather,
    open val current_weather_units: CurrentWeatherUnits,
    open val elevation: Double,
    open val generationtime_ms: Double,
    open val latitude: Double,
    open val longitude: Double,
    open val timezone: String,
    open val timezone_abbreviation: String,
    open val utc_offset_seconds: Int
) : Parcelable

@Parcelize
data class RainDayFallBean(
    val daily: Daily,
    val daily_units: DailyUnits,
    override val current_weather: CurrentWeather,
    override val current_weather_units: CurrentWeatherUnits,
    override val elevation: Double,
    override val generationtime_ms: Double,
    override val latitude: Double,
    override val longitude: Double,
    override val timezone: String,
    override val timezone_abbreviation: String,
    override val utc_offset_seconds: Int
) : RainBaseFallBean(
    current_weather,
    current_weather_units,
    elevation,
    generationtime_ms,
    latitude,
    longitude,
    timezone,
    timezone_abbreviation,
    utc_offset_seconds
)


@Parcelize
data class RainHourFallBean(
    val hourly: Hour,
    val hourly_units: HourUnits,
    override val current_weather: CurrentWeather,
    override val current_weather_units: CurrentWeatherUnits,
    override val elevation: Double,
    override val generationtime_ms: Double,
    override val latitude: Double,
    override val longitude: Double,
    override val timezone: String,
    override val timezone_abbreviation: String,
    override val utc_offset_seconds: Int
) : RainBaseFallBean(
    current_weather,
    current_weather_units,
    elevation,
    generationtime_ms,
    latitude,
    longitude,
    timezone,
    timezone_abbreviation,
    utc_offset_seconds
)



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



@Parcelize
data class Hour(
    val precipitation: List<Float>,
    val time: List<String>
): Parcelable


@Parcelize
data class HourUnits(
    val precipitation: String,
    val time: String
): Parcelable
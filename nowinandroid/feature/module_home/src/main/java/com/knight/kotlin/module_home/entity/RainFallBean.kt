package com.knight.kotlin.module_home.entity

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize


/**
 * @author created by luguian
 * @organize
 * @Date 2025/3/6 10:38
 * @descript:
 */



@Parcelize
class RainDayFallBean(
    @SerializedName("daily") val daily: Daily,
    @SerializedName("daily_units") val dailyUnits: DailyUnits,
    @SerializedName("current_weather")  val currentWeather: CurrentWeather,
    @SerializedName("current_weather_units")val currentWeatherUnits: CurrentWeatherUnits,
    @SerializedName("elevation")val elevation: Double,
    @SerializedName("generationtime_ms")  val generationTimeMs: Double,
    @SerializedName("latitude") val latitude: Double,
    @SerializedName("longitude")  val longitude: Double,
    @SerializedName("timezone") val timezone: String,
    @SerializedName("timezone_abbreviation") val timezoneAbbreviation: String,
    @SerializedName("utc_offset_seconds") val utcOffsetSeconds: Int
) :Parcelable




@Parcelize
data class RainHourFallBean(
    @SerializedName("hourly") val hourly: Hour,
    @SerializedName("hourly_units") val hourlyUnits: HourUnits,
    @SerializedName("current_weather")  val currentWeather: CurrentWeather,
    @SerializedName("current_weather_units")val currentWeatherUnits: CurrentWeatherUnits,
    @SerializedName("elevation")val elevation: Double,
    @SerializedName("generationtime_ms")  val generationTimeMs: Double,
    @SerializedName("latitude") val latitude: Double,
    @SerializedName("longitude")  val longitude: Double,
    @SerializedName("timezone") val timezone: String,
    @SerializedName("timezone_abbreviation") val timezoneAbbreviation: String,
    @SerializedName("utc_offset_seconds") val utcOffsetSeconds: Int
) :Parcelable



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
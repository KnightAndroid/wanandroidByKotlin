package com.knight.kotlin.library_base.entity

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize


/**
 * @author created by luguian
 * @organize
 * @Date 2025/2/24 10:22
 * @descript:未来一天或者一周具体详细天气预报
 */
@Parcelize
data class WeatherDetailBean (
    val forecast_1h: List<WeatherEveryHour>,
    val forecast_24h: List<WeatherEveryDay>,
    val index: Index,
    val limit: Limit,
    val tips:TipObserve,
    val air:WeatherAir,
    val rise: List<Rise>,
    val limit_forecast:List<Limit>,
    val observe:TodayWeatherDataBean
): Parcelable


@Parcelize
data class WeatherAir(
    val aqi :Int,
    val aqi_level:Int,
    val aqi_name:String,
    val aqi_url:String,//空气图标
    val co:String,
    val no2:String,
    val o3:String,
    val pm10:String,
    @SerializedName("pm2.5") val pmTwoPointFive :String,
    val pm25:String,
    val so2:String,
    val update_time:String,
    val rank:Int,
    val total:Int
): Parcelable


@Parcelize
data class WeatherEveryHour (
    val degree: String,
    @SerializedName("update_time") val updateTime: String,
    val weather: String,
    @SerializedName("weather_code") val weatherCode: String,
    @SerializedName("weather_short") val weatherShort: String,
    @SerializedName("weather_url") val weatherUrl: String,
    @SerializedName("wind_direction") val windDirection: String,
    @SerializedName("wind_power") val windPower: String
): Parcelable

@Parcelize
data class WeatherEveryDay(
    @SerializedName("aqi_level") val aqiLevel: Int,
    @SerializedName("aqi_name") val aqiName: String,
    @SerializedName("aqi_url") val aqiUrl: String,
    @SerializedName("day_weather") val dayWeather: String,//白天天气
    @SerializedName("day_weather_code") val dayWeatherCode: String,
    @SerializedName("day_weather_short") val dayWeatherShort: String,
    @SerializedName("day_weather_url") val dayWeatherUrl: String,
    @SerializedName("day_wind_direction") val dayWindDirection: String,//凤向
    @SerializedName("day_wind_direction_code") val dayWindDirectionCode: String,
    @SerializedName("day_wind_power") val dayWindPower: String,//风级
    @SerializedName("day_wind_power_code") val dayWindPowerCode: String,
    @SerializedName("max_degree") val maxDegree: String,//白天最高温度
    @SerializedName("min_degree") val minDegree: String,//晚上最低温度
    @SerializedName("night_weather") val nightWeather: String,//晚上天气
    @SerializedName("night_weather_code") val nightWeatherCode: String,
    @SerializedName("night_weather_short") val nightWeatherShort: String,
    @SerializedName("night_weather_url") val nightWeatherUrl: String,
    @SerializedName("night_wind_direction") val nightWindDirection: String,
    @SerializedName("night_wind_direction_code") val nightWindDirectionCode: String,
    @SerializedName("night_wind_power") val nightWindPower: String,
    @SerializedName("night_wind_power_code") val nightWindPowerCode: String,
    val time: String, //日期
): Parcelable


@Parcelize
data class Index(
    val airconditioner: Airconditioner,
    val allergy: Allergy,
    val carwash: Carwash,
    val chill: Chill,
    val clothes: Clothes,
    val cold: Cold,
    val comfort: Comfort,
    val diffusion: Diffusion,
    val dry: Dry,
    val drying: Drying,
    val fish: Fish,
    val heatstroke: Heatstroke,
    val makeup: Makeup,
    val mood: Mood,
    val morning: Morning,
    val sports: Sports,
    val sunglasses: Sunglasses,
    val sunscreen: Sunscreen,
    val time: String,
    val tourism: Tourism,
    val traffic: Traffic,
    val ultraviolet: Ultraviolet,
    val umbrella: Umbrella
): Parcelable
@Parcelize
data class Airconditioner(val detail: String, val info: String, val name: String,val url:String): Parcelable
@Parcelize
data class Allergy(val detail: String, val info: String, val name: String,val url:String): Parcelable
@Parcelize
data class Carwash(val detail: String, val info: String, val name: String,val url:String): Parcelable
@Parcelize
data class Chill(val detail: String, val info: String, val name: String,val url:String): Parcelable
@Parcelize
data class Clothes(val detail: String, val info: String, val name: String,val url:String): Parcelable
@Parcelize
data class Cold(val detail: String, val info: String, val name: String,val url:String): Parcelable
@Parcelize
data class Comfort(val detail: String, val info: String, val name: String,val url:String): Parcelable
@Parcelize
data class Diffusion(val detail: String, val info: String, val name: String,val url:String): Parcelable
@Parcelize
data class Dry(val detail: String, val info: String, val name: String,val url:String): Parcelable
@Parcelize
data class Drying(val detail: String, val info: String, val name: String,val url:String): Parcelable
@Parcelize
data class Fish(val detail: String, val info: String, val name: String,val url:String): Parcelable
@Parcelize
data class Heatstroke(val detail: String, val info: String, val name: String,val url:String): Parcelable
@Parcelize
data class Makeup(val detail: String, val info: String, val name: String,val url:String): Parcelable
@Parcelize
data class Mood(val detail: String, val info: String, val name: String,val url:String): Parcelable
@Parcelize
data class Morning(val detail: String, val info: String, val name: String,val url:String): Parcelable
@Parcelize
data class Sports(val detail: String, val info: String, val name: String,val url:String): Parcelable
@Parcelize
data class Sunglasses(val detail: String, val info: String, val name: String,val url:String): Parcelable
@Parcelize
data class Sunscreen(val detail: String, val info: String, val name: String,val url:String): Parcelable
@Parcelize
data class Tourism(val detail: String, val info: String, val name: String,val url:String): Parcelable
@Parcelize
data class Traffic(val detail: String, val info: String, val name: String,val url:String): Parcelable
@Parcelize
data class Ultraviolet(val detail: String, val info: String, val name: String,val url:String): Parcelable
@Parcelize
data class Umbrella(val detail: String, val info: String, val name: String,val url:String): Parcelable
@Parcelize
data class Limit(
    @SerializedName("tail_number") val tailNumber: String //限行尾号
): Parcelable

@Parcelize
data class Rise(
    val sunrise: String,
    val sunset: String,
    val time: String
): Parcelable


@Parcelize
data class TipObserve(
    @SerializedName("0") val firstTips:String,
    @SerializedName("1") val secondTips:String,
    @SerializedName("2") val thirdTips:String
): Parcelable


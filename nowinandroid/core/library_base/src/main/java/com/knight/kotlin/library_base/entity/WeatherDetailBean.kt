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
    val alarm: List<AlarmDetail?>,
    val limit: Limit,
    val tips: TipObserve,
    val air: WeatherAir,
    val rise: List<Rise>,
    val limit_forecast:List<Limit>,
    val observe: TodayWeatherDataBean,
    val nearby_alarm:List<AlarmDetail?>
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
    val airconditioner: WeatherIndexItem,
    val allergy: WeatherIndexItem,
    val carwash: WeatherIndexItem,
    val chill: WeatherIndexItem,
    val clothes: WeatherIndexItem,
    val cold: WeatherIndexItem,
    val comfort: WeatherIndexItem,
    val diffusion: WeatherIndexItem,
    val dry: WeatherIndexItem,
    val drying: WeatherIndexItem,
    val fish: WeatherIndexItem,
    val heatstroke: WeatherIndexItem,
    val makeup: WeatherIndexItem,
    val mood: WeatherIndexItem,
    val morning: WeatherIndexItem,
    val sports: WeatherIndexItem,
    val sunglasses: WeatherIndexItem,
    val sunscreen: WeatherIndexItem,
    val tourism: WeatherIndexItem,
    val traffic: WeatherIndexItem,
    val ultraviolet: WeatherIndexItem,
    val umbrella: WeatherIndexItem
): Parcelable
@Parcelize
data class WeatherIndexItem (
    val detail: String,
    val info: String,
    val name: String,
    val url: String? // url 可能是 null，因此使用 String?
): Parcelable


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



// 预警详情类
@Parcelize
data class AlarmDetail(
    @SerializedName("detail") val detail: String,          // 预警详情
    @SerializedName("info") val info: String,              // 预警信息编码
    @SerializedName("level_code") val levelCode: String,  // 预警级别编码
    @SerializedName("level_name") val levelName: String,  // 预警级别名称
    @SerializedName("province") val province: String,     // 省份
    @SerializedName("type_code") val typeCode: String,    // 预警类型编码
    @SerializedName("type_name") val typeName: String,    // 预警类型名称
    @SerializedName("update_time") val updateTime: String, // 更新时间
    @SerializedName("url") val url: String                // 预警详情链接
): Parcelable



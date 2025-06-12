package com.knight.kotlin.library_base.enum


/**
 * @author created by luguian
 * @organize
 * @Date 2025/2/26 9:32
 * @descript:天气code码
 */
enum class WeatherCode(val id: String) {

    CLEAR("clear"),
    PARTLY_CLOUDY("partly_cloudy"),
    CLOUDY("cloudy"),
    RAIN("rain"),
    SNOW("snow"),
    WIND("wind"),
    FOG("fog"),
    HAZE("haze"),
    SLEET("sleet"),
    HAIL("hail"),
    THUNDER("thunder"),
    THUNDERSTORM("thunderstorm"),
    ;

    companion object {
        fun getInstance(
            value: String?,
        ): WeatherCode? = WeatherCode.entries.firstOrNull {
            it.id.equals(value, ignoreCase = true)
        }
    }
}

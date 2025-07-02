package com.knight.kotlin.library_widget.weatherview

import CloudImplementor
import HailImplementor
import MeteorShowerImplementor
import RainImplementor
import SnowImplementor
import SunImplementor
import WindImplementor
import android.content.Context
import android.graphics.Color
import android.view.Window
import androidx.core.graphics.ColorUtils
import com.core.library_base.util.dp2px
import com.core.library_base.util.setSystemBarStyle
import com.knight.kotlin.library_widget.R


/**
 * @author created by luguian
 * @organize
 * @Date 2025/2/26 11:23
 * @descript:
 */
class MaterialWeatherThemeDelegate : WeatherThemeDelegate {

    companion object {

        private fun getBrighterColor(color: Int): Int {
            val hsv = FloatArray(3)
            Color.colorToHSV(color, hsv)
            hsv[1] = hsv[1] - 0.25f
            hsv[2] = hsv[2] + 0.25f
            return Color.HSVToColor(hsv)
        }

        private fun innerGetBackgroundColor(
            context: Context,
            @WeatherView.WeatherKindRule weatherKind: Int,
            daytime: Boolean,
        ): Int = when (weatherKind) {
            WeatherView.WEATHER_KIND_CLEAR -> if (daytime) {
                SunImplementor.themeColor
            } else {
                MeteorShowerImplementor.themeColor
            }

            WeatherView.WEATHER_KIND_CLOUDY ->
                CloudImplementor.getThemeColor(context, CloudImplementor.TYPE_CLOUDY, daytime)

            WeatherView.WEATHER_KIND_CLOUD ->
                CloudImplementor.getThemeColor(context, CloudImplementor.TYPE_CLOUD, daytime)

            WeatherView.WEATHER_KIND_FOG ->
                CloudImplementor.getThemeColor(context, CloudImplementor.TYPE_FOG, daytime)

            WeatherView.WEATHER_KIND_HAIL ->
                HailImplementor.getThemeColor(daytime)

            WeatherView.WEATHER_KIND_HAZE ->
                CloudImplementor.getThemeColor(context, CloudImplementor.TYPE_HAZE, daytime)

            WeatherView.WEATHER_KIND_RAINY ->
                RainImplementor.getThemeColor(context, RainImplementor.TYPE_RAIN, daytime)

            WeatherView.WEATHER_KIND_SLEET ->
                RainImplementor.getThemeColor(context, RainImplementor.TYPE_SLEET, daytime)

            WeatherView.WEATHER_KIND_SNOW ->
                SnowImplementor.getThemeColor(daytime)

            WeatherView.WEATHER_KIND_THUNDERSTORM ->
                RainImplementor.getThemeColor(context, RainImplementor.TYPE_THUNDERSTORM, daytime)

            WeatherView.WEATHER_KIND_THUNDER ->
                CloudImplementor.getThemeColor(context, CloudImplementor.TYPE_THUNDER, daytime)

            WeatherView.WEATHER_KIND_WIND ->
                WindImplementor.getThemeColor(daytime)

            else -> Color.TRANSPARENT
        }
    }

    override fun getWeatherView(context: Context): WeatherView = MaterialWeatherView(context)

    override fun getThemeColors(
        context: Context,
        weatherKind: Int,
        daylight: Boolean,
    ): IntArray {
        var color = innerGetBackgroundColor(context, weatherKind, daylight)
        if (!daylight) {
            color = getBrighterColor(color)
        }
        return intArrayOf(
            color,
            color,
            ColorUtils.setAlphaComponent(color, (0.5 * 255).toInt())
        )
    }

    override fun getBackgroundColor(
        context: Context,
        weatherKind: Int,
        daylight: Boolean,
    ): Int {
        return innerGetBackgroundColor(context, weatherKind, daylight)
    }

    override fun getHeaderTopMargin(context: Context): Int =
        (context.resources.displayMetrics.heightPixels * 0.25).toInt()

    override fun getHeaderTextColor(context: Context): Int {
        return Color.WHITE
    }

    override fun setSystemBarStyle(
        context: Context,
        window: Window,
        statusShader: Boolean,
        lightStatus: Boolean,
        navigationShader: Boolean,
        lightNavigation: Boolean,
    ) {
        window.setSystemBarStyle(
            statusShaderP = statusShader,
            lightStatusP = lightStatus,
            navigationShaderP = navigationShader,
            lightNavigationP = lightNavigation
        )
    }

    override fun getHomeCardRadius(context: Context): Float = context
        .resources
        .getDimension(R.dimen.widget_material3_card_list_item_corner_radius)

    override fun getHomeCardElevation(context: Context): Float =
        2.dp2px().toFloat()

    override fun getHomeCardMargins(context: Context): Int = context
        .resources
        .getDimensionPixelSize(R.dimen.widget_little_margin)
}
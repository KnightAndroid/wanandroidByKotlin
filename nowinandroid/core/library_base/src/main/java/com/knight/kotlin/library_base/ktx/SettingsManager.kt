package com.knight.kotlin.library_base.ktx

import android.content.Context
import com.knight.kotlin.library_common.util.CacheUtils
import com.knight.kotlin.library_common.enum.BackgroundAnimationMode
import com.knight.kotlin.library_common.enum.DarkMode


/**
 * @author created by luguian
 * @organize
 * @Date 2025/2/26 9:47
 * @descript:
 */
class SettingsManager private constructor(
    context: Context,
) {

    companion object {

        @Volatile
        private var instance: SettingsManager? = null

        fun getInstance(context: Context): SettingsManager {
            if (instance == null) {
                synchronized(SettingsManager::class) {
                    if (instance == null) {
                        instance = SettingsManager(context)
                    }
                }
            }
            return instance!!
        }

        const val DEFAULT_CARD_DISPLAY = "precipitation_nowcast" +
                "&daily_overview" +
                "&hourly_overview" +
                "&air_quality" +
                "&pollen" +
                "&sunrise_sunset" +
                "&live"
        const val DEFAULT_DAILY_TREND_DISPLAY = "temperature" +
                "&air_quality" +
                "&wind" +
                "&uv_index" +
                "&precipitation" +
                "&sunshine" +
                "&feels_like"
        const val DEFAULT_HOURLY_TREND_DISPLAY = "temperature" +
                "&air_quality" +
                "&wind" +
                "&uv_index" +
                "&precipitation" +
                "&feels_like" +
                "&humidity" +
                "&pressure" +
                "&cloud_cover" +
                "&visibility"
        private const val DEFAULT_DETAILS_DISPLAY = "feels_like" +
                "&wind" +
                "&uv_index" +
                "&humidity"

        const val DEFAULT_TODAY_FORECAST_TIME = "07:00"
        const val DEFAULT_TOMORROW_FORECAST_TIME = "21:00"
    }


    var isGravitySensorEnabled: Boolean
        set(value) {
            com.knight.kotlin.library_common.util.CacheUtils.setGravitySensorSwitch(value)
        }
        get() = com.knight.kotlin.library_common.util.CacheUtils.getGravitySensorSwitch()


    var darkMode: com.knight.kotlin.library_common.enum.DarkMode
        set(value) {
            com.knight.kotlin.library_common.util.CacheUtils.setDarkMode(value.id)
        }
        get() = com.knight.kotlin.library_common.enum.DarkMode.getInstance(
            com.knight.kotlin.library_common.util.CacheUtils.getDarkMode()
        )

    var backgroundAnimationMode: com.knight.kotlin.library_common.enum.BackgroundAnimationMode
        set(value) {
            com.knight.kotlin.library_common.util.CacheUtils.setBackgroundAnimationMode(value.id)
        }
        get() = com.knight.kotlin.library_common.enum.BackgroundAnimationMode.getInstance(
            com.knight.kotlin.library_common.util.CacheUtils.getBackgroundAnimationMode()

        )

    var iconProvider: String
        set(value) {
            com.knight.kotlin.library_common.util.CacheUtils.setIconProvider(value)
        }
        get() = com.knight.kotlin.library_common.util.CacheUtils.getIconProvider()
}
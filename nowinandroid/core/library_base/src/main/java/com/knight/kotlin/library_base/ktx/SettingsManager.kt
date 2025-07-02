package com.knight.kotlin.library_base.ktx

import android.content.Context
import com.knight.kotlin.library_base.utils.CacheUtils
import com.knight.kotlin.library_base.enum.BackgroundAnimationMode
import com.knight.kotlin.library_base.enum.DarkMode


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
            CacheUtils.setGravitySensorSwitch(value)
        }
        get() = CacheUtils.getGravitySensorSwitch()


    var darkMode: DarkMode
        set(value) {
            CacheUtils.setDarkMode(value.id)
        }
        get() = DarkMode.getInstance(
            CacheUtils.getDarkMode()
        )

    var backgroundAnimationMode: BackgroundAnimationMode
        set(value) {
            CacheUtils.setBackgroundAnimationMode(value.id)
        }
        get() = BackgroundAnimationMode.getInstance(
            CacheUtils.getBackgroundAnimationMode()

        )

    var iconProvider: String
        set(value) {
            CacheUtils.setIconProvider(value)
        }
        get() = CacheUtils.getIconProvider()
}
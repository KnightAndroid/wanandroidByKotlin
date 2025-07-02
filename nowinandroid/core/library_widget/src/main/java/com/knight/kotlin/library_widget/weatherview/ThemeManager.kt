package com.knight.kotlin.library_widget.weatherview

import android.annotation.SuppressLint
import android.content.Context
import android.content.res.Configuration
import android.graphics.Color
import android.util.TypedValue
import androidx.annotation.AttrRes
import androidx.appcompat.app.AppCompatDelegate
import com.knight.kotlin.library_base.ktx.SettingsManager


/**
 * @author created by luguian
 * @organize
 * @Date 2025/2/26 11:03
 * @descript:
 */
class ThemeManager private constructor(
    val weatherThemeDelegate: WeatherThemeDelegate,
    var darkMode: com.knight.kotlin.library_base.enum.DarkMode,
) {

    companion object {

        @Volatile
        private var instance: ThemeManager? = null

        fun getInstance(context: Context): ThemeManager {
            if (instance == null) {
                synchronized(ThemeManager::class) {
                    if (instance == null) {
                        instance = ThemeManager(
                            weatherThemeDelegate = MaterialWeatherThemeDelegate(),
                            darkMode = SettingsManager.getInstance(context).darkMode
                        )
                    }
                }
            }
            return instance!!
        }

        private fun generateGlobalUIMode(
            darkMode: com.knight.kotlin.library_base.enum.DarkMode,
        ): Int = when (darkMode) {
            com.knight.kotlin.library_base.enum.DarkMode.LIGHT -> AppCompatDelegate.MODE_NIGHT_NO
            com.knight.kotlin.library_base.enum.DarkMode.DARK -> AppCompatDelegate.MODE_NIGHT_YES
            else -> AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM
        }
    }

  //  val uiMode: EqualtableLiveData<Int> = EqualtableLiveData(
   //     generateGlobalUIMode(darkMode = darkMode)
  //  )
    private val typedValue = TypedValue()

//    fun update(darkMode: DarkMode) {
//        this.darkMode = darkMode
//
//        uiMode.setValue(
//            generateGlobalUIMode(
//                darkMode = this.darkMode
//            )
//        )
//    }

    fun getThemeColor(context: Context, @AttrRes id: Int): Int {
        context.theme.resolveAttribute(id, typedValue, true)
        return typedValue.data
    }

    @SuppressLint("ResourceType")
    fun getThemeColors(context: Context, @AttrRes ids: IntArray): IntArray {
        val a = context.theme.obtainStyledAttributes(ids)
        val colors = ids.mapIndexed { index, _ ->
            a.getColor(index, Color.TRANSPARENT)
        }
        a.recycle()

        return colors.toIntArray()
    }

    fun generateThemeContext(
        context: Context,
        lightTheme: Boolean,
    ): Context = context.createConfigurationContext(
        Configuration(context.resources.configuration).apply {
            uiMode = uiMode and Configuration.UI_MODE_NIGHT_MASK.inv()
            uiMode = uiMode or if (lightTheme) {
                Configuration.UI_MODE_NIGHT_NO
            } else {
                Configuration.UI_MODE_NIGHT_YES
            }
        }
    ).apply {
        setTheme(com.core.library_base.R.style.base_AppTheme)
    }
}

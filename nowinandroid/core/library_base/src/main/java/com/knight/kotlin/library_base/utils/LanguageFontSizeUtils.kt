package com.knight.kotlin.library_base.utils

import android.content.Context
import android.content.res.Resources
import android.os.Build
import android.os.LocaleList
import androidx.annotation.RequiresApi
import java.util.Locale

/**
 * Author:Knight
 * Time:2022/3/9 19:05
 * Description:LanguageFontSizeUtils
 */
object LanguageFontSizeUtils {
    fun attachBaseContext(context: Context, fontSize: Float): Context? {
        // 8.0需要使用createConfigurationContext处理
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            updateResources(context, fontSize)
        } else {
            context
        }
    }

    /**
     *
     * 低于8.0以下改变app内语言
     * @param context
     */
    fun setAppLanguage(context: Context) {
        val resources = context.resources
        val configuration = resources.configuration
        val locale = getSetLanguageLocale()
        configuration.setLocale(locale)
        context.createConfigurationContext(configuration)

    }


    @RequiresApi(api = Build.VERSION_CODES.N)
    fun updateResources(context: Context, fontSize: Float): Context? {
        val resources = context.resources
        val locale = getSetLanguageLocale()
        val configuration = resources.configuration
        configuration.fontScale = fontSize
        configuration.setLocale(locale)
        configuration.setLocales(LocaleList(locale))
        return context.createConfigurationContext(configuration)
    }

    /**
     * 获取选择的语言设置
     *
     *
     * @return
     */
    fun getSetLanguageLocale(): Locale {
        return when (CacheUtils.getLanguageMode()) {
            "Auto" -> getSystemLocale()
            "简体中文" -> Locale.SIMPLIFIED_CHINESE
            "English" -> Locale.ENGLISH
            else -> Locale.SIMPLIFIED_CHINESE
        }
    }


    /**
     * 获取系统local
     *
     * @return
     */
    private fun getSystemLocale(): Locale {
        //获取系统默认语言,兼容版本
        var locale: Locale = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            LocaleList.getDefault()[0]
        } else {
            Locale.getDefault()
        }
        //暂时只处理英文 如果系统语言设置英文以外 默认为英文
        locale = if (locale.language == Locale.SIMPLIFIED_CHINESE.language) {
            Locale.SIMPLIFIED_CHINESE
        } else {
            Locale.ENGLISH
        }
        return locale
    }


    /**
     *
     * 判断是否是中文
     * @return
     */
    fun isChinese(): Boolean {
        val locale = getSetLanguageLocale()
        return locale.language == Locale.SIMPLIFIED_CHINESE.language
    }

    /**
     * 保持字体大小不随系统设置变化（用在界面加载之前）
     * 要重写Activity的getResources()
     */
    fun getResources(context: Context, resources: Resources, fontScale: Float): Resources? {
        val config = resources.configuration
        return if (config.fontScale != fontScale) {
            config.fontScale = fontScale
            //  resources.getDisplayMetrics().scaledDensity = resources.getDisplayMetrics().scaledDensity* fontScale;
            context.createConfigurationContext(config).resources
        } else {
            resources
        }
    }



}
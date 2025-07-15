package com.knight.kotlin.library_common.enum

import android.content.Context
import com.knight.kotlin.library_common.R


/**
 * @author created by luguian
 * @organize
 * @Date 2025/4/16 10:00
 * @descript:污染物等级
 */
enum class PollutantIndex (
    val id: String,
    val thresholds: List<Float>,
) {
    O3("o3",listOf(0f,100f,160f,215f,265f,800f)),
    PM25("pm25", listOf(0f, 35f, 75f, 115f, 150f, 250f)),
    PM10("pm10", listOf(0f, 50f, 150f, 250f, 350f, 420f)),
    NO2("no2",listOf(0f, 40f, 80f, 180f, 280f, 400f)),
    SO2("so2",listOf(0f, 40f, 80f, 160f, 200f, 400f)),
    CO("co",listOf(0f, 3.0f, 5.0f, 10.0f,15.0f, 30.0f));

    /**
     *
     * 返回对应等级
     */
    fun getAirQualityLevelStr(concentration: Float,context: Context): String {
        for (i in 1 until thresholds.size) {
            if (concentration <= thresholds[i]) {
                return when (i) {
                    1 -> context.getString(R.string.base_air_quality_level_1)
                    2 -> context.getString(R.string.base_air_quality_level_2)
                    3 -> context.getString(R.string.base_air_quality_level_3)
                    4 -> context.getString(R.string.base_air_quality_level_4)
                    5 -> context.getString(R.string.base_air_quality_level_5)
                    else -> context.getString(R.string.base_air_quality_level_6)
                }
            }
        }
        return context.getString(R.string.base_air_quality_level_2)
    }

    /**
     *
     * 返回等级几
     */
    fun getAirQualityLevel(concentration: Float): Int {
        for (i in 1 until thresholds.size) {
            if (concentration <= thresholds[i]) {
                return i - 1
            }
        }
        return 1
    }

    /**
     *
     * 获取最大值
     */
    fun getMaxValue():Float {
        return  thresholds.last()
    }
}
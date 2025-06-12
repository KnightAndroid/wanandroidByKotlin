package com.knight.kotlin.module_home.utils

import android.os.Build
import androidx.annotation.RequiresApi
import com.knight.kotlin.library_base.entity.MoonPeriodEntity
import com.knight.kotlin.library_util.TimeUtils
import org.shredzone.commons.suncalc.MoonTimes
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId


/**
 * @author created by luguian
 * @organize 
 * @Date 2025/4/10 18:22
 * @descript:日月升落帮助类
 */
object MoonRiseSetUtils {
    /**
     *
     * 获取今天月出和明天的月落
     *
     */
    @RequiresApi(Build.VERSION_CODES.O)
    fun getMoonPeriodForNight(date: LocalDate, lat: Double, lng: Double): MoonPeriodEntity {
        val today = MoonTimes.compute().on(date).at(lat, lng).fullCycle().execute()
        return MoonPeriodEntity(
            today.rise ?: Instant.ofEpochMilli(1744273980000).atZone(ZoneId.of(TimeUtils.getDefaultTimeZoneId())),
            today.set ?: Instant.ofEpochMilli(1744318800000).atZone(ZoneId.of(TimeUtils.getDefaultTimeZoneId()))
        )

    }
}
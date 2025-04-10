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
 * @descript:
 */
object MoonRiseSetUtils {

    @RequiresApi(Build.VERSION_CODES.O)
    fun getMoonPeriodForNight(date: LocalDate, lat: Double, lng: Double): MoonPeriodEntity {
        val today = MoonTimes.compute().on(date).at(lat, lng).execute()
        val tomorrow = MoonTimes.compute().on(date.plusDays(1)).at(lat, lng).execute()
        return MoonPeriodEntity(
            today.rise ?: Instant.ofEpochMilli(1744273980000).atZone(ZoneId.of(TimeUtils.getDefaultTimeZoneId())),
            tomorrow.set ?: Instant.ofEpochMilli(1744318800000).atZone(ZoneId.of(TimeUtils.getDefaultTimeZoneId()))
        )

    }
}
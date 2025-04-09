package com.knight.kotlin.module_home.utils

import android.os.Build
import androidx.annotation.RequiresApi
import com.knight.kotlin.module_home.entity.MoonRiseSetTimestamp
import org.shredzone.commons.suncalc.MoonTimes
import java.time.ZoneId
import java.util.Date

/**
 * @Description
 * @Author knight
 * @Time 2025/4/9 23:37
 *
 */

object MoonRiseSetUtils {

    @RequiresApi(Build.VERSION_CODES.O)
    fun getMoonriseAndSetTimestamp(
        lat: Double,
        lon: Double,
        zoneId: ZoneId,
        date: Date
    ): MoonRiseSetTimestamp {
        // 将 java.util.Date 转换成 ZonedDateTime
        val baseZdt = date.toInstant().atZone(zoneId).toLocalDate().atStartOfDay(zoneId)
        val tomorrowZdt = baseZdt.plusDays(1)
        val dayAfterZdt = baseZdt.plusDays(2)

        val moonToday = MoonTimes.compute()
            .on(Date.from(baseZdt.toInstant()))
            .at(lat, lon)
            .execute()

        val moonriseZdt = moonToday.rise?.toInstant()?.atZone(zoneId)

        if (moonriseZdt == null) {
            return MoonRiseSetTimestamp(0, 0)
        }

        val candidates = listOf(baseZdt, tomorrowZdt, dayAfterZdt)

        for (zdt in candidates) {
            val times = MoonTimes.compute()
                .on(Date.from(zdt.toInstant()))
                .at(lat, lon)
                .execute()

            val moonsetZdt = times.set?.toInstant()?.atZone(zoneId)

            if (moonsetZdt != null && moonsetZdt.isAfter(moonriseZdt)) {
                return MoonRiseSetTimestamp(
                    moonrise = moonriseZdt.toInstant().toEpochMilli(),
                    moonset = moonsetZdt.toInstant().toEpochMilli()
                )
            }
        }

        return MoonRiseSetTimestamp(
            moonrise = moonriseZdt.toInstant().toEpochMilli(),
            moonset = 0
        )
    }




    fun getMoonriseTimestamp(
        lat: Double,
        lon: Double,
        zoneId: ZoneId,
        date: Date
    ): Long? {
        val zdt = date.toInstant().atZone(zoneId).toLocalDate().atStartOfDay(zoneId)

        val moonTimes = MoonTimes.compute()
            .on(Date.from(zdt.toInstant()))
            .at(lat, lon)
            .execute()

        return moonTimes.rise?.toInstant()?.toEpochMilli()
    }

    fun getMoonsetTimestamp(
        lat: Double,
        lon: Double,
        zoneId: ZoneId,
        date: Date
    ): Long? {
        // 先获取月出时间
        val moonriseTimestamp = getMoonriseTimestamp(lat, lon, zoneId, date)
        if (moonriseTimestamp == null) return null

        val baseZdt = date.toInstant().atZone(zoneId).toLocalDate().atStartOfDay(zoneId)

        // 设置第二天凌晨时刻
        val moonsetTargetZdt = baseZdt.plusDays(1).withHour(0).withMinute(0).withSecond(0).withNano(0)

        val moonTimes = MoonTimes.compute()
            .on(Date.from(moonsetTargetZdt.toInstant()))
            .at(lat, lon)
            .execute()

        // 确保月落时间是在月出之后且是第二天凌晨
        val moonsetTimestamp = moonTimes.set?.toInstant()?.toEpochMilli()
        if (moonsetTimestamp != null && moonsetTimestamp > moonriseTimestamp) {
            return moonsetTimestamp
        }

        return null
    }
}
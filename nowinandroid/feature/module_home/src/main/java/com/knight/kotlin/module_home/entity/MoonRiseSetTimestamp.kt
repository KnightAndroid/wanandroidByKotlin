package com.knight.kotlin.module_home.entity

import android.os.Build
import android.os.Parcelable
import androidx.annotation.RequiresApi
import kotlinx.parcelize.Parcelize
import org.shredzone.commons.suncalc.MoonTimes
import java.time.ZoneId
import java.util.Date

/**
 * @Description
 * @Author knight
 * @Time 2025/4/9 23:28
 *
 */

@Parcelize
data class MoonRiseSetTimestamp (
    val moonrise: Long,
    val moonset: Long
): Parcelable



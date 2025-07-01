package com.core.library_base.entity

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import java.time.ZonedDateTime


/**
 * @author created by luguian
 * @organize
 * @Date 2025/4/10 18:21
 * @descript:
 */
@Parcelize
data class MoonPeriodEntity(val rise: ZonedDateTime, val set: ZonedDateTime) : Parcelable
package com.knight.kotlin.module_constellate.entity

import android.os.Parcelable
import kotlinx.parcelize.Parcelize


/**
 * @author created by luguian
 * @organize
 * @Date 2025/8/6 16:54
 * @descript:日运 七天日历
 */
@Parcelize
data class ConstellateDateEntity (val dayNumber: Int, val display: String): Parcelable
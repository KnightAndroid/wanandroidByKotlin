package com.knight.kotlin.module_constellate.entity

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

/**
 * @Description
 * @Author knight
 * @Time 2025/9/2 20:21
 *
 */

@Parcelize
data class ConstellateResponseEntity (
    val day:ConstellateFortuneEntity,
    val tomorrow:ConstellateFortuneEntity,
    val week:ConstellateFortuneEntity,
    val month:ConstellateFortuneEntity,
    val year:ConstellateFortuneEntity
): Parcelable
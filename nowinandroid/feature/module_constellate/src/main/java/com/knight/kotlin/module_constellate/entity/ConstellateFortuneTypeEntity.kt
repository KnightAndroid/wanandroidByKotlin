package com.knight.kotlin.module_constellate.entity

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

/**
 * @Description 星座运势
 * @Author knight
 * @Time 2025/9/9 21:31
 *
 */
@Parcelize
data class ConstellateFortuneTypeEntity(
    val type: String,
    val value: String,
    val desc: String

) : Parcelable
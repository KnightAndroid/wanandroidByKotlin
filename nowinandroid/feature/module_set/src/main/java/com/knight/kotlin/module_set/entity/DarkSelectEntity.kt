package com.knight.kotlin.module_set.entity

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

/**
 * Author:Knight
 * Time:2022/5/20 17:25
 * Description:DarkSelectEntity
 */
@Parcelize
data class DarkSelectEntity(
    val name: String,
    val englishNamn: String,
    var select: Boolean,
    val isDark:Boolean,
    val showLine:Boolean
) : Parcelable
package com.knight.kotlin.module_set.entity

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

/**
 * Author:Knight
 * Time:2022/5/31 16:21
 * Description:LanguageEntity
 */
@Parcelize
data class LanguageEntity(
    val languageName: String,
    val englishName: String,
    var select: Boolean,
    val showLine: Boolean

) : Parcelable
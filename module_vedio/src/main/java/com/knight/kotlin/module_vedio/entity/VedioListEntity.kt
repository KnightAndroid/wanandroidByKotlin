package com.knight.kotlin.module_vedio.entity

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

/**
 * Author:Knight
 * Time:2024/2/26 11:25
 * Description:VedioListEntity
 */
@Parcelize
data class VedioListEntity(
    val joke:VedioEntity
): Parcelable

@Parcelize
data class VedioEntity(
    var videoUrl : String,
    val videoSize : String,
    val content : String,
    val addTime : String,
    val videoTime : Long
): Parcelable



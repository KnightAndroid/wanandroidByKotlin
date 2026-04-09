package com.knight.kotlin.library_common.entity

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

/**
 * @Description
 * @Author knight 地图模型
 * @Time 2026/4/9 21:55
 *
 */
@Parcelize
data class LocationEntity (
    val lat: Double,
    val lng: Double,
    val province:String,
    val city:String,
    val district:String,
    val address:String
): Parcelable
package com.knight.kotlin.module_home.entity

import android.os.Parcelable
import kotlinx.parcelize.Parcelize


/**
 * @author created by luguian
 * @organize
 * @Date 2026/4/10 10:56
 * @descript:城市经纬度
 */
@Parcelize
data class CityLatLngBean (
    val city:String,
    val latitude:Double,
    val longitude:Double
) : Parcelable
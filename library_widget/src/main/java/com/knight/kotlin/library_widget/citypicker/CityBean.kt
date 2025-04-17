package com.knight.kotlin.library_widget.citypicker

import android.os.Parcelable
import kotlinx.parcelize.Parcelize


/**
 * @author created by luguian
 * @organize
 * @Date 2025/4/17 10:43
 * @descript:
 */
@Parcelize
data class CityBean (
    val city:String,
    val province:String
) : Parcelable
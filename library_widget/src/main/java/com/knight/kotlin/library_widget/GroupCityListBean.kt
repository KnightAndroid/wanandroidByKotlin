package com.knight.kotlin.library_widget

import android.os.Parcelable
import com.knight.kotlin.library_widget.citypicker.CityBean
import kotlinx.parcelize.Parcelize


/**
 * @author created by luguian
 * @organize
 * @Date 2025/4/17 16:42
 * @descript:
 */
@Parcelize
data class GroupCityListBean (
    val group:String,
    val city:List<CityBean>
): Parcelable
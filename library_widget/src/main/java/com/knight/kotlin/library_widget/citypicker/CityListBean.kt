package com.knight.kotlin.library_widget.citypicker

import android.os.Parcelable
import kotlinx.parcelize.Parcelize


/**
 * @author created by luguian
 * @organize
 * @Date 2025/4/17 15:11
 * @descript:
 */
@Parcelize
data class CityListBean (
    val type:Int = 0, //0是当前城市 1是热门城市 2是普通
    val data:List<CityBean>
): Parcelable
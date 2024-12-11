package com.knight.kotlin.module_eye_discover.entity

import android.os.Parcelable
import kotlinx.parcelize.Parcelize


/**
 * @author created by luguian
 * @organize
 * @Date 2024/12/11 9:39
 * @descript:发现导航数据
 */
data class EyeDiscoverNavEntity (
    val nav_item: EyeDiscoverNavItem = EyeDiscoverNavItem(),
    val style: String = ""
):BaseEyeDiscoverEntity()


@Parcelize
data class EyeDiscoverNavItem(
    val left: List<EyeDiscoverNavItemLeft> = emptyList(),
) : Parcelable

@Parcelize
data class EyeDiscoverNavItemLeft(
    val label: String = "",
    val text: String = "",
    val type: String = ""
):Parcelable



































package com.knight.kotlin.library_base.entity

import android.os.Parcelable
import kotlinx.parcelize.Parcelize


/**
 * @author created by luguian
 * @organize
 * @Date 2025/4/7 17:12
 * @descript:ip实体
 */
@Parcelize
data class IpEntity (
    val ip:String
): Parcelable
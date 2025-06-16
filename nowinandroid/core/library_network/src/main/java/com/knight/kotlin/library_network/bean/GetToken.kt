package com.knight.kotlin.library_network.bean

import android.os.Parcelable
import kotlinx.parcelize.Parcelize


/**
 * @author created by luguian
 * @organize
 * @Date 2024/12/9 18:15
 * @descript:
 */
@Parcelize
data class GetToken (
    val grant_type: String = "",
    val server_timestamp: Long = 0,
    val uid: Long = 0
): Parcelable
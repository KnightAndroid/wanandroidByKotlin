package com.knight.kotlin.library_network.bean

import android.os.Parcelable
import kotlinx.parcelize.Parcelize


/**
 * @author created by luguian
 * @organize
 * @Date 2024/12/9 17:57
 * @descript:开眼需要
 */
@Parcelize
data class AuthToken (
    val access_token: String,
    val device_id: String,
    val expires_in: Int,
    val refresh_token: String,
    val server_timestamp: Int
): Parcelable
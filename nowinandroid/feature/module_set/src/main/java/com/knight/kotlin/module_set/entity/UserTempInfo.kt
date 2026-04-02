package com.knight.kotlin.module_set.entity

import android.os.Parcelable
import kotlinx.parcelize.Parcelize


/**
 * @author created by luguian
 * @organize
 * @Date 2026/4/2 16:32
 * @descript:设置模块临时UserInfo
 */
@Parcelize
data class UserTempInfo (
    val username: String,
    val email: String,
    val coinCount: String,
    val rank: String
): Parcelable
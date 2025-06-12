package com.knight.kotlin.library_base.entity

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

/**
 * Author:Knight
 * Time:2022/1/17 14:56
 * Description:UserInfoEntity
 */
@Parcelize
data class UserInfoEntity(
    val admin: Boolean,
    val coinCount: Int,
    val collectIds: List<Int>,
    val email: String,
    val icon: String,
    val id: Int,
    val nickname: String,
    val password: String,
    val publicName: String,
    val token: String,
    val type: Int,
    val username: String
) : Parcelable
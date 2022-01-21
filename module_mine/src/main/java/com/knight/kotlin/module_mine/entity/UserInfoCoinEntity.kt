package com.knight.kotlin.module_mine.entity

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

/**
 * Author:Knight
 * Time:2022/1/18 17:20
 * Description:UserInfoCoinEntity
 */
@Parcelize
data class UserInfoCoinEntity(
    val coinCount:Int,
    val level:Int,
    val nickname:String,
    val rank:String,
    val userId:Int,
    val username:String
): Parcelable

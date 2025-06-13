package com.knight.kotlin.module_mine.entity

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

/**
 * Author:Knight
 * Time:2022/5/7 13:50
 * Description:CoinRankEntity
 */
@Parcelize
data class CoinRankEntity(
    val coinCount:Int,
    val level:Int,
    val nickname:String,
    val rank:String,
    val userId:Int,
    val username:String
): Parcelable
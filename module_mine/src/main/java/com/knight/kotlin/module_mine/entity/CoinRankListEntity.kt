package com.knight.kotlin.module_mine.entity

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

/**
 * Author:Knight
 * Time:2022/5/7 13:51
 * Description:CoinRankListEntity
 */
@Parcelize
data class CoinRankListEntity(
    val curpage: Int,
    val offset: Int,
    val over: Boolean,
    val pageCount: Int,
    val size: Int,
    val total: Int,
    val datas: MutableList<CoinRankEntity>
) : Parcelable
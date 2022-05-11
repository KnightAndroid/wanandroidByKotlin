package com.knight.kotlin.module_mine.entity

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

/**
 * Author:Knight
 * Time:2022/5/11 14:07
 * Description:MyDetailCoinListEntity
 */
@Parcelize
data class MyDetailCoinListEntity(
    val curPage:Int,
    val offset:Int,
    val over:Boolean,
    val pageCount:Int,
    val size:Int,
    val total:Int,
    val datas:MutableList<MyDetailCoinEntity>
): Parcelable

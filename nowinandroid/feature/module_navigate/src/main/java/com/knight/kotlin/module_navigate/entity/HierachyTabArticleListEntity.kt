package com.knight.kotlin.module_navigate.entity

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

/**
 * Author:Knight
 * Time:2022/5/6 15:57
 * Description:HierachyTabArticleListEntity
 */

@Parcelize
data class HierachyTabArticleListEntity(
    val curpage: Int,
    val offset: Int,
    val over: Boolean,
    val pageCount: Int,
    val size: Int,
    val total: Int,
    val datas: MutableList<HierachyTabArticleEntity>
) : Parcelable
package com.knight.kotlin.module_square.entity

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

/**
 * Author:Knight
 * Time:2022/4/27 17:49
 * Description:SquareArticleListBean
 */
@Parcelize
data class SquareArticleListBean (
    val curpage:Int,
    val offset:Int,
    val over:Boolean,
    val pageCount:Int,
    val size:Int,
    val total:Int,
    val datas:MutableList<SquareArticleBean>
    ): Parcelable
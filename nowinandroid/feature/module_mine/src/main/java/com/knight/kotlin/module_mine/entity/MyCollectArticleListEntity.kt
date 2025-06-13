package com.knight.kotlin.module_mine.entity

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

/**
 * Author:Knight
 * Time:2022/5/13 10:23
 * Description:MyCollectArticleListEntity
 */
@Parcelize
data class MyCollectArticleListEntity(
    val curPage: Int,
    val offset: Int,
    val over: Boolean,
    val pageCount: Int,
    val size: Int,
    val total: Int,
    val datas: MutableList<MyCollectArticleEntity>
) : Parcelable
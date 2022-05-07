package com.knight.kotlin.module_mine.entity

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

/**
 * Author:Knight
 * Time:2022/5/7 16:26
 * Description:OtherShareArticleListEntity
 */
@Parcelize
data class OtherShareArticleListEntity(
    val coinInfo: UserMessageEntity,
    val shareArticles: MyArticleListEntity,
) : Parcelable
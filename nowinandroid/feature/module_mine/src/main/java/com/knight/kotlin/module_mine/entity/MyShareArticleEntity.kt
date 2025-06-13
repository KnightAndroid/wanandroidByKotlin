package com.knight.kotlin.module_mine.entity

import android.os.Parcelable
import com.knight.kotlin.library_base.entity.UserInfoEntity
import kotlinx.parcelize.Parcelize

/**
 * Author:Knight
 * Time:2022/5/17 10:19
 * Description:MyShareArticleEntity
 */
@Parcelize
data class MyShareArticleEntity(
    val coinInfo: UserInfoEntity,
    val shareArticles: MyArticleListEntity
) : Parcelable
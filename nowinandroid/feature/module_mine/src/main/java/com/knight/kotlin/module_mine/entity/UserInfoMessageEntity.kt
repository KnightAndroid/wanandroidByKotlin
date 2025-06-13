package com.knight.kotlin.module_mine.entity

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

/**
 * Author:Knight
 * Time:2022/5/13 9:47
 * Description:UserInfoMessageEntity
 */


@Parcelize
data class UserInfoMessageEntity(
    val coinInfo: UserInfoCoinEntity,
    val collectArticleInfo: CollectArticleInfo
) : Parcelable


@Parcelize
data class UserInfoCoinEntity(
    val coinCount: Int,
    val level: Int,
    val nickname: String,
    val rank: String,
    val userId: Int,
    val username: String
) : Parcelable

@Parcelize
data class CollectArticleInfo(
    val count: Int
) : Parcelable
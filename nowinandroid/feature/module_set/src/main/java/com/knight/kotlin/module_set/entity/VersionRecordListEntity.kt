package com.knight.kotlin.module_set.entity

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

/**
 * Author:Knight
 * Time:2022/8/25 16:46
 * Description:VersionRecordListEntity
 */
@Parcelize
data class VersionRecordListEntity(
    val datas: MutableList<VersionRecordEntity>
) : Parcelable


@Parcelize
data class VersionRecordEntity(
    val title: String,
    val desc: String,
    val publishTime: String,
    val updateTime: String,
    val versionName: String,
    val versionCode: Int
) : Parcelable
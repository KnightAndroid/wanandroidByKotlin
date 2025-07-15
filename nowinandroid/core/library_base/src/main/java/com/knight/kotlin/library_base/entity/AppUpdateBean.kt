package com.knight.kotlin.library_base.entity

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

/**
 * Author:Knight
 * Time:2022/1/10 15:14
 * Description:AppUpdateBean
 * APP 更新实体
 */
@Parcelize
data class AppUpdateBean(
    val downLoadLink: String,
    val versionCode: Long,
    val versionName: String,
    val forceUpdate: Boolean,
    val updateDesc: String,
    val title: String,
    val updateTime: String
) : Parcelable
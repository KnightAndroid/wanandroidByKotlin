package com.knight.kotlin.module_home.entity

import android.os.Parcelable
import com.knight.kotlin.library_database.entity.EveryDayPushEntity
import kotlinx.parcelize.Parcelize

/**
 * Author:Knight
 * Time:2021/12/27 19:53
 * Description:EveryDayPushArticlesBean
 */
@Parcelize
data class EveryDayPushArticlesBean(
    val total: Int,
    val pushStatus: Boolean,
    val time:String,
    val datas: List<EveryDayPushEntity>
) : Parcelable
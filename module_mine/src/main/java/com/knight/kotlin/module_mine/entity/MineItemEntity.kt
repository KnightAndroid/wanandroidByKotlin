package com.knight.kotlin.module_mine.entity

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

/**
 * Author:Knight
 * Time:2024/4/23 15:14
 * Description:MineItemEntity
 */
@Parcelize
data class MineItemEntity(
    val id:Int,
    val name_zh:String,
    val name_en:String
): Parcelable

package com.knight.kotlin.module_navigate.entity

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

/**
 * Author:Knight
 * Time:2022/5/5 15:33
 * Description:HierachyListEntity
 */
@Parcelize
data class HierachyListEntity(
    val name: String,
    val courseId: String,
    val id: Int,
    val order: Int,
    val parentChapterId: Int,
    val userControlSetTop: Boolean,
    val visible: Int,
    val children:MutableList<HierachyChildrenEntity>
    ) : Parcelable
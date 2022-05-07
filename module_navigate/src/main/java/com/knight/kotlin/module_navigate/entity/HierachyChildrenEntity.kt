package com.knight.kotlin.module_navigate.entity

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

/**
 * Author:Knight
 * Time:2022/5/5 15:37
 * Description:HierachyChildrenEntity
 */

@Parcelize
data class HierachyChildrenEntity(
    val courseId: String,
    val id: Int,
    val name: String,
    val order: Int,
    val parentChapterId: Int,
    val userControlSetTop: Boolean,
    val visible: Int
) : Parcelable
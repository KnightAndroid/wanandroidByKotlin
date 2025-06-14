package com.knight.kotlin.module_navigate.entity

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

/**
 * Author:Knight
 * Time:2022/5/5 14:32
 * Description:NavigateListEntity
 */
@Parcelize
data class NavigateListEntity(
    val cid: Int,
    val name: String,
    val articles: MutableList<NavigateChildrenEntity>
) : Parcelable
package com.knight.kotlin.module_utils.entity

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

/**
 * Author:Knight
 * Time:2022/6/2 14:46
 * Description:UtilsEntity
 */
@Parcelize
data class UtilsEntity(
    val id: Int,
    val isNew: Int,
    val link: String,
    val name: String,
    val tabName: String,
    val showInTab: Int,
    val desc: String,
    val visible: Int
) : Parcelable
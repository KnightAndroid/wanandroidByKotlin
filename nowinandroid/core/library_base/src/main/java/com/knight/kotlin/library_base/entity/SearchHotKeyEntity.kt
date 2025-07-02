package com.knight.kotlin.library_base.entity

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

/**
 * Author:Knight
 * Time:2022/4/12 15:20
 * Description:SearchHotKeyEntity
 */
@Parcelize
data class SearchHotKeyEntity (
    val id:Int,
    val link:String,
    val name:String,
    val visible:Int,
    val order:Int
    ): Parcelable
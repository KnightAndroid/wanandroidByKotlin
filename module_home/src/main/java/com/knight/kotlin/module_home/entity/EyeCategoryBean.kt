package com.knight.kotlin.module_home.entity

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

/**
 * Author:Knight
 * Time:2024/4/26 15:10
 * Description:EyeCategoryBean
 */
@Parcelize
data class EyeCategoryBean (
    val categoryId:Int,
    val categoryName:String,
    val desc:String,
    val categoryColor:String
): Parcelable
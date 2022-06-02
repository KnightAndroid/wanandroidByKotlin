package com.knight.kotlin.module_course.entity

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

/**
 * Author:Knight
 * Time:2022/6/2 17:14
 * Description:CourseDetailListEntity
 */
@Parcelize
data class CourseDetailListEntity(
    val curPage: Int,
    val offset: Int,
    val over: Boolean,
    val pageCount: Int,
    val size: Int,
    val total: Int,
    val datas: MutableList<CourseDetailEntity>
) : Parcelable
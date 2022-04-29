package com.knight.kotlin.module_square.entity

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

/**
 * Author:Knight
 * Time:2022/4/27 16:52
 * Description:SquareQuestionListBean
 */
@Parcelize
data class SquareQuestionListBean(
    val curPage: Int,
    val offset: Int,
    val over: Boolean,
    val pageCount: Int,
    val size: Int,
    val total: Int,
    val datas: MutableList<SquareQuestionBean>
) : Parcelable
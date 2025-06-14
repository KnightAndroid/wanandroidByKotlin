package com.knight.kotlin.module_project.entity

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

/**
 * Author:Knight
 * Time:2022/4/28 17:28
 * Description:ProjectArticleListBean
 */
@Parcelize
data class ProjectArticleListBean(
    val curPage: Int,
    val offset: Int,
    val over: Boolean,
    val pageCount: Int,
    val size: Int,
    val total: Int,
    val datas: MutableList<ProjectArticleBean>
) : Parcelable
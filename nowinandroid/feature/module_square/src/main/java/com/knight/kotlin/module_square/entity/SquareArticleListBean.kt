package com.knight.kotlin.module_square.entity

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

/**
 * Author:Knight
 * Time:2022/2/18 14:24
 * Description:HomeArticleListBean
 */
@Parcelize
data class SquareArticleListBean(
    val curPage: Int,
    val offset: Int,
    val over: Boolean,
    val pageCount: Int,
    val size: Int,
    val total: Int,
    val datas: MutableList<SquareArticleEntity>

) : Parcelable

@Parcelize
data class SquareArticleEntity(
    val apkLink: String?,
    val audit: Int?,
    val author: String?,
    val canEdit: Boolean?,
    val chapterId: Int?,
    val chapterName: String?,
    var collect: Boolean,
    val courseId: Int?,
    val desc: String?,
    val descMd: String?,
    val envelopePic: String?,
    val fresh: Boolean?,
    val host: String?,
    val id: Int,
    val link: String,
    val niceDate: String?,
    val niceShareDate: String?,
    val origin: String?,
    val prefix: String?,
    val projectLink: String?,
    val publishTime: Long?,
    val realSuperChapterId: Int?,
    val selfVisible: Int?,
    val shareDate: Long?,
    val shareUser: String?,
    val superChapterId: Int?,
    val superChapterName: String?,
    val tags: List<Tag>?,
    val title: String,
    val type: Int?,
    val userId: Int?,
    val visible: Int?,
    val zan: Int?,
    var top: Boolean?
) : Parcelable

@Parcelize
data class Tag(
    val name: String,
    val url: String
) : Parcelable



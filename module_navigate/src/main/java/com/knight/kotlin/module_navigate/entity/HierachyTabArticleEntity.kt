package com.knight.kotlin.module_navigate.entity

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

/**
 * Author:Knight
 * Time:2022/5/6 15:47
 * Description:HierachyTabArticleEntity
 */
@Parcelize
data class HierachyTabArticleEntity(
    val apkLink: String,
    val audit: Int,
    val author: String,
    val canEdit: Boolean,
    val chapterId: String,
    val chapterName: String,
    var collect: Boolean,
    val courseId: String,
    val desc: String,
    val descMd: String,
    val envelopePic: String,
    val fresh: Boolean,
    val id: Int,
    val link: String,
    val niceDate: String,
    val niceShareDate: String,
    val origin: String,
    val prefix: String,
    val projectLink: String,
    val publishTime: Long,
    val realSuperChapterId: Int,
    val selfVisible: Int,
    val shareDate: String? = "",
    val shareUser: String,
    val superChapterId: Int,
    val superChapterName: String,
    val title: String,
    val type: Int,
    val userId: Int,
    val visible: Int,
    val zan: Int,
    ): Parcelable

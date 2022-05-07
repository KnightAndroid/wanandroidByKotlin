package com.knight.kotlin.module_navigate.entity

import android.os.Parcelable
import android.text.TextUtils
import com.chad.library.adapter.base.entity.MultiItemEntity
import com.knight.kotlin.library_base.config.Appconfig
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

    ) : Parcelable, MultiItemEntity {
    override val itemType: Int
        get() = if (TextUtils.isEmpty(envelopePic)) {
            Appconfig.ARTICLE_TEXT_TYPE
        } else Appconfig.ARTICLE_PICTURE_TYPE
}
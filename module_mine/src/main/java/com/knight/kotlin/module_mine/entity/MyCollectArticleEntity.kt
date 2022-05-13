package com.knight.kotlin.module_mine.entity

import android.os.Parcelable
import android.text.TextUtils
import com.chad.library.adapter.base.entity.MultiItemEntity
import com.knight.kotlin.library_base.config.Appconfig
import kotlinx.parcelize.Parcelize

/**
 * Author:Knight
 * Time:2022/5/13 10:08
 * Description:MyCollectArticleEntity
 */
@Parcelize
data class MyCollectArticleEntity(
    val author: String,
    val chapterId: Int,
    val chapterName: String,
    val courseId: Int,
    val desc: String,
    val envelopePic: String,
    val id: Int,
    val link: String,
    val niceDate: String,
    val origin: String,
    val originId: Int,
    val publishTime: Long,
    val title: String,
    val userId: Int,
    val visible: Int,
    val zan: Int,
    val shareUser: String,
) : Parcelable, MultiItemEntity {
    override val itemType: Int
        get() = if (TextUtils.isEmpty(envelopePic)) {
            Appconfig.ARTICLE_TEXT_TYPE
        } else Appconfig.ARTICLE_PICTURE_TYPE
}
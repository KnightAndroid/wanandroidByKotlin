package com.knight.kotlin.module_eye_video_detail.entity

import android.os.Parcelable
import com.knight.kotlin.library_base.entity.EyeCommonAuthorEntity
import kotlinx.parcelize.Parcelize

/**
 * @Description
 * @Author knight
 * @Time 2025/5/20 21:56
 *
 */


@Parcelize
data class EyeVideoResultComment(
    val item_per_page:Int,
    val page_count:Int,
    val item_list:MutableList<EyeVideoCommentEntity>,
    val item_count:Int
): Parcelable
@Parcelize
data class EyeVideoCommentEntity(
    val be_favorite: Boolean,
    val can_delete: Boolean,
    val comment_content: String,
    val comment_id: String,
    val comment_time: String,
    val count_summary: EyeVideoCommentCountSummary,
    val location: String,
    val parent_id: String,
    val reply_count: Int,
    val reply_list: List<String>,
    val resource_id: String,
    val resource_type: String,
    val root_id: String,
    val user: EyeVideoCommentAuthorEntity
): Parcelable






@Parcelize
data class EyeVideoCommentCountSummary(
    val favorite: EyeVideoCommentFavorite,
    val reply: EyeVideoCommentReply
): Parcelable
@Parcelize
data class EyeVideoCommentFavorite(
    val count: Int
): Parcelable
@Parcelize
data class EyeVideoCommentReply(
    val count: Int
): Parcelable


@Parcelize
data class EyeVideoCommentAuthorEntity (
    val avatar: String ,
    val description: String = "",
    val link: String = "",
    val nick: String = "",
    val user_type: String = "",
    val uid: Int = 0    ,
    val is_mine:Boolean = false

): Parcelable
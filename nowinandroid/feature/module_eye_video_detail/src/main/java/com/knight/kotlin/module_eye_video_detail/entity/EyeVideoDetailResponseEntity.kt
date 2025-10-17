package com.knight.kotlin.module_eye_video_detail.entity

import android.os.Parcelable
import kotlinx.parcelize.Parcelize


/**
 * @author created by luguian
 * @organize
 * @Date 2025/10/17 10:05
 * @descript:视频详情实体模型
 */
@Parcelize
class EyeVideoDetailResponseEntity (
    val item_id:String,
    val publish_time:String,
    val raw_publish_time:String,
    val topic: EyeVideoDetailTopicEntity,
    val category: EyeVideoDetailCategoryEntity,
    val type:String,
    val text:String,
    val video:EyeVideoPlayDetailEntity,
    val recommend_level:String,
    val author:EyeVideoPlayAuthor,
    val consumption:EyeVideoPlayConsumption,
    val resource_id:String,
    val resource_type:String,
    val real_location:String
): Parcelable

@Parcelize
class EyeVideoDetailTopicEntity (
    val id:Long,
    val title:String,
    val link:String
): Parcelable

@Parcelize
class EyeVideoDetailCategoryEntity (
    val id:Long,
    val name:String
): Parcelable
@Parcelize
data class EyeVideoPlayDetailEntity(
    val background: EyeVideoPlayBackground,
    val cover: EyeVideoPlayCover,
    val description_editor: String,
    val duration: EyeVideoPlayDuration,
    val height: Int,
    val play_ctrl: EyeVideoPlayPlayCtrl,
    val play_url: String,
    val recommend_level: String,
    val tags: List<EyeVideoPlayTag>,
    val title: String,
    val title_pgc: String,
    val video_id: String,
    val width: Int
): Parcelable
@Parcelize
data class EyeVideoPlayBackground(
    val img_info: EyeVideoPlayImgInfo,
    val url: String
): Parcelable
@Parcelize
data class EyeVideoPlayCover(
    val img_info: EyeVideoPlayImgInfo,
    val url: String
): Parcelable
@Parcelize
data class EyeVideoPlayDuration(
    val text: String,
    val value: Int
): Parcelable
@Parcelize
data class EyeVideoPlayPlayCtrl(
    val autoplay: Boolean,
    val autoplay_times: Int,
    val need_cellular: Boolean,
    val need_wifi: Boolean
): Parcelable
@Parcelize
data class EyeVideoPlayTag(
    val id: Int,
    val link: String,
    val title: String
): Parcelable
@Parcelize
data class EyeVideoPlayImgInfo(
    val height: Int,
    val scale: Double,
    val width: Int
): Parcelable

@Parcelize
data class EyeVideoPlayAuthor(
    val avatar: EyeVideoPlayAvatar,
    val description: String,
    val followed: Boolean,
    val link: String,
    val nick: String,
    val type: String,
    val uid: Int
): Parcelable
@Parcelize
data class EyeVideoPlayAvatar(
    val img_info: EyeVideoPlayImgInfo,
    val shape: String,
    val url: String
): Parcelable

@Parcelize
data class EyeVideoPlayConsumption(
    val collection_count: Int,
    val comment_count: Int,
    val like_count: Int,
    val share_count: Int
): Parcelable






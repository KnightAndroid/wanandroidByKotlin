package com.core.library_base.entity

import kotlinx.serialization.Serializable

/**
 * @Description 视频实体
 * @Author knight
 * @Time 2024/12/26 21:51
 *
 */
@Serializable
data class EyeCommonVideoEntity (
   val video_id:String = "",
   val title:String = "",
   val duration:EyeCommonDuration? = null,
   val play_url:String = "",
   val preview_url:String = "",
   val recommend_level: String = "",
   val tags: List<EyeCommonTag>? = null,
   val cover: EyeCommonCover? = null,
   val author: EyeCommonAuthor? = null,
   val resource_id: Long = 0,
   val resource_type: String = "",

    )

@Serializable
data class EyeCommonDuration (
    val value:Long = 0,
    val text:String = ""
)

@Serializable
data class EyeCommonTag(
    val id: Int,
    val title: String,
    val link: String
)

@Serializable
data class EyeCommonCover(
    val url: String = "",
    val img_info: EyeCommonImageInfo? = null,
)


@Serializable
data class EyeCommonImageInfo(
    val width: Double = 0.0,
    val height: Double = 0.0,
    val scale: Double = 0.0,
)

@Serializable
data class EyeCommonAuthor(
    val uid: Long = 0,
    val nick: String = "",
    val description: String = "",
    val avatar: EyeCommonAvatar? = null,
    val link: String = "",
    val type: String = "",
    val followed: Boolean = false,
    val user_type: String = "",
    val status: String = "",
    val is_m: Boolean = false
)



@Serializable
data class EyeCommonAvatar(
    val url: String = "",
    val img_info: EyeCommonImageInfo? = null,
    val shape: String = "",
)







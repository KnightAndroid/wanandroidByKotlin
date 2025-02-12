package com.knight.kotlin.library_base.entity

import kotlinx.serialization.Serializable


/**
 * @author created by luguian
 * @organize
 * @Date 2025/1/23 17:49
 * @descript:图文实体类
 */
@Serializable
data class EyeCommonGraphicEntity(

    val image_id: Long = 0,
    val publish_time: String = "",
    val raw_publish_time: String = "",
    val text: String = "",
    val cover: EyeCommonCover? = null,
    val title: String = "",
    val author: EyeCommonAuthor? = null,
    val liked: Boolean = false,
    val collected: Boolean = false,
    val is_mine: Boolean = false,
    val show_follow_btn: Boolean = false,
    val consumption: EyeCommonConsumption? = null,
    val topics: List<EyeCommonTopic>? = null
)


@Serializable
data class EyeCommonTopic(
    val id: Long = 0,
    val title: String = "",
    val link: String = ""
)

@Serializable
data class EyeCommonConsumption(
    val like_count: Long = 0,
    val collection_count: Long = 0,
    val comment_count: Long = 0,
    val share_count: Long = 0,
    val play_count:Long = 0
)



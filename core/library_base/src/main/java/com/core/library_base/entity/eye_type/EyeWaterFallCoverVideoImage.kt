package com.core.library_base.entity.eye_type

import com.core.library_base.entity.EyeCommonAuthor
import com.core.library_base.entity.EyeCommonConsumption
import com.core.library_base.entity.EyeCommonCover
import com.core.library_base.entity.EyeCommonDuration
import com.core.library_base.entity.EyeCommonTag
import com.core.library_base.entity.EyeCommonTopic
import com.core.library_base.ktx.NumberOrStringSerializer

import kotlinx.serialization.Serializable
import org.greenrobot.eventbus.Poster

/**
 * @Description
 * @Author knight
 * @Time 2025/2/11 22:35
 * 社区/广场 视频还是图像卡片
 */
@Serializable
data class EyeWaterFallCoverVideoImage (
    val image_id: Long = 0,
    val video_id:String = "",
    val duration: EyeCommonDuration?=null,
    val play_ctrl:EyeVideoCtrl?=null,
    val tags: List<EyeCommonTag> = emptyList(),
    val title: String = "",
    val recommend_level:String = "",
    val cover: EyeCommonCover? = null,
    val author: EyeCommonAuthor? = null,
    val liked: Boolean = false,
    val image_count: Long = 0,
    val resource_id: Long = 0,
    val resource_type:String = "",
    val debug:EyeVideoItemCard?=null
)

@Serializable
data class EyeVideoCtrl(
    val autoplay:Boolean,
    val autoplay_times: Long,
    val need_wifi: Boolean,
    val need_cellular:Boolean
)
@Serializable
data class EyeVideoItemCard(
    val itemId: String = "",
    val publish_time: String = "",
    val tag_list:List<EyeCommonVideoTag>?=null,
    val new_tag_list:List<EyeCommonVideoTag>?=null,
    val add_watermark:Int = 0,
    val raw_publish_time: String = "",
    val location: EyeLocation? = null,
    val topics: List<EyeCommonTopic> = listOf(),
    val category: EyeCategory? = null,
    val type: String = "",
    val text: String = "",
    val video: EyeVideo? = null,
    val recommend_level: String = "",
    val status:String = "",
    val author: EyeVideoAuthor? = null,
    val consumption: EyeCommonConsumption? = null,
    val liked: Boolean = false,
    val collected: Boolean = false,
    val resource_id: Long = 0,
    val resource_type: String = "",
    val private_msg_link: String = "",
    val is_mine: Boolean = false,
    val link: String = "",
    val app_link: String = "",
    val real_location: String = "",
)


@Serializable
data class EyeLocation(
    val area: String = "",
    val city: String = "",
    @Serializable(with = NumberOrStringSerializer::class)
    val longitude: String ="0.0000000",
    @Serializable(with = NumberOrStringSerializer::class)
    val latitude: String = "0.0000000"
)

@Serializable
data class EyeCategory(
    val id: Int = 0,
    val name: String = ""
)


@Serializable
data class EyeVideo(
    val video_id: String = "",
    val title: String = "",
    val duration: EyeCommonDuration? = null,
    val play_ctrl: EyeVideoCtrl? = null,
    val play_url: String = "",
    val play_info: List<EyePlayInfo> = emptyList(),
    val preview_url:String = "",
    val play_url_with_watermark:String = "",
    val origin_url:String = "",
    val cover_url:String = "",
    val cover_blurred:String = "",
    val summary_url:String = "",
    val thumbnail_list:List<String> = emptyList(),
    val recommend_level: String = "",
    val tags: List<EyeCommonTag> = emptyList(),
    val cover: EyeCommonCover? = null,
    val width: Int = 0,
    val height: Int = 0,
    val poster: Poster? = null
)


@Serializable
data class EyePlayInfo(
    val height: Long = 0,
    val width: Long = 0,
    val name: String,
    val type: String,
    val url: String,
    val url_list: List<EyeUrlList>
)

@Serializable
data class EyeUrlList(
    val name: String,
    val url: String,
    val size: Int
)
@Serializable
data class EyeCommonVideoTag(
    @Serializable(with = NumberOrStringSerializer::class)
    val tag_id:String = "",
    val title:String = ""
)
@Serializable
data class EyeVideoAuthor(
    val uid: Long = 0,
    val nick: String = "",
    val description: String = "",
    val avatar: String ="",
    val followed: Boolean = false,
    val user_type: String = "",
    val status: String = "",
    val is_m: Boolean = false
)

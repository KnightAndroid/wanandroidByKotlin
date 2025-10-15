package com.knight.kotlin.module_eye_recommend.entity

import com.knight.kotlin.library_base.entity.EyeCommonAuthor
import com.knight.kotlin.library_base.entity.EyeCommonCover
import com.knight.kotlin.library_base.entity.EyeCommonDuration
import com.knight.kotlin.library_base.entity.EyeCommonTag
import com.knight.kotlin.library_base.entity.eye_type.EyeVideoCtrl
import kotlinx.serialization.Serializable


/**
 * @author created by luguian
 * @organize
 * @Date 2025/10/13 17:06
 * @descript:推荐大视频实体模型
 */
@Serializable
data class EyeRecommendVideoEntity(
    val video_id: String = "",
    val title: String = "",
    val duration: EyeCommonDuration? = null,
    val play_ctrl: EyeVideoCtrl? = null,
    val play_url: String = "",
    val preview_url:String = "",
    val tags: List<EyeCommonTag> = emptyList(),
    val cover: EyeCommonCover? = null,
    val author: EyeCommonAuthor,
    val resource_id: Long,
    val resource_type: String
)
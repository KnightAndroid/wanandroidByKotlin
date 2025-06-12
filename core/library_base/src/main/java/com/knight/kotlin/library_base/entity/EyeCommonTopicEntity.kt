package com.knight.kotlin.library_base.entity

import kotlinx.serialization.Serializable


/**
 * @author created by luguian
 * @organize
 * @Date 2025/1/24 11:14
 * @descript:
 */
@Serializable
data class EyeCommonTopicEntity (
val topic_id: String = "",
val title: String = "",
val description: String = "",
val tags: List<EyeCommonTag>? = null,
val cover: EyeCommonCover? = null,
val resource_type: String = "",
val resource_id: String = ""

)
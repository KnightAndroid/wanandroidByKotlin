package com.knight.kotlin.library_base.entity.eye_type

import com.knight.kotlin.library_base.entity.EyeAuthorEntity
import com.knight.kotlin.library_base.entity.EyeCommonAuthor
import com.knight.kotlin.library_base.entity.EyeCommonCover
import com.knight.kotlin.library_base.entity.EyeCover
import kotlinx.serialization.Serializable

/**
 * @Description
 * @Author knight
 * @Time 2025/2/11 22:35
 *
 */
@Serializable
class EyeWaterFallCoverImage (
    val image_id: Long = 0,
    val title: String = "",
    val recommend_level:String = "",
    val cover: EyeCommonCover? = null,
    val author: EyeCommonAuthor? = null,
    val liked: Boolean = false,
    val image_count: Long = 0,
    val resource_id: Long = 0,
    val resource_type:String = ""
)
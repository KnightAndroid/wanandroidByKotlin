package com.knight.kotlin.library_base.entity.eye_type

import com.knight.kotlin.library_base.entity.EyeCommonCover
import kotlinx.serialization.Serializable


/**
 * @author created by luguian
 * @organize
 * @Date 2025/2/11 16:06
 * @descript:开眼类型卡片
 */
@Serializable
data class EyeBannerImageList(
    val image_id: Long = 0,
    val cover: EyeCommonCover? = null,
    val footer: EyeRouterStyle? = null,
    val resource_type: String = "",
    val resource_id: Long = 0,
)


@Serializable
data class EyeRouterStyle(
    val left: EyeRouter? = null,
    val right: EyeRouter? = null
)

@Serializable
data class EyeRouter(
    val text: String = "",
    val link: String = ""
)



@Serializable
data class EyeBannerSquare(
    val itemlist: List<EyeBannerImageList>? = null
)

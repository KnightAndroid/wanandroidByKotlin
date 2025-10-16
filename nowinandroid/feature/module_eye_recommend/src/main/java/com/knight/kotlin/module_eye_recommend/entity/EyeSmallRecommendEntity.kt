package com.knight.kotlin.module_eye_recommend.entity

import com.core.library_base.ktx.NumberOrStringSerializer
import com.knight.kotlin.library_base.entity.EyeMetroCard
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonObject


/**
 * @author created by luguian
 * @organize
 * @Date 2025/10/16 16:10
 * @descript:小视频实体模型
 */

@Serializable
data class EyeSmallRecommendEntity(
    private val card_list:List<EyeMetroCard<JsonObject>>? = null,
    private val item_list:List<EyeMetroCard<JsonObject>>? = null,
    val item_count: Long = 0,
    @Serializable(with = NumberOrStringSerializer::class)
    val last_item_id: String = "",
) {
    val list: List<EyeMetroCard<JsonObject>>?
        get() = card_list ?: item_list
}
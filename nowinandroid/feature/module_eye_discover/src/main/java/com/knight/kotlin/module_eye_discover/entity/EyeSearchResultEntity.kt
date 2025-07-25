package com.knight.kotlin.module_eye_discover.entity

import com.knight.kotlin.library_base.entity.EyeCardEntity
import com.knight.kotlin.library_base.entity.EyeMetroCard
import com.core.library_base.ktx.NumberOrStringSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonObject

/**
 * @Description 搜索结果实体
 * @Author knight
 * @Time 2025/1/2 20:42
 *
 */

@Serializable
data class EyeSearchResultEntity (
    val item_list:List<EyeSearchResultItem>
)


@Serializable
data class EyeSearchResultItem(
    val nav:EyeSearchNav,
    val card_list:List<EyeCardEntity>


)



@Serializable
data class EyeSearchNav(
    val title:String = "",
    val type:String = ""
)


@Serializable
data class EyeSearchResultLoadEntity (
    val item_list: List<EyeMetroCard<JsonObject>>? = null,
    val item_count:Int,
    @Serializable(with = NumberOrStringSerializer::class)
    val last_item_id: String = "",
)

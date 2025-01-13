package com.knight.kotlin.module_eye_discover.entity

import com.knight.kotlin.library_base.entity.EyeCardEntity
import kotlinx.serialization.Serializable

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
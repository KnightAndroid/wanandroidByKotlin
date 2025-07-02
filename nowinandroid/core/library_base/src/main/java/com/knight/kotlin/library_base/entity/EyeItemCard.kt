package com.knight.kotlin.library_base.entity

import kotlinx.serialization.json.JsonObject

/**
 * @Description 开眼视频卡片枚举
 * @Author knight
 * @Time 2025/1/13 21:40
 *
 */

sealed class EyeItemCard {
   abstract val uniqueId : String
   abstract val tpl_label:String  //==> 这个type 从tpl—label转换
}


/**
 *
 * 头部卡片
 */
data class EyeHeaderItemCard(
    override val tpl_label:String = com.knight.kotlin.library_base.config.EyeCardType.HEADER,
    val left: EyeLayout
) : EyeItemCard() {
    override val uniqueId:String
        get() = toString()
}

/**
 *
 * 普通卡片
 */
data class EyeMetroItemCard(
    override val tpl_label: String,
    val data: EyeMetroCard<JsonObject>,
    val indes:Int,
) : EyeItemCard() {
    override val uniqueId:String
        get() = data.metro_unique_id
}

/**
 *
 * 垂直滑动卡片
 */
data class EyeSlideItemCard(
    override val tpl_label: String,
    val data:List<EyeMetroCard<JsonObject>>
) : EyeItemCard() {
    override val uniqueId:String
        get() = data[0].metro_unique_id
}


/**
 *
 * 底部卡片
 */
data class FooterItemCard(override val tpl_label: String = com.knight.kotlin.library_base.config.EyeCardType.FOOTER) : EyeItemCard() {
    override val uniqueId:String
        get() = toString()

}


package com.knight.kotlin.module_eye_discover.entity

import com.knight.kotlin.library_base.ktx.NumberOrStringSerializer
import com.knight.kotlin.library_base.ktx.json
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.serializer
import kotlin.reflect.KType
import kotlin.reflect.typeOf


/**
 * @author created by luguian
 * @organize
 * @Date 2024/12/20 9:36
 * @descript:推荐视频实体模型
 */
@Serializable
data class EyeDiscoverCardListEntity(
    private val card_list:List<EyeDiscoverCardEntity>? = null,
    private val item_list:List<EyeDiscoverCardEntity>? = null,
    val item_count: Long = 0,
    @Serializable(with = NumberOrStringSerializer::class)
    val last_item_id: String = "",
) {
    val list: List<EyeDiscoverCardEntity>?
        get() = card_list ?: item_list
}


@Serializable
data class EyeDiscoverCardEntity(
    val card_id: Long = 0,
    val type: String = "",
    val style: EyeDiscoverStyle? = null,
    val interaction: EyeDiscoverInteraction? = null,
    val card_data: EyeDiscoverCardDataEntity? = null,
    val card_unique_id: String = "",
)



@Serializable
data class EyeDiscoverCardDataEntity(
    val header: EyeDiscoverLayout? = null,
    var body: EyeDiscoverMetroList? = null,
    val footer: EyeDiscoverLayout? = null,
)


@Serializable
data class EyeDiscoverStyle(
    val tpl_label: String = "",
    val padding: EyeDiscoverParams? = null,
)



@Serializable
data class EyeDiscoverParams(
    val top: Double = 0.0,
    val right: Double = 0.0,
    val bottom: Double = 0.0,
    val left: Double = 0.0,
)



@Serializable
data class EyeDiscoverInteraction(
    val scroll: String = "",
)


@Serializable
data class EyeDiscoverLayout(
    val style: EyeDiscoverStyle? = null,
    val left: List<EyeDiscoverMetroCard<JsonObject>>? = null,
    val right: List<EyeDiscoverMetroCard<JsonObject>>? = null,
    val center: List<EyeDiscoverMetroCard<JsonObject>>? = null,
    val bottom: List<EyeDiscoverMetroCard<JsonObject>>? = null,
)


@Serializable
data class EyeDiscoverMetroList(
    val api_request: EyeDiscoverApiRequest? = null,
    val metro_list: List<EyeDiscoverMetroCard<JsonObject>>? = null,
)

@Serializable
data class EyeDiscoverMetroCard<T>(
    val metro_id: Long = 0,
    val type: String = "",
    val alias_name: String = "",
    val style: EyeDiscoverStyle? = null,
    val metro_unique_id: String = "",
    val metro_data: T,
    @Serializable(with = NumberOrStringSerializer::class)
    val link: String = ""
)
inline fun <reified T> EyeDiscoverMetroCard<JsonObject>.toMetroCard(): EyeDiscoverMetroCard<T> {
    return EyeDiscoverMetroCard(
        metro_id = metro_id,
        type = type,
        alias_name = alias_name,
        style = this.style,
        metro_unique_id = metro_unique_id,
        metro_data = json.decodeFromJsonElement(serializer(typeOf<T>()), metro_data) as T,
        link = this.link
    )
}
fun EyeDiscoverMetroCard<JsonObject>.toMetroCard(kType: KType): EyeDiscoverMetroCard<Any> {
    return EyeDiscoverMetroCard(
        metro_id = metro_id,
        type = type,
        alias_name = alias_name,
        style = this.style,
        metro_unique_id = metro_unique_id,
        metro_data = json.decodeFromJsonElement(serializer(kType), metro_data) as Any,
        link = this.link
    )
}


@Serializable
data class EyeDiscoverApiRequest(
    val url: String = "",
    val params: JsonObject? = null,
)





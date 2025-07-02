package com.knight.kotlin.library_base.entity

import com.core.library_base.ktx.NumberOrStringSerializer
import com.core.library_base.ktx.json
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
data class EyeCardListEntity(
    private val card_list:List<EyeCardEntity>? = null,
    private val item_list:List<EyeCardEntity>? = null,
    val item_count: Long = 0,
    @Serializable(with = NumberOrStringSerializer::class)
    val last_item_id: String = "",
) {
    val list: List<EyeCardEntity>?
        get() = card_list ?: item_list
}


@Serializable
data class EyeCardEntity(
    val card_id: Long = 0,
    val type: String = "",
    val style: EyeStyle? = null,
    val interaction: EyeInteraction? = null,
    val card_data: EyeCardDataEntity? = null,
    val card_unique_id: String = "",
)



@Serializable
data class EyeCardDataEntity(
    val header: EyeLayout? = null,
    var body: EyeMetroList? = null,
    val footer: EyeLayout? = null,
)


@Serializable
data class EyeStyle(
    val tpl_label: String = "",
    val padding: EyeParams? = null,
)



@Serializable
data class EyeParams(
    val top: Double = 0.0,
    val right: Double = 0.0,
    val bottom: Double = 0.0,
    val left: Double = 0.0,
)



@Serializable
data class EyeInteraction(
    val scroll: String = "",
)


@Serializable
data class EyeLayout(
    val style: EyeStyle? = null,
    val left: List<EyeMetroCard<JsonObject>>? = null,
    val right: List<EyeMetroCard<JsonObject>>? = null,
    val center: List<EyeMetroCard<JsonObject>>? = null,
    val bottom: List<EyeMetroCard<JsonObject>>? = null,
)


@Serializable
data class EyeMetroList(
    val api_request: EyeApiRequest? = null,
    val metro_list: List<EyeMetroCard<JsonObject>>? = null,
)

@Serializable
data class EyeMetroCard<T>(
    val metro_id: Long = 0,
    val type: String? = "",
    val alias_name: String? = "",
    val style: EyeStyle? = null,
    val metro_unique_id: String = "",
    val metro_data: T,
    @Serializable(with = NumberOrStringSerializer::class)
    val link: String? = ""
)
inline fun <reified T> EyeMetroCard<JsonObject>.toMetroCard(): EyeMetroCard<T> {
    return EyeMetroCard(
        metro_id = metro_id,
        type = type,
        alias_name = alias_name,
        style = this.style,
        metro_unique_id = metro_unique_id,
        metro_data = json.decodeFromJsonElement(serializer(typeOf<T>()), metro_data) as T,
        link = this.link
    )
}
fun EyeMetroCard<JsonObject>.toMetroCard(kType: KType): EyeMetroCard<Any> {
    return EyeMetroCard(
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
data class EyeApiRequest(
    val url: String = "",
    val params: JsonObject? = null,
)





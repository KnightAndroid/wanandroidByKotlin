package com.core.library_base.ktx

import com.core.library_base.util.GsonUtils
import kotlinx.serialization.KSerializer
import kotlinx.serialization.SerializationException
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonDecoder
import kotlinx.serialization.json.JsonPrimitive
import kotlinx.serialization.json.contentOrNull
import java.lang.reflect.Type

fun toJson(any: Any): String = GsonUtils.toJson(any)
inline fun <reified T> fromJson(json: String): T = GsonUtils.get(json,T::class.java)!!
fun <T> fromJson(json: String, typeOfT: Type): T? = GsonUtils.get<T>(json,typeOfT)

val json = Json {
    ignoreUnknownKeys = true
    coerceInputValues = true
    isLenient = true
}

/**
 *
 * 下面是@Serializable需要
 */
object NumberOrStringSerializer : KSerializer<String> {
    override val descriptor: SerialDescriptor =
        PrimitiveSerialDescriptor("NumberOrString", PrimitiveKind.STRING)

    override fun deserialize(decoder: Decoder): String {
        val jsonDecoder = decoder as? JsonDecoder
            ?: throw SerializationException("This class can be decoded only by Json")
        return when (val jsonElement = jsonDecoder.decodeJsonElement()) {
            is JsonPrimitive -> jsonElement.contentOrNull.toString()
            else -> throw IllegalArgumentException("Unexpected JSON type: ${jsonElement::class}")
        }
    }

    override fun serialize(encoder: Encoder, value: String) {
        encoder.encodeString(value)
    }
}

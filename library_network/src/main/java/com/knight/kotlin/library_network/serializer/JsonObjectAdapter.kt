package com.knight.kotlin.library_network.serializer


import com.google.gson.Gson
import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonNull
import com.google.gson.JsonSerializationContext
import com.google.gson.JsonSerializer
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.jsonObject
import java.lang.reflect.Type


/**
 * @author created by luguian
 * @organize
 * @Date 2024/12/20 14:28
 * @descript:兼容解析kotlinx.serialization
 */
// 自定义 Gson TypeAdapter 用于解析 kotlinx.serialization.json.JsonObject
class JsonObjectAdapter : JsonDeserializer<JsonObject>, JsonSerializer<JsonObject> {


    // 将 kotlinx.serialization.json.JsonObject 转换为 Gson 的 JsonObject
    override fun serialize(
        src: JsonObject?,
        typeOfSrc: Type?,
        context: JsonSerializationContext?
    ): com.google.gson.JsonElement {
        if (src == null) {
            return JsonNull.INSTANCE
        }
        val jsonString = Json.encodeToString(JsonObject.serializer(), src)
        // 替代 JsonParser，直接从字符串创建 Gson 的 JsonElement
        return Gson().fromJson(jsonString, com.google.gson.JsonElement::class.java)
    }

    override fun deserialize(json: com.google.gson.JsonElement?, typeOfT: Type?, context: JsonDeserializationContext?): JsonObject {
        if (json == null || !json.isJsonObject) {
            return JsonObject(emptyMap()) // 空对象
        }
        val gsonJsonObject = json.asJsonObject
        return Json.parseToJsonElement(gsonJsonObject.toString()).jsonObject
    }
}
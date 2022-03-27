package com.knight.kotlin.library_base.util

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.JsonArray
import com.google.gson.JsonParser
import com.google.gson.reflect.TypeToken
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import java.lang.reflect.Type


/**
 * Author:Knight
 * Time:2021/12/29 15:27
 * Description:GsonUtils
 */
object GsonUtils {
    /**
     * 转换Json对象到对象（非泛型，非List）
     * @param jsonObject
     * @param cls
     * @param <T>
     * @return
    </T> */
    operator fun <T> get(jsonObject: JSONObject, cls: Class<T>?): T? {
        return get(jsonObject.toString(), cls)
    }

    /**
     * 转换Json字符串到对象（非泛型，非List）
     * @param jsonString
     * @param cls
     * @param <T>
     * @return
    </T> */
    operator fun <T> get(jsonString: String?, cls: Class<T>?): T? {
        var t: T? = null
        try {
            val gson = Gson()
            t = gson.fromJson(jsonString, cls)
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return t
    }

    /**
     * 转换Json字符串到对象（非泛型，非List）
     * @param jsonString
     * @param cls
     * @param <T>
     * @return
    </T> */
    fun <T> getToSpecial(jsonString: String?, cls: Class<T>?): T? {
        var t: T? = null
        try {
            val gson = Gson()
            t = gson.fromJson(Gson().toJson(jsonString), cls)
        } catch (e: java.lang.Exception) {
            e.printStackTrace()
        }
        return t
    }

    /**
     * 转换Json字符串到对象 由调用者处理异常
     * @param jsonString
     * @param cls
     * @param <T>
     * @return
    </T> */
    @Throws(java.lang.Exception::class)
    operator fun <T> get(jsonString: String?, cls: Class<T>?, type: Int): T? {
        var t: T? = null
        val gson = Gson()
        t = gson.fromJson(jsonString, cls)
        return t
    }

    /**
     * 转换Json字符串到对象（非List）
     * 当转换的对象为复杂泛型类型时，可以使用此接口：
     * Type type = new TypeToken<ReturnData></ReturnData><Business>>(){}.getType();
     * @param jsonString
     * @param type
     * @param <T>
     * @return
    </T></Business> */
    operator fun <T> get(jsonString: String?, type: Type?): T? {
        var t: T? = null
        try {
            val gson = Gson()
            t = gson.fromJson(jsonString, type)
        } catch (e: java.lang.Exception) {
        }
        return t
    }

    /**
     * 转换JSONObject到对象（非List）
     * 当转换的对象为复杂泛型类型时，可以使用此接口：
     * Type type = new TypeToken<ReturnData></ReturnData><Business>>(){}.getType();
     * @param jsonObject
     * @param type
     * @param <T>
     * @return
    </T></Business> */
    operator fun <T> get(jsonObject: JSONObject, type: Type?): T? {
        return get(jsonObject.toString(), type)
    }

    /**
     * 转换Json字符串到列表(Json字符串内容必须是数组）
     * @param jsonString
     * @param type  Type type = new TypeToken<ArrayList></ArrayList><T>>() {}.getType();
     * @param <T>
     * @return
    </T></T> */
    fun <T> getList(jsonString: String?, type: Type?): MutableList<T> {
        var list: ArrayList<T> = ArrayList()
        try {
            //  list = JSON.parseObject(jsonString, type);

            //  Gson gson = GsonAdapter.buildGson();
            list = Gson().fromJson(jsonString, type)
        } catch (e: java.lang.Exception) {
            e.printStackTrace()
        }
        return list
    }

    fun <T> getObjectList(jsonString: String?, cls: Class<T>?): List<T>? {
        val list: MutableList<T> = ArrayList()
        try {
            val gson = Gson()
            val arry: JsonArray = JsonParser.parseString(jsonString).getAsJsonArray()
            for (jsonElement in arry) {
                list.add(gson.fromJson(jsonElement, cls))
            }
        } catch (e: java.lang.Exception) {
            e.printStackTrace()
        }
        return list
    }

    /**
     * 转换Json字符串到列表(Json字符串内容必须是数组）
     * @param jsonString
     * @return
     */
    fun getList(jsonString: String?): List<*>? {
        val typeToken: TypeToken<ArrayList<*>> = object : TypeToken<ArrayList<*>>() {}
        var list: List<*> = ArrayList<String>()
        try {
            val gson = Gson()
            list = gson.fromJson(jsonString, typeToken.getType())
        } catch (e: java.lang.Exception) {
        }
        return list
    }

    /**
     * 转换JSONArray到列表
     * @param array
     * @return
     */
    fun getList(array: JSONArray): List<*>? {
        return getList(array.toString())
    }

    /**
     * 对象转换为Json字符串
     * @param obj
     * @return
     */
    fun toJson(obj: Any?): String {
        val gson: Gson = GsonBuilder().disableHtmlEscaping().create()
        return gson.toJson(obj)
    }

    fun getString(jsonObject: JSONObject?, key: String?, defaultValue: String?): String? {
        if (jsonObject == null || key.isNullOrEmpty()) {
            return defaultValue
        }

        //add: 20160420
        return if (!jsonObject.has(key)) {
            defaultValue
        } else jsonObject.optString(key)
    }


    /**
     *
     *
     * 根据对应json key 值转为字符串
     * @param jsonObject
     * @param key
     * @param defaultValue
     * @return
     */
    fun jsonToString(jsonObject: String?, key: String?, defaultValue: String?): String? {
        try {
            return getString(JSONObject(jsonObject), key, defaultValue)
        } catch (e: JSONException) {
            e.printStackTrace()
        }
        return ""
    }


    /**
     *
     *
     * 根据json 解析出对应model
     * @param json
     * @param key
     * @param clazz
     * @param <T>
     * @return
    </T> */
    fun <T> jsonToModel(json: String?, key: String?, clazz: Class<T>?): T? {
        return get(jsonToString(json, key, ""), clazz)
    }


}
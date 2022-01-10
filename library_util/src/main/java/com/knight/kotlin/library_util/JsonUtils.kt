package com.knight.kotlin.library_util


import android.content.Context
import android.content.res.AssetManager
import com.knight.kotlin.library_util.StringUtils.isEmpty
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader


/**
 * Author:Knight
 * Time:2021/12/29 14:36
 * Description:JsonUtils
 * Json转换方法
 */
object JsonUtils {
    var isPrintException = true

    /**
     * get Long from jsonObject
     *
     * @param jsonObject
     * @param key
     * @param defaultValue
     * @return
     *  * if jsonObject is null, return defaultValue
     *  * if key is null or empty, return defaultValue
     *  * if [JSONObject.getLong] exception, return defaultValue
     *  * return [JSONObject.getLong]
     *
     */
    fun getLong(jsonObject: JSONObject?, key: String?, defaultValue: Long?): Long? {
        return if (jsonObject == null || StringUtils.isEmpty(key)) {
            defaultValue
        } else try {
            jsonObject.getLong(key)
        } catch (e: JSONException) {
            if (isPrintException) {
                e.printStackTrace()
            }
            defaultValue
        }
    }

    /**
     * get Long from jsonData
     *
     * @param jsonData
     * @param key
     * @param defaultValue
     * @return
     *  * if jsonObject is null, return defaultValue
     *  * if jsonData [JSONObject.JSONObject] exception, return defaultValue
     *  * return {JSONUtils#getLong(org.json.JSONObject, String, org.json.JSONObject)}
     *
     */
    fun getLong(jsonData: String?, key: String?, defaultValue: Long?): Long? {
        return if (isEmpty(jsonData)) {
            defaultValue
        } else try {
            val jsonObject = JSONObject(jsonData)
            getLong(jsonObject, key, defaultValue)
        } catch (e: JSONException) {
            if (isPrintException) {
                e.printStackTrace()
            }
            defaultValue
        }
    }

    /**
     * @param jsonObject
     * @param key
     * @param defaultValue
     * @return
     */
    fun getLong(jsonObject: JSONObject?, key: String?, defaultValue: Long): Long {
        return getLong(jsonObject, key, defaultValue)
    }

    /**
     * @param jsonData
     * @param key
     * @param defaultValue
     * @return
     */
    fun getLong(jsonData: String?, key: String?, defaultValue: Long): Long {
        return getLong(jsonData, key, defaultValue)
    }

    /**
     * get Int from jsonObject
     *
     * @param jsonObject
     * @param key
     * @param defaultValue
     * @return
     *  * if jsonObject is null, return defaultValue
     *  * if key is null or empty, return defaultValue
     *  * if [JSONObject.getInt] exception, return defaultValue
     *  * return [JSONObject.getInt]
     *
     */
    fun getInt(jsonObject: JSONObject?, key: String?, defaultValue: Int?): Int? {
        return if (jsonObject == null || isEmpty(key)) {
            defaultValue
        } else try {
            jsonObject.getInt(key)
        } catch (e: JSONException) {
            if (isPrintException) {
                e.printStackTrace()
            }
            defaultValue
        }
    }

    /**
     * get Int from jsonData
     *
     * @param jsonData
     * @param key
     * @param defaultValue
     * @return
     *  * if jsonObject is null, return defaultValue
     *  * if jsonData [JSONObject.JSONObject] exception, return defaultValue
     *
     */
    fun getInt(jsonData: String?, key: String?, defaultValue: Int?): Int? {
        return if (isEmpty(jsonData)) {
            defaultValue
        } else try {
            val jsonObject = JSONObject(jsonData)
            getInt(jsonObject, key, defaultValue)
        } catch (e: JSONException) {
            if (isPrintException) {
                e.printStackTrace()
            }
            defaultValue
        }
    }

    /**
     * @param jsonObject
     * @param key
     * @param defaultValue
     * @return
     */
    fun getInt(jsonObject: JSONObject?, key: String?, defaultValue: Int): Int {
        return getInt(jsonObject, key, defaultValue)
    }

    /**
     * @param jsonData
     * @param key
     * @param defaultValue
     * @return
     */
    fun getInt(jsonData: String?, key: String?, defaultValue: Int): Int {
        return getInt(jsonData, key, defaultValue)
    }

    /**
     * get Double from jsonObject
     *
     * @param jsonObject
     * @param key
     * @param defaultValue
     * @return
     *  * if jsonObject is null, return defaultValue
     *  * if key is null or empty, return defaultValue
     *  * if [JSONObject.getDouble] exception, return defaultValue
     *  * return [JSONObject.getDouble]
     *
     */
    fun getDouble(jsonObject: JSONObject?, key: String?, defaultValue: Double?): Double? {
        return if (jsonObject == null || isEmpty(key)) {
            defaultValue
        } else try {
            jsonObject.getDouble(key)
        } catch (e: JSONException) {
            if (isPrintException) {
                e.printStackTrace()
            }
            defaultValue
        }
    }

    /**
     * get Double from jsonData
     *
     * @param jsonData
     * @param key
     * @param defaultValue
     * @return
     *  * if jsonObject is null, return defaultValue
     *  * if jsonData [JSONObject.JSONObject] exception, return defaultValue
     *
     */
    fun getDouble(jsonData: String?, key: String?, defaultValue: Double?): Double? {
        return if (isEmpty(jsonData)) {
            defaultValue
        } else try {
            val jsonObject = JSONObject(jsonData)
            getDouble(jsonObject, key, defaultValue)
        } catch (e: JSONException) {
            if (isPrintException) {
                e.printStackTrace()
            }
            defaultValue
        }
    }

    /**
     * @param jsonObject
     * @param key
     * @param defaultValue
     * @return
     */
    fun getDouble(jsonObject: JSONObject?, key: String?, defaultValue: Double): Double {
        return getDouble(jsonObject, key, defaultValue)
    }

    /**
     * @param jsonData
     * @param key
     * @param defaultValue
     * @return
     */
    fun getDouble(jsonData: String?, key: String?, defaultValue: Double): Double {
        return getDouble(jsonData, key, defaultValue)
    }

    /**
     * get String from jsonObject
     *
     * @param jsonObject
     * @param key
     * @param defaultValue
     * @return
     *  * if jsonObject is null, return defaultValue
     *  * if key is null or empty, return defaultValue
     *  * if [JSONObject.getString] exception, return defaultValue
     *  * return [JSONObject.getString]
     *
     */
    fun getString(jsonObject: JSONObject?, key: String?, defaultValue: String?): String? {
        if (jsonObject == null || isEmpty(key)) {
            return defaultValue
        }

        //add: 20160420
        return if (!jsonObject.has(key)) {
            defaultValue
        } else jsonObject.optString(key)
    }

    /**
     * get String from jsonData
     *
     * @param jsonData
     * @param key
     * @param defaultValue
     * @return
     *  * if jsonObject is null, return defaultValue
     *  * if jsonData [JSONObject.JSONObject] exception, return defaultValue
     *
     */
    fun getString(jsonData: String?, key: String?, defaultValue: String?): String? {
        return if (isEmpty(jsonData)) {
            defaultValue
        } else try {
            val jsonObject = JSONObject(jsonData)
            getString(jsonObject, key, defaultValue)
        } catch (e: JSONException) {
            if (isPrintException) {
                e.printStackTrace()
            }
            defaultValue
        }
    }

    /**
     * get String from jsonObject
     *
     * @param jsonObject
     * @param defaultValue
     * @param keyArray
     * @return
     *  * if jsonObject is null, return defaultValue
     *  * if keyArray is null or empty, return defaultValue
     *  * get [.getJSONObject] by recursion, return it. if anyone is
     * null, return directly
     *
     */
    fun getStringCascade(
        jsonObject: JSONObject?,
        defaultValue: String?,
        vararg keyArray: String?
    ): String? {
        if (jsonObject == null || ArrayUtils.isEmpty(keyArray)) {
            return defaultValue
        }
        var data: String? = jsonObject.toString()
        for (key in keyArray) {
            data = getStringCascade(data, key, defaultValue)
            if (data == null) {
                return defaultValue
            }
        }
        return data
    }

    /**
     * get String from jsonData
     *
     * @param jsonData
     * @param defaultValue
     * @param keyArray
     * @return
     *  * if jsonData is null, return defaultValue
     *  * if keyArray is null or empty, return defaultValue
     *  * get [.getJSONObject] by recursion, return it. if anyone is
     * null, return directly
     *
     */
    fun getStringCascade(
        jsonData: String?,
        defaultValue: String?,
        vararg keyArray: String?
    ): String? {
        if (isEmpty(jsonData)) {
            return defaultValue
        }
        var data = jsonData
        for (key in keyArray) {
            data = getString(data, key, defaultValue)
            if (data == null) {
                return defaultValue
            }
        }
        return data
    }

    /**
     * get String array from jsonObject
     *
     * @param jsonObject
     * @param key
     * @param defaultValue
     * @return
     *  * if jsonObject is null, return defaultValue
     *  * if key is null or empty, return defaultValue
     *  * if [JSONObject.getJSONArray] exception, return defaultValue
     *  * if [JSONArray.getString] exception, return defaultValue
     *  * return string array
     *
     */
    fun getStringArray(
        jsonObject: JSONObject?,
        key: String?,
        defaultValue: Array<String?>?
    ): Array<String?>? {
        if (jsonObject == null || isEmpty(key)) {
            return defaultValue
        }
        try {
            val statusArray = jsonObject.getJSONArray(key)
            if (statusArray != null) {
                val value = arrayOfNulls<String>(statusArray.length())
                for (i in 0 until statusArray.length()) {
                    value[i] = statusArray.getString(i)
                }
                return value
            }
        } catch (e: JSONException) {
            if (isPrintException) {
                e.printStackTrace()
            }
            return defaultValue
        }
        return defaultValue
    }

    /**
     * get String array from jsonData
     *
     * @param jsonData
     * @param key
     * @param defaultValue
     * @return
     *  * if jsonObject is null, return defaultValue
     *  * if jsonData [JSONObject.JSONObject] exception, return defaultValue
     *
     */
    fun getStringArray(
        jsonData: String?,
        key: String?,
        defaultValue: Array<String?>?
    ): Array<String?>? {
        return if (isEmpty(jsonData)) {
            defaultValue
        } else try {
            val jsonObject = JSONObject(jsonData)
            getStringArray(jsonObject, key, defaultValue)
        } catch (e: JSONException) {
            if (isPrintException) {
                e.printStackTrace()
            }
            defaultValue
        }
    }

    /**
     * get String list from jsonObject
     *
     * @param jsonObject
     * @param key
     * @param defaultValue
     * @return
     *  * if jsonObject is null, return defaultValue
     *  * if key is null or empty, return defaultValue
     *  * if [JSONObject.getJSONArray] exception, return defaultValue
     *  * if [JSONArray.getString] exception, return defaultValue
     *  * return string array
     *
     */
    fun getStringList(
        jsonObject: JSONObject?,
        key: String?,
        defaultValue: List<String?>?
    ): List<String?>? {
        if (jsonObject == null || isEmpty(key)) {
            return defaultValue
        }
        try {
            val statusArray = jsonObject.getJSONArray(key)
            if (statusArray != null) {
                val list: MutableList<String?> = ArrayList()
                for (i in 0 until statusArray.length()) {
                    list.add(statusArray.getString(i))
                }
                return list
            }
        } catch (e: JSONException) {
            if (isPrintException) {
                e.printStackTrace()
            }
            return defaultValue
        }
        return defaultValue
    }

    /**
     * get String list from jsonData
     *
     * @param jsonData
     * @param key
     * @param defaultValue
     * @return
     *  * if jsonObject is null, return defaultValue
     *  * if jsonData [JSONObject.JSONObject] exception, return defaultValue
     *
     */
    fun getStringList(
        jsonData: String?,
        key: String?,
        defaultValue: List<String?>?
    ): List<String?>? {
        return if (isEmpty(jsonData)) {
            defaultValue
        } else try {
            val jsonObject = JSONObject(jsonData)
            getStringList(jsonObject, key, defaultValue)
        } catch (e: JSONException) {
            if (isPrintException) {
                e.printStackTrace()
            }
            defaultValue
        }
    }

    /**
     * get JSONObject from jsonObject
     *
     * @param jsonObject
     * @param key
     * @param defaultValue
     * @return
     *  * if jsonObject is null, return defaultValue
     *  * if key is null or empty, return defaultValue
     *  * if [JSONObject.getJSONObject] exception, return defaultValue
     *  * return [JSONObject.getJSONObject]
     *
     */
    fun getJSONObject(
        jsonObject: JSONObject?,
        key: String?,
        defaultValue: JSONObject?
    ): JSONObject? {
        return if (jsonObject == null || isEmpty(key)) {
            defaultValue
        } else try {
            jsonObject.getJSONObject(key)
        } catch (e: JSONException) {
            if (isPrintException) {
                e.printStackTrace()
            }
            defaultValue
        }
    }

    /**
     * get JSONObject from jsonData
     *
     * @param jsonData
     * @param key
     * @param defaultValue
     * @return
     *  * if jsonData is null, return defaultValue
     *  * if jsonData [JSONObject.JSONObject] exception, return defaultValue
     *
     */
    fun getJSONObject(jsonData: String?, key: String?, defaultValue: JSONObject?): JSONObject? {
        return if (isEmpty(jsonData)) {
            defaultValue
        } else try {
            val jsonObject = JSONObject(jsonData)
            getJSONObject(jsonObject, key, defaultValue)
        } catch (e: JSONException) {
            if (isPrintException) {
                e.printStackTrace()
            }
            defaultValue
        }
    }

    /**
     * get JSONObject from jsonObject
     *
     * @param jsonObject
     * @param defaultValue
     * @param keyArray
     * @return
     *  * if jsonObject is null, return defaultValue
     *  * if keyArray is null or empty, return defaultValue
     *  * get [.getJSONObject] by recursion, return it. if anyone is
     * null, return directly
     *
     */
    fun getJSONObjectCascade(
        jsonObject: JSONObject?,
        defaultValue: JSONObject?,
        vararg keyArray: String?
    ): JSONObject? {
        if (jsonObject == null || ArrayUtils.isEmpty(keyArray)) {
            return defaultValue
        }
        var js = jsonObject
        for (key in keyArray) {
            js = getJSONObject(js, key, defaultValue)
            if (js == null) {
                return defaultValue
            }
        }
        return js
    }

    /**
     * get JSONObject from jsonData
     *
     * @param jsonData
     * @param defaultValue
     * @param keyArray
     * @return
     *  * if jsonData is null, return defaultValue
     *  * if keyArray is null or empty, return defaultValue
     *  * get [.getJSONObject] by recursion, return it. if anyone is
     * null, return directly
     *
     */
    fun getJSONObjectCascade(
        jsonData: String?,
        defaultValue: JSONObject?,
        vararg keyArray: String?
    ): JSONObject? {
        return if (isEmpty(jsonData)) {
            defaultValue
        } else try {
            val jsonObject = JSONObject(jsonData)
            getJSONObjectCascade(jsonObject, defaultValue, *keyArray)
        } catch (e: JSONException) {
            if (isPrintException) {
                e.printStackTrace()
            }
            defaultValue
        }
    }

    /**
     * get JSONArray from jsonObject
     *
     * @param jsonObject
     * @param key
     * @param defaultValue
     * @return
     *  * if jsonObject is null, return defaultValue
     *  * if key is null or empty, return defaultValue
     *  * if [JSONObject.getJSONArray] exception, return defaultValue
     *  * return [JSONObject.getJSONArray]
     *
     */
    fun getJSONArray(jsonObject: JSONObject?, key: String?, defaultValue: JSONArray?): JSONArray? {
        return if (jsonObject == null || isEmpty(key)) {
            defaultValue
        } else try {
            jsonObject.getJSONArray(key)
        } catch (e: JSONException) {
            if (isPrintException) {
                e.printStackTrace()
            }
            defaultValue
        }
    }

    /**
     * get JSONArray from jsonData
     *
     * @param jsonData
     * @param key
     * @param defaultValue
     * @return
     *  * if jsonObject is null, return defaultValue
     *  * if jsonData [JSONObject.JSONObject] exception, return defaultValue
     *
     */
    fun getJSONArray(jsonData: String?, key: String?, defaultValue: JSONArray?): JSONArray? {
        return if (isEmpty(jsonData)) {
            defaultValue
        } else try {
            val jsonObject = JSONObject(jsonData)
            getJSONArray(jsonObject, key, defaultValue)
        } catch (e: JSONException) {
            if (isPrintException) {
                e.printStackTrace()
            }
            defaultValue
        }
    }

    /**
     * get Boolean from jsonObject
     *
     * @param jsonObject
     * @param key
     * @param defaultValue
     * @return
     *  * if jsonObject is null, return defaultValue
     *  * if key is null or empty, return defaultValue
     *  * return [JSONObject.getBoolean]
     *
     */
    fun getBoolean(jsonObject: JSONObject?, key: String?, defaultValue: Boolean): Boolean {
        if (jsonObject == null || isEmpty(key)) {
            return defaultValue
        }

        //add: 20160420
        return if (!jsonObject.has(key)) defaultValue else try {
            jsonObject.getBoolean(key)
        } catch (e: JSONException) {
            if (isPrintException) {
                e.printStackTrace()
            }
            defaultValue
        }
    }

    /**
     * get Boolean from jsonData
     *
     * @param jsonData
     * @param key
     * @param defaultValue
     * @return
     *  * if jsonObject is null, return defaultValue
     *  * if jsonData [JSONObject.JSONObject] exception, return defaultValue
     *
     */
    fun getBoolean(jsonData: String?, key: String?, defaultValue: Boolean): Boolean {
        return if (isEmpty(jsonData)) {
            defaultValue
        } else try {
            val jsonObject = JSONObject(jsonData)
            getBoolean(jsonObject, key, defaultValue)
        } catch (e: JSONException) {
            if (isPrintException) {
                e.printStackTrace()
            }
            defaultValue
        }
    }

    /**
     * get map from jsonObject.
     *
     * @param jsonObject key-value pairs json
     * @param key
     * @return
     *  * if jsonObject is null, return null
     *
     */
    fun getMap(jsonObject: JSONObject?, key: String?): Map<String?, String?>? {
        return parseKeyAndValueToMap(getString(jsonObject, key, null))
    }

    /**
     * get map from jsonData.
     *
     * @param jsonData key-value pairs string
     * @param key
     * @return
     *  * if jsonData is null, return null
     *  * if jsonData length is 0, return empty map
     *  * if jsonData [JSONObject.JSONObject] exception, return null
     *
     */
    fun getMap(jsonData: String?, key: String?): Map<String?, String?>? {
        if (jsonData == null) {
            return null
        }
        return if (jsonData.length == 0) {
            HashMap()
        } else try {
            val jsonObject = JSONObject(jsonData)
            getMap(jsonObject, key)
        } catch (e: JSONException) {
            if (isPrintException) {
                e.printStackTrace()
            }
            null
        }
    }

    /**
     * parse key-value pairs to map. ignore empty key, if getValue exception, put empty value
     *
     * @param sourceObj key-value pairs json
     * @return
     *  * if sourceObj is null, return null
     *
     */
    fun parseKeyAndValueToMap(sourceObj: JSONObject?): Map<String?, String?>? {
        if (sourceObj == null) {
            return null
        }
        val keyAndValueMap: MutableMap<String?, String?> = HashMap()
        val iter: Iterator<*> = sourceObj.keys()
        while (iter.hasNext()) {
            val key = iter.next() as String
            MapUtils.putMapNotEmptyKey(keyAndValueMap, key, getString(sourceObj, key, ""))
        }
        return keyAndValueMap
    }

    /**
     * parse key-value pairs to map. ignore empty key, if getValue exception, put empty value
     *
     * @param source key-value pairs json
     * @return
     *  * if source is null or source's length is 0, return empty map
     *  * if source [JSONObject.JSONObject] exception, return null
     *
     */
    fun parseKeyAndValueToMap(source: String?): Map<String?, String?>? {
        return if (isEmpty(source)) {
            null
        } else try {
            val jsonObject = JSONObject(source)
            parseKeyAndValueToMap(jsonObject)
        } catch (e: JSONException) {
            if (isPrintException) {
                e.printStackTrace()
            }
            null
        }
    }

    /**
     *
     *
     * 根据json文件读取json
     * @return
     */
    fun getJson(context: Context, fileName: String?): String? {
        val stringBuilder = StringBuilder()
        try {
            val assetManager: AssetManager = context.getAssets()
            val bf = BufferedReader(InputStreamReader(assetManager.open(fileName!!)))
            var line: String?
            while (bf.readLine().also { line = it } != null) {
                stringBuilder.append(line)
            }
        } catch (e: IOException) {
            e.printStackTrace()
        }
        return stringBuilder.toString()
    }


}
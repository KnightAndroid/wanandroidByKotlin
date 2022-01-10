package com.knight.kotlin.library_util

import com.knight.kotlin.library_util.ObjectUtils.isEquals
import com.knight.kotlin.library_util.StringUtils.isEmpty

/**
 * Author:Knight
 * Time:2021/12/29 14:53
 * Description:MapUtils
 */
object MapUtils {
    /** default separator between key and value  */
    const val DEFAULT_KEY_AND_VALUE_SEPARATOR = ":"

    /** default separator between key-value pairs  */
    const val DEFAULT_KEY_AND_VALUE_PAIR_SEPARATOR = ","

    /**
     * is null or its size is 0
     *
     * <pre>
     * isEmpty(null)   =   true;
     * isEmpty({})     =   true;
     * isEmpty({1, 2})    =   false;
    </pre> *
     *
     * @param sourceMap
     * @return if map is null or its size is 0, return true, else return false.
     */
    fun <K, V> isEmpty(sourceMap: Map<K, V>?): Boolean {
        return sourceMap == null || sourceMap.size == 0
    }

    /**
     * add key-value pair to map, and key need not null or empty
     *
     * @param map
     * @param key
     * @param value
     * @return
     *  * if map is null, return false
     *  * if key is null or empty, return false
     *  * return [Map.put]
     *
     */
    fun putMapNotEmptyKey(
        map: MutableMap<String?, String?>,
        key: String?,
        value: String?
    ): Boolean {
        if (map == null || isEmpty(key)) {
            return false
        }
        map[key] = value
        return true
    }

    /**
     * add key-value pair to map, both key and value need not null or empty
     *
     * @param map
     * @param key
     * @param value
     * @return
     *  * if map is null, return false
     *  * if key is null or empty, return false
     *  * if value is null or empty, return false
     *  * return [Map.put]
     *
     */
    fun putMapNotEmptyKeyAndValue(
        map: MutableMap<String?, String?>?,
        key: String?,
        value: String?
    ): Boolean {
        if (map == null || isEmpty(key) || isEmpty(value)) {
            return false
        }
        map[key] = value
        return true
    }

    /**
     * add key-value pair to map, key need not null or empty
     *
     * @param map
     * @param key
     * @param value
     * @param defaultValue
     * @return
     *  * if map is null, return false
     *  * if key is null or empty, return false
     *  * if value is null or empty, put defaultValue, return true
     *  * if value is neither null nor empty，put value, return true
     *
     */
    fun putMapNotEmptyKeyAndValue(
        map: MutableMap<String?, String?>?, key: String?, value: String?,
        defaultValue: String?
    ): Boolean {
        if (map == null || isEmpty(key)) {
            return false
        }
        map[key] = if (isEmpty(value)) defaultValue else value
        return true
    }

    /**
     * add key-value pair to map, key need not null
     *
     * @param map
     * @param key
     * @param value
     * @return
     *  * if map is null, return false
     *  * if key is null, return false
     *  * return [Map.put]
     *
     */
    fun <K, V> putMapNotNullKey(map: MutableMap<K, V>?, key: K?, value: V): Boolean {
        if (map == null || key == null) {
            return false
        }
        map[key] = value
        return true
    }

    /**
     * add key-value pair to map, both key and value need not null
     *
     * @param map
     * @param key
     * @param value
     * @return
     *  * if map is null, return false
     *  * if key is null, return false
     *  * if value is null, return false
     *  * return [Map.put]
     *
     */
    fun <K, V> putMapNotNullKeyAndValue(map: MutableMap<K, V>?, key: K?, value: V?): Boolean {
        if (map == null || key == null || value == null) {
            return false
        }
        map[key] = value
        return true
    }

    /**
     * get key by value, match the first entry front to back
     *
     * **Attentions:**
     *  * for HashMap, the order of entry not same to put order, so you may need to use TreeMap
     *
     *
     * @param <V>
     * @param map
     * @param value
     * @return
     *  * if map is null, return null
     *  * if value exist, return key
     *  * return null
     *
    </V> */
    fun <K, V> getKeyByValue(map: Map<K, V>, value: V): K? {
        if (isEmpty(map)) {
            return null
        }
        for ((key, value1) in map) {
            if (isEquals(value1, value)) {
                return key
            }
        }
        return null
    }

    /**
     * parse key-value pairs to map, ignore empty key
     *
     * <pre>
     * parseKeyAndValueToMap("","","",true)=null
     * parseKeyAndValueToMap(null,"","",true)=null
     * parseKeyAndValueToMap("a:b,:","","",true)={(a,b)}
     * parseKeyAndValueToMap("a:b,:d","","",true)={(a,b)}
     * parseKeyAndValueToMap("a:b,c:d","","",true)={(a,b),(c,d)}
     * parseKeyAndValueToMap("a=b, c = d","=",",",true)={(a,b),(c,d)}
     * parseKeyAndValueToMap("a=b, c = d","=",",",false)={(a, b),( c , d)}
     * parseKeyAndValueToMap("a=b, c=d","=", ",", false)={(a,b),( c,d)}
     * parseKeyAndValueToMap("a=b; c=d","=", ";", false)={(a,b),( c,d)}
     * parseKeyAndValueToMap("a=b, c=d", ",", ";", false)={(a=b, c=d)}
    </pre> *
     *
     * @param source key-value pairs
     * @param keyAndValueSeparator separator between key and value
     * @param keyAndValuePairSeparator separator between key-value pairs
     * @param ignoreSpace whether ignore space at the begging or end of key and value
     * @return
     */
    fun parseKeyAndValueToMap(
        source: String, keyAndValueSeparator: String?,
        keyAndValuePairSeparator: String?, ignoreSpace: Boolean
    ): Map<String?, String?>? {
        var keyAndValueSeparator = keyAndValueSeparator
        var keyAndValuePairSeparator = keyAndValuePairSeparator
        if (isEmpty(source)) {
            return null
        }
        if (isEmpty(keyAndValueSeparator)) {
            keyAndValueSeparator = DEFAULT_KEY_AND_VALUE_SEPARATOR
        }
        if (isEmpty(keyAndValuePairSeparator)) {
            keyAndValuePairSeparator = DEFAULT_KEY_AND_VALUE_PAIR_SEPARATOR
        }
        val keyAndValueMap: MutableMap<String?, String?> = HashMap()
        val keyAndValueArray = source.split(keyAndValuePairSeparator!!).toTypedArray()
            ?: return null
        var seperator: Int
        for (valueEntity in keyAndValueArray) {
            if (!isEmpty(valueEntity)) {
                seperator = valueEntity.indexOf(keyAndValueSeparator!!)
                if (seperator != -1) {
                    if (ignoreSpace) {
                        putMapNotEmptyKey(keyAndValueMap,
                            valueEntity.substring(0, seperator).trim { it <= ' ' },
                            valueEntity.substring(seperator + 1).trim { it <= ' ' })
                    } else {
                        putMapNotEmptyKey(
                            keyAndValueMap, valueEntity.substring(0, seperator),
                            valueEntity.substring(seperator + 1)
                        )
                    }
                }
            }
        }
        return keyAndValueMap
    }

    /**
     * parse key-value pairs to map, ignore empty key
     *
     * @param source key-value pairs
     * @param ignoreSpace whether ignore space at the begging or end of key and value
     * @return
     * [.DEFAULT_KEY_AND_VALUE_SEPARATOR], keyAndValuePairSeparator is
     * [.DEFAULT_KEY_AND_VALUE_PAIR_SEPARATOR]
     */
    fun parseKeyAndValueToMap(source: String, ignoreSpace: Boolean): Map<String?, String?>? {
        return parseKeyAndValueToMap(
            source, DEFAULT_KEY_AND_VALUE_SEPARATOR, DEFAULT_KEY_AND_VALUE_PAIR_SEPARATOR,
            ignoreSpace
        )
    }

    /**
     * parse key-value pairs to map, ignore empty key, ignore space at the begging or end of key and value
     *
     * @param source key-value pairs
     * @return
     * [.DEFAULT_KEY_AND_VALUE_SEPARATOR], keyAndValuePairSeparator is
     * [.DEFAULT_KEY_AND_VALUE_PAIR_SEPARATOR], ignoreSpace is true
     */
    fun parseKeyAndValueToMap(source: String): Map<String?, String?>? {
        return parseKeyAndValueToMap(
            source, DEFAULT_KEY_AND_VALUE_SEPARATOR, DEFAULT_KEY_AND_VALUE_PAIR_SEPARATOR,
            true
        )
    }

    /**
     * join map
     *
     * @param map
     * @return
     */
    fun toJson(map: Map<String?, String?>?): String? {
        if (map == null || map.size == 0) {
            return null
        }
        val paras = StringBuilder()
        paras.append("{")
        val ite = map.entries.iterator()
        while (ite.hasNext()) {
            val (key, value) = ite.next()
            paras.append("\"").append(key).append("\":\"").append(value).append("\"")
            if (ite.hasNext()) {
                paras.append(",")
            }
        }
        paras.append("}")
        return paras.toString()
    }


}
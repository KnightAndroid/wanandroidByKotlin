package com.knight.kotlin.library_network.log.converter

import java.lang.reflect.Type

/**
 * Author:Knight
 * Time:2021/12/22 10:54
 * Description:Converter
 */
interface Converter {

    /**
     * 将字符串转换成type类型的对象
     * @param json
     * @param type
     * @param <T>
     * @return
     */
    fun <T> fromJson(json: String, type: Type): T

    /**
     * 将对象序列化成字符串对象
     * @param data
     * @return
     */
    fun toJson(data: Any): String
}
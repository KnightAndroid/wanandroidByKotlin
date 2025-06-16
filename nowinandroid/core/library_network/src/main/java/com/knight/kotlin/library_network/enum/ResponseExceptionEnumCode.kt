package com.knight.kotlin.library_network.enum

/**
 * Author:Knight
 * Time:2021/12/15 14:19
 * Description:请求响应异常枚举的抽象
 */
interface ResponseExceptionEnumCode {

    /**
     * 获取该异常枚举的code码
     * @return Int
     */
    fun getCode():Int

    /**
     * 获取该异常枚举的描述
     * @return String
     */
    fun getMessage():String
}
package com.knight.kotlin.library_network.log.parser

import com.knight.kotlin.library_network.log.formatter.Formatter

/**
 * Author:Knight
 * Time:2021/12/22 10:50
 * Description:Parser
 */
interface Parser<T> {

    /**
     * 将对象解析成字符串
     */
    fun parseString(t: T,formatter: Formatter): String
}
package com.knight.kotlin.library_network.log.formatter

/**
 * Author:Knight
 * Time:2021/12/22 10:49
 * Description:Formatter
 */
interface Formatter {

    fun top():String

    fun middle():String

    fun bottom():String

    fun spliter():String
}
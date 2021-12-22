package com.knight.kotlin.library_network.log.formatter

import com.knight.kotlin.library_network.log.LoggerPrinter

/**
 * Author:Knight
 * Time:2021/12/22 10:49
 * Description:SimpleFormatter
 */
object SimpleFormatter:Formatter {

    override fun top()    = LoggerPrinter.BR

    override fun middle() = LoggerPrinter.COMMA + LoggerPrinter.BLANK

    override fun bottom() = LoggerPrinter.BR

    override fun spliter()= ""
}
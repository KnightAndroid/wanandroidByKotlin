package com.knight.kotlin.library_network.log.formatter

import com.knight.kotlin.library_network.log.LoggerPrinter


/**
 * Author:Knight
 * Time:2021/12/22 10:47
 * Description:BorderFormatter
 */
object BorderFormatter: Formatter {

    override fun top()    = LoggerPrinter.BR + LoggerPrinter.TOP_BORDER + LoggerPrinter.BR + LoggerPrinter.HORIZONTAL_DOUBLE_LINE

    override fun middle() = LoggerPrinter.BR + LoggerPrinter.MIDDLE_BORDER + LoggerPrinter.BR

    override fun bottom() = LoggerPrinter.BR + LoggerPrinter.BOTTOM_BORDER + LoggerPrinter.BR

    override fun spliter()= LoggerPrinter.HORIZONTAL_DOUBLE_LINE
}
package com.knight.kotlin.library_network.log.extension

import com.knight.kotlin.library_network.log.LoggerPrinter
import org.json.JSONArray

/**
 * Author:Knight
 * Time:2021/12/22 10:56
 * Description:JSONArray+Extension
 */
fun JSONArray.formatJSON(): String = this.toString(LoggerPrinter.JSON_INDENT)
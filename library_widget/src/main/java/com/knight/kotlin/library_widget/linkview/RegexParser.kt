package com.knight.kotlin.library_widget.linkview

import android.util.Patterns

/**
 * Author:Knight
 * Time:2024/3/18 15:42
 * Description:RegexParser
 */
internal object RegexParser {

    @JvmField
    val PHONE_PATTERN = Patterns.PHONE.pattern()
    @JvmField
    val EMAIL_PATTERN = Patterns.EMAIL_ADDRESS.pattern()

    //at、话题、链接匹配表达式
    const val MENTION_PATTERN = "@.{1,15}?\\s"
    const val HASHTAG_PATTERN = "#.{1,15}?\\s"
    const val URL_PATTERN = "(http|https|ftp|svn)://([a-zA-Z0-9]+[/?.?])" +
            "+[a-zA-Z0-9]*\\??([a-zA-Z0-9]*=[a-zA-Z0-9]*&?)*"
}
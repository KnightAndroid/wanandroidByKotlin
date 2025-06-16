package com.knight.kotlin.library_util


/**
 * @author created by luguian
 * @organize
 * @Date 2024/8/8 16:50
 * @descript:链接工具类
 */


/**
 * 根据链接截取http
 */
fun String.splitUrl() : List<String> {
    return if (this.contains("https")) {
        this.split("https")
    } else {
        this.split("http")
    }
}

/**
 * 传入指定字符串进行切割
 */
fun String.splitString(value:String) : List<String> {
    return this.split(value)
}

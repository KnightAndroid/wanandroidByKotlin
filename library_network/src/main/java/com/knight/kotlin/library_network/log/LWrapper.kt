package com.knight.kotlin.library_network.log

import com.knight.kotlin.library_network.log.converter.Converter

/**
 * Author:Knight
 * Time:2021/12/22 11:18
 * Description:LWrapper
 */
class LWrapper {

    var logLevel:LogLevel          = LogLevel.DEBUG

    var header:String?             = null

    var tag:String                 = "SAF_L"

    var converter: Converter?      = null

    var displayThreadInfo:Boolean  = true

    var displayClassInfo:Boolean   = true
}

fun configL(init: LWrapper.() -> Unit):L {

    val wrap = LWrapper()

    wrap.init()

    return configWrap(wrap)
}

internal fun configWrap(wrap:LWrapper):L {

    L.logLevel = wrap.logLevel

    wrap.header?.let {
        L.header(it)
    }

    wrap.tag?.let {
        L.init(it)
    }

    wrap.converter?.let {
        L.converter(it)
    }

    L.displayClassInfo(wrap.displayClassInfo)

    L.displayThreadInfo(wrap.displayThreadInfo)

    return L
}
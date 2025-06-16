package com.knight.kotlin.library_network.util

import okhttp3.Call
import java.util.*

/**
 * Author:Knight
 * Time:2021/12/21 17:23
 * Description:CallFactoryProxy
 */
abstract class CallFactoryProxy(delegate: Call.Factory) : Call.Factory {
    protected val delegate: Call.Factory

    init {
        Objects.requireNonNull(delegate, "delegate==null")
        this.delegate = delegate
    }
}
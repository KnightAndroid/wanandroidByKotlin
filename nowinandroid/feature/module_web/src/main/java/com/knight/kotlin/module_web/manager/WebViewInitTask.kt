package com.knight.kotlin.module_web.manager

import android.app.Application
import com.knight.kotlin.module_web.interceptor.WebViewInterceptRequestProxy

/**
 * @Author: leavesCZY
 * @Date: 2021/9/20 23:47
 * @Desc:
 * @Github：https://github.com/leavesCZY
 */
object WebViewInitTask {

    fun init(application: Application) {
        WebViewCacheHolder.init(application)
        WebViewInterceptRequestProxy.init(application)
    }

}
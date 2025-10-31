package com.peakmain.webview.manager.cache.interfaces

import com.knight.kotlin.module_web.bean.cache.CacheRequest
import com.knight.kotlin.module_web.bean.cache.WebResource


/**
 * author ：knight
 * createTime：2024/3/4
 * mail:15015706912@163.com
 * describe：
 */
interface ICacheInterceptor {
    //每一层需要处理的逻辑
    fun cacheInterceptor(chain:Chain): WebResource?
    interface Chain {
        //获取上一层请求的请求体
        fun request(): CacheRequest

        //交给下层处理
        fun process(request: CacheRequest): WebResource?
    }
}

package com.knight.kotlin.module_web

import com.knight.kotlin.module_web.cache.WebResource


/**
 * @author created by luguian
 * @organize
 * @Date 2025/11/7 9:53
 * @descript:
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
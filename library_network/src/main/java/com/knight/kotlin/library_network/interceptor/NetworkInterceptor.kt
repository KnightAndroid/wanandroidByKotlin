package com.knight.kotlin.library_network.interceptor

import okhttp3.CacheControl
import okhttp3.Interceptor
import okhttp3.Response
import java.util.concurrent.TimeUnit


/**
 * Author:Knight
 * Time:2022/4/22 16:18
 * Description:NetworkInterceptor
 */
class NetworkInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val response = chain.proceed(chain.request())
        // 缓存 10 秒
        val cacheControl: CacheControl = CacheControl.Builder()
            .maxAge(10, TimeUnit.SECONDS)
            .build()

        return response.newBuilder()
            .removeHeader("Pragma")
            .header("Cache-Control", cacheControl.toString())
            .build()
    }
}
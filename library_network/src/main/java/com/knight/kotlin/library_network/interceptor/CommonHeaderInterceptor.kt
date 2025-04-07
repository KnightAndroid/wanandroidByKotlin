package com.knight.kotlin.library_network.interceptor

import com.knight.kotlin.library_base.config.Appconfig
import com.knight.kotlin.library_base.util.CacheUtils
import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response


/**
 * @author created by luguian
 * @organize
 * @Date 2025/4/7 16:52
 * @descript:添加公用头部拦截器
 */
class CommonHeaderInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
//        val response = chain.proceed(chain.request())
//        // 缓存 10 秒
//        val cacheControl: CacheControl = CacheControl.Builder()
//            .maxAge(10, TimeUnit.SECONDS)
//            .build()
//
//        return response.newBuilder()
//            .removeHeader("Pragma")
//            .header("Cache-Control", cacheControl.toString())
//            .build()


        // 获取原始请求
        val originalRequest: Request = chain.request()
        if (Appconfig.IP == "0.0.0.0") {
            Appconfig.IP = CacheUtils.getIp()
        }
        // 构造一个带有 IP 的请求
        val requestWithIp: Request = originalRequest.newBuilder()
            .addHeader("ip", Appconfig.IP) // 添加自定义头部
            .build()

        return chain.proceed(requestWithIp)
    }

}
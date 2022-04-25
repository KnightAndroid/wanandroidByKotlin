package com.knight.kotlin.library_network.interceptor

import com.knight.kotlin.library_base.BaseApp
import com.knight.kotlin.library_base.util.NetworkStateUtils
import okhttp3.CacheControl
import okhttp3.Interceptor
import okhttp3.Response

/**
 * Author:Knight
 * Time:2022/4/21 11:25
 * Description:CacheInterceptor
 */
class CacheInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
//        //获取request
//        var request = chain.request()
//        if (!NetworkStateUtils.hasNetworkCapability(BaseApp.context)) {
//            //无网络时从缓存读取
//            request = request.newBuilder().cacheControl(CacheControl.FORCE_CACHE).build()
//        }
//        val response = chain.proceed(request)
//
//        if (NetworkStateUtils.hasNetworkCapability(BaseApp.context)) {
//            val maxTime = 180
//            // 有网络时 设置缓存超时时间0个小时 ,意思就是不读取缓存数据,只对get有用,post没有缓冲
//            val cacheControl = request.cacheControl.toString()
//            response.newBuilder()
//                .header("Cache-Control", "public, max-age=$maxTime")
//                .removeHeader("Pragma")
//                .build()
//        } else {
//            //无网络连接时 设置超时为4周  只对get有用,post没有缓冲
//            val maxStale = 60 * 60 * 24 * 28
//            response.newBuilder()
//                .header("Cache-Control", "public, only-if-cached, max-stale=$maxStale")
//                .removeHeader("Pragma")
//                .build()
//        }
//        return response

        var request = chain.request()
        if (!NetworkStateUtils.hasNetworkCapability(BaseApp.context)) {
            // 无网络时强制从缓存读取，如果缓存中没有数据会返回 504
            // FORCE_CACHE 里设置了 only-if-cached==true 和 maxStale
            // only-if-cached 顾名思义就是只有缓存中有数据才返回，没有时返回 504
            // maxStale 指可以取过期多久的数据，FORCE_CACHE 中设置为了Int最大值
            request = request.newBuilder()
                .cacheControl(CacheControl.FORCE_CACHE)
                .build()
        } else {
            // 有网络时从网络获取
            // FORCE_NETWORK 里设置了 no-cache
            // no-cache 顾名思义，强制客户端直接向服务器发送请求,也就是说每次请求都必须向服务器发送。服务器接收到请求，然后判断资源是否变更，是则返回新内容，否则返回304(Not Modified)。
            // 注意服务器根据 Etag 判断资源未变更时返回的是 304，这时还是从缓存中拿，这里有点违背 no-cache 的含义，要注意理解。
            request = request.newBuilder()
                .cacheControl(CacheControl.FORCE_NETWORK)
                .build();
        }
        return chain.proceed(request);

    }
}
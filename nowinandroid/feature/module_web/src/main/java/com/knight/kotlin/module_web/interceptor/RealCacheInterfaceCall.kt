package com.knight.kotlin.module_web.interceptor

import android.content.Context
import com.knight.kotlin.module_web.cache.CacheRequest
import com.knight.kotlin.module_web.interceptor.RealCacheInterfaceChain
import com.knight.kotlin.module_web.cache.WebResource
import com.knight.kotlin.module_web.interceptor.impl.ICacheInterceptor
import com.knight.kotlin.module_web.interceptor.impl.ICall

/**
 * @author created by luguian
 * @organize
 * @Date 2025/11/7 10:49
 * @descript:管理拦截器
 */
class RealCacheInterfaceCall(private val context: Context?, private val cacheRequest: CacheRequest) : ICall {
    override fun call(): WebResource? {
        val cacheInterceptorList = ArrayList<ICacheInterceptor>()
        cacheInterceptorList.add(MemoryCacheIntercept())
        cacheInterceptorList.add(DiskCacheInterceptor(context))
        cacheInterceptorList.add(NetworkCacheInterceptor(context))
        val realCacheInterfaceChain = RealCacheInterfaceChain(cacheInterceptorList, 0, cacheRequest)
        return realCacheInterfaceChain.process(cacheRequest)
    }

}
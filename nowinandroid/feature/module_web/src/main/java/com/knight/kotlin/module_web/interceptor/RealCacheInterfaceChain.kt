package com.knight.kotlin.module_web.interceptor

import com.knight.kotlin.module_web.cache.CacheRequest
import com.knight.kotlin.module_web.cache.WebResource
import com.knight.kotlin.module_web.interceptor.impl.ICacheInterceptor

/**
 * @author created by luguian
 * @organize
 * @Date 2025/11/7 14:41
 * @descript:
 */
class RealCacheInterfaceChain(
    private val cacheInterfaceList: MutableList<ICacheInterceptor>,//所有的拦截器
    private val index: Int,//当前拦截器的index
    val request: CacheRequest,//请求参数
) : ICacheInterceptor.Chain {
    override fun request(): CacheRequest {
        return request
    }

    override fun process(request: CacheRequest): WebResource? {
        //所有拦截器的中转地
        if (index >= cacheInterfaceList.size) throw AssertionError()
        //获取到当前的拦截器
        val currentCacheInterface = cacheInterfaceList[index]
        //获取下个拦截器
        val nextCacheInterface =
            RealCacheInterfaceChain(cacheInterfaceList, index + 1, request)
        //调用当前拦截器的process，并将下一个拦截器传给当前拦截器
        return currentCacheInterface.cacheInterceptor(nextCacheInterface)
    }

}
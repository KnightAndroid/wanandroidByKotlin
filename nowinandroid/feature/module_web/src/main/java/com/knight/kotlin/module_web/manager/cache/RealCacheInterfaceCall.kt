package com.peakmain.webview.manager.cache

import android.content.Context
import com.knight.kotlin.module_web.bean.cache.CacheRequest
import com.knight.kotlin.module_web.bean.cache.WebResource
import com.peakmain.webview.manager.cache.implements.DiskCacheInterceptor
import com.peakmain.webview.manager.cache.implements.MemoryCacheIntercept
import com.peakmain.webview.manager.cache.implements.NetworkCacheInterceptor
import com.peakmain.webview.manager.cache.interfaces.ICacheInterceptor
import com.peakmain.webview.manager.cache.interfaces.ICall

/**
 * author ：Peakmain
 * createTime：2024/3/4
 * mail:2726449200@qq.com
 * describe：管理拦截器
 */
class RealCacheInterfaceCall(private val context: Context?,private val cacheRequest: CacheRequest) : ICall {
    override fun call(): WebResource? {
        val cacheInterceptorList = ArrayList<ICacheInterceptor>()
        cacheInterceptorList.add(MemoryCacheIntercept())
        cacheInterceptorList.add(DiskCacheInterceptor(context))
        cacheInterceptorList.add(NetworkCacheInterceptor(context))
        val realCacheInterfaceChain = RealCacheInterfaceChain(cacheInterceptorList, 0, cacheRequest)
        return realCacheInterfaceChain.process(cacheRequest)
    }

}
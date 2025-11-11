package com.knight.kotlin.module_web.interceptor

import android.util.LruCache
import com.knight.kotlin.module_web.interceptor.impl.ICacheInterceptor
import com.knight.kotlin.module_web.cache.MemorySizeCalculator
import com.knight.kotlin.module_web.cache.WebResource


/**
 * @author created by luguian
 * @organize
 * @Date 2025/11/7 9:49
 * @descript:内存拦截器
 */
class MemoryCacheIntercept : ICacheInterceptor {
    private var mLruCache: LruCache<String, WebResource>? = null

    init {
        val size = MemorySizeCalculator.Companion.instance.getSize()
        if (size > 0) {
            mLruCache = ResourceMemoryCache(size)
        }
    }

    override fun cacheInterceptor(chain: ICacheInterceptor.Chain): WebResource? {
        val request = chain.request()


        mLruCache?.let {
            val resource = it.get(request.key)
            if (checkResourceValid(resource)) {

                return resource
            }
        }
        val resource = chain.process(request)
        //内存缓存资源
        if (mLruCache != null && checkResourceValid(resource) && resource?.isCacheable() == true){

            mLruCache?.put(request.key, resource)
        }
        return resource
    }

    private fun checkResourceValid(resource: WebResource?): Boolean {
        if (resource == null) return false
        return resource.originBytes != null && resource.originBytes!!.isNotEmpty()
                && resource.responseHeaders != null
                && resource.responseHeaders!!.isNotEmpty()
    }

    class ResourceMemoryCache constructor(maxSize: Int) : LruCache<String, WebResource>(maxSize) {
        override fun sizeOf(key: String?, value: WebResource?): Int {
            return value?.originBytes?.size ?: 0
        }
    }

}
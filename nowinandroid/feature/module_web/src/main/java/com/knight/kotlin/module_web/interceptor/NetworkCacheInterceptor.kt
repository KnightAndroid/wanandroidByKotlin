package com.knight.kotlin.module_web.interceptor

import android.content.Context
import com.knight.kotlin.module_web.interceptor.impl.ICacheInterceptor
import com.knight.kotlin.module_web.manager.InterceptRequestManager
import com.knight.kotlin.module_web.manager.OKHttpManager
import com.knight.kotlin.module_web.cache.WebResource
import com.knight.kotlin.module_web.utils.WebViewUtils


/**
 * @author created by luguian
 * @organize
 * @Date 2025/11/7 11:14
 * @descript:网络缓存拦截器
 */
class NetworkCacheInterceptor(val context: Context?) : ICacheInterceptor {
    override fun cacheInterceptor(chain: ICacheInterceptor.Chain): WebResource? {
        val request = chain.request()

        val mimeType = request.mimeType
        val isCacheContentType = WebViewUtils.instance.isCacheContentType(mimeType ?: "")
        return context?.let {
            if (WebViewUtils.instance.isImageType(request.mimeType ?:"")) {
                InterceptRequestManager.instance.loadImage(context,request)
            } else{
                OKHttpManager(it).getResource(request,isCacheContentType)
            }

        }
    }
}
package com.knight.kotlin.module_web

import android.app.Application
import android.content.Context
import android.webkit.WebResourceResponse
import com.knight.kotlin.module_web.cache.WebResource


/**
 * @author created by luguian
 * @organize
 * @Date 2025/11/7 14:12
 * @descript:
 */
class InterceptRequestManager private constructor() {
    private lateinit var mApplication: Application



    fun loadImage(context:Context, request: CacheRequest): WebResource? {
        val url = request.url
        try {
//            // 使用 Glide 加载图片
//            val bitmap = ImageLoader.loadUrlByBitmap(context,url)
//            // 创建 WebResource 对象并设置数据
//            val remoteResource = WebResource()
//            val outputStream = ByteArrayOutputStream()
//            bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream)
//            remoteResource.originBytes = outputStream.toByteArray()
//            remoteResource.responseCode = 200
//            remoteResource.message = "OK"
//            remoteResource.isModified = true // 如果需要根据实际情况设置是否修改了
//
//            // 设置响应头
//            val headersMap = HashMap<String, String>()
//            // 添加你需要的响应头信息
//            remoteResource.responseHeaders = headersMap

 //           return remoteResource
        } catch (e: Exception) {
            e.printStackTrace()
        }

        return null
    }

    companion object {
        @JvmStatic
        val instance by lazy(LazyThreadSafetyMode.SYNCHRONIZED) {
            InterceptRequestManager()
        }
    }

    fun getLocalWebResourceResponse(fileName: String, mimeType: String): WebResourceResponse? {
        val inputStream = mApplication.assets.open(fileName)
        return WebResourceResponse(mimeType, "utf-8", inputStream)
    }

    fun init(application: Application) {
        this.mApplication = application
    }
}
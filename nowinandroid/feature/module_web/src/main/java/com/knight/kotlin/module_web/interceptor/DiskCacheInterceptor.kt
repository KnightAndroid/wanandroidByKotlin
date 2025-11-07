package com.knight.kotlin.module_web.interceptor


import android.content.Context
import android.os.Build
import android.text.TextUtils
import androidx.annotation.RequiresApi
import com.knight.kotlin.module_web.ICacheInterceptor
import com.knight.kotlin.module_web.WebViewCacheHolder
import com.knight.kotlin.module_web.cache.CacheConfig
import com.knight.kotlin.module_web.cache.WebResource
import com.knight.kotlin.module_web.utils.WebViewUtils
import com.knight.kotlin.module_web.utils.disk.DiskLruCache
import okhttp3.Headers
import okio.buffer
import okio.sink
import okio.source
import java.io.File

/**
 * @author created by luguian
 * @organize
 * @Date 2025/11/7 10:53
 * @descript:磁盘缓存拦截器
 */
class DiskCacheInterceptor(val context: Context?) : ICacheInterceptor {
    private var mDiskLruCache: DiskLruCache? = null


    @RequiresApi(Build.VERSION_CODES.M)
    override fun cacheInterceptor(chain: ICacheInterceptor.Chain): WebResource? {
        val request = chain.request()
        createLruCache()
        var webResource = getWebResourceFromDiskCache(request.key)
        if (webResource != null && isContentTypeCacheable(webResource)) {

            return webResource
        }
        webResource = chain.process(request)
        //磁盘进行缓存
        if (webResource != null && isContentTypeCacheable(webResource)) {
            cacheToDisk(request.key, webResource)
        }
        return webResource
    }

    @Synchronized
    private fun createLruCache() {
        if (mDiskLruCache != null && !mDiskLruCache!!.isClosed) {
            return
        }
        if (context == null) return
        var cacheConfig = WebViewCacheHolder.getCacheConfig()
        if (cacheConfig == null) {
            cacheConfig = CacheConfig.Builder(context).build()
        }
        val dir =
            if (!TextUtils.isEmpty(cacheConfig.getCacheDir())) cacheConfig.getCacheDir() else cacheConfig.getDefaultCache()
        val version = cacheConfig.getVersion()
        val cacheSize = cacheConfig.getDiskCacheSize()
        try {
            mDiskLruCache = DiskLruCache.open(dir?.let { File(it) }, version, 2, cacheSize)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun cacheToDisk(key: String?, webResource: WebResource) {
        if (!webResource.isCacheable() || mDiskLruCache == null || mDiskLruCache!!.isClosed) return
        val edit = mDiskLruCache?.edit(key) ?: return
        val outputStream = edit.newOutputStream(0)
        var sink = outputStream.sink().buffer()
        sink.writeUtf8(webResource.responseCode.toString()).writeByte('\n'.code)
        sink.writeUtf8(webResource.message ?: "").writeByte('\n'.code)
        val headers = webResource.responseHeaders
        sink.writeDecimalLong(headers?.size?.toLong() ?: 0L).writeByte('\n'.code)
        headers?.let {
            for ((headerKey, value) in headers) {
                sink.writeUtf8(headerKey)
                    .writeUtf8(": ")
                    .writeUtf8(value)
                    .writeByte('\n'.code)
            }
        }
        sink.flush()
        sink.close()
        //写入body
        val bodyOutputSteam = edit.newOutputStream(1)
        sink = bodyOutputSteam.sink().buffer()
        val originBytes = webResource.originBytes
        if (originBytes != null && originBytes.isNotEmpty()) {
            sink.write(originBytes)
            sink.flush()
            edit.commit()
        }
        sink.close()
    }

    private fun isContentTypeCacheable(resource: WebResource?): Boolean {
        if (resource == null) {
            return false
        }
        val contentType: String? = WebViewUtils.Companion.instance.getContentType(resource)
        return contentType != null && WebViewUtils.Companion.instance.isCacheContentType(contentType)
    }


    @RequiresApi(Build.VERSION_CODES.M)
    private fun getWebResourceFromDiskCache(key: String?): WebResource? {
        if (mDiskLruCache?.isClosed == true) return null
        val snapshot = mDiskLruCache?.get(key)
        snapshot?.let {
            //获取状态
            val source = snapshot.getInputStream(0).source().buffer()
            val responseCode = source.readUtf8LineStrict()//读取一行数据，直到遇到换行符为止
            val message = source.readUtf8LineStrict()
            //获取headers
            var headerSize = source.readDecimalLong()
            val headers: Map<String, String>?
            val builder = Headers.Builder()
            val placeHolder = source.readUtf8LineStrict()
            if (!TextUtils.isEmpty(placeHolder.trim())) {
                builder.add(placeHolder)
                headerSize--
            }
            for (i in 0 until headerSize) {
                val line = source.readUtf8LineStrict()
                if (!TextUtils.isEmpty(line)) {
                    builder.add(line)
                }
            }
            headers = WebViewUtils.Companion.instance.generateHeadersMap(builder.build())

            //获取到body
            val inputStream = snapshot.getInputStream(1)
            if (inputStream != null) {
                val webResource = WebResource()
                webResource.message = message
                webResource.responseCode = Integer.parseInt(responseCode)
                webResource.originBytes = WebViewUtils.Companion.instance.streamToBytes(inputStream)
                webResource.isModified = false
                webResource.responseHeaders = headers
                return webResource
            }
            snapshot.close()
        }
        return null
    }
}
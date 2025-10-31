package com.peakmain.webview.manager.cache

import android.content.Context
import android.os.Looper
import android.os.MessageQueue
import com.peakmain.webview.manager.WebViewPool
import com.peakmain.webview.view.WanWebView

/**
 * author ：knight
 * createTime：2024/3/11
 * mail:15015706912@163.com
 * describe：外部WebView的管理，提供给外部使用
 */
class CacheWebViewManager private constructor() {

    private var mCacheConfig: CacheConfig? = null

    companion object {
        @JvmStatic
        val instance by lazy(LazyThreadSafetyMode.SYNCHRONIZED) {
            CacheWebViewManager()
        }

    }


    private var webViewPool = WebViewPool.instance

    fun getWebView(context: Context?): WanWebView? {
        return webViewPool.getWebView(context)
    }

    fun releaseWebView(webView: WanWebView?) {
        return webViewPool.releaseWebView(webView)
    }

    fun setCacheConfig(cacheConfig: CacheConfig): CacheWebViewManager {
        this.mCacheConfig = cacheConfig
        return this
    }

    fun getVersion(): Int {
        return mCacheConfig?.getVersion() ?: 0
    }

    fun clearDiskCache():CacheWebViewManager{
        mCacheConfig?.clearDiskCache(true)
        return this
    }
    fun getCacheConfig(): CacheConfig? {
        return mCacheConfig
    }

    /**
     * 预加载
     */
    fun preLoadUrl(context: Context?, url: String) {
        Looper.myQueue().addIdleHandler(object : MessageQueue.IdleHandler {
            override fun queueIdle(): Boolean {
                val webView = getWebView(context) ?: return true
                webView.preLoadUrl(url)
                WebViewPool.instance.releaseWebView(webView)
                return false
            }
        })
    }
}
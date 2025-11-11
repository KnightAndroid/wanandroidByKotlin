package com.knight.kotlin.module_web.manager

import android.app.Application
import android.content.Context
import android.content.MutableContextWrapper
import android.os.Looper
import com.knight.kotlin.module_web.cache.CacheConfig
import com.knight.kotlin.module_web.view.WanWebView
import github.leavesczy.robustwebview.utils.log
import java.util.Stack

/**
 * @Author: leavesCZY
 * @Date: 2021/10/4 18:57
 * @Desc:
 * @Github：https://github.com/leavesCZY
 */
object WebViewCacheHolder {

    private val webViewCacheStack = Stack<WanWebView>()

    private const val CACHED_WEB_VIEW_MAX_NUM = 2

    private var mCacheConfig: CacheConfig? = null
    private lateinit var application: Application

    fun init(application: Application) {
        this.application = application
        prepareWebView()
    }

    fun prepareWebView() {
        if (webViewCacheStack.size < CACHED_WEB_VIEW_MAX_NUM) {
            Looper.myQueue().addIdleHandler {
                log("WebViewCacheStack Size: " + webViewCacheStack.size)
                if (webViewCacheStack.size < CACHED_WEB_VIEW_MAX_NUM) {
                    webViewCacheStack.push(createWebView(MutableContextWrapper(application)))
                }
                false
            }
        }
    }

    fun acquireWebViewInternal(context: Context): WanWebView {
        if (webViewCacheStack.isEmpty()) {
            return createWebView(context)
        }
        val webView = webViewCacheStack.pop()
        val contextWrapper = webView.context as MutableContextWrapper
        contextWrapper.baseContext = context
        return webView
    }

    private fun createWebView(context: Context): WanWebView {
        return WanWebView(context)
    }

    fun setCacheConfig(cacheConfig: CacheConfig): WebViewCacheHolder {
        this.mCacheConfig = cacheConfig
        return this
    }
    fun getCacheConfig(): CacheConfig? {
        return mCacheConfig
    }


}
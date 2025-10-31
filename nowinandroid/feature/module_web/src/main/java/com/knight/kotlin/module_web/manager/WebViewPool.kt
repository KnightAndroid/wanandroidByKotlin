package com.peakmain.webview.manager

import android.content.Context
import android.content.MutableContextWrapper
import android.os.Handler
import android.os.Looper
import android.view.ViewGroup
import android.webkit.WebView
import com.knight.kotlin.library_base.BaseApp
import com.peakmain.webview.implement.WebViewChromeClientImpl
import com.peakmain.webview.implement.WebViewClientImpl
import com.peakmain.webview.utils.WebViewEventManager
import com.peakmain.webview.view.WanWebView
import java.util.LinkedList
import java.util.concurrent.Executors

/**
 * author ：knight
 * createTime：2025/10/04
 * mail:15015706912@163.com
 * describe：WebView 缓存池（异步预热 + 主线程安全创建）
 */
internal class WebViewPool private constructor() {

    private lateinit var mUserAgent: String
    private lateinit var mWebViewPool: LinkedList<WanWebView?>
    private val mainHandler = Handler(Looper.getMainLooper())
    private val lock = Any()
    var mParams: WebViewController.WebViewParams? = null

    companion object {
        private var WEB_VIEW_COUNT = 3
        val instance by lazy(LazyThreadSafetyMode.SYNCHRONIZED) { WebViewPool() }
    }

    /**
     * 初始化 WebView 缓存池
     */
    fun initWebViewPool(params: WebViewController.WebViewParams?) {
        if (params == null) return
        WEB_VIEW_COUNT = params.mWebViewCount
        mUserAgent = params.userAgent
        mParams = params
        mWebViewPool = LinkedList()

        // 1️⃣ 先在子线程预热 WebView 内核（避免首次创建卡顿）
        Executors.newSingleThreadExecutor().execute {
            try {
                WebView(params.application)
            } catch (_: Throwable) {
            }

            // 2️⃣ 主线程空闲时分批创建 WebView
            mainHandler.post {
                val queue = Looper.myQueue()
                var createdCount = 0
                queue.addIdleHandler {
                    if (createdCount < WEB_VIEW_COUNT) {
                        try {
                            val webView = createWebView(params, mUserAgent)
                            synchronized(lock) {
                                mWebViewPool.add(webView)
                            }
                        } catch (e: Exception) {
                            e.printStackTrace()
                        }
                        createdCount++
                        true
                    } else false
                }
            }
        }

        // 3️⃣ JS 实体注册可异步处理
        Thread {
            registerEntities(params)
        }.start()
    }

    private fun registerEntities(params: WebViewController.WebViewParams) {
        params.mEntities?.let {
            WebViewEventManager.instance.registerEntities(*it)
        }
    }

    /**
     * 获取可复用的 WebView
     */
    fun getWebView(context: Context?): WanWebView? {
        if (!::mWebViewPool.isInitialized) return null

        synchronized(lock) {
            if (mWebViewPool.isEmpty()) {
                if (mParams == null) return null
                val webView = createWebView(mParams!!, mUserAgent)
                return resetWebViewContext(webView, context)
            }

            val webView = mWebViewPool.removeFirst()
            return resetWebViewContext(webView, context)
        }
    }

    private fun resetWebViewContext(webView: WanWebView?, context: Context?): WanWebView? {
        (webView?.context as? MutableContextWrapper)?.baseContext = context
        return webView
    }

    /**
     * 释放 WebView（回收到池中）
     */
    fun releaseWebView(webView: WanWebView?) {
        webView?.apply {
            stopLoading()
            removeAllViews()
            clearHistory()
            clearCache(false)
            webChromeClient = null

            // 还原 Context 避免内存泄漏
            (context as? MutableContextWrapper)?.baseContext = BaseApp.application
            (parent as? ViewGroup)?.removeView(this)

            synchronized(lock) {
                if (::mWebViewPool.isInitialized && mWebViewPool.size < WEB_VIEW_COUNT) {
                    mWebViewPool.add(this)
                } else {
                    destroy()
                }
            }
        }
    }

    /**
     * 真正创建 WebView（必须在主线程）
     */
    private fun createWebView(
        params: WebViewController.WebViewParams,
        userAgent: String,
    ): WanWebView {
        val webView = WanWebView(MutableContextWrapper(params.application))
        webView.setWebViewParams(params)
        params.apply {
            mWebViewSetting.initWebViewSetting(webView, userAgent)
            WebViewClientImpl(params.mWebViewClientCallback).initWebClient(webView)
            WebViewChromeClientImpl(params.mWebViewChromeClientCallback)
                .initWebChromeClient(webView)
        }
        return webView
    }
}

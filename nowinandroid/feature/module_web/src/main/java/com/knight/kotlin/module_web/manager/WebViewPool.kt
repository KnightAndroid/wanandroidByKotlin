package com.peakmain.webview.manager

import android.content.Context
import android.content.MutableContextWrapper
import android.os.Handler
import android.os.Looper
import android.view.ViewGroup
import com.knight.kotlin.library_base.BaseApp
import com.peakmain.webview.implement.WebViewChromeClientImpl
import com.peakmain.webview.implement.WebViewClientImpl
import com.peakmain.webview.utils.WebViewEventManager
import com.peakmain.webview.view.WanWebView
import java.util.LinkedList

/**
 * author ：Peakmain
 * createTime：2023/04/04
 * mail:2726449200@qq.com
 * describe：WebView缓存池管理
 */
internal class WebViewPool private constructor() {
    private lateinit var mUserAgent: String
    private lateinit var mWebViewPool: LinkedList<WanWebView?>
    var mParams: WebViewController.WebViewParams? = null

    companion object {
        private var WEB_VIEW_COUNT = 3
        val instance by lazy(LazyThreadSafetyMode.SYNCHRONIZED) {
            WebViewPool()
        }

    }

    /**
     * 初始化WebView池
     */
    fun initWebViewPool(params: WebViewController.WebViewParams?) {
//        if (params == null) return
//        WEB_VIEW_COUNT = params.mWebViewCount
//        mWebViewPool = LinkedList()
//        mUserAgent = params.userAgent
//        mParams = params
//        for (i in 0 until WEB_VIEW_COUNT) {
//            mWebViewPool.add(createWebView(params, mUserAgent))
//        }
//        registerEntities(params)
        if (params == null) return
        WEB_VIEW_COUNT = params.mWebViewCount
        mUserAgent = params.userAgent
        mParams = params
        mWebViewPool = LinkedList()

        Handler(Looper.getMainLooper()).post {
            repeat(WEB_VIEW_COUNT) { index ->
                Handler(Looper.getMainLooper()).postDelayed({
                    mWebViewPool.add(createWebView(params, mUserAgent))
                }, index * 400L) // 每隔400ms创建1个WebView
            }
        }
        registerEntities(params)



    }

    private fun registerEntities(params: WebViewController.WebViewParams) {
        params.mEntities?.let { WebViewEventManager.instance.registerEntities(*it) }
    }

    /**
     * 获取webView
     */
    fun getWebView(context: Context?): WanWebView? {
        if (!::mWebViewPool.isInitialized) {
            return null
        }
        if (mWebViewPool.size <= 0) {
            if (mParams == null) return null
            mWebViewPool.add(createWebView(mParams!!, mUserAgent))
        }
        for (i in 0 until WEB_VIEW_COUNT) {
            if (mWebViewPool[i] != null) {
                val webView = mWebViewPool[i]
                val contextWrapper = webView?.context as MutableContextWrapper?
                contextWrapper?.baseContext = context
                mWebViewPool.remove()
                return webView
            }
        }
        return null
    }

    /**
     * Activity销毁时需要释放当前WebView
     */
    fun releaseWebView(webView: WanWebView?) {
        webView?.apply {
            stopLoading()
            removeAllViews()
            clearHistory()
            clearCache(true)
            destroy()
            webChromeClient = null

            // 还原上下文为 Application，避免 Activity 泄漏
            (context as? MutableContextWrapper)?.baseContext = BaseApp.application
            (parent as ViewGroup?)?.removeView(this)
            if (!::mWebViewPool.isInitialized) {
                return
            }
            if (mWebViewPool.size < WEB_VIEW_COUNT) {
               mParams?.let {
                   mWebViewPool.add(createWebView(it, mUserAgent))
               }
            }
        }

    }

    private fun createWebView(
        params: WebViewController.WebViewParams, userAgent: String,
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
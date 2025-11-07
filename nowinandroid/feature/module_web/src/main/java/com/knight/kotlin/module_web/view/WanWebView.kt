package com.knight.kotlin.module_web.view

import android.content.Context
import android.content.MutableContextWrapper
import android.graphics.Bitmap
import android.graphics.Canvas
import android.os.Build
import android.util.AttributeSet
import android.view.ViewGroup
import android.webkit.CookieManager
import android.webkit.WebChromeClient
import android.webkit.WebResourceRequest
import android.webkit.WebResourceResponse
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import com.knight.kotlin.library_base.BaseApp
import com.knight.kotlin.module_web.JsInterface
import com.knight.kotlin.module_web.WebResourceResponseManager


import java.io.File

/**
 * @Author: leavesCZY
 * @Date: 2021/9/20 22:45
 * @Desc:
 * @Github：https://github.com/leavesCZY
 */
interface WebViewListener {

    fun onProgressChanged(webView: WanWebView, progress: Int) {

    }

    fun onReceivedTitle(webView:WanWebView, title: String) {

    }

    fun onPageFinished(webView: WanWebView, url: String) {

    }

}

class WanWebView(context: Context, attributeSet: AttributeSet? = null) :
    WebView(context, attributeSet) {
    var blackMonitorCallback: ((Boolean) -> Unit)? = null
    private val baseCacheDir by lazy {
        File(context.cacheDir, "webView")
    }

    private val databaseCachePath by lazy {
        File(baseCacheDir, "databaseCache").absolutePath
    }

    private val appCachePath by lazy {
        File(baseCacheDir, "appCache").absolutePath
    }

    var hostLifecycleOwner: LifecycleOwner? = null

    var webViewListener: WebViewListener? = null

    private val mWebChromeClient = object : WebChromeClient() {

        override fun onProgressChanged(webView: WebView, newProgress: Int) {
            super.onProgressChanged(webView, newProgress)
            webViewListener?.onProgressChanged(this@WanWebView, newProgress)
        }

        override fun onReceivedTitle(webView: WebView, title: String?) {
            super.onReceivedTitle(webView, title)
            webViewListener?.onReceivedTitle(this@WanWebView, title ?: "")
        }

//        override fun onJsAlert(
//            webView: WebView,
//            url: String?,
//            message: String?,
//            result: JsResult
//        ): Boolean {
//            log("onJsAlert: $webView $message")
//            return super.onJsAlert(webView, url, message, result)
//        }
//
//        override fun onJsConfirm(
//            webView: WebView,
//            url: String?,
//            message: String?,
//            result: JsResult
//        ): Boolean {
//            log("onJsConfirm: $url $message")
//            return super.onJsConfirm(webView, url, message, result)
//        }
//
//        override fun onJsPrompt(
//            webView: WebView,
//            url: String?,
//            message: String?,
//            defaultValue: String?,
//            result: JsPromptResult?
//        ): Boolean {
//            log("onJsPrompt: $url $message $defaultValue")
//            return super.onJsPrompt(webView, url, message, defaultValue, result)
//        }
    }

    private val mWebViewClient = object : WebViewClient() {

        private var startTime = 0L

        override fun shouldOverrideUrlLoading(webView: WebView, url: String): Boolean {
            webView.loadUrl(url)
            return true
        }

        override fun onPageStarted(webView: WebView, url: String?, favicon: Bitmap?) {
            super.onPageStarted(webView, url, favicon)
            startTime = System.currentTimeMillis()
        }

        override fun onPageFinished(webView: WebView, url: String?) {
            super.onPageFinished(webView, url)
            webViewListener?.onPageFinished(this@WanWebView, url ?: "")
        }

//        override fun onReceivedSslError(
//            webView: WebView,
//            handler: SslErrorHandler?,
//            error: SslError?
//        ) {
//            log("onReceivedSslError-$error")
//            super.onReceivedSslError(webView, handler, error)
//        }

//        override fun shouldInterceptRequest(webView: WebView, url: String): WebResourceResponse? {
//            return super.shouldInterceptRequest(webView, url)
//        }

        override fun shouldInterceptRequest(
            webView: WebView,
            request: WebResourceRequest
        ): WebResourceResponse? {

            if (toProxy(request)) {
                return WebResourceResponseManager.getResponse(
                    (webView?.context as MutableContextWrapper?)?.baseContext,
                    request,
                    "Android"
                )
            } else {
              return  super.shouldInterceptRequest(webView, request)
            }
//
//            return WebViewInterceptRequestProxy.shouldInterceptRequest(request)
//                ?: super.shouldInterceptRequest(webView, request)
        }
    }


    fun postBlankMonitorRunnable() {
        removeCallbacks(blackMonitorRunnable)
        postDelayed(blackMonitorRunnable, 1000)
    }

    fun removeBlankMonitorRunnable() {
        removeCallbacks(blackMonitorRunnable)
    }

    private val blackMonitorRunnable = Runnable {
        val height = measuredHeight / 6
        val width = measuredWidth / 6
        val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
        Canvas(bitmap).run {
            draw(this)
        }
        with(bitmap) {
            //像素总数
            val pixelCount = (width * height).toFloat()
            val whitePixelCount = getWhitePixelCount()
            recycle()
            if (whitePixelCount == 0) return@Runnable
            post {
                blackMonitorCallback?.invoke(whitePixelCount / pixelCount > 0.95)
            }
        }

    }

    private fun Bitmap.getWhitePixelCount(): Int {
        var whitePixelCount = 0
        for (i in 0 until width) {
            for (j in 0 until height) {
                if (getPixel(i, j) == -1) {
                    whitePixelCount++
                }
            }
        }
        return whitePixelCount
    }



    init {
        webViewClient = mWebViewClient
        webChromeClient = mWebChromeClient
        initWebViewSettings(this)
        addJavascriptInterface(JsInterface(), "android")
        setDownloadListener { url, userAgent, contentDisposition, mimetype, contentLength ->
        }
    }

    fun toLoadUrl(url: String, cookie: String) {
        val mCookieManager = CookieManager.getInstance()
        mCookieManager?.setCookie(url, cookie)
        mCookieManager?.flush()
        loadUrl(url)
    }

    fun toGoBack(): Boolean {
        if (canGoBack()) {
            goBack()
            return false
        }
        return true
    }

    private fun initWebViewSettings(webView: WebView) {
        val settings = webView.settings
//        settings.userAgentString = "android-leavesCZY"
        settings.javaScriptEnabled = true
       // settings.pluginsEnabled = true
        settings.useWideViewPort = true
        settings.loadWithOverviewMode = true
        settings.setSupportMultipleWindows(false)
        settings.setSupportZoom(false)
        settings.builtInZoomControls = false
        settings.textZoom = 100
        settings.displayZoomControls = false
        settings.allowFileAccess = true
        settings.allowContentAccess = true
        settings.loadsImagesAutomatically = true
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            settings.safeBrowsingEnabled = true
        }
        settings.domStorageEnabled = true
        settings.databaseEnabled = true
        settings.databasePath = databaseCachePath
      //  settings.setAppCacheEnabled(true)
     //   settings.setAppCachePath(appCachePath)
        settings.cacheMode = WebSettings.LOAD_DEFAULT
        settings.javaScriptCanOpenWindowsAutomatically = true
        settings.mixedContentMode = WebSettings.MIXED_CONTENT_ALWAYS_ALLOW
    }



    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        (hostLifecycleOwner ?: findLifecycleOwner(context))?.let {
            addHostLifecycleObserver(it)
        }
    }

    private fun findLifecycleOwner(context: Context): LifecycleOwner? {
        if (context is LifecycleOwner) {
            return context
        }
        if (context is MutableContextWrapper) {
            val baseContext = context.baseContext
            if (baseContext is LifecycleOwner) {
                return baseContext
            }
        }
        return null
    }

    private fun addHostLifecycleObserver(lifecycleOwner: LifecycleOwner) {
        lifecycleOwner.lifecycle.addObserver(object : DefaultLifecycleObserver {
            override fun onResume(owner: LifecycleOwner) {
                onHostResume()
            }

            override fun onPause(owner: LifecycleOwner) {
                onHostPause()
            }

            override fun onDestroy(owner: LifecycleOwner) {
                onHostDestroy()
            }
        })
    }

    private fun onHostResume() {
        onResume()
    }

    private fun onHostPause() {
        onPause()
    }

    private fun onHostDestroy() {
        release()
    }

    private fun release() {
        hostLifecycleOwner = null
        webViewListener = null
        webChromeClient = null
        stopLoading()
        removeAllViews()
        clearHistory()
        clearCache(false)
      //  webViewClient = null
        (context as? MutableContextWrapper)?.baseContext = BaseApp.application
        (parent as? ViewGroup)?.removeView(this)
        destroy()



//        webView?.apply {
//            stopLoading()
//            removeAllViews()
//            clearHistory()
//            clearCache(false)
//            webChromeClient = null
//
//            // 还原 Context 避免内存泄漏
//            (kotlin.context as? MutableContextWrapper)?.baseContext = BaseApp.application
//            (parent as? ViewGroup)?.removeView(this)
//            destroy()
//            synchronized(lock) {
//                if (::mWebViewPool.isInitialized && mWebViewPool.size < WEB_VIEW_COUNT) {
//                    //  mWebViewPool.add(this)
//                    mParams?.let {
//                        val webView = createWebView(it, mUserAgent)
//                        synchronized(lock) {
//                            mWebViewPool.add(webView)
//                        }
//
//                    }
//                }
//            }
//        }
    }



    private fun toProxy(webResourceRequest: WebResourceRequest): Boolean {
        if (webResourceRequest.isForMainFrame) {
            return false
        }
        val url = webResourceRequest.url ?: return false
        if (!webResourceRequest.method.equals("GET", true)) {
            return false
        }
        if (url.scheme == "https" || url.scheme == "http") {
            val urlString = url.toString()
            if (urlString.endsWith(".js", true) ||
                urlString.endsWith(".css", true) ||
                urlString.endsWith(".jpg", true) ||
                urlString.endsWith(".png", true) ||
                urlString.endsWith(".webp", true) ||
                urlString.endsWith(".awebp", true)
            ) {
                return true
            }
        }
        return false
    }



}
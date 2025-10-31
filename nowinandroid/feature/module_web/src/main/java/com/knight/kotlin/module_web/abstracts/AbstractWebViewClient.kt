package com.knight.kotlin.module_web.abstracts


import android.graphics.Bitmap
import android.text.TextUtils
import android.webkit.WebResourceRequest
import android.webkit.WebResourceResponse
import android.webkit.WebView
import android.webkit.WebViewClient
import com.knight.kotlin.module_web.fragment.WebViewFragment
import com.peakmain.webview.callback.WebViewClientCallback
import com.peakmain.webview.manager.WebViewManager
import com.peakmain.webview.sealed.HandleResult
import java.lang.ref.WeakReference

abstract class AbstractWebViewClient constructor(
    private val webViewClientCallback: WebViewClientCallback?
) : WebViewClient() {

    // 用 WeakReference 避免强引用 WebViewFragment
    private var fragmentRef: WeakReference<WebViewFragment>? = null

    abstract fun initWebClient(webView: WebView)

    override fun onPageStarted(view: WebView, url: String, favicon: Bitmap?) {
        getWebViewFragment()?.onPageStarted(view, url)
        webViewClientCallback?.onPageStarted(view, url, getWebViewFragment())
    }

    override fun onPageFinished(view: WebView, url: String) {
        getWebViewFragment()?.onPageFinished(view, url)
        webViewClientCallback?.onPageFinished(view, url, getWebViewFragment())
    }

    override fun onReceivedError(view: WebView?, errorCode: Int, description: String?, failingUrl: String?) {
        getWebViewFragment()?.onReceivedError(view, errorCode, description, failingUrl)
        webViewClientCallback?.onReceivedError(view, errorCode, description, failingUrl, getWebViewFragment())
    }

    override fun shouldOverrideUrlLoading(view: WebView, url: String): Boolean {
        if (TextUtils.isEmpty(url)) return super.shouldOverrideUrlLoading(view, url)
        val handleResult = getWebViewFragment()?.shouldOverrideUrlLoading(view, url)
        if (handleResult == HandleResult.Consumed || handleResult == HandleResult.Consuming) {
            return true
        }
        return webViewClientCallback?.shouldOverrideUrlLoading(view, url, getWebViewFragment())
            ?: super.shouldOverrideUrlLoading(view, url)
    }

    private fun getWebViewFragment(): WebViewFragment? {
        if (fragmentRef == null || fragmentRef?.get() == null) {
            fragmentRef = WeakReference(WebViewManager.instance.getFragment())
        }
        return fragmentRef?.get()
    }

    override fun shouldInterceptRequest(view: WebView?, request: WebResourceRequest): WebResourceResponse? {
        getWebViewFragment()  // This updates the fragment reference if necessary
        val url = request.url.toString()
        if (!url.startsWith("http") || !url.startsWith("https")) {
            return super.shouldInterceptRequest(view, request)
        }
        if (request.requestHeaders.containsKey("noImage") && request.requestHeaders["noImage"] != null) {
            return WebResourceResponse("", "", null)
        }
        return webViewClientCallback?.shouldInterceptRequest(view, request)
            ?: super.shouldInterceptRequest(view, request)
    }

    override fun onReceivedHttpError(view: WebView?, request: WebResourceRequest?, errorResponse: WebResourceResponse?) {
        // Handle HTTP errors if necessary
    }
}

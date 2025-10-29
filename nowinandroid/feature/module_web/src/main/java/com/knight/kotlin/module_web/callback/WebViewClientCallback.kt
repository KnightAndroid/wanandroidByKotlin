package com.peakmain.webview.callback

import android.webkit.WebResourceRequest
import android.webkit.WebResourceResponse
import android.webkit.WebView
import com.knight.kotlin.module_web.fragment.WebViewFragment

/**
 * author ：Peakmain
 * createTime：2023/4/1
 * mail:2726449200@qq.com
 * describe：
 */
interface WebViewClientCallback {
    fun onPageStarted(view: WebView, url: String,fragment: WebViewFragment?)
    fun onPageFinished(view: WebView, url: String, fragment: WebViewFragment?)
    fun shouldOverrideUrlLoading(view: WebView, url: String,fragment: WebViewFragment?): Boolean?
    fun onReceivedError(view: WebView?, err: Int, des: String?, url: String?,fragment: WebViewFragment?)
    fun shouldInterceptRequest(view: WebView?, request: WebResourceRequest): WebResourceResponse?
}
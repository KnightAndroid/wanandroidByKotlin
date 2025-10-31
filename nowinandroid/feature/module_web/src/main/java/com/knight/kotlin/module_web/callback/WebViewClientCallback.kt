package com.peakmain.webview.callback

import android.webkit.WebResourceRequest
import android.webkit.WebResourceResponse
import android.webkit.WebView
import com.knight.kotlin.module_web.fragment.WebViewFragment

/**
 * author ：knight
 * createTime：2025/9/16
 * mail:15015706912@163.com
 * describe：
 */
interface WebViewClientCallback {
    fun onPageStarted(view: WebView, url: String,fragment: WebViewFragment?)
    fun onPageFinished(view: WebView, url: String, fragment: WebViewFragment?)
    fun shouldOverrideUrlLoading(view: WebView, url: String,fragment: WebViewFragment?): Boolean?
    fun onReceivedError(view: WebView?, err: Int, des: String?, url: String?,fragment: WebViewFragment?)
    fun shouldInterceptRequest(view: WebView?, request: WebResourceRequest): WebResourceResponse?
}
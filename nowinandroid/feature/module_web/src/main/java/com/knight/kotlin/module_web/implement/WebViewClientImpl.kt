package com.peakmain.webview.implement

import android.webkit.WebView
import com.knight.kotlin.module_web.abstracts.AbstractWebViewClient

import com.peakmain.webview.callback.WebViewClientCallback

/**
 * author ：knight
 * createTime：2023/04/07
 * mail:15015706912@163.com
 * describe：
 */
internal class WebViewClientImpl(webViewClientCallback: WebViewClientCallback?) :
    AbstractWebViewClient(webViewClientCallback) {
    override fun initWebClient(webView: WebView) {
//        val webViewClient = WebViewClientImpl(webViewClientCallback)
//        webView.webViewClient = webViewClient
        webView.webViewClient = this
    }
}
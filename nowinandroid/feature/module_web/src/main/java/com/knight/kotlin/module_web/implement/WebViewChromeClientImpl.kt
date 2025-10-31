package com.peakmain.webview.implement

import android.webkit.WebView
import com.knight.kotlin.module_web.abstracts.AbstractWebViewChromeClient

import com.peakmain.webview.callback.WebViewChromeClientCallback

/**
 * author ：knight
 * createTime：2023/04/07
 * mail:15015706912@163.com
 * describe：
 */
internal class WebViewChromeClientImpl(webViewChromeClientCallback: WebViewChromeClientCallback?) :
    AbstractWebViewChromeClient(webViewChromeClientCallback) {
    override fun initWebChromeClient(webView: WebView) {
        webView.webChromeClient = WebViewChromeClientImpl(webViewChromeClientCallback)
    }

}
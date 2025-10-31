package com.peakmain.webview.interfaces

import android.webkit.WebView

/**
 * author ：knight
 * createTime：2023/04/07
 * mail:15015706912@163.com
 * describe：
 */
interface InitWebViewSetting {
    fun initWebViewSetting(webView: WebView, userAgent: String? = null)
}
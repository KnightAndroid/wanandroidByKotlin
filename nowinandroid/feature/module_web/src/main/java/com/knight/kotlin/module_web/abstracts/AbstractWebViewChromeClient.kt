package com.knight.kotlin.module_web.abstracts

import android.webkit.WebChromeClient
import android.webkit.WebView
import com.knight.kotlin.module_web.fragment.WebViewFragment
import com.peakmain.webview.callback.WebViewChromeClientCallback

import com.peakmain.webview.manager.WebViewManager

/**
 * author ：knight
 * createTime：2025/10/30
 * mail:15015706912@163.com
 * describe：
 */
abstract class AbstractWebViewChromeClient(val webViewChromeClientCallback: WebViewChromeClientCallback?) :
    WebChromeClient() {
    private var fragment: WebViewFragment? = null
    abstract fun initWebChromeClient(webView: WebView)
    override fun onProgressChanged(view: WebView?, newProgress: Int) {
        if (fragment == null) {
            fragment = WebViewManager.instance.getFragment()
        }
        fragment?.onProgressChanged(view, newProgress)
        webViewChromeClientCallback?.onProgressChanged(view, newProgress, fragment)
    }

    override fun onReceivedTitle(view: WebView?, title: String) {
        if (fragment == null) {
            fragment = WebViewManager.instance.getFragment()
        }
        fragment?.onReceivedTitle(view, title)
        webViewChromeClientCallback?.onReceivedTitle(view, title, fragment)
    }

}
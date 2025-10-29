package com.peakmain.webview.callback

import android.net.Uri

import android.webkit.ValueCallback
import android.webkit.WebView
import com.knight.kotlin.module_web.fragment.WebViewFragment

/**
 * author ：Peakmain
 * createTime：2023/04/07
 * mail:2726449200@qq.com
 * describe：
 */
class DefaultWebViewChromeClientCallback:WebViewChromeClientCallback {
    override fun onReceivedTitle(view: WebView?, title: String?, fragment: WebViewFragment?) {

    }

    override fun openFileInput(
        fileUploadCallbackFirst: ValueCallback<Uri>?,
        fileUploadCallbackSecond: ValueCallback<Array<Uri>>?,
        acceptType: String?
    ) {
    }

    override fun onProgressChanged(view: WebView?, newProgress: Int, fragment: WebViewFragment?) {

    }
}
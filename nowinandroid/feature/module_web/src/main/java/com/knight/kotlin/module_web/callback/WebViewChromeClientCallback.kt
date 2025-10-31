package com.peakmain.webview.callback

import android.net.Uri
import android.webkit.ValueCallback
import android.webkit.WebView
import com.knight.kotlin.module_web.fragment.WebViewFragment


/**
 * author ：knight
 * createTime：2025/10/30
 * mail:15015706912@163.com
 * describe：
 */
interface WebViewChromeClientCallback {
    fun onReceivedTitle(view: WebView?, title: String?, fragment: WebViewFragment?)
    fun openFileInput(
        fileUploadCallbackFirst: ValueCallback<Uri>?,
        fileUploadCallbackSecond: ValueCallback<Array<Uri>>?,
        acceptType: String?
    )
    fun onProgressChanged(view: WebView?, newProgress: Int, fragment: WebViewFragment?)
}
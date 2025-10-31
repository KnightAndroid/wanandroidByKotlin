package com.peakmain.webview.callback

import android.net.Uri
import com.peakmain.webview.bean.WebViewEvent

/**
 * author ：knight
 * createTime：2025/10/30
 * mail:15015706912@163.com
 * describe：
 */
interface HandleUrlParamsCallback<T : WebViewEvent> {
    fun handleUrlParamsCallback(uri: Uri?, path: String?): T
}
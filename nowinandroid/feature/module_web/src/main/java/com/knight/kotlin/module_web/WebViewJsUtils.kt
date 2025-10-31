package com.peakmain.webview

import android.webkit.WebView
import com.knight.kotlin.library_util.EncodeUtils

/**
 * author ：knight
 * createTime：2025/10/3
 * mail:15015706912@163.com
 * describe：
 */
class WebViewJsUtils private constructor(val jsNameSpace: String) {
    companion object {
        @Volatile
        private var mInstance: WebViewJsUtils? = null
        fun getInstance(jsNameSpace: String = "Prius"): WebViewJsUtils {
            return mInstance ?: synchronized(this) {
                mInstance ?: WebViewJsUtils(jsNameSpace).also {
                    mInstance = it
                }
            }
        }

    }

    /*    fun executeJs(
            webView: WebView?,
            method: String,
            status: Int,
            data: HashMap<String, String>,
            callId: String
        ): Boolean {
            val webViewModel = WebViewModel(status, data, callId)
            return executeJs(webView, method, GsonUtils.toJson(webViewModel))
        }*/

    fun executeJs(webView: WebView?, method: String, data: String): Boolean {
        return executeJs(webView, method, datas = arrayOf(data))
    }

    fun executeJs(webView: WebView?, method: String, vararg datas: String): Boolean {
        if (webView == null) return false
        val sb = StringBuilder()
        if (datas.isNotEmpty()) {
            datas.forEachIndexed { index, s ->
                if (index > 0) {
                    sb.append(", ")
                }
                sb.append("'")
                    .append(EncodeUtils.encode(datas[index]))
                    .append("'");
            }
        }
        val jsCode = String.format("%s.%s(%s)", jsNameSpace, method, sb.toString())
        webView.evaluateJavascript(jsCode, null)
        return true
    }
}
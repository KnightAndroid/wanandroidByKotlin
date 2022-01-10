package com.knight.kotlin.module_web.widget


import android.app.Activity
import android.view.LayoutInflater
import android.webkit.WebView
import com.just.agentweb.IWebLayout
import com.knight.kotlin.module_web.R


/**
 * Author:Knight
 * Time:2022/1/6 10:47
 * Description:WebLayout
 */
class WebLayout constructor(actiivty: Activity) : IWebLayout<WebView,WebView> {

    private var mWebView:WebView = LayoutInflater.from(actiivty).inflate(R.layout.web_layout, null) as WebView
    override fun getLayout(): WebView {
        return mWebView
    }

    override fun getWebView(): WebView? {
        return mWebView
    }
}
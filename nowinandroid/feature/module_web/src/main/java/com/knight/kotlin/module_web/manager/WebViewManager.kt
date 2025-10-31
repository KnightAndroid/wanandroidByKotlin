package com.peakmain.webview.manager

import com.knight.kotlin.module_web.fragment.WebViewFragment

/**
 * author ：knight
 * createTime：2023/04/06
 * mail:15015706912@163.com
 * describe：
 */
internal class WebViewManager private constructor(){
    companion object{
        val instance:WebViewManager by lazy(LazyThreadSafetyMode.SYNCHRONIZED) {
            WebViewManager()
        }
    }
    private var mWebViewFragment: WebViewFragment? = null
    fun register(webViewFragment: WebViewFragment?){
        mWebViewFragment=webViewFragment
    }
    fun getFragment():WebViewFragment?{
        return mWebViewFragment
    }
    fun unRegister(){
        mWebViewFragment=null
    }
}
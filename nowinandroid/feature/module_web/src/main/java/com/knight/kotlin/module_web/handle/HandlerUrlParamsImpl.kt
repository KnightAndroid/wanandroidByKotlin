package com.knight.kotlin.module_web.handle

import android.net.Uri
import android.text.TextUtils
import com.core.library_base.util.GsonUtils
import com.knight.kotlin.library_util.EncodeUtils
import com.knight.kotlin.module_web.bean.NewHybridModel
import com.knight.kotlin.module_web.bean.WebViewModel
import com.knight.kotlin.module_web.bean.WebViewModelEvent
import com.peakmain.webview.callback.HandleUrlParamsCallback

/**
 * author ：knight
 * createTime：2025/10/31
 * mail:15015706912@163.com
 * describe：
 */
class HandlerUrlParamsImpl : HandleUrlParamsCallback<WebViewModelEvent> {
    override fun handleUrlParamsCallback(uri: Uri?, path: String?): WebViewModelEvent {
        val params = uri?.getQueryParameter("param")
        val webViewModelEvent = WebViewModelEvent()
        if (!TextUtils.isEmpty(params)) {
            val decodeParam: String =
                EncodeUtils.decode(params!!.replace(" ", "+"))
           
            if (TextUtils.equals("/jumpToWhere", path)) {
                val newHybridModel: NewHybridModel? = GsonUtils.get(
                    decodeParam,
                    NewHybridModel::class.java
                )
                webViewModelEvent.newHybridModel = newHybridModel
            } else {
                val webViewModel: WebViewModel? =
                    GsonUtils.get(decodeParam, WebViewModel::class.java)
                webViewModelEvent.webViewModel = webViewModel
            }
        }
        return webViewModelEvent
    }

}
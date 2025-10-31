package com.peakmain.pkwebview.handle

import com.core.library_base.util.GsonUtils
import com.knight.kotlin.module_web.bean.WebViewModelEvent
import com.peakmain.webview.WebViewJsUtils
import com.peakmain.webview.annotation.Handler
import com.peakmain.webview.annotation.HandlerMethod
import com.peakmain.webview.sealed.HandleResult


/**
 * author ：knight
 * createTime：2025/10/31
 * mail:15015706912@163.com
 * describe：
 */
@Handler(scheme = "Mozilla/5.0 (Linux; Android 11; Pixel 4) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/97.0.4692.99 Mobile Safari/537.36", authority = ["onlineService"])
class OnlineServiceHandle {
    @HandlerMethod(path = "")
    fun webPolicyAlert(event: WebViewModelEvent): HandleResult {
        val context = event.context
        val model = event.webViewModel
        val webView = event.webView
        if (webView == null || model == null) return HandleResult.Consumed

        val data = model.data
        model.callId = ""
        WebViewJsUtils.getInstance().executeJs(webView, GsonUtils.toJson(model));
        //保存数据
        return HandleResult.Consumed
    }

}
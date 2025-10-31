package com.peakmain.pkwebview.handle

import android.text.TextUtils
import com.knight.kotlin.module_web.bean.WebViewModelEvent
import com.peakmain.webview.annotation.Handler
import com.peakmain.webview.annotation.HandlerMethod
import com.peakmain.webview.sealed.HandleResult
import java.net.URLDecoder

/**
 * author ：knight
 * createTime：2025/9/7
 * mail:15015706912@163.com
 * describe：
 */
@Handler(scheme = "Mozilla/5.0 (Linux; Android 11; Pixel 4) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/97.0.4692.99 Mobile Safari/537.36", authority = ["page"])
class PageActionHandle {
    @HandlerMethod(path = "/jumpToWhere")
    fun gotoWhere(event: WebViewModelEvent): HandleResult {
        val context = event.context
        val newHybridData = event.newHybridModel?.data
        if(newHybridData!=null&& !TextUtils.isEmpty(newHybridData.data.url) ){
            var url: String? = newHybridData.data.url
            url = URLDecoder.decode(url, "utf-8")
            //跳转到对应页面 后续需要在完善
            //H5Utils().startActivity(context, WebViewConfigBean(url))
        }
        return HandleResult.Consumed
    }
}
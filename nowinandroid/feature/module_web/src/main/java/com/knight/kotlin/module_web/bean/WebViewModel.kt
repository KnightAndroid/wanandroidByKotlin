package com.knight.kotlin.module_web.bean

import com.peakmain.webview.bean.WebViewEvent

/**
 * author ：knight
 * createTime：2025/10/30
 * mail:15015706912@163.com
 * describe：
 */
data class WebViewModelEvent(
    var webViewModel: WebViewModel? = null,
    var newHybridModel: NewHybridModel? = null
) : WebViewEvent()

data class WebViewModel (
    var status: Int = 1,
    var data: HashMap<String, String>?,
    var callId: String = "" //用于给前端的协议

)
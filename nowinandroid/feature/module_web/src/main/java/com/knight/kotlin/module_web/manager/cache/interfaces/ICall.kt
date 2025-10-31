package com.peakmain.webview.manager.cache.interfaces

import com.knight.kotlin.module_web.bean.cache.WebResource

/**
 * author ：knight
 * createTime：2024/3/4
 * mail:15015706912@163.com
 * describe：
 */
interface ICall {
    fun call(): WebResource?
}
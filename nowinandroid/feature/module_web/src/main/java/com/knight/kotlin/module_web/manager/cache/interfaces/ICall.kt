package com.peakmain.webview.manager.cache.interfaces

import com.knight.kotlin.module_web.bean.cache.WebResource

/**
 * author ：Peakmain
 * createTime：2024/3/4
 * mail:2726449200@qq.com
 * describe：
 */
interface ICall {
    fun call(): WebResource?
}
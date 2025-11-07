package com.knight.kotlin.module_web

import com.knight.kotlin.module_web.utils.WebViewUtils
import java.util.Locale

/**
 * @author created by luguian
 * @organize
 * @Date 2025/11/7 9:54
 * @descript:
 */
class CacheRequest {

    // 缓存唯一识别，将请求链接转为 MD5
    var key: String = ""

    var url: String = ""
        set(value) {
            field = value
            // md5 进入 DiskLruCache 缓存时必须为小写字母
            key = WebViewUtils.instance.getMd5(value, true).lowercase(Locale.getDefault())
        }

    // 资源类型
    var mimeType: String? = null

    // 设置是否缓存模式
    var cacheMode: Boolean = false

    var webViewCacheMode: Int = 0

    var headers: Map<String, String>? = null

    var userAgent: String? = null
}

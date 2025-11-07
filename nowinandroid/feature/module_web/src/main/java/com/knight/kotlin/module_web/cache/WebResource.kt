package com.knight.kotlin.module_web.cache
import okhttp3.internal.http.StatusLine.Companion.HTTP_PERM_REDIRECT


/**
 * @author created by luguian
 * @organize
 * @Date 2025/11/7 10:11
 * @descript:
 */
class WebResource {

    // 资源返回值
    var responseCode: Int = 0

    // 描述状态代码的短语，例如“OK”
    var message: String? = null

    // 响应资源的Header集合
    var responseHeaders: Map<String, String>? = null

    // 是否新变更
    var isModified: Boolean = true

    var originBytes: ByteArray? = null

    /**
     * 参考
     * Okhttp3.internal.cache.CacheStrategy
     * @return boolean 是否可以缓存HTTP响应
     */
    fun isCacheable(): Boolean {
        return when (responseCode) {
            HTTP_OK,
            HTTP_NOT_AUTHORITATIVE,
            HTTP_NO_CONTENT,
            HTTP_MULT_CHOICE,
            HTTP_MOVED_PERM,
            HTTP_NOT_FOUND,
            HTTP_BAD_METHOD,
            HTTP_GONE,
            HTTP_REQ_TOO_LONG,
            HTTP_NOT_IMPLEMENTED,
            HTTP_PERM_REDIRECT -> true
            else -> false
        }
    }

    companion object {
        // 常量定义
        const val HTTP_OK = 200
        const val HTTP_NOT_AUTHORITATIVE = 203
        const val HTTP_NO_CONTENT = 204
        const val HTTP_MULT_CHOICE = 300
        const val HTTP_MOVED_PERM = 301
        const val HTTP_NOT_FOUND = 404
        const val HTTP_BAD_METHOD = 405
        const val HTTP_GONE = 410
        const val HTTP_REQ_TOO_LONG = 414
        const val HTTP_NOT_IMPLEMENTED = 501
    }
}
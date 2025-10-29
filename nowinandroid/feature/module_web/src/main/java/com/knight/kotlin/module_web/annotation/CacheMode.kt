package com.peakmain.webview.annotation

import android.webkit.WebSettings
import androidx.annotation.IntDef

/**
 * author ：Peakmain
 * createTime：2023/06/01
 * mail:2726449200@qq.com
 * describe：
 */
/**
 *
 * LOAD_DEFAULT:默认缓存模式，优先使用缓存，如果缓存中没有数据，则使用网络加载数据
 * LOAD_NORMAL:类似于LOAD_DEFAULT，但不会使用缓存中已过期的数据，而是强制从网络加载数据,已过时
 * LOAD_CACHE_ELSE_NETWORK:先使用缓存，如果缓存中没有数据，则使用网络加载数据
 * LOAD_NO_CACHE:不使用缓存，强制从网络加载数据。适用于需要最新数据或者数据频繁变化的场景(保存地址）
 * LOAD_CACHE_ONLY:只使用缓存，不使用网络。如果缓存中没有数据，则无法加载数据
 */

object CacheMode {
    const val LOAD_NONE = -2
    const val LOAD_DEFAULT = WebSettings.LOAD_DEFAULT
    const val LOAD_NORMAL = WebSettings.LOAD_NORMAL
    const val LOAD_CACHE_ELSE_NETWORK = WebSettings.LOAD_CACHE_ELSE_NETWORK
    const val LOAD_NO_CACHE = WebSettings.LOAD_NO_CACHE
    const val LOAD_CACHE_ONLY = WebSettings.LOAD_CACHE_ONLY
}

@IntDef(
    value = [
       CacheMode.LOAD_NONE,
       CacheMode.LOAD_DEFAULT,
       CacheMode.LOAD_NORMAL,
       CacheMode.LOAD_CACHE_ELSE_NETWORK,
       CacheMode.LOAD_NO_CACHE,
       CacheMode.LOAD_CACHE_ONLY]
)
@Retention(AnnotationRetention.SOURCE)
internal annotation class CacheModeState

package com.knight.kotlin.library_network.cookie

import android.os.Build
import java.net.CookieStore
import java.net.HttpCookie
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale
import java.util.TimeZone

/**
 * Author:Knight
 * Time:2023/5/4 15:49
 * Description:CookieStoreExtensions
 */

fun HttpCookie.toSetCookieString(): String {
    val expires = if (maxAge != -1L) {
        val dateFormat = SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss z", Locale.UK).apply {
            timeZone = TimeZone.getTimeZone("GMT")
        }

        val calendar = Calendar.getInstance(Locale.UK).apply { set(Calendar.SECOND, maxAge.toInt()) }

        "; expires=${dateFormat.format(calendar.time)}"
    } else {
        ""
    }

    val path = if (path != null) "; path=$path" else ""
    val domain = if (domain != null) "; domain=$domain" else ""
    val secure = if (secure) "; secure" else ""
    val httpOnly = if (Build.VERSION.SDK_INT >= 24) {
        if (isHttpOnly) "; httponly" else ""
    } else {
        ""
    }

    return "$name=$value$expires$path$domain$secure$httpOnly"
}

@Synchronized
fun CookieStore.syncToWebKitCookieManager() {
    val webKitCookieManager = android.webkit.CookieManager.getInstance()

    cookies.forEach {
        val hostUrl = "${if (it.secure) "https" else "http"}://${it.domain}"
        webKitCookieManager.setCookie(hostUrl, it.toSetCookieString())
    }

    if (Build.VERSION.SDK_INT >= 21) {
        webKitCookieManager.flush()
    }
}

@Synchronized
@Suppress("DEPRECATION")
fun android.webkit.CookieManager.removeAll() {
    if (Build.VERSION.SDK_INT >= 21) {
        removeAllCookies(null)
        flush()
    } else {
        removeAllCookie()
    }
}
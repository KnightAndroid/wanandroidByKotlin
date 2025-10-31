package com.peakmain.webview.annotation

/**
 * author ：knight
 * createTime：2025/10/30
 * mail:15015706912@163.com
 * describe：
 */
@Target(
    AnnotationTarget.FUNCTION
)
@Retention(AnnotationRetention.RUNTIME)
annotation class HandlerMethod(
    val path: String
)
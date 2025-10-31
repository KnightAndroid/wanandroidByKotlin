package com.peakmain.webview.annotation


/**
 * author ：knight
 * createTime：2025/10/30
 * mail:15015706912@163.com
 * describe：
 */
@Target(AnnotationTarget.TYPE, AnnotationTarget.CLASS)
@Retention(AnnotationRetention.RUNTIME)
annotation class Handler(
    /**
     * 申明Scheme
     * @return String
     */
    val scheme: String,
    /**
     * 申明Authority
     * @return String
     */
    val authority: Array<String>
)
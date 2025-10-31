package com.peakmain.webview.sealed

/**
 * author ：knight
 * createTime：2025/08/21
 * mail:15015706912@163.com
 * describe：
 */
sealed class HandleResult {

    /**
     * 没有处理完成
     */
    object NotConsume : HandleResult()

    /**
     * 已经处理完成
     */
    object Consumed : HandleResult()

    /**
     * 正在处理中
     */
    object Consuming : HandleResult()
}
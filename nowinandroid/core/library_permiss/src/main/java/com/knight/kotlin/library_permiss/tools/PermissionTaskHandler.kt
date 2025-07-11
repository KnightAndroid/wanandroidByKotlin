package com.knight.kotlin.library_permiss.tools

import android.os.Handler
import android.os.Looper
import android.os.SystemClock


/**
 * @Description
 * @Author knight
 * @Time 2025/6/8 19:53
 *
 */

internal object PermissionTaskHandler {
    /** Handler 对象  */
    private val HANDLER: Handler = Handler(Looper.getMainLooper())

    /**
     * 延迟发送一个任务
     */
    fun sendTask( runnable: Runnable, delayMillis: Long) {
        HANDLER.postDelayed(runnable, delayMillis)
    }

    /**
     * 延迟发送一个指定令牌的任务
     */
    fun sendTask( runnable: Runnable, token: Any?, delayMillis: Long) {
        var delayMillis = delayMillis
        if (delayMillis < 0) {
            delayMillis = 0
        }
        val uptimeMillis = SystemClock.uptimeMillis() + delayMillis
        HANDLER.postAtTime(runnable, token, uptimeMillis)
    }

    /**
     * 取消一个指定的令牌任务
     */
    fun cancelTask( token: Any) {
        // 移除和当前对象相关的消息回调
        HANDLER.removeCallbacksAndMessages(token)
    }
}
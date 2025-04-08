package com.knight.kotlin.library_util

import android.os.Handler
import android.os.Looper

/**
 * @Description
 * @Author knight
 * @Time 2025/4/8 21:49
 *
 */

object HandlerUtils {
    private val handler = Handler(Looper.getMainLooper())
    private var runnable: Runnable? = null

    /**
     * 延迟执行 Runnable，并返回一个 Runnable 用于取消
     *
     * @param action      需要执行的 Runnable
     * @param delayMillis 延迟时间 (毫秒)
     * @return 用于取消 Runnable 的 Runnable
     */
    fun postDelayed(action: Runnable, delayMillis: Long): Runnable {
        runnable?.let { handler.removeCallbacks(it) }

        runnable = Runnable {
            action.run()
            runnable = null
        }

        handler.postDelayed(runnable!!, delayMillis)

        // 返回一个 Runnable，用于取消延迟执行
        return Runnable {
            runnable?.let {
                handler.removeCallbacks(it)
                runnable = null
            }
        }
    }
}
package com.knight.kotlin.library_util

import android.os.Handler
import android.os.Looper
import java.util.concurrent.Executors

/**
 * Author:Knight
 * Time:2024/4/19 14:54
 * Description:ThreadUtils
 */
object ThreadUtils {

    private val HANDLER = Handler(Looper.getMainLooper())

    private val pool = Executors.newCachedThreadPool()
    private val singlePool = Executors.newSingleThreadExecutor()

    /**
     * 在主线程中执行
     *
     * @param r
     * @return
     */
    fun postMain(r: Runnable): Boolean {
        return HANDLER.post(r)
    }

    /**
     * 在主线程中执行
     *
     * @param r
     * @return
     */
    fun postMainDelayed(r: Runnable, delayMillis: Long): Boolean {
        return HANDLER.postDelayed(r, delayMillis)
    }



    fun execute(runnable: Runnable?) {
        pool.execute(runnable)
    }

    fun singleExecute(runnable: Runnable?) {
        singlePool.execute(runnable)
    }
}
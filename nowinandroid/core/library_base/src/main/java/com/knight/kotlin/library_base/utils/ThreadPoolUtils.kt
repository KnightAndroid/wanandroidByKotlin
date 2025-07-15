package com.knight.kotlin.library_base.utils

import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

/**
 * Author:Knight
 * Time:2022/1/12 10:55
 * Description:ThreadPoolUtils
 */
class ThreadPoolUtils {

    companion object {
        private val pool: ExecutorService = Executors.newCachedThreadPool()
        private val singlePool: ExecutorService = Executors.newSingleThreadExecutor()

        fun execute(runnable: Runnable?) {
            pool.execute(runnable)
        }

        fun singleExecute(runnable: Runnable?) {
            singlePool.execute(runnable)
        }
    }
}
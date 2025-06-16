package com.knight.kotlin.library_util.toast

import android.os.Handler
import android.os.Looper
import android.os.Message
import android.view.WindowManager

/**
 * Author:Knight
 * Time:2021/12/17 18:17
 * Description:SafeHandler
 */
class SafeHandler constructor(handler : Handler): Handler(Looper.getMainLooper()) {
    private val mHandler = handler

    @Override
    override fun handleMessage(msg: Message) {
        // 捕获这个异常，避免程序崩溃
        try {
            // 目前发现在 Android 7.1 主线程被阻塞之后弹吐司会导致崩溃，可使用 Thread.sleep(5000) 进行复现
            // 查看源码得知 Google 已经在 Android 8.0 已经修复了此问题
            // 主线程阻塞之后 Toast 也会被阻塞，Toast 因为显示超时导致 Window Token 失效
            mHandler.handleMessage(msg)
        } catch (e: WindowManager.BadTokenException) {
            // android.view.WindowManager$BadTokenException：Unable to add window -- token android.os.BinderProxy is not valid; is your activity running?
            // java.lang.IllegalStateException：View android.widget.TextView has already been added to the window manager.
            e.printStackTrace()
        } catch (e: IllegalStateException) {
            e.printStackTrace()
        }
    }

}
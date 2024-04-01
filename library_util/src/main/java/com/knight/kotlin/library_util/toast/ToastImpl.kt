package com.knight.kotlin.library_util.toast

import android.R
import android.app.Activity
import android.content.Context
import android.graphics.PixelFormat
import android.os.Build
import android.os.Handler
import android.os.Looper
import android.view.WindowManager
import android.view.WindowManager.BadTokenException
import android.widget.Toast
import com.knight.kotlin.library_util.toast.callback.ToastInterface


/**
 * Author:Knight
 * Time:2021/12/17 14:25
 * Description:ToastImpl
 */
class ToastImpl constructor(activity: Activity,toast:ToastInterface) {

    private val HANDLER = Handler(Looper.getMainLooper())
    /**
     * 短吐司显示的时长
     */
    private val SHORT_DURATION_TIMEOUT = 2000L

    /**
     *
     * 长吐司显示的时长
     */
    private val LONG_DURATION_TIMEOUT = 3500L

    /**
     *
     * 当前的吐司对象
     */
    private val mToast:ToastInterface = toast

    /**
     * WindowManager 辅助类
     */
    private val mWindowLifecycle:WindowLifecycle = WindowLifecycle(activity)

    /**
     * 当前应用的包名
     *
     */
    private val mPackgeName = activity.packageName

    /**
     *
     * 当前是否已经显示
     */
    private var mShow = false

    /**
     *
     * 显示吐司弹窗
     */
    fun show() {
        if (mShow) {
            return
        }
        HANDLER.removeCallbacks(mShowRunnable)
        HANDLER.post(mShowRunnable)
    }

    /**
     *
     * 取消吐司弹窗
     */
    fun cancel() {
        if (!mShow) {
            return
        }
        //移除之前移除吐司的任务
        HANDLER.removeCallbacks(mCancelRunnable)
        //HANDLER.post(mCancelRunnable)
    }

    private val mShowRunnable = Runnable {
        val activity: Activity = mWindowLifecycle.mActivity
        if (activity == null || activity.isFinishing) {
            return@Runnable
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1 && activity.isDestroyed) {
            return@Runnable
        }
        val params = WindowManager.LayoutParams()
        params.height = WindowManager.LayoutParams.WRAP_CONTENT
        params.width = WindowManager.LayoutParams.WRAP_CONTENT
        params.format = PixelFormat.TRANSLUCENT
        params.windowAnimations = R.style.Animation_Toast
        params.flags = (WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON
                or WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
                or WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
        params.packageName = mPackgeName
        params.gravity = mToast.getGravity()
        params.x = mToast.getXOffset()
        params.y = mToast.getYOffset()
        params.verticalMargin = mToast.getVerticalMargin()
        params.horizontalMargin = mToast.getHorizontalMargin()
        val windowManager = activity.getSystemService(Context.WINDOW_SERVICE) as WindowManager
        try {
            windowManager.addView(mToast.getView(), params)
            // 添加一个移除吐司的任务
            HANDLER.postDelayed(
                Runnable { cancel() },
                if (mToast.getDuration() == Toast.LENGTH_LONG) LONG_DURATION_TIMEOUT else SHORT_DURATION_TIMEOUT.toLong()
            )
            // 注册生命周期管控
            mWindowLifecycle.register(this@ToastImpl)
            // 当前已经显示
            mShow = true
        } catch (e: IllegalStateException) {
            // 如果这个 View 对象被重复添加到 WindowManager 则会抛出异常
            // java.lang.IllegalStateException: View android.widget.TextView has already been added to the window manager.
            // 如果 WindowManager 绑定的 Activity 已经销毁，则会抛出异常
            // android.view.WindowManager$BadTokenException: Unable to add window -- token android.os.BinderProxy@ef1ccb6 is not valid; is your activity running?
            e.printStackTrace()
        } catch (e: BadTokenException) {
            e.printStackTrace()
        }
    }

    private val mCancelRunnable = Runnable {
        try {
            val activity: Activity = mWindowLifecycle.mActivity
            val windowManager =
                activity.getSystemService(Context.WINDOW_SERVICE) as WindowManager
            windowManager.removeViewImmediate(mToast.getView())
        } catch (e: IllegalArgumentException) {
            // 如果当前 WindowManager 没有附加这个 View 则会抛出异常
            // java.lang.IllegalArgumentException: View=android.widget.TextView not attached to window manager
            e.printStackTrace()
        } finally {
            // 反注册生命周期管控
            mWindowLifecycle.unregister()
            // 当前没有显示
            mShow = false
        }
    }


}
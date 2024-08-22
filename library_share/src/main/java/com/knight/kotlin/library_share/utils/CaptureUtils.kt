package com.knight.kotlin.library_share.utils

import android.app.Activity
import android.graphics.Bitmap
import android.graphics.Rect


/**
 * @author created by luguian
 * @organize
 * @Date 2024/8/22 16:49
 * @descript:
 */
object CaptureUtils {

    /**
     * 截取当前窗体的截图，根据[isShowStatusBar]判断是否包含当前窗体的状态栏
     * 原理是获取当前窗体decorView的缓存生成图片
     */
    fun captureWindow(activity: Activity, isShowStatusBar: Boolean): Bitmap? {
        // 获取当前窗体的View对象
        val view = activity.window.decorView
        view.isDrawingCacheEnabled = true
        // 生成缓存
        view.buildDrawingCache()

        val bitmap = if (isShowStatusBar) {
            // 绘制整个窗体，包括状态栏
            Bitmap.createBitmap(view.drawingCache, 0, 0, view.measuredWidth, view.measuredHeight)
        } else {
            // 获取状态栏高度
            val rect = Rect()
            view.getWindowVisibleDisplayFrame(rect)
            val display = activity.windowManager.defaultDisplay

            // 减去状态栏高度
            Bitmap.createBitmap(view.drawingCache, 0,
                rect.top, display.width, display.height - rect.top)
        }

        view.isDrawingCacheEnabled = false
        view.destroyDrawingCache()

        return bitmap
    }
}
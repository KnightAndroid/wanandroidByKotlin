package com.knight.kotlin.library_util

import android.app.Activity
import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Rect
import android.util.Log
import android.view.View
import com.core.library_base.ktx.screenHeight
import com.core.library_base.ktx.screenWidth

/**
 * Author:Knight
 * Time:2022/3/8 18:31
 * Description:BlurBuilderUtils
 */
object BlurBuilderUtils {

    private const val BITMAP_SCALE = 0.6f
    private const val BLUR_RADIUS = 25f

    private var tab_bg: Bitmap? = null
    private var overlay: Bitmap? = null
    private var blurFlag = false

    fun isBlurFlag(): Boolean {
        return blurFlag
    }

    fun setBlurFlag(mBlurFlag: Boolean) {
       blurFlag = mBlurFlag
    }

    fun blur(v: View): Bitmap? {
        if (tab_bg == null) {
            Log.i("", "tab_bg == null")
            blurFlag = false
            return null
        }
        blurFlag = true
        blur(v.context, tab_bg!!)
        return overlay
    }

    fun blur(ctx: Context?, image: Bitmap) {
        if (overlay != null) {
            recycle()
        }
        try {
            val width = Math.round(image.width * BITMAP_SCALE)
            val height = Math.round(image.height * BITMAP_SCALE)
            overlay = Bitmap.createScaledBitmap(
                image, width,
                height, false
            )
            overlay = FastBlurUtils.doBlur(overlay!!, BLUR_RADIUS.toInt(), true)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun getScreenshot(v: View) {
        if (tab_bg != null) {
            recycle()
        }
        try {
            tab_bg = Bitmap.createBitmap(
                v.width, v.height,
                Bitmap.Config.RGB_565
            )
            tab_bg?.let {
                val c = Canvas(it)
                v.draw(c)
            }


        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    /**
     * 获取当前屏幕截图，不包含状态栏
     *
     * @param activity
     * @return
     */
    fun snapShotWithoutStatusBar(activity: Activity) {
        if (tab_bg != null) {
            recycle()
        }
        val view = activity.window.decorView
        try {
            view.buildDrawingCache()
            tab_bg = view.drawingCache
            val frame = Rect()
            activity.window.decorView
                .getWindowVisibleDisplayFrame(frame)
            val statusBarHeight = frame.top
            val width: Int = activity.screenWidth
            val height: Int = activity.screenHeight

            tab_bg = Bitmap.createBitmap(
                tab_bg!!, 0, statusBarHeight, width,
                height - statusBarHeight
            )
            view.destroyDrawingCache()
        } catch (e: Exception) {
            e.printStackTrace()
            getScreenshot(view)
        }
    }

    fun recycle() {
        try {
            tab_bg?.let {
                it.recycle()
                System.gc()
                tab_bg = null
            }
            overlay?.let {
                it.recycle()
                System.gc()
                overlay = null
            }

        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}
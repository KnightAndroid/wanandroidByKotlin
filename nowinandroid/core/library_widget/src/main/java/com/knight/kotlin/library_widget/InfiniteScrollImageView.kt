package com.knight.kotlin.library_widget

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.BitmapShader
import android.graphics.Canvas
import android.graphics.Matrix
import android.graphics.Paint
import android.graphics.Shader
import android.util.AttributeSet
import android.view.View
import androidx.annotation.DrawableRes


/**
 * @author created by luguian
 * @organize
 * @Date 2025/6/18 17:23
 * @descript:
 */
class InfiniteScrollImageView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null
) : View(context, attrs) {

    private var bitmap: Bitmap? = null
    private var shader: BitmapShader? = null
    private val paint = Paint(Paint.ANTI_ALIAS_FLAG)
    private val matrix = Matrix()
    private var offset = 0f
    var scrollSpeed = 1f

    private val frameDelay = 16L

    private val scrollRunnable = object : Runnable {
        override fun run() {
            offset += scrollSpeed
            invalidate()
            postDelayed(this, frameDelay)
        }
    }

    fun setBitmapResource(@DrawableRes resId: Int) {
        bitmap = BitmapFactory.decodeResource(resources, resId)
        bitmap?.let {
            shader = BitmapShader(it, Shader.TileMode.REPEAT, Shader.TileMode.CLAMP)
            paint.shader = shader
        }
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        shader?.let {
            matrix.setTranslate(-offset, 0f)
            it.setLocalMatrix(matrix)
            paint.shader = it
            canvas.drawRect(0f, 0f, width.toFloat(), height.toFloat(), paint)
        }
    }

    fun startScroll() {
        removeCallbacks(scrollRunnable)
        post(scrollRunnable)
    }

    fun stopScroll() {
        removeCallbacks(scrollRunnable)
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        stopScroll()
    }
}
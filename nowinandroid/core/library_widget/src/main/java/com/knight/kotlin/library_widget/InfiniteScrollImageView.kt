package com.knight.kotlin.library_widget

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.BitmapShader
import android.graphics.Canvas
import android.graphics.Matrix
import android.graphics.Paint
import android.graphics.Path
import android.graphics.Region
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

    // === 避让区域 ===
    private var avoidCx = -1f
    private var avoidCy = -1f
    private var avoidRadius = -1f
    private val path = Path()

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

    /**
     * 设置要避开的圆形区域
     */
    fun setAvoidCircle(cx: Float, cy: Float, radius: Float) {
        avoidCx = cx
        avoidCy = cy
        avoidRadius = radius
        invalidate()
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        shader?.let {
            matrix.setTranslate(-offset, 0f)
            it.setLocalMatrix(matrix)
            paint.shader = it

            canvas.save()

            // 如果设置了避让圆，则剪掉这部分区域
            if (avoidCx > 0 && avoidCy > 0 && avoidRadius > 0) {
                path.reset()
                path.addCircle(avoidCx, avoidCy, avoidRadius, Path.Direction.CCW)

                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.P) {
                    canvas.clipOutPath(path)
                } else {
                    @Suppress("DEPRECATION")
                    canvas.clipPath(path, Region.Op.DIFFERENCE)
                }
            }

            // 绘制云图
            canvas.drawRect(0f, 0f, width.toFloat(), height.toFloat(), paint)

            canvas.restore()
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
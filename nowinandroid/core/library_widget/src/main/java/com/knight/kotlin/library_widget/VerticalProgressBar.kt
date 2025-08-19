package com.knight.kotlin.library_widget

/**
 * @Description
 * @Author knight
 * @Time 2025/8/19 20:55
 *
 */

import android.animation.ValueAnimator
import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.RectF
import android.util.AttributeSet
import android.view.View
import androidx.core.content.ContextCompat

class VerticalProgressBar @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    private var progress: Int = 0
    private var max: Int = 100

    private var progressColor: Int
    private var backgroundColor: Int
    private var cornerRadius: Float

    private val paint = Paint(Paint.ANTI_ALIAS_FLAG)
    private var animator: ValueAnimator? = null

    init {
        val typedArray = context.obtainStyledAttributes(attrs, R.styleable.VerticalProgressBar)

        progress = typedArray.getInt(R.styleable.VerticalProgressBar_vertical_progress, 0)
        max = typedArray.getInt(R.styleable.VerticalProgressBar_vertical_max, 100)
        progressColor = typedArray.getColor(
            R.styleable.VerticalProgressBar_vertical_progressColor,
            ContextCompat.getColor(context, android.R.color.holo_blue_light)
        )
        backgroundColor = typedArray.getColor(
            R.styleable.VerticalProgressBar_vertical_backgroundColorCustom,
            ContextCompat.getColor(context, android.R.color.darker_gray)
        )
        cornerRadius = typedArray.getDimension(R.styleable.VerticalProgressBar_vertical_cornerRadius, 20f)

        typedArray.recycle()
    }

    fun setProgress(value: Int, animated: Boolean = true) {
        val target = when {
            value < 0 -> 0
            value > max -> max
            else -> value
        }

        if (animated) {
            animator?.cancel()
            animator = ValueAnimator.ofInt(progress, target).apply {
                duration = 500
                addUpdateListener { animation ->
                    progress = animation.animatedValue as Int
                    invalidate()
                }
                start()
            }
        } else {
            progress = target
            invalidate()
        }
    }

    fun setMax(value: Int) {
        if (value > 0) {
            max = value
            if (progress > max) progress = max
            invalidate()
        }
    }

    fun setProgressColor(color: Int) {
        progressColor = color
        invalidate()
    }

    fun setBackgroundColorCustom(color: Int) {
        backgroundColor = color
        invalidate()
    }

    fun setCornerRadius(radius: Float) {
        cornerRadius = radius
        invalidate()
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        val width = width.toFloat()
        val height = height.toFloat()

        // 背景
        val rect = RectF(0f, 0f, width, height)
        paint.color = backgroundColor
        canvas.drawRoundRect(rect, cornerRadius, cornerRadius, paint)

        // 进度条（从下往上）
        val progressHeight = (progress / max.toFloat()) * height
        val progressRect = RectF(0f, height - progressHeight, width, height)
        paint.color = progressColor
        canvas.drawRoundRect(progressRect, cornerRadius, cornerRadius, paint)
    }
}

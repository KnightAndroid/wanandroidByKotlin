package com.knight.kotlin.library_widget

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
import android.util.AttributeSet
import android.view.View
import androidx.annotation.ColorInt
import com.core.library_base.util.dp2px


/**
 * @author created by luguian
 * @organize
 * @Date 2025/4/16 11:06
 * @descript:
 */
class RoundProgress @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0,
) : View(context, attrs, defStyleAttr) {
    private val mProgressPaint = Paint().apply {
        isAntiAlias = true
        style = Paint.Style.FILL
        strokeCap = Paint.Cap.ROUND
    }
    private val mBackgroundRectF = RectF()
    private val mProgressRectF = RectF()
    private var mProgress = 0f
    private var mMax = 100f

    @ColorInt
    private var mProgressColor = Color.BLACK

    @ColorInt
    private var mBackgroundColor = Color.GRAY

    var progress: Float
        get() = mProgress
        set(progress) {
            mProgress = progress
            if (mProgress > max) {
                mProgress = max
            }
            invalidate()
        }
    var max: Float
        get() = mMax
        set(max) {
            if (max > 0) {
                mMax = max
                invalidate()
            }
        }

    fun setProgressColor(@ColorInt progressColor: Int) {
        mProgressColor = progressColor
        invalidate()
    }

    fun setProgressBackgroundColor(@ColorInt backgroundColor: Int) {
        mBackgroundColor = backgroundColor
        invalidate()
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        val padding = 2f.dp2px()
        mBackgroundRectF.set(
            padding.toFloat(),
            padding.toFloat(),
            (measuredWidth - padding).toFloat(),
            (measuredHeight - padding).toFloat()
        )
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        val radius = mBackgroundRectF.height() / 2f
        mProgressPaint.color = mBackgroundColor
        canvas.drawRoundRect(mBackgroundRectF, radius, radius, mProgressPaint)

        mProgressRectF.set(
            mBackgroundRectF.left,
            mBackgroundRectF.top,
            mBackgroundRectF.left + mBackgroundRectF.width() * mProgress / mMax,
            mBackgroundRectF.bottom
        )
        mProgressPaint.color = mProgressColor
        if (mProgressRectF.width() < 2 * radius) {
            canvas.drawCircle(
                mProgressRectF.left + radius,
                mProgressRectF.top + radius,
                radius,
                mProgressPaint
            )
        } else {
            canvas.drawRoundRect(mProgressRectF, radius, radius, mProgressPaint)
        }
    }
}
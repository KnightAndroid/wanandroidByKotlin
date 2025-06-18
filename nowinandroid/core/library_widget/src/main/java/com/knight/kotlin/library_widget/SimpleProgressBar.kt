package com.knight.kotlin.library_widget

import android.animation.ValueAnimator
import android.content.Context
import android.content.res.TypedArray
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Path
import android.util.AttributeSet
import android.view.View
import android.view.animation.AccelerateDecelerateInterpolator


/**
 * @author created by luguian
 * @organize
 * @Date 2025/6/18 11:17
 * @descript:简易进度条
 */
class SimpleProgressBar(context: Context, attrs: AttributeSet) : View(context,attrs) {
    private val backgroundPaint = Paint()
    private val progressPaint = Paint()
    private var progress: Int = 0
    private val pathBackground = Path()
    private val pathProgress = Path()
    private var mStroke = 0f

    private val ta: TypedArray
    private lateinit var progressAnimator: ValueAnimator

    init {
        // Initialize paints
        ta = context.theme.obtainStyledAttributes(
            attrs,
            R.styleable.SimpleProgressBar,0,0
        )
        mStroke = ta.getFloat(R.styleable.SimpleProgressBar_simplestroke, DEFAULT_STROKE)
        progress = ta.getInt(R.styleable.SimpleProgressBar_simpleprogress,0)
        backgroundPaint.color = ta.getColor(R.styleable.SimpleProgressBar_simplebackgroundcolor, Color.GRAY)
        backgroundPaint.style = Paint.Style.STROKE
        backgroundPaint.strokeWidth = mStroke
        backgroundPaint.strokeCap = Paint.Cap.ROUND

        progressPaint.color = ta.getColor(R.styleable.SimpleProgressBar_simpleprogresscolor, Color.GREEN)
        progressPaint.style = Paint.Style.STROKE
        progressPaint.strokeWidth = mStroke
        progressPaint.strokeCap = Paint.Cap.ROUND
        progressAnimator = ValueAnimator()
        ta.recycle()
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        val width = width.toFloat() ?: 0f
        val height = height.toFloat() ?: 0f

        // Calculate progress width based on the current progress
        val progressWidth = width * (progress / 100f)
        val margin = mStroke / 2 // Adjust the margin as needed

        // Set stroke cap to round for both paints
        backgroundPaint.strokeCap = Paint.Cap.ROUND
        progressPaint.strokeCap = Paint.Cap.ROUND

        // Create the path for the background
        pathBackground.reset()
        pathBackground.moveTo(margin, height / 2)
        pathBackground.lineTo(width -margin, height / 2)

        // Create the path for the progress with margins
        pathProgress.reset()
        pathProgress.moveTo(margin, height / 2)
        pathProgress.lineTo(progressWidth - margin, height / 2)

        // Draw background
        canvas.drawPath(pathBackground, backgroundPaint)

        // Draw progress
        canvas.drawPath(pathProgress, progressPaint)
    }


    fun setProgress(value: Int) {
        progressAnimator.cancel() // Cancel any ongoing animator

        progressAnimator = ValueAnimator.ofInt(progress, value)
        progressAnimator.duration = 1000 // Animation duration in milliseconds
        progressAnimator.interpolator = AccelerateDecelerateInterpolator()
        progressAnimator.addUpdateListener { animator ->
            val animatedValue = animator.animatedValue as Int
            progress = animatedValue
            invalidate() // Redraw the view
        }
        progressAnimator.start()
    }
    companion object {
        val DEFAULT_STROKE = 30f
    }
}
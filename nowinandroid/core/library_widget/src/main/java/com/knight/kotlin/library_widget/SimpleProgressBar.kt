package com.knight.kotlin.library_widget

import android.animation.ValueAnimator
import android.content.Context
import android.content.res.TypedArray
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Path
import android.graphics.Typeface
import android.util.AttributeSet
import android.view.View
import android.view.animation.AccelerateDecelerateInterpolator


/**
 * @author created by luguian
 * @organize
 * @Date 2025/6/18 11:17
 * @descript:简易进度条
 */
class SimpleProgressBar(context: Context, attrs: AttributeSet) : View(context, attrs) {

    private val backgroundPaint = Paint(Paint.ANTI_ALIAS_FLAG)
    private val progressPaint = Paint(Paint.ANTI_ALIAS_FLAG)
    private val textPaint = Paint(Paint.ANTI_ALIAS_FLAG)

    private var progress: Int = 0
    private var showText: Boolean = true  // 新增控制是否显示文字
    private var text = "正在初始化..."
    private val pathBackground = Path()
    private val pathProgress = Path()
    private var mStroke = 0f

    private val ta: TypedArray
    private lateinit var progressAnimator: ValueAnimator
    // 新增属性控制左端是否直角
    private var leftCornerStraight: Boolean = false

    init {
        ta = context.theme.obtainStyledAttributes(
            attrs,
            R.styleable.SimpleProgressBar, 0, 0
        )

        mStroke = ta.getFloat(R.styleable.SimpleProgressBar_simplestroke, DEFAULT_STROKE)
        progress = ta.getInt(R.styleable.SimpleProgressBar_simpleprogress, 0)
        showText = ta.getBoolean(R.styleable.SimpleProgressBar_simpleShowText, true)  // 读取属性
        leftCornerStraight = ta.getBoolean(R.styleable.SimpleProgressBar_simpleLeftCornerStraight, false)
        backgroundPaint.color = ta.getColor(R.styleable.SimpleProgressBar_simplebackgroundcolor, Color.GRAY)
        backgroundPaint.style = Paint.Style.STROKE
        backgroundPaint.strokeWidth = mStroke
        backgroundPaint.strokeCap = Paint.Cap.ROUND

        progressPaint.color = ta.getColor(R.styleable.SimpleProgressBar_simpleprogresscolor, Color.GREEN)
        progressPaint.style = Paint.Style.STROKE
        progressPaint.strokeWidth = mStroke
       // progressPaint.strokeCap = Paint.Cap.ROUND

        textPaint.color = Color.WHITE
        textPaint.textSize = 36f
        textPaint.textAlign = Paint.Align.CENTER
        textPaint.typeface = Typeface.DEFAULT_BOLD

        ta.recycle()
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val density = context.resources.displayMetrics.density
        val defaultHeightPx = (40 * density).toInt()

        val width = MeasureSpec.getSize(widthMeasureSpec)
        val height = when (MeasureSpec.getMode(heightMeasureSpec)) {
            MeasureSpec.EXACTLY -> MeasureSpec.getSize(heightMeasureSpec)
            MeasureSpec.AT_MOST, MeasureSpec.UNSPECIFIED -> defaultHeightPx
            else -> defaultHeightPx
        }

        setMeasuredDimension(width, height)
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        val width = width.toFloat()
        val height = height.toFloat()
        val margin = mStroke / 2f

        val progressWidth = width * (progress / 100f)
        val maxProgressEnd = width - margin
        val safeProgressWidth = progressWidth.coerceIn(margin, maxProgressEnd)

        // ==== 1. 背景线绘制 ====
        if (leftCornerStraight) {
            // 1.1 左侧直角段
            backgroundPaint.strokeCap = Paint.Cap.BUTT
            pathBackground.reset()
            pathBackground.moveTo(margin, height / 2f)
            pathBackground.lineTo(width - margin - mStroke / 2f, height / 2f)
            canvas.drawPath(pathBackground, backgroundPaint)

            // 1.2 右侧圆角段
            backgroundPaint.strokeCap = Paint.Cap.ROUND
            pathBackground.reset()
            pathBackground.moveTo(width - margin - mStroke / 2f, height / 2f)
            pathBackground.lineTo(width - margin, height / 2f)
            canvas.drawPath(pathBackground, backgroundPaint)
        } else {
            backgroundPaint.strokeCap = Paint.Cap.ROUND
            pathBackground.reset()
            pathBackground.moveTo(margin, height / 2f)
            pathBackground.lineTo(width - margin, height / 2f)
            canvas.drawPath(pathBackground, backgroundPaint)
        }

        // ==== 2. 进度线绘制 ====
        if (safeProgressWidth > margin) {
            pathProgress.reset()
            val originalStyle = progressPaint.style

            if (leftCornerStraight) {
                // 左端直角
                progressPaint.strokeCap = Paint.Cap.BUTT
                progressPaint.style = Paint.Style.STROKE
                pathProgress.moveTo(margin, height / 2f)
                pathProgress.lineTo(safeProgressWidth, height / 2f)
                canvas.drawPath(pathProgress, progressPaint)

                // 右端圆角，绘制一个圆点
                progressPaint.style = Paint.Style.FILL
                canvas.drawCircle(safeProgressWidth, height / 2f, mStroke / 2f, progressPaint)
                progressPaint.style = originalStyle
            } else {
                // 左右都是圆角
                progressPaint.strokeCap = Paint.Cap.ROUND
                progressPaint.style = Paint.Style.STROKE
                pathProgress.moveTo(margin, height / 2f)
                pathProgress.lineTo(safeProgressWidth, height / 2f)
                canvas.drawPath(pathProgress, progressPaint)
            }
        }

        // ==== 3. 文字绘制 ====
        if (showText) {
            val fontMetrics = textPaint.fontMetrics
            val baseline = height / 2f - (fontMetrics.ascent + fontMetrics.descent) / 2f
            canvas.drawText(text, width / 2f, baseline, textPaint)
        }
    }

    fun setProgressWithText(value: Int, text: String) {
        if (this::progressAnimator.isInitialized) {
            progressAnimator.cancel()
        }
        progressAnimator = ValueAnimator.ofInt(progress, value.coerceIn(0, 100)).apply {
            duration = 1000
            interpolator = AccelerateDecelerateInterpolator()
            addUpdateListener { animator ->
                progress = animator.animatedValue as Int
                this@SimpleProgressBar.text = text
                invalidate()
            }
            start()
        }
    }
    fun setShowText(show: Boolean) {
        showText = show
        invalidate()
    }

    fun setText(text:String) {
        this.text = text
        invalidate()
    }

    companion object {
        const val DEFAULT_STROKE = 30f
    }
}
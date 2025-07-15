package com.knight.kotlin.library_widget.weatherview

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.LinearGradient
import android.graphics.Paint
import android.graphics.RectF
import android.graphics.Shader
import android.text.TextPaint
import android.util.AttributeSet
import android.view.View
import androidx.annotation.ColorInt
import androidx.annotation.Size
import androidx.core.graphics.ColorUtils
import com.core.library_base.util.getTypefaceFromTextAppearance
import com.core.library_common.util.dp2px
import com.knight.kotlin.library_widget.R
import com.knight.kotlin.library_widget.weatherview.sunmoon.DayNightShaderWrapper
import kotlin.math.cos


/**
 * @author created by luguian
 * @organize
 * @Date 2025/4/15 15:02
 * @descript:空气质量进度圈
 */
class ArcProgress @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0,
) : View(context, attrs, defStyleAttr) {
    private val mProgressPaint: Paint
    private val mShadowPaint: Paint
    private val mCenterTextPaint: Paint
    private val mBottomTextPaint: Paint
    private val mShaderWrapper: DayNightShaderWrapper
    private val mRectF = RectF()
    private var mArcBottomHeight = 0f
    private var mProgress: Float
    private var mProgressMaxed: Float
    private var mMax: Float
    private val mArcAngle: Float
    private val mProgressWidth: Float

    @ColorInt
    private var mProgressColor: Int

    @ColorInt
    private var mShadowColor: Int

    @ColorInt
    private var mShaderColor: Int

    @ColorInt
    private var mBackgroundColor: Int
    private var mText: String?
    private val mTextSize: Float

    @ColorInt
    private var mTextColor: Int

    @Size(2)
    private val mShaderColors: IntArray
    private var mBottomText: String?
    private val mBottomTextSize: Float

    @ColorInt
    private var mBottomTextColor: Int
    private var drawStatus:ArcProgressDrawStatus = ArcProgressDrawStatus .NOTDRAW //0没开始绘制 1开始绘制 2绘制完成
    init {
        val attributes = context.theme
            .obtainStyledAttributes(attrs, R.styleable.ArcProgress, defStyleAttr, 0)
        mProgress = attributes.getInt(R.styleable.ArcProgress_progress, 0).toFloat()
        mProgressMaxed = attributes.getInt(R.styleable.ArcProgress_progress, 0).toFloat()
        mMax = attributes.getInt(R.styleable.ArcProgress_max, 100).toFloat()
        mArcAngle = attributes.getFloat(R.styleable.ArcProgress_arc_angle, 360 * 0.8f)
        mProgressWidth = attributes.getDimension(R.styleable.ArcProgress_progress_width, 8f.dp2px())
        mProgressColor = attributes.getColor(R.styleable.ArcProgress_progress_color, Color.BLACK)
        mShadowColor = Color.argb((0.2 * 255).toInt(), 0, 0, 0)
        mShaderColor = Color.argb((0.2 * 255).toInt(), 0, 0, 0)
        mBackgroundColor = attributes.getColor(R.styleable.ArcProgress_background_color, Color.GRAY)
        mText = attributes.getString(R.styleable.ArcProgress_text)
        mTextSize = attributes.getDimension(R.styleable.ArcProgress_text_size, 36f.dp2px())
        mTextColor = attributes.getColor(R.styleable.ArcProgress_text_color, Color.DKGRAY)
        mBottomText = attributes.getString(R.styleable.ArcProgress_bottom_text)
        mBottomTextSize = attributes.getDimension(
            R.styleable.ArcProgress_bottom_text_size,
            14f.dp2px()
        )
        mBottomTextColor = attributes.getColor(R.styleable.ArcProgress_bottom_text_color, Color.DKGRAY)
        attributes.recycle()
        mProgressPaint = Paint().apply {
            isAntiAlias = true
            strokeWidth = mProgressWidth
            style = Paint.Style.STROKE
            strokeCap = Paint.Cap.ROUND
        }
        mShadowPaint = Paint().apply {
            isAntiAlias = true
            style = Paint.Style.FILL
        }
        mCenterTextPaint = TextPaint().apply {
            textSize = mTextSize
            isAntiAlias = true
            typeface = getContext().getTypefaceFromTextAppearance(R.style.large_title_text)
        }
        mBottomTextPaint = TextPaint().apply {
            set(mCenterTextPaint)
            typeface = getContext().getTypefaceFromTextAppearance(R.style.content_text)
        }
        mShaderColors = intArrayOf(Color.BLACK, Color.WHITE)
        mShaderWrapper = DayNightShaderWrapper(measuredWidth, measuredHeight, lightTheme = true, mShaderColors)
    }

    var progress: Float
        get() = mProgress
        set(progress) {
            mProgress = progress
            mProgressMaxed = progress
            if (mProgressMaxed > max) {
                mProgressMaxed = max
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

    fun setProgressColor(lightTheme: Boolean) {
        setProgressColor(mProgressColor, lightTheme)
    }

    fun setProgressColor(@ColorInt progressColor: Int, lightTheme: Boolean) {
        mProgressColor = progressColor
        mShadowColor = com.core.library_common.util.ColorUtils.getDarkerColor(progressColor)
        mShaderColor = ColorUtils.setAlphaComponent(
            progressColor,
            (255 * if (lightTheme) SHADOW_ALPHA_FACTOR_LIGHT else SHADOW_ALPHA_FACTOR_DARK).toInt()
        )
        invalidate()
    }

    fun setArcBackgroundColor(@ColorInt backgroundColor: Int) {
        mBackgroundColor = backgroundColor
        invalidate()
    }

    fun setText(text: String?) {
        mText = text
    }

    fun setTextColor(@ColorInt textColor: Int) {
        mTextColor = textColor
    }

    fun setBottomText(bottomText: String?) {
        mBottomText = bottomText
    }

    fun setBottomTextColor(@ColorInt bottomTextColor: Int) {
        mBottomTextColor = bottomTextColor
    }

    private fun ensureShadowShader() {
        mShaderColors[0] = mShaderColor
        mShaderColors[1] = Color.TRANSPARENT
        if (mShaderWrapper.isDifferent(measuredWidth, measuredHeight, false, mShaderColors)) {
            mShaderWrapper.setShader(
                LinearGradient(
                    0f,
                    mRectF.top,
                    0f,
                    mRectF.bottom,
                    mShaderColors[0],
                    mShaderColors[1],
                    Shader.TileMode.CLAMP
                ),
                measuredWidth,
                measuredHeight,
                false,
                mShaderColors
            )
        }
    }

    override fun getSuggestedMinimumHeight(): Int {
        return 100f.dp2px().toInt()
    }

    override fun getSuggestedMinimumWidth(): Int {
        return 100f.dp2px().toInt()
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        setMeasuredDimension(widthMeasureSpec, heightMeasureSpec)
        val width = MeasureSpec.getSize(widthMeasureSpec)
        val arcPadding =4f.dp2px().toInt()
        mRectF.set(
            mProgressWidth / 2f + arcPadding,
            mProgressWidth / 2f + arcPadding,
            width - mProgressWidth / 2f - arcPadding,
            MeasureSpec.getSize(heightMeasureSpec) - mProgressWidth / 2f - arcPadding
        )
        val radius = (width - 2 * arcPadding) / 2f
        val angle = (360 - mArcAngle) / 2f
        mArcBottomHeight = radius * (1 - cos(angle / 180 * Math.PI)).toFloat()
        ensureShadowShader()
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        val startAngle = 270 - mArcAngle / 2f
        val progressSweepAngle = (1.0 * mProgressMaxed / max * mArcAngle).toFloat()
        val progressEndAngle = startAngle + progressSweepAngle
        val deltaAngle = (mProgressWidth / 2 / Math.PI / (mRectF.width() / 2) * 180).toFloat()
        if (mProgressMaxed > 0) {
            ensureShadowShader()
            mShadowPaint.setShader(mShaderWrapper.shader)
            if (progressEndAngle + deltaAngle >= 360) {
                canvas.drawCircle(
                    mRectF.centerX(),
                    mRectF.centerY(),
                    mRectF.width() / 2,
                    mShadowPaint
                )
            } else if (progressEndAngle + deltaAngle > 180) {
                canvas.drawArc(
                    mRectF,
                    360 - progressEndAngle - deltaAngle,
                    360 - 2 * (360 - progressEndAngle - deltaAngle),
                    false,
                    mShadowPaint
                )
            }
        }
        mProgressPaint.color = mBackgroundColor
        canvas.drawArc(mRectF, startAngle, mArcAngle, false, mProgressPaint)
        if (mProgressMaxed > 0) {
            mProgressPaint.color = mProgressColor
            canvas.drawArc(mRectF, startAngle, progressSweepAngle, false, mProgressPaint)
        }
        if (!mText.isNullOrEmpty()) {
            mCenterTextPaint.color = mTextColor
            mCenterTextPaint.textSize = mTextSize
            val textHeight = mCenterTextPaint.descent() + mCenterTextPaint.ascent()
            val textBaseline = (height - textHeight) / 2.0f
            canvas.drawText(
                mText!!,
                (width - mCenterTextPaint.measureText(mText)) / 2.0f,
                textBaseline,
                mCenterTextPaint
            )
        }
        if (mArcBottomHeight == 0f) {
            val radius = width / 2f
            val angle = (360 - mArcAngle) / 2f
            mArcBottomHeight = radius * (1 - cos(angle / 180 * Math.PI)).toFloat()
        }
        if (!mBottomText.isNullOrEmpty()) {
            mBottomTextPaint.color = mBottomTextColor
            mBottomTextPaint.textSize = mBottomTextSize
            val bottomTextBaseline = (
                    height +
                            (mBottomTextPaint.descent() + mBottomTextPaint.ascent()) / 2 -
                            mProgressWidth * 0.33f
                    )
            canvas.drawText(
                mBottomText!!,
                (width - mBottomTextPaint.measureText(mBottomText)) / 2.0f,
                bottomTextBaseline,
                mBottomTextPaint
            )
        }
    }

    fun setDrawStatus(drawaStatus: ArcProgressDrawStatus ) {
        this.drawStatus = drawaStatus
    }

    fun getDrawStatus(): ArcProgressDrawStatus  {
        return this.drawStatus
    }


    companion object {
        private const val SHADOW_ALPHA_FACTOR_LIGHT = 0.1f
        private const val SHADOW_ALPHA_FACTOR_DARK = 0.1f
    }

    enum class ArcProgressDrawStatus {
        // 没有绘制
        NOTDRAW,
        // 绘制中
        DRAWING,
        // 绘制完成
        COMPLETE
    }
}
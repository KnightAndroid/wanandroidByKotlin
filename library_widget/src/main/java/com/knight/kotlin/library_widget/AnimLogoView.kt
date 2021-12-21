package com.knight.kotlin.library_widget



import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.ValueAnimator
import android.animation.ValueAnimator.AnimatorUpdateListener
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.LinearGradient
import android.graphics.Matrix
import android.graphics.Paint
import android.graphics.PointF
import android.graphics.Shader
import android.text.TextUtils
import android.util.AttributeSet
import android.util.Log
import android.util.SparseArray
import android.view.View
import android.view.animation.AccelerateDecelerateInterpolator


/**
 * Author:Knight
 * Time:2021/12/21 13:56
 * Description:AnimLogoView
 */
class AnimLogoView : View {
    private val DEFAULT_LOGO = "SEAGAZER"
    private val DEFAULT_TEXT_PADDING = 10
    private val ANIM_LOGO_DURATION = 1500
    private val ANIM_LOGO_GRADIENT_DURATION = 1500
    private val ANIM_LOGO_TEXT_SIZE = 30
    private val ANIM_LOGO_TEXT_COLOR: Int = Color.BLACK
    private val ANIM_LOGO_GRADIENT_COLOR: Int = Color.YELLOW
    private val mLogoTexts = SparseArray<String>()
    private val mQuietPoints = SparseArray<PointF>()
    private val mRadonPoints = SparseArray<PointF>()
    private var mOffsetAnimator: ValueAnimator? = null
    private var mGradientAnimator: ValueAnimator? = null
    private var mPaint: Paint? = null
    private var mTextPadding = 0
    private var mTextColor = 0
    private var mTextSize = 0
    private var mOffsetAnimProgress = 0f
    private var mOffsetDuration = 0
    private var isOffsetAnimEnd = false
    private var mGradientDuration = 0
    private var mLinearGradient: LinearGradient? = null
    private var mGradientColor = 0
    private var mGradientMatrix: Matrix? = null
    private var mMatrixTranslate = 0
    private var isAutoPlay = false
    private var mWidth = 0
    private var mHeight: Int = 0
    private var isShowGradient = false
    private var mLogoOffset = 0
    private var mGradientListener: Animator.AnimatorListener? = null
    @JvmOverloads
    constructor(context: Context, attributeSet: AttributeSet? = null, defAttrStyle: Int = 0)
            : super(context, attributeSet, defAttrStyle) {

        val ta = context.obtainStyledAttributes(attributeSet,R.styleable.AnimLogoView)
        var logoName = ta.getString(R.styleable.AnimLogoView_logoName)
        isAutoPlay = ta.getBoolean(R.styleable.AnimLogoView_autoPlay, true)
        isShowGradient = ta.getBoolean(R.styleable.AnimLogoView_showGradient, false)
        mOffsetDuration = ta.getInt(R.styleable.AnimLogoView_offsetAnimDuration, ANIM_LOGO_DURATION)
        mGradientDuration =
            ta.getInt(R.styleable.AnimLogoView_gradientAnimDuration, ANIM_LOGO_GRADIENT_DURATION)
        mTextColor = ta.getColor(R.styleable.AnimLogoView_textColor, ANIM_LOGO_TEXT_COLOR)
        mGradientColor =
            ta.getColor(R.styleable.AnimLogoView_gradientColor, ANIM_LOGO_GRADIENT_COLOR)
        mTextPadding =
            ta.getDimensionPixelSize(R.styleable.AnimLogoView_textPadding, DEFAULT_TEXT_PADDING)
        mTextSize = ta.getDimensionPixelSize(R.styleable.AnimLogoView_textSize, ANIM_LOGO_TEXT_SIZE)
        mLogoOffset = ta.getDimensionPixelOffset(R.styleable.AnimLogoView_verticalOffset, 0)
        ta.recycle()
        if (TextUtils.isEmpty(logoName)) {
            logoName = DEFAULT_LOGO // default logo
        }

        if (!logoName.isNullOrBlank()){
            fillLogoTextArray(logoName)
        }
        initPaint()
        initOffsetAnimation()
    }

    // fill the text to array
    private fun fillLogoTextArray(logoName: String) {
        if (TextUtils.isEmpty(logoName)) {
            return
        }
        if (mLogoTexts.size() > 0) {
            mLogoTexts.clear()
        }
        for (i in 0 until logoName.length) {
            val c = logoName[i]
            val s = c.toString()
            mLogoTexts.put(i, s)
        }
    }

    private fun initPaint() {
        if (mPaint == null) {
            mPaint = Paint()
            mPaint?.setAntiAlias(true)
            mPaint?.setStyle(Paint.Style.FILL)
            mPaint?.setStrokeCap(Paint.Cap.ROUND)
        }
        mPaint?.setTextSize(mTextSize.toFloat())
        mPaint?.setColor(mTextColor)
    }

    // init the translation animation
    private fun initOffsetAnimation() {
        if (mOffsetAnimator == null) {
            mOffsetAnimator = ValueAnimator.ofFloat(0f, 1f)
            mOffsetAnimator?.setInterpolator(AccelerateDecelerateInterpolator())
            mOffsetAnimator?.addUpdateListener(AnimatorUpdateListener { animation ->
                if (mQuietPoints.size() <= 0 || mRadonPoints.size() <= 0) {
                    return@AnimatorUpdateListener
                }
                mOffsetAnimProgress = animation.animatedValue as Float
                invalidate()
            })
            mOffsetAnimator?.addListener(object : AnimatorListenerAdapter() {
                override fun onAnimationEnd(animation: Animator) {
                    if (mGradientAnimator != null && isShowGradient) {
                        isOffsetAnimEnd = true
                        mPaint?.shader = mLinearGradient
                        mGradientAnimator?.start()
                    }
                }
            })
        }
        mOffsetAnimator!!.duration = mOffsetDuration.toLong()
    }

    // init the gradient animation
    private fun initGradientAnimation(width: Int) {
        if (width == 0) {
            Log.w(this.javaClass.simpleName, "The view has not measure, it will auto init later.")
            return
        }
        if (mGradientAnimator == null) {
            mGradientAnimator = ValueAnimator.ofInt(0, 2 * width)
            if (mGradientListener != null) {
                mGradientAnimator?.addListener(mGradientListener)
            }
            mGradientAnimator?.addUpdateListener(AnimatorUpdateListener { animation ->
                mMatrixTranslate = animation.animatedValue as Int
                invalidate()
            })
            mLinearGradient = LinearGradient(
                -width.toFloat(),
                0f,
                0f,
                0f,
                intArrayOf(mTextColor, mGradientColor, mTextColor),
                floatArrayOf(0f, 0.5f, 1f),
                Shader.TileMode.CLAMP
            )
            mGradientMatrix = Matrix()
        }
        mGradientAnimator!!.duration = mGradientDuration.toLong()
    }
    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        if (visibility == VISIBLE && isAutoPlay) {
            mOffsetAnimator?.start()
        }
    }

    override fun onDetachedFromWindow() {
        // release animation
        mOffsetAnimator?.let {
            if (it.isRunning) {
                mOffsetAnimator!!.cancel()
            }
        }

        mGradientAnimator?.let {
            if(it.isRunning){
                mGradientAnimator!!.cancel()
            }
        }
        super.onDetachedFromWindow()
    }

    /**
     * 监听offset动画状态
     *
     * @param listener AnimatorListener
     */
    fun addOffsetAnimListener(listener: Animator.AnimatorListener?) {
        mOffsetAnimator?.addListener(listener)
    }

    /**
     * 监听gradient动画状态
     *
     * @param listener AnimatorListener
     */
    fun addGradientAnimListener(listener: Animator.AnimatorListener) {
        mGradientListener = listener
    }

    /**
     * 开启动画
     */
    fun startAnimation() {
        if (visibility == VISIBLE) {
            if (mOffsetAnimator!!.isRunning) {
                mOffsetAnimator!!.cancel()
            }
            isOffsetAnimEnd = false
            mOffsetAnimator!!.start()
        } else {
            Log.w("AnimLogoView", "The view is not visible, not to play the animation .")
        }
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        mWidth = w
        mHeight = h
        initLogoCoordinate()
        initGradientAnimation(w)
    }

    private fun initLogoCoordinate() {
        if (mWidth == 0 || mHeight == 0) {
            Log.w(this.javaClass.simpleName, "The view has not measure, it will auto init later.")
            return
        }
        val centerY = mHeight / 2f + mPaint?.textSize!! / 2 + mLogoOffset
        // calculate the final xy of the text
        var totalLength = 0f
        for (i in 0 until mLogoTexts.size()) {
            val str = mLogoTexts[i]
            val currentLength = mPaint!!.measureText(str)
            totalLength += if (i != mLogoTexts.size() - 1) {
                currentLength + mTextPadding
            } else {
                currentLength
            }
        }
        // the draw width of the logo must small than the width of this AnimLogoView
        check(totalLength <= mWidth) { "The text of logoName is too large that this view can not display all text" }
        var startX = (mWidth - totalLength) / 2
        if (mQuietPoints.size() > 0) {
            mQuietPoints.clear()
        }
        for (i in 0 until mLogoTexts.size()) {
            val str = mLogoTexts[i]
            val currentLength = mPaint!!.measureText(str)
            mQuietPoints.put(i, PointF(startX, centerY))
            startX += currentLength + mTextPadding
        }
        // generate random start xy of the text
        if (mRadonPoints.size() > 0) {
            mRadonPoints.clear()
        }
        for (i in 0 until mLogoTexts.size()) {
            mRadonPoints.put(
                i, PointF(
                    Math.random().toFloat() * mWidth, Math.random()
                        .toFloat() * mHeight
                )
            )
        }
    }

    override fun onDraw(canvas: Canvas) {
        if (!isOffsetAnimEnd) { // offset animation
            mPaint?.alpha = Math.min(255f, 255 * mOffsetAnimProgress + 100).toInt()
            for (i in 0 until mQuietPoints.size()) {
                val quietP = mQuietPoints[i]
                val radonP = mRadonPoints[i]
                val x = radonP.x + (quietP.x - radonP.x) * mOffsetAnimProgress
                val y = radonP.y + (quietP.y - radonP.y) * mOffsetAnimProgress
                canvas.drawText(mLogoTexts[i], x, y, mPaint!!)
            }
        } else { // gradient animation
            for (i in 0 until mQuietPoints.size()) {
                val quietP = mQuietPoints[i]
                canvas.drawText(mLogoTexts[i], quietP.x, quietP.y, mPaint!!)
            }
            mGradientMatrix?.setTranslate(mMatrixTranslate.toFloat(), 0f)
            mLinearGradient?.setLocalMatrix(mGradientMatrix)
        }
    }

    /**
     * 设置logo名
     *
     * @param logoName logo名称
     */
    fun setLogoText(logoName: String?) {
        fillLogoTextArray(logoName!!)
        // if set the new logoName, should refresh the coordinate again
        initLogoCoordinate()
    }

    /**
     * 设置logo文字动效时长
     *
     * @param duration 动效时长
     */
    fun setOffsetAnimDuration(duration: Int) {
        mOffsetDuration = duration
        initOffsetAnimation()
    }

    /**
     * 设置logo文字渐变动效时长
     *
     * @param duration 动效时长
     */
    fun setGradientAnimDuration(duration: Int) {
        mGradientDuration = duration
        initGradientAnimation(mWidth)
    }

    /**
     * 设置logo文字渐变颜色
     *
     * @param gradientColor 渐变颜色
     */
    fun setGradientColor(gradientColor: Int) {
        mGradientColor = gradientColor
    }

    /**
     * 设置是否显示logo文字渐变
     *
     * @param isShowGradient 是否显示logo渐变动效
     */
    fun setShowGradient(isShowGradient: Boolean) {
        this.isShowGradient = isShowGradient
    }

    /**
     * 设置logo字体边距
     *
     * @param padding 字体边距
     */
    fun setTextPadding(padding: Int) {
        mTextPadding = padding
        initLogoCoordinate()
    }

    /**
     * 设置logo字体颜色
     *
     * @param color 字体颜色
     */
    fun setTextColor(color: Int) {
        mTextColor = color
        initPaint()
    }

    /**
     * 设置logo字体大小
     *
     * @param size 字体大小
     */
    fun setTextSize(size: Int) {
        mTextSize = size
        initPaint()
    }


}
package com.knight.kotlin.library_widget.shadeview.drawable

import android.annotation.SuppressLint
import android.graphics.BlurMaskFilter
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.ColorFilter
import android.graphics.DashPathEffect
import android.graphics.LinearGradient
import android.graphics.Paint
import android.graphics.Path
import android.graphics.PixelFormat
import android.graphics.RadialGradient
import android.graphics.Rect
import android.graphics.RectF
import android.graphics.Shader
import android.graphics.SweepGradient
import android.graphics.drawable.Drawable
import android.os.Build
import android.view.Gravity
import android.view.View
import androidx.annotation.ColorInt
import androidx.annotation.NonNull
import kotlin.math.min


/**
 * @author created by luguian
 * @organize
 * @Date 2024/9/5 15:52
 * @descript:在 {@link android.graphics.drawable.GradientDrawable} 的基础上改造
 */
class ShapeDrawable @JvmOverloads constructor(state: ShapeState = ShapeState()) : Drawable() {
    private var mShapeState: ShapeState

    private val mSolidPaint = Paint(Paint.ANTI_ALIAS_FLAG)
    private var mPadding: Rect? = null
    private val mStrokePaint = Paint(Paint.ANTI_ALIAS_FLAG) // optional, set by the caller
    private var mShadowPaint: Paint? = null
    private var mColorFilter: ColorFilter? = null // optional, set by the caller
    private var mAlpha = 0xFF // modified by the caller
    private var mDither = false

    private val mPath: Path = Path()
    private val mRect = RectF()

    private val mShadowRect = RectF()
    private val mShadowPath: Path = Path()

    private var mLayerPaint: Paint? = null // internal, used if we use saveLayer()
    private var mRectDirty: Boolean // internal state
    private var mMutated: Boolean
    private var mRingPath: Path? = Path()
    private var mPathDirty = true

    /** 当前布局方向  */
    private var mLayoutDirection = 0

    init {
        mShapeState = state
        initializeWithState(state)
        mRectDirty = true
        mMutated = false

        mStrokePaint.style = Paint.Style.STROKE
    }

    /**
     * 获取 Shape 状态对象
     */
    fun getShapeState(): ShapeState {
        return mShapeState
    }

    override fun getPadding(@NonNull padding: Rect): Boolean {
        if (mPadding != null) {
            padding.set(mPadding!!)
            return true
        }
        return super.getPadding(padding)
    }

    fun setPadding(paddingLeft: Int, paddingTop: Int, paddingRight: Int, paddingBottom: Int): ShapeDrawable {
        return setPadding(Rect(paddingLeft, paddingTop, paddingRight, paddingBottom))
    }

    fun setPadding(padding: Rect?): ShapeDrawable {
        mPadding = padding
        mPathDirty = true
        invalidateSelf()
        return this
    }

    /**
     * 设置 Shape 形状
     *
     * @param shape         Shape 形状类型
     */
    fun setType(@ShapeTypeLimit shape: Int): ShapeDrawable {
        mRingPath = null
        mShapeState.setType(shape)
        mPathDirty = true
        invalidateSelf()
        return this
    }

    /**
     * 设置 Shape 宽度
     */
    fun setWidth(width: Int): ShapeDrawable {
        mShapeState.width = width
        mPathDirty = true
        invalidateSelf()
        return this
    }

    /**
     * 设置 Shape 高度
     */
    fun setHeight(height: Int): ShapeDrawable {
        mShapeState.height = height
        mPathDirty = true
        invalidateSelf()
        return this
    }

    /**
     * 设置矩形的圆角大小
     */
    fun setRadius(radius: Float): ShapeDrawable {
        mShapeState.setCornerRadius(radius)
        mPathDirty = true
        invalidateSelf()
        return this
    }

    /**
     * 设置矩形的圆角大小
     *
     * @param topLeftRadius         左上圆角大小
     * @param topRightRadius        右上圆角大小
     * @param bottomLeftRadius      左下圆角大小
     * @param bottomRightRadius     右下圆角大小
     */
    fun setRadius(topLeftRadius: Float, topRightRadius: Float, bottomLeftRadius: Float, bottomRightRadius: Float): ShapeDrawable {
        if (topLeftRadius == topRightRadius && topLeftRadius == bottomLeftRadius && topLeftRadius == bottomRightRadius) {
            return setRadius(topLeftRadius)
        }
        // 为 4 个角中的每一个指定半径，对于每个角，该数组包含 2 个值 [X_radius, Y_radius]，角的顺序是左上角、右上角、右下角、左下角
        mShapeState.setCornerRadii(
            floatArrayOf(
                topLeftRadius, topLeftRadius, topRightRadius, topRightRadius,
                bottomRightRadius, bottomRightRadius, bottomLeftRadius, bottomLeftRadius
            )
        )
        mPathDirty = true
        invalidateSelf()
        return this
    }

    /**
     * 设置填充色
     */
    fun setSolidColor(@ColorInt startColor: Int, @ColorInt endColor: Int): ShapeDrawable {
        return setSolidColor(*intArrayOf(startColor, endColor))
    }

    fun setSolidColor(@ColorInt startColor: Int, @ColorInt centerColor: Int, @ColorInt endColor: Int): ShapeDrawable {
        return setSolidColor(*intArrayOf(startColor, centerColor, endColor))
    }

    fun setSolidColor(@ColorInt vararg colors: Int): ShapeDrawable {
        mShapeState.setShapeSolidColor(*colors)
        if (colors == null) {
            mSolidPaint.color = 0
        } else if (colors.size == 1) {
            mSolidPaint.color = colors[0]
            mSolidPaint.clearShadowLayer()
        }
        mRectDirty = true
        invalidateSelf()
        return this
    }

    /**
     * 设置填充色渐变类型
     */
    fun setSolidGradientType(@ShapeGradientTypeLimit type: Int): ShapeDrawable {
        mShapeState.setShapeSolidGradientType(type)
        mRectDirty = true
        invalidateSelf()
        return this
    }

    /**
     * 设置填充色渐变方向
     */
    fun setSolidGradientOrientation(orientation: ShapeGradientOrientation): ShapeDrawable {
        mShapeState.solidGradientOrientation = orientation
        mRectDirty = true
        invalidateSelf()
        return this
    }

    /**
     * 设置填充色渐变中心 X 点坐标的相对位置（默认值为 0.5）
     */
    fun setSolidGradientCenterX(centerX: Float): ShapeDrawable {
        mShapeState.solidCenterX = centerX
        mRectDirty = true
        invalidateSelf()
        return this
    }

    /**
     * 设置填充色渐变中心 Y 点坐标的相对位置（默认值为 0.5）
     */
    fun setSolidGradientCenterY(centerY: Float): ShapeDrawable {
        mShapeState.solidCenterY = centerY
        mRectDirty = true
        invalidateSelf()
        return this
    }

    /**
     * 设置填充色渐变半径大小
     */
    fun setSolidGradientRadius(radius: Float): ShapeDrawable {
        mShapeState.gradientRadius = radius
        mRectDirty = true
        invalidateSelf()
        return this
    }

    /**
     * 设置边框色
     */
    fun setStrokeColor(@ColorInt startColor: Int, @ColorInt endColor: Int): ShapeDrawable {
        return setStrokeColor(*intArrayOf(startColor, endColor))
    }

    fun setStrokeColor(@ColorInt startColor: Int, @ColorInt centerColor: Int, @ColorInt endColor: Int): ShapeDrawable {
        return setStrokeColor(*intArrayOf(startColor, centerColor, endColor))
    }

    fun setStrokeColor(@ColorInt vararg colors: Int): ShapeDrawable {
        mShapeState.setShapeStrokeColor(*colors)
        if (colors == null) {
            mStrokePaint.color = 0
        } else if (colors.size == 1) {
            mStrokePaint.color = colors[0]
            mStrokePaint.clearShadowLayer()
        }
        mRectDirty = true
        invalidateSelf()
        return this
    }

    /**
     * 设置边框色渐变方向
     */
    fun setStrokeGradientOrientation(orientation: ShapeGradientOrientation): ShapeDrawable {
        mShapeState.strokeGradientOrientation = orientation
        mRectDirty = true
        invalidateSelf()
        return this
    }

    /**
     * 设置边框大小
     */
    fun setStrokeSize(size: Int): ShapeDrawable {
        mShapeState.setShapeStrokeSize(size)
        mStrokePaint.strokeWidth = size.toFloat()
        mRectDirty = true
        invalidateSelf()
        return this
    }

    /**
     * 设置边框每一节虚线宽度
     */
    fun setStrokeDashSize(dashSize: Float): ShapeDrawable {
        mShapeState.strokeDashSize = dashSize
        mStrokePaint.setPathEffect(if (dashSize > 0) DashPathEffect(floatArrayOf(dashSize, mShapeState.strokeDashGap), 0f) else null)
        mRectDirty = true
        invalidateSelf()
        return this
    }

    /**
     * 设置边框虚线每一节间隔
     */
    fun setStrokeDashGap(dashGap: Float): ShapeDrawable {
        mShapeState.strokeDashGap = dashGap
        mStrokePaint.setPathEffect(if (mShapeState.strokeDashSize > 0) DashPathEffect(floatArrayOf(mShapeState.strokeDashSize, dashGap), 0f) else null)
        mRectDirty = true
        invalidateSelf()
        return this
    }

    /**
     *
     * Sets whether or not this drawable will honor its `level`
     * property.
     *
     * **Note**: changing this property will affect all instances
     * of a drawable loaded from a resource. It is recommended to invoke
     * [.mutate] before changing this property.
     *
     * @param useLevel True if this drawable should honor its level, false otherwise
     *
     * @see .mutate
     * @see .setLevel
     * @see .getLevel
     */
    fun setUseLevel(useLevel: Boolean): ShapeDrawable {
        mShapeState.useLevel = useLevel
        mRectDirty = true
        invalidateSelf()
        return this
    }

    /**
     * 设置阴影颜色
     */
    fun setShadowColor(@ColorInt color: Int): ShapeDrawable {
        mShapeState.shadowColor = color
        mPathDirty = true
        mRectDirty = true
        invalidateSelf()
        return this
    }

    /**
     * 设置阴影大小
     */
    fun setShadowSize(size: Int): ShapeDrawable {
        mShapeState.shadowSize = size
        mPathDirty = true
        mRectDirty = true
        invalidateSelf()
        return this
    }

    /**
     * 设置阴影水平偏移
     */
    fun setShadowOffsetX(offsetX: Int): ShapeDrawable {
        mShapeState.shadowOffsetX = offsetX
        mPathDirty = true
        mRectDirty = true
        invalidateSelf()
        return this
    }

    /**
     * 设置阴影垂直偏移
     */
    fun setShadowOffsetY(offsetY: Int): ShapeDrawable {
        mShapeState.shadowOffsetY = offsetY
        mPathDirty = true
        mRectDirty = true
        invalidateSelf()
        return this
    }

    /**
     * 设置内环的半径大小
     */
    fun setRingInnerRadiusSize(size: Int): ShapeDrawable {
        mShapeState.ringInnerRadiusSize = size
        mShapeState.ringInnerRadiusRatio = 0f
        mRectDirty = true
        invalidateSelf()
        return this
    }

    /**
     * 设置内环的半径比率
     */
    fun setRingInnerRadiusRatio(ratio: Float): ShapeDrawable {
        mShapeState.ringInnerRadiusRatio = ratio
        mShapeState.ringInnerRadiusSize = -1
        mRectDirty = true
        invalidateSelf()
        return this
    }

    /**
     * 设置外环的厚度大小
     */
    fun setRingThicknessSize(size: Int): ShapeDrawable {
        mShapeState.ringThicknessSize = size
        mShapeState.ringThicknessRatio = 0f
        mRectDirty = true
        invalidateSelf()
        return this
    }

    /**
     * 设置外环的厚度比率
     */
    fun setRingThicknessRatio(ratio: Float): ShapeDrawable {
        mShapeState.ringThicknessRatio = ratio
        mShapeState.ringThicknessSize = -1
        mRectDirty = true
        invalidateSelf()
        return this
    }

    /**
     * 设置线条重心
     */
    fun setLineGravity(lineGravity: Int): ShapeDrawable {
        mShapeState.lineGravity = lineGravity
        mRectDirty = true
        invalidateSelf()
        return this
    }

    /**
     * 将当前 Drawable 对象应用到 View 背景
     */
    fun intoBackground(view: View) {
        if (mShapeState.strokeDashGap > 0 || mShapeState.shadowSize > 0) {
            // 需要关闭硬件加速，否则虚线或者阴影在某些手机上面无法生效
            view.setLayerType(View.LAYER_TYPE_SOFTWARE, null)
        }
        view.background = this
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            // 布局方向
            val layoutDirection = view.layoutDirection
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                setLayoutDirection(layoutDirection)
            }
        }
    }

    @SuppressLint("WrongConstant")
    override fun draw(@NonNull canvas: Canvas) {
        if (!ensureValidRect()) {
            // nothing to draw
            return
        }

        // remember the alpha values, in case we temporarily overwrite them
        // when we modulate them with mAlpha
        val prevFillAlpha = mSolidPaint.alpha
        val prevStrokeAlpha = mStrokePaint.alpha
        // compute the modulate alpha values
        val currFillAlpha = modulateAlpha(prevFillAlpha)
        val currStrokeAlpha = modulateAlpha(prevStrokeAlpha)

        val haveShadow: Boolean = mShapeState.shadowSize > 0
        val haveStroke = currStrokeAlpha > 0 && mStrokePaint.strokeWidth > 0
        val haveFill = currFillAlpha > 0
        val st: ShapeState = mShapeState
        /*  we need a layer iff we're drawing both a fill and stroke, and the
            stroke is non-opaque, and our shape type actually supports
            fill+stroke. Otherwise we can just draw the stroke (if any) on top
            of the fill (if any) without worrying about blending artifacts.
         */
        val useLayer = haveStroke && haveFill && st.shapeType !== ShapeType.LINE && currStrokeAlpha < 255 && (mAlpha < 255 || mColorFilter != null)

        /*  Drawing with a layer is slower than direct drawing, but it
            allows us to apply paint effects like alpha and color filter to
            the result of multiple separate draws. In our case, if the user
            asks for a non-opaque alpha value (via setAlpha), and we're
            stroking, then we need to apply the alpha AFTER we've drawn
            both the fill and the stroke.
        */
        if (useLayer) {
            if (mLayerPaint == null) {
                mLayerPaint = Paint()
            }
            mLayerPaint!!.isDither = mDither
            mLayerPaint!!.alpha = mAlpha
            mLayerPaint!!.setColorFilter(mColorFilter)

            val rad = mStrokePaint.strokeWidth
            ShapeDrawableUtils.saveCanvasLayer(
                canvas, mRect.left - rad, mRect.top - rad,
                mRect.right + rad, mRect.bottom + rad, mLayerPaint!!
            )

            // don't perform the filter in our individual paints
            // since the layer will do it for us
            mSolidPaint.setColorFilter(null)
            mStrokePaint.setColorFilter(null)
        } else {
            /*  if we're not using a layer, apply the dither/filter to our
                individual paints
            */
            mSolidPaint.alpha = currFillAlpha
            mSolidPaint.isDither = mDither
            mSolidPaint.setColorFilter(mColorFilter)
            if (mColorFilter != null && !mShapeState.hasSolidColor) {
                mSolidPaint.color = mAlpha shl 24
            }
            if (haveStroke) {
                mStrokePaint.alpha = currStrokeAlpha
                mStrokePaint.isDither = mDither
                mStrokePaint.setColorFilter(mColorFilter)
            }
        }

        if (haveShadow) {
            if (mShadowPaint == null) {
                mShadowPaint = Paint()
                mShadowPaint!!.color = Color.TRANSPARENT
                mShadowPaint!!.style = Paint.Style.STROKE
            }

            if (haveStroke) {
                mShadowPaint!!.strokeWidth = mStrokePaint.strokeWidth
            } else {
                mShadowPaint!!.strokeWidth = mShapeState.shadowSize / 4f
            }

            var shadowColor: Int = mShapeState.shadowColor
            // 如果阴影颜色是非透明的，则需要设置一点透明度进去，否则会显示不出来
            if (ShapeDrawableUtils.setColorAlphaComponent(mShapeState.shadowColor, 255) === mShapeState.shadowColor) {
                shadowColor = ShapeDrawableUtils.setColorAlphaComponent(mShapeState.shadowColor, 254)
            }

            mShadowPaint!!.color = shadowColor
            // 这里解释一下为什么要阴影大小除以倍数，因为如果不这么做会导致阴影显示会超过 View 边界，从而导致出现阴影被截断的效果
            val shadowRadius: Float = if (Build.VERSION.SDK_INT >= 28) {
                mShapeState.shadowSize / 2f
            } else {
                mShapeState.shadowSize / 3f
            }
            mShadowPaint!!.setMaskFilter(BlurMaskFilter(shadowRadius, BlurMaskFilter.Blur.NORMAL))
        } else {
            if (mShadowPaint != null) {
                mShadowPaint!!.clearShadowLayer()
            }
        }

        when (st.shapeType) {
            ShapeType.RECTANGLE -> if (st.radiusArray != null) {
                if (mPathDirty || mRectDirty) {
                    mPath.reset()
                    mPath.addRoundRect(mRect, st.radiusArray!!, Path.Direction.CW)
                    mRectDirty = false
                    mPathDirty = mRectDirty
                }
                if (haveShadow) {
                    mShadowPath.reset()
                    mShadowPath.addRoundRect(mShadowRect, st.radiusArray!!, Path.Direction.CW)
                    canvas.drawPath(mShadowPath, mShadowPaint!!)
                }
                canvas.drawPath(mPath, mSolidPaint)
                if (haveStroke) {
                    canvas.drawPath(mPath, mStrokePaint)
                }
            } else if (st.radius > 0.0f) {
                // since the caller is only giving us 1 value, we will force
                // it to be square if the rect is too small in one dimension
                // to show it. If we did nothing, Skia would clamp the rad
                // independently along each axis, giving us a thin ellipse
                // if the rect were very wide but not very tall
                var rad: Float = st.radius
                val r = (min(mRect.width().toDouble(), mRect.height().toDouble()) * 0.5f).toFloat()
                if (rad > r) {
                    rad = r
                }
                if (haveShadow) {
                    canvas.drawRoundRect(mShadowRect, rad, rad, mShadowPaint!!)
                }
                canvas.drawRoundRect(mRect, rad, rad, mSolidPaint)
                if (haveStroke) {
                    canvas.drawRoundRect(mRect, rad, rad, mStrokePaint)
                }
            } else {
                if (haveShadow) {
                    canvas.drawRect(mShadowRect, mShadowPaint!!)
                }
                if (mSolidPaint.color != 0 || mColorFilter != null || mSolidPaint.shader != null) {
                    canvas.drawRect(mRect, mSolidPaint)
                }
                if (haveStroke) {
                    canvas.drawRect(mRect, mStrokePaint)
                }
            }

            ShapeType.OVAL -> {
                if (haveShadow) {
                    canvas.drawOval(mShadowRect, mShadowPaint!!)
                }
                canvas.drawOval(mRect, mSolidPaint)
                if (haveStroke) {
                    canvas.drawOval(mRect, mStrokePaint)
                }
            }

            ShapeType.LINE -> {
                val r = mRect
                val startX: Float
                val startY: Float
                val stopX: Float
                val stopY: Float
                val lineGravity: Int
                val callback = callback
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1 && callback is View) {
                    val layoutDirection = callback.context.resources.configuration.layoutDirection
                    lineGravity = Gravity.getAbsoluteGravity(st.lineGravity, layoutDirection)
                } else {
                    lineGravity = st.lineGravity
                }

                when (lineGravity) {
                    Gravity.LEFT -> {
                        startX = 0f
                        startY = 0f
                        stopX = 0f
                        stopY = r.bottom
                    }

                    Gravity.RIGHT -> {
                        startX = r.right
                        startY = 0f
                        stopX = r.right
                        stopY = r.bottom
                    }

                    Gravity.TOP -> {
                        startX = 0f
                        startY = 0f
                        stopX = r.right
                        stopY = 0f
                    }

                    Gravity.BOTTOM -> {
                        startX = 0f
                        startY = r.bottom
                        stopX = r.right
                        stopY = r.bottom
                    }

                    Gravity.CENTER -> {
                        val y = r.centerY()
                        startX = r.left
                        startY = y
                        stopX = r.right
                        stopY = y
                    }

                    else -> {
                        val y = r.centerY()
                        startX = r.left
                        startY = y
                        stopX = r.right
                        stopY = y
                    }
                }
                if (haveShadow) {
                    canvas.drawLine(startX, startY, stopX, stopY, mShadowPaint!!)
                }
                canvas.drawLine(startX, startY, stopX, stopY, mStrokePaint)
            }

            ShapeType.RING -> {
                val path: Path? = buildRing(st)
                path?.run{
                    if (haveShadow) {
                        canvas.drawPath(this, mShadowPaint!!)
                    }
                    canvas.drawPath(this, mSolidPaint)
                    if (haveStroke) {
                        canvas.drawPath(this, mStrokePaint)
                    }
                }

            }

            else -> {}
        }
        if (useLayer) {
            canvas.restore()
        } else {
            mSolidPaint.alpha = prevFillAlpha
            if (haveStroke) {
                mStrokePaint.alpha = prevStrokeAlpha
            }
        }
    }

    override fun onLayoutDirectionChanged(layoutDirection: Int): Boolean {
        mLayoutDirection = layoutDirection
        return mShapeState.shapeType === ShapeType.LINE
    }

    private fun modulateAlpha(alpha: Int): Int {
        val scale = mAlpha + (mAlpha shr 7)
        return alpha * scale shr 8
    }

    override fun getChangingConfigurations(): Int {
        return super.getChangingConfigurations() or mShapeState.changingConfigurations
    }

    override fun setAlpha(alpha: Int) {
        if (alpha != mAlpha) {
            mAlpha = alpha
            invalidateSelf()
        }
    }

    override fun getAlpha(): Int {
        return mAlpha
    }

    override fun setDither(dither: Boolean) {
        if (dither != mDither) {
            mDither = dither
            invalidateSelf()
        }
    }

    override fun setColorFilter(cf: ColorFilter?) {
        if (cf !== mColorFilter) {
            mColorFilter = cf
            invalidateSelf()
        }
    }

    override fun getOpacity(): Int {
        return if (mShapeState.opaque) PixelFormat.OPAQUE else PixelFormat.TRANSLUCENT
    }

    protected override fun onBoundsChange(r: Rect) {
        super.onBoundsChange(r!!)
        mRingPath = null
        mPathDirty = true
        mRectDirty = true
    }

    override fun onLevelChange(level: Int): Boolean {
        super.onLevelChange(level)
        mRectDirty = true
        mPathDirty = true
        invalidateSelf()
        return true
    }

    private fun buildRing(shapeState: ShapeState): Path? {
        if (mRingPath != null && (!shapeState.useLevelForShape || !mPathDirty)) {
            return mRingPath
        }
        mPathDirty = false

        val sweep = if (shapeState.useLevelForShape) (360.0f * level / 10000f) else 360f

        var bounds = RectF(mRect)

        val x = bounds.width() / 2.0f
        val y = bounds.height() / 2.0f

        val thickness: Float = if (shapeState.ringThicknessSize !== -1) shapeState.ringThicknessSize.toFloat() else bounds.width() / shapeState.ringThicknessRatio
        // inner radius
        val radius: Float = if (shapeState.ringInnerRadiusSize !== -1) shapeState.ringInnerRadiusSize.toFloat() else bounds.width() / shapeState.ringInnerRadiusRatio

        val innerBounds = RectF(bounds)
        innerBounds.inset(x - radius, y - radius)

        bounds = RectF(innerBounds)
        bounds.inset(-thickness, -thickness)

        if (mRingPath == null) {
            mRingPath = Path()
        } else {
            mRingPath?.reset()
        }

        val ringPath: Path? = mRingPath
        // arcTo treats the sweep angle mod 360, so check for that, since we
        // think 360 means draw the entire oval
        ringPath?.run {
            if (sweep < 360 && sweep > -360) {
                setFillType(Path.FillType.EVEN_ODD)
                // inner top
                moveTo(x + radius, y)
                // outer top
                lineTo(x + radius + thickness, y)
                // outer arc
                arcTo(bounds, 0.0f, sweep, false)
                // inner arc
                arcTo(innerBounds, sweep, -sweep, false)
                close()
            } else {
                // add the entire ovals
                addOval(bounds, Path.Direction.CW)
                addOval(innerBounds, Path.Direction.CCW)
            }
        }


        return ringPath
    }

    /**
     * This checks mRectIsDirty, and if it is true, recomputes both our drawing
     * rectangle (mRect) and the gradient itself, since it depends on our
     * rectangle too.
     * @return true if the resulting rectangle is not empty, false otherwise
     */
    private fun ensureValidRect(): Boolean {
        if (!mRectDirty) {
            return !mRect.isEmpty
        }

        mRectDirty = false

        val bounds = bounds
        val inset = mStrokePaint.strokeWidth * 0.5f

        val st: ShapeState = mShapeState

        val let: Float = bounds.left + inset + mShapeState.shadowSize
        val top: Float = bounds.top + inset + mShapeState.shadowSize
        val right: Float = bounds.right - inset - mShapeState.shadowSize
        val bottom: Float = bounds.bottom - inset - mShapeState.shadowSize

        mRect[let, top, right] = bottom

        val shadowLet: Float
        val shadowTop: Float
        val shadowRight: Float
        val shadowBottom: Float

        if (mShapeState.shadowOffsetX > 0) {
            shadowLet = let + mShapeState.shadowOffsetX
            shadowRight = right
        } else {
            shadowLet = let
            shadowRight = right + mShapeState.shadowOffsetX
        }

        if (mShapeState.shadowOffsetY > 0) {
            shadowTop = top + mShapeState.shadowOffsetY
            shadowBottom = bottom
        } else {
            shadowTop = top
            shadowBottom = bottom + mShapeState.shadowOffsetY
        }

        mShadowRect[shadowLet, shadowTop, shadowRight] = shadowBottom

        if (st.solidColors == null) {
            mSolidPaint.setShader(null)
        }

        if (st.strokeColors == null) {
            mStrokePaint.setShader(null)
        }

        if (st.solidColors != null) {
            val rect = mRect

            when (st.solidGradientType) {
                ShapeGradientType.LINEAR_GRADIENT -> {
                    val level = if (st.useLevel) level / 10000f else 1f
                    val coordinate: FloatArray = ShapeDrawableUtils.computeLinearGradientCoordinate(
                        mLayoutDirection, mRect, level, st.solidGradientOrientation
                    )
                    mSolidPaint.setShader(
                        LinearGradient(
                            coordinate[0], coordinate[1], coordinate[2], coordinate[3],
                            st.solidColors!!, st.positions, Shader.TileMode.CLAMP
                        )
                    )
                }

                ShapeGradientType.RADIAL_GRADIENT -> {
                    val x0: Float
                    val y0: Float
                    x0 = rect.left + (rect.right - rect.left) * st.solidCenterX
                    y0 = rect.top + (rect.bottom - rect.top) * st.solidCenterY

                    val level = if (st.useLevel) level / 10000f else 1f

                    mSolidPaint.setShader(
                        RadialGradient(
                            x0, y0,
                            level * st.gradientRadius, st.solidColors!!, null,
                            Shader.TileMode.CLAMP
                        )
                    )
                }

                ShapeGradientType.SWEEP_GRADIENT -> {
                    val x0: Float
                    val y0: Float

                    x0 = rect.left + (rect.right - rect.left) * st.solidCenterX
                    y0 = rect.top + (rect.bottom - rect.top) * st.solidCenterY

                    var tempSolidColors: IntArray = st.solidColors!!
                    var tempSolidPositions: FloatArray? = null

                    if (st.useLevel) {
                        tempSolidColors = st.tempSolidColors
                        val length: Int = st.solidColors!!.size
                        if (tempSolidColors == null || tempSolidColors.size != length + 1) {
                            st.tempSolidColors = IntArray(length + 1)
                            tempSolidColors = st.tempSolidColors
                        }
                        System.arraycopy(st.solidColors, 0, tempSolidColors, 0, length)
                        tempSolidColors[length] = st.solidColors!!.get(length - 1)


                        tempSolidPositions = st.tempSolidPositions
                        val fraction = 1f / (length - 1)
                        if (tempSolidPositions == null || tempSolidPositions.size != length + 1) {
                            st.tempSolidPositions = FloatArray(length + 1)
                            tempSolidPositions = st.tempSolidPositions
                        }

                        val level = level / 10000f
                        var i = 0
                        while (i < length) {
                            tempSolidPositions[i] = i * fraction * level
                            i++
                        }
                        tempSolidPositions[length] = 1f
                    }

                    mSolidPaint.setShader(SweepGradient(x0, y0, tempSolidColors, tempSolidPositions))
                }

                else -> {}
            }
            // If we don't have a solid color, the alpha channel must be
            // maxed out so that alpha modulation works correctly.
            if (!st.hasSolidColor) {
                mSolidPaint.color = Color.BLACK
            }
        }

        if (st.strokeColors != null) {
            val level = if (st.useLevel) level / 10000f else 1f
            val coordinate: FloatArray = ShapeDrawableUtils.computeLinearGradientCoordinate(
                mLayoutDirection, mRect, level, st.strokeGradientOrientation
            )
            mStrokePaint.setShader(
                LinearGradient(
                    coordinate[0], coordinate[1], coordinate[2], coordinate[3],
                    st.strokeColors!!, st.positions, Shader.TileMode.CLAMP
                )
            )

            if (!st.hasStrokeColor) {
                mStrokePaint.color = Color.BLACK
            }
        }
        return !mRect.isEmpty
    }

    override fun getIntrinsicWidth(): Int {
        return mShapeState.width
    }

    override fun getIntrinsicHeight(): Int {
        return mShapeState.height
    }

    override fun getConstantState(): ConstantState {
        mShapeState.shapeChangingConfigurations = changingConfigurations
        return mShapeState
    }

    @NonNull
    override fun mutate(): Drawable {
        if (!mMutated && super.mutate() === this) {
            mShapeState = ShapeState(mShapeState)
            initializeWithState(mShapeState)
            mMutated = true
        }
        return this
    }

    private fun initializeWithState(state: ShapeState) {
        if (state.hasSolidColor) {
            mSolidPaint.color = state.solidColor
        } else if (state.solidColors == null) {
            // If we don't have a solid color and we don't have a gradient,
            // the app is stroking the shape, set the color to the default
            // value of state.solidColor
            mSolidPaint.color = 0
        } else {
            // Otherwise, make sure the fill alpha is maxed out.
            mSolidPaint.color = Color.BLACK
        }
        mPadding = state.padding
        if (state.strokeSize >= 0) {
            if (state.hasStrokeColor) {
                setStrokeColor(state.strokeColor)
            } else {
                state.strokeColors?.let { setStrokeColor(*it) }
            }
            setStrokeSize(state.strokeSize)
            setStrokeDashSize(state.strokeDashSize)
            setStrokeDashGap(state.strokeDashGap)
        }
    }
}
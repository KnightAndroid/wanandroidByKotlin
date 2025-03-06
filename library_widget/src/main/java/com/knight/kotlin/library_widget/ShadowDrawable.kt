package com.knight.kotlin.library_widget

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.ColorFilter
import android.graphics.LinearGradient
import android.graphics.Paint
import android.graphics.PixelFormat
import android.graphics.PorterDuff
import android.graphics.PorterDuffXfermode
import android.graphics.RectF
import android.graphics.Shader
import android.graphics.drawable.Drawable
import android.view.View
import androidx.core.view.ViewCompat
import kotlin.math.min


/**
 * @author created by luguian
 * @organize
 * @Date 2025/3/6 17:30
 * @descript:阴影效果
 */
class ShadowDrawable private constructor(shape: Int, bgColor: IntArray, shapeRadius: Int, shadowColor: Int, shadowRadius: Int, offsetX: Int, offsetY: Int) :
    Drawable() {
    private val mShadowPaint: Paint
    private val mBgPaint: Paint
    private val mShadowRadius: Int
    private val mShape: Int
    private val mShapeRadius: Int
    private val mOffsetX: Int
    private val mOffsetY: Int
    private val mBgColor: IntArray?
    private var mRect: RectF? = null

    init {
        this.mShape = shape
        this.mBgColor = bgColor
        this.mShapeRadius = shapeRadius
        this.mShadowRadius = shadowRadius
        this.mOffsetX = offsetX
        this.mOffsetY = offsetY

        mShadowPaint = Paint()
        mShadowPaint.color = Color.TRANSPARENT
        mShadowPaint.isAntiAlias = true
        mShadowPaint.setShadowLayer(shadowRadius.toFloat(), offsetX.toFloat(), offsetY.toFloat(), shadowColor)
        mShadowPaint.setXfermode(PorterDuffXfermode(PorterDuff.Mode.DST_ATOP))

        mBgPaint = Paint()
        mBgPaint.isAntiAlias = true
    }

    override fun setBounds(left: Int, top: Int, right: Int, bottom: Int) {
        super.setBounds(left, top, right, bottom)
        mRect = RectF(
            (left + mShadowRadius - mOffsetX).toFloat(), (top + mShadowRadius - mOffsetY).toFloat(), (right - mShadowRadius - mOffsetX).toFloat(),
            (bottom - mShadowRadius - mOffsetY).toFloat()
        )
    }

    override fun draw(canvas: Canvas) {
        if (mBgColor != null) {
            if (mBgColor.size == 1) {
                mBgPaint.color = mBgColor[0]
            } else {
                mBgPaint.setShader(
                    LinearGradient(
                        mRect!!.left, mRect!!.height() / 2, mRect!!.right,
                        mRect!!.height() / 2, mBgColor, null, Shader.TileMode.CLAMP
                    )
                )
            }
        }

        if (mShape == SHAPE_ROUND) {
            canvas.drawRoundRect(mRect!!, mShapeRadius.toFloat(), mShapeRadius.toFloat(), mShadowPaint)
            canvas.drawRoundRect(mRect!!, mShapeRadius.toFloat(), mShapeRadius.toFloat(), mBgPaint)
        } else {
            canvas.drawCircle(
                mRect!!.centerX(), mRect!!.centerY(),
                (min(mRect!!.width().toDouble(), mRect!!.height().toDouble()) / 2).toFloat(), mShadowPaint
            )
            canvas.drawCircle(
                mRect!!.centerX(), mRect!!.centerY(),
                (min(mRect!!.width().toDouble(), mRect!!.height().toDouble()) / 2).toFloat(), mBgPaint
            )
        }
    }

    override fun setAlpha(alpha: Int) {
        mShadowPaint.alpha = alpha
    }

    override fun setColorFilter(colorFilter: ColorFilter?) {
        mShadowPaint.setColorFilter(colorFilter)
    }

    override fun getOpacity(): Int {
        return PixelFormat.TRANSLUCENT
    }

    class Builder {
        private var mShape: Int
        private var mShapeRadius = 12
        private var mShadowColor: Int
        private var mShadowRadius = 18
        private var mOffsetX = 0
        private var mOffsetY = 0
        private var mBgColor: IntArray

        init {
            mShape = SHAPE_ROUND
            mShadowColor = Color.parseColor("#4d000000")
            mBgColor = IntArray(1)
            mBgColor[0] = Color.TRANSPARENT
        }

        fun setShape(mShape: Int): Builder {
            this.mShape = mShape
            return this
        }

        fun setShapeRadius(ShapeRadius: Int): Builder {
            this.mShapeRadius = ShapeRadius
            return this
        }

        fun setShadowColor(shadowColor: Int): Builder {
            this.mShadowColor = shadowColor
            return this
        }

        fun setShadowRadius(shadowRadius: Int): Builder {
            this.mShadowRadius = shadowRadius
            return this
        }

        fun setOffsetX(OffsetX: Int): Builder {
            this.mOffsetX = OffsetX
            return this
        }

        fun setOffsetY(OffsetY: Int): Builder {
            this.mOffsetY = OffsetY
            return this
        }

        fun setBgColor(BgColor: Int): Builder {
            mBgColor[0] = BgColor
            return this
        }

        fun setBgColor(BgColor: IntArray): Builder {
            this.mBgColor = BgColor
            return this
        }

        fun builder(): ShadowDrawable {
            return ShadowDrawable(mShape, mBgColor, mShapeRadius, mShadowColor, mShadowRadius, mOffsetX, mOffsetY)
        }
    }

    companion object {
        const val SHAPE_ROUND: Int = 1
        const val SHAPE_CIRCLE: Int = 2

        fun setShadowDrawable(view: View, drawable: Drawable?) {
            view.setLayerType(View.LAYER_TYPE_SOFTWARE, null)
            ViewCompat.setBackground(view, drawable)
        }

        /**
         * 为指定View添加阴影
         * @param view 目标View
         * @param shapeRadius View的圆角
         * @param shadowColor 阴影的颜色
         * @param shadowRadius 阴影的宽度
         * @param offsetX 阴影水平方向的偏移量
         * @param offsetY 阴影垂直方向的偏移量
         */
        fun setShadowDrawable(view: View, shapeRadius: Int, shadowColor: Int, shadowRadius: Int, offsetX: Int, offsetY: Int) {
            val drawable = Builder()
                .setShapeRadius(shapeRadius)
                .setShadowColor(shadowColor)
                .setShadowRadius(shadowRadius)
                .setOffsetX(offsetX)
                .setOffsetY(offsetY)
                .builder()
            view.setLayerType(View.LAYER_TYPE_SOFTWARE, null)
            ViewCompat.setBackground(view, drawable)
        }

        /**
         * 为指定View设置带阴影的背景
         * @param view 目标View
         * @param bgColor View背景色
         * @param shapeRadius View的圆角
         * @param shadowColor 阴影的颜色
         * @param shadowRadius 阴影的宽度
         * @param offsetX 阴影水平方向的偏移量
         * @param offsetY 阴影垂直方向的偏移量
         */
        fun setShadowDrawable(view: View, bgColor: Int, shapeRadius: Int, shadowColor: Int, shadowRadius: Int, offsetX: Int, offsetY: Int) {
            val drawable = Builder()
                .setBgColor(bgColor)
                .setShapeRadius(shapeRadius)
                .setShadowColor(shadowColor)
                .setShadowRadius(shadowRadius)
                .setOffsetX(offsetX)
                .setOffsetY(offsetY)
                .builder()
            view.setLayerType(View.LAYER_TYPE_SOFTWARE, null)
            ViewCompat.setBackground(view, drawable)
        }

        /**
         * 为指定View设置指定形状并带阴影的背景
         * @param view 目标View
         * @param shape View的形状 取值可为：GradientDrawable.RECTANGLE， GradientDrawable.OVAL， GradientDrawable.RING
         * @param bgColor View背景色
         * @param shapeRadius View的圆角
         * @param shadowColor 阴影的颜色
         * @param shadowRadius 阴影的宽度
         * @param offsetX 阴影水平方向的偏移量
         * @param offsetY 阴影垂直方向的偏移量
         */
        fun setShadowDrawable(view: View, shape: Int, bgColor: Int, shapeRadius: Int, shadowColor: Int, shadowRadius: Int, offsetX: Int, offsetY: Int) {
            val drawable = Builder()
                .setShape(shape)
                .setBgColor(bgColor)
                .setShapeRadius(shapeRadius)
                .setShadowColor(shadowColor)
                .setShadowRadius(shadowRadius)
                .setOffsetX(offsetX)
                .setOffsetY(offsetY)
                .builder()
            view.setLayerType(View.LAYER_TYPE_SOFTWARE, null)
            ViewCompat.setBackground(view, drawable)
        }

        /**
         * 为指定View设置带阴影的渐变背景
         * @param view
         * @param bgColor
         * @param shapeRadius
         * @param shadowColor
         * @param shadowRadius
         * @param offsetX
         * @param offsetY
         */
        fun setShadowDrawable(view: View, bgColor: IntArray, shapeRadius: Int, shadowColor: Int, shadowRadius: Int, offsetX: Int, offsetY: Int) {
            val drawable = Builder()
                .setBgColor(bgColor)
                .setShapeRadius(shapeRadius)
                .setShadowColor(shadowColor)
                .setShadowRadius(shadowRadius)
                .setOffsetX(offsetX)
                .setOffsetY(offsetY)
                .builder()
            view.setLayerType(View.LAYER_TYPE_SOFTWARE, null)
            ViewCompat.setBackground(view, drawable)
        }
    }
}
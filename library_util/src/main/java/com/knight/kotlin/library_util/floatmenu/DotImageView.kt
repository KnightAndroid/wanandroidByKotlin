package com.knight.kotlin.library_util.floatmenu

import android.animation.Animator
import android.animation.ValueAnimator
import android.animation.ValueAnimator.AnimatorUpdateListener
import android.content.Context
import android.graphics.Bitmap
import android.graphics.Camera
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Matrix
import android.graphics.Paint
import android.graphics.Rect
import android.util.AttributeSet
import android.view.View
import android.view.animation.LinearInterpolator
import androidx.annotation.Nullable
import com.knight.kotlin.library_base.util.dp2px
import com.knight.kotlin.library_base.util.sp2px
import com.wyjson.router.utils.TextUtils


/**
 * @author created by luguian
 * @organize
 * @Date 2025/4/11 14:46
 * @descript:
 */
class DotImageView : View {
    private var mPaint: Paint? = null //用于画anything

    private var mPaintBg: Paint? = null //用于画anything
    private var dotNum: String? = null //红点数字
    private var mAlphaValue = 0f //透明度动画值
    private var mRotateValue = 1f //旋转动画值
    private var inited = false //标记透明动画是否执行过，防止因onreseme 切换导致重复执行


    private var mBitmap: Bitmap? = null //logo
    private val mLogoBackgroundRadius = 25f.dp2px() //logo的灰色背景圆的半径
    private val mLogoWhiteRadius = 20f.dp2px() //logo的白色背景的圆的半径
    private val mRedPointRadiusWithNum = 6f.dp2px() //红点圆半径
    private val mRedPointRadius = 3f.dp2px() //红点圆半径
    private val mRedPointOffset = 10f.dp2px()//红点对logo的偏移量，比如左红点就是logo中心的 x - mRedPointOffset

    private var isDrag = false //是否 绘制旋转放大动画，只有 非停靠边缘才绘制
    private var scaleOffset = 0f //放大偏移值
    private var mDragValueAnimator: ValueAnimator? = null //放大、旋转 属性动画
    private val mLinearInterpolator = LinearInterpolator() //通用用加速器
    var mDrawDarkBg: Boolean = true //是否绘制黑色背景，当菜单关闭时，才绘制灰色背景
    private var mCamera: Camera? = null //camera用于执行3D动画

    private var mDrawNum = false //只绘制红点还是红点+白色数字

    private var mStatus = NORMAL //0 正常，1 左，2右,3 中间方法旋转
    private var mLastStatus = mStatus
    private var mMatrix: Matrix? = null
    private var mIsResetPosition = false

    private var mBgColor = -0x67000000


    fun setBgColor(bgColor: Int) {
        mBgColor = bgColor
    }


    fun setDrawNum(drawNum: Boolean) {
        this.mDrawNum = drawNum
    }

    fun setDrawDarkBg(drawDarkBg: Boolean) {
        mDrawDarkBg = drawDarkBg
        invalidate()
    }

    fun getStatus(): Int {
        return mStatus
    }


    fun setStatus(status: Int) {
        this.mStatus = status
        isDrag = false
        if (this.mStatus != NORMAL) {
            setDrawNum(mDrawNum)
            this.mDrawDarkBg = true
        }
        invalidate()
    }

    fun setBitmap(bitmap: Bitmap?) {
        mBitmap = bitmap
    }

    constructor(context: Context?, bitmap: Bitmap?) : super(context) {
        this.mBitmap = bitmap
        init()
    }


    constructor(context: Context?) : super(context) {
        init()
    }

    constructor(context: Context?, @Nullable attrs: AttributeSet?) : super(context, attrs) {
        init()
    }

    constructor(context: Context?, @Nullable attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        init()
    }

    private fun init() {
        mPaint = Paint()
        mPaint!!.isAntiAlias = true
        mPaint!!.textSize = 10f.sp2px()
        mPaint!!.style = Paint.Style.FILL

        mPaintBg = Paint()
        mPaintBg!!.isAntiAlias = true
        mPaintBg!!.style = Paint.Style.FILL
        mPaintBg!!.color = mBgColor //60% 黑色背景 （透明度 40%）

        mCamera = Camera()
        mMatrix = Matrix()
    }

    /**
     * 这个方法是否有优化空间
     */
    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        val wh = mLogoBackgroundRadius * 2
        setMeasuredDimension(wh.toInt(), wh.toInt())
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        val centerX = (width / 2).toFloat()
        val centerY = (height / 2).toFloat()
        canvas.save() //保存一份快照，方便后面恢复
        mCamera?.save()
        if (mStatus == NORMAL) {
            if (mLastStatus != NORMAL) {
                canvas.restore() //恢复画布的原始快照
                mCamera?.restore()
            }

            if (isDrag) {
                //如果当前是拖动状态则放大并旋转
                canvas.scale((scaleOffset + 1f), (scaleOffset + 1f), (width / 2).toFloat(), (height / 2).toFloat())
                if (mIsResetPosition) {
                    //手指拖动后离开屏幕复位时使用 x轴旋转 3d动画
                    mCamera?.save()
                    mCamera?.rotateX(720 * scaleOffset) //0-720度 最多转两圈
                    mCamera?.getMatrix(mMatrix)

                    mMatrix?.preTranslate((-width / 2).toFloat(), (-height / 2).toFloat())
                    mMatrix?.postTranslate((width / 2).toFloat(), (height / 2).toFloat())
                    canvas.concat(mMatrix)
                    mCamera?.restore()
                } else {
                    //手指拖动且手指未离开屏幕则使用 绕图心2d旋转动画
                    canvas.rotate(60 * mRotateValue, (width / 2).toFloat(), (height / 2).toFloat())
                }
            }
        } else if (mStatus == HIDE_LEFT) {
            canvas.translate(-width * hideOffset, 0f)
            canvas.rotate(-45f, (width / 2).toFloat(), (height / 2).toFloat())
        } else if (mStatus == HIDE_RIGHT) {
            canvas.translate(width * hideOffset, 0f)
            canvas.rotate(45f, (width / 2).toFloat(), (height / 2).toFloat())
        }
        canvas.save()
        if (!isDrag) {
            if (mDrawDarkBg) {
                mPaintBg!!.color = mBgColor
                canvas.drawCircle(centerX, centerY, mLogoBackgroundRadius.toFloat(), mPaintBg!!)
                // 60% 白色 （透明度 40%）
                mPaint!!.color = -0x66000001
            } else {
                //100% 白色背景 （透明度 0%）
                mPaint!!.color = -0x1
            }
            if (mAlphaValue != 0f) {
                mPaint!!.alpha = (mAlphaValue * 255).toInt()
            }
            canvas.drawCircle(centerX, centerY, mLogoWhiteRadius.toFloat(), mPaint!!)
        }

        canvas.restore()
        //100% 白色背景 （透明度 0%）
        mPaint!!.color = -0x1
        val left = (centerX - mBitmap!!.width / 2).toInt()
        val top = (centerY - mBitmap!!.height / 2).toInt()
        canvas.drawBitmap(mBitmap!!, left.toFloat(), top.toFloat(), mPaint)


        if (!TextUtils.isEmpty(dotNum)) {
            val readPointRadus = (if (mDrawNum) mRedPointRadiusWithNum else mRedPointRadius)
            mPaint!!.color = Color.RED
            if (mStatus == HIDE_LEFT) {
                canvas.drawCircle(centerX + mRedPointOffset, centerY - mRedPointOffset, readPointRadus.toFloat(), mPaint!!)
                if (mDrawNum) {
                    mPaint!!.color = Color.WHITE
                    canvas.drawText(
                        dotNum!!, centerX + mRedPointOffset - getTextWidth(dotNum, mPaint) / 2, centerY - mRedPointOffset + getTextHeight(dotNum, mPaint) / 2,
                        mPaint!!
                    )
                }
            } else if (mStatus == HIDE_RIGHT) {
                canvas.drawCircle(centerX - mRedPointOffset, centerY - mRedPointOffset, readPointRadus.toFloat(), mPaint!!)
                if (mDrawNum) {
                    mPaint!!.color = Color.WHITE
                    canvas.drawText(
                        dotNum!!, centerX - mRedPointOffset - getTextWidth(dotNum, mPaint) / 2, centerY - mRedPointOffset + getTextHeight(dotNum, mPaint) / 2,
                        mPaint!!
                    )
                }
            } else {
                if (mLastStatus == HIDE_LEFT) {
                    canvas.drawCircle(centerX + mRedPointOffset, centerY - mRedPointOffset, readPointRadus.toFloat(), mPaint!!)
                    if (mDrawNum) {
                        mPaint!!.color = Color.WHITE
                        canvas.drawText(
                            dotNum!!,
                            centerX + mRedPointOffset - getTextWidth(dotNum, mPaint) / 2,
                            centerY - mRedPointOffset + getTextHeight(dotNum, mPaint) / 2,
                            mPaint!!
                        )
                    }
                } else if (mLastStatus == HIDE_RIGHT) {
                    canvas.drawCircle(centerX - mRedPointOffset, centerY - mRedPointOffset, readPointRadus.toFloat(), mPaint!!)
                    if (mDrawNum) {
                        mPaint!!.color = Color.WHITE
                        canvas.drawText(
                            dotNum!!,
                            centerX - mRedPointOffset - getTextWidth(dotNum, mPaint) / 2,
                            centerY - mRedPointOffset + getTextHeight(dotNum, mPaint) / 2,
                            mPaint!!
                        )
                    }
                }
            }
        }
        mLastStatus = mStatus
    }


    fun setDotNum(num: Int, l: Animator.AnimatorListener?) {
        if (!inited) {
            startAnim(num, l)
        } else {
            refreshDot(num)
        }
    }

    private fun refreshDot(num: Int) {
        if (num > 0) {
            val dotNumTmp = num.toString()
            if (!TextUtils.equals(dotNum, dotNumTmp)) {
                dotNum = dotNumTmp
                invalidate()
            }
        } else {
            dotNum = null
        }
    }


    fun startAnim(num: Int, l: Animator.AnimatorListener?) {
        val valueAnimator = ValueAnimator.ofFloat(1f, 0.6f, 1f, 0.6f)
        valueAnimator.interpolator = mLinearInterpolator
        valueAnimator.setDuration(3000)
        valueAnimator.addUpdateListener { animation ->
            mAlphaValue = animation.animatedValue as Float
            invalidate()
        }
        valueAnimator.addListener(l)
        valueAnimator.addListener(object : Animator.AnimatorListener {
            override fun onAnimationStart(animation: Animator) {
            }

            override fun onAnimationEnd(animation: Animator) {
                inited = true
                refreshDot(num)
                mAlphaValue = 0f
            }

            override fun onAnimationCancel(animation: Animator) {
                mAlphaValue = 0f
            }

            override fun onAnimationRepeat(animation: Animator) {
            }
        })
        valueAnimator.start()
    }

    fun setDrag(drag: Boolean, offset: Float, isResetPosition: Boolean) {
        isDrag = drag
        this.mIsResetPosition = isResetPosition
        if (offset > 0 && offset != this.scaleOffset) {
            this.scaleOffset = offset
        }
        if (isDrag && mStatus == NORMAL) {
            if (mDragValueAnimator != null) {
                if (mDragValueAnimator!!.isRunning) return
            }
            mDragValueAnimator = ValueAnimator.ofFloat(0f, 6f, 12f, 0f)
            mDragValueAnimator?.setInterpolator(mLinearInterpolator)
            mDragValueAnimator?.addUpdateListener(AnimatorUpdateListener { animation ->
                mRotateValue = animation.animatedValue as Float
                invalidate()
            })
            mDragValueAnimator?.addListener(object : Animator.AnimatorListener {
                override fun onAnimationStart(animation: Animator) {
                }

                override fun onAnimationEnd(animation: Animator) {
                    isDrag = false
                    mIsResetPosition = false
                }

                override fun onAnimationCancel(animation: Animator) {
                }

                override fun onAnimationRepeat(animation: Animator) {
                }
            })
            mDragValueAnimator?.setDuration(1000)
            mDragValueAnimator?.start()
        }
    }



    private fun getTextHeight(text: String?, paint: Paint?): Float {
        val rect: Rect = Rect()
        paint!!.getTextBounds(text, 0, text!!.length, rect)
        return rect.height() / 1.1f
    }

    private fun getTextWidth(text: String?, paint: Paint?): Float {
        return paint!!.measureText(text)
    }

    companion object {
        private val TAG: String = DotImageView::class.java.simpleName
        const val NORMAL: Int = 0 //不隐藏
        const val HIDE_LEFT: Int = 1 //左边隐藏
        const val HIDE_RIGHT: Int = 2 //右边隐藏
        private const val hideOffset = 0.4f //往左右隐藏多少宽度的偏移值， 隐藏宽度的0.4
    }
}
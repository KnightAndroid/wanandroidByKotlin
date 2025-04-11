package com.knight.kotlin.library_util.floatmenu

import android.animation.Animator
import android.animation.ObjectAnimator
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.PointF
import android.graphics.Rect
import android.graphics.RectF
import android.text.TextUtils
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import com.knight.kotlin.library_base.util.dp2px
import com.knight.kotlin.library_base.util.sp2px


/**
 * @author created by luguian
 * @organize
 * @Date 2025/4/11 15:01
 * @descript:
 */
class FloatMenuView : View {
    private var mStatus = STATUS_RIGHT //默认右边

    private var mPaint: Paint? = null //画笔
    private var mBackgroundColor = 0x00FFFFFF //默认背景颜色 完全透明的白色

    private var mMenuBackgroundColor = -1 //菜单的背景颜色

    private var mBgRect: RectF? = null //菜单的背景矩阵
    private val mItemWidth = 50f.dp2px() //菜单项的宽度
    private val mItemHeight = 50f.dp2px() //菜单项的高度
    private var mItemLeft = 0 //菜单项左边的默认偏移值，这里是0
    private val mCorner = 2f.dp2px() //菜单背景的圆角多出的宽度


    private val mRadius = 4f.dp2px() //红点消息半径
    private val mRedPointRadiuWithNoNum = 3f.dp2px() //红点圆半径

    private val mFontSizePointNum = 10f.sp2px() //红点消息数字的文字大小

    private val mFontSizeTitle = 12f.sp2px() //菜单项的title的文字大小
    private var mFirstItemTop = 0f //菜单项的最小y值，上面起始那条线
    private var mDrawNum = false //是否绘制数字，false则只绘制红点
    private var circleBg = false //菜单项背景是否绘制成圆形，false则绘制矩阵

    private var mItemList: List<FloatItem> = ArrayList() //菜单项的内容
    private val mItemRectList: MutableList<RectF> = ArrayList() //菜单项所占用位置的记录，用于判断点击事件

    private var mOnMenuClickListener: OnMenuClickListener? = null //菜单项的点击事件回调

    private var mAlphaAnim: ObjectAnimator? = null //消失关闭动画的透明值

    //设置菜单内容集合
    fun setItemList(itemList: List<FloatItem>) {
        mItemList = itemList
    }

    //设置是否绘制红点数字
    fun drawNum(drawNum: Boolean) {
        mDrawNum = drawNum
    }

    //设置是否绘制圆形菜单，否则矩阵
    fun setCircleBg(circleBg: Boolean) {
        this.circleBg = circleBg
    }


    //用于标记所依赖的view的screen的坐标，实际view的坐标是window坐标，所以这里后面会减去状态栏的高度
    //设置菜单的背景颜色
    fun setMenuBackgroundColor(mMenuBackgroundColor: Int) {
        this.mMenuBackgroundColor = mMenuBackgroundColor
    }

    //设置这个view（整个屏幕）的背景，这里默认透明
    override fun setBackgroundColor(BackgroundColor: Int) {
        this.mBackgroundColor = BackgroundColor
    }


    //下面开始的注释我写不动了，看不懂的话请自行领悟吧
    constructor(context: Context?) : super(context)

    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs)

    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    constructor(baseContext: Context?, status: Int) : super(baseContext) {
        mStatus = status
        val screenWidth = resources.displayMetrics.widthPixels
        val screenHeight = resources.displayMetrics.heightPixels
        mBgRect = RectF(0f, 0f, screenWidth.toFloat(), screenHeight.toFloat())
        initView()
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        setMeasuredDimension((mItemWidth * mItemList.size).toInt(), mItemHeight.toInt())
    }

    private fun initView() {
        mPaint = Paint()
        mPaint!!.isAntiAlias = true
        mPaint!!.style = Paint.Style.FILL
        mPaint!!.textSize = 12f.sp2px()

        mAlphaAnim = ObjectAnimator.ofFloat(this, "alpha", 1.0f, 0f)
        mAlphaAnim?.setDuration(50)
        mAlphaAnim?.addListener(object : MyAnimListener() {
            override fun onAnimationEnd(animation: Animator) {
                if (mOnMenuClickListener != null) {
                    removeView()
                    mOnMenuClickListener!!.dismiss()
                }
            }
        })

        mFirstItemTop = 0f
        mItemLeft = if (mStatus == STATUS_LEFT) {
            0
        } else {
            0
        }
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        when (mStatus) {
            STATUS_LEFT -> {
                drawBackground(canvas)
                drawFloatLeftItem(canvas)
            }

            STATUS_RIGHT -> {
                drawBackground(canvas)
                drawFloatLeftItem(canvas)
            }
        }
    }

    private fun drawBackground(canvas: Canvas) {
        mPaint!!.color = mBackgroundColor
        canvas.drawRect(mBgRect!!, mPaint!!)
    }

    private fun drawFloatLeftItem(canvas: Canvas) {
        mItemRectList.clear()
        for (i in mItemList.indices) {
            canvas.save()
            mPaint!!.color = mMenuBackgroundColor
            if (circleBg) {
                val cx = ((mItemLeft + i * mItemWidth) + mItemWidth / 2).toFloat() //x中心点
                val cy = mFirstItemTop + mItemHeight / 2 //y中心点
                val radius = (mItemWidth / 2).toFloat() //半径
                canvas.drawCircle(cx, cy, radius, mPaint!!)
            } else {
                mPaint!!.color = mItemList[i].bgColor
                canvas.drawRect(
                    (mItemLeft + i * mItemWidth).toFloat(), mFirstItemTop, (mItemLeft + mItemWidth + i * mItemWidth).toFloat(), mFirstItemTop + mItemHeight,
                    mPaint!!
                )
            }

            mItemRectList.add(
                RectF(
                    (mItemLeft + i * mItemWidth).toFloat(),
                    mFirstItemTop,
                    (mItemLeft + mItemWidth + i * mItemWidth).toFloat(),
                    mFirstItemTop + mItemHeight
                )
            )
            mPaint!!.color = mItemList[i].bgColor
            drawIconTitleDot(canvas, i)
        }
        canvas.restore()
    }


    private fun drawIconTitleDot(canvas: Canvas, position: Int) {
        val floatItem = mItemList[position]

        if (floatItem.icon != null) {
            val centerX = (mItemLeft + mItemWidth / 2 + (mItemWidth) * position).toFloat() //计算每一个item的中心点x的坐标值
            val centerY = mFirstItemTop + mItemHeight / 2 //计算每一个item的中心点的y坐标值

            val left = centerX - mItemWidth / 4 //计算icon的左坐标值 中心点往左移宽度的四分之一
            val right = centerX + mItemWidth / 4

            val iconH = mItemHeight * 0.5f //计算出icon的宽度 = icon的高度

            val textH = getTextHeight(floatItem.title, mPaint)
            val paddingH = (mItemHeight - iconH - textH - mRadius) / 2 //总高度减去文字的高度，减去icon高度，再除以2就是上下的间距剩余

            val top = centerY - mItemHeight / 2 + paddingH //计算icon的上坐标值
            val bottom = top + iconH //剩下的高度空间用于画文字

            //画icon
            mPaint!!.color = floatItem.titleColor
            canvas.drawBitmap(floatItem.icon, null, RectF(left, top, right, bottom), mPaint)
            if (!TextUtils.isEmpty(floatItem.dotNum) && floatItem.dotNum != "0") {
                val dotLeft = centerX + mItemWidth / 5
                val cx = dotLeft + mCorner //x中心点
                val cy = top + mCorner //y中心点

                val radius = if (mDrawNum) mRadius else mRedPointRadiuWithNoNum
                //画红点
                mPaint!!.color = Color.RED
                canvas.drawCircle(cx, cy, radius.toFloat(), mPaint!!)
                if (mDrawNum) {
                    mPaint!!.color = Color.WHITE
                    mPaint!!.textSize = mFontSizePointNum.toFloat()
                    //画红点消息数
                    canvas.drawText(
                        floatItem.dotNum!!, cx - getTextWidth(floatItem.dotNum, mPaint) / 2, cy + getTextHeight(floatItem.dotNum, mPaint) / 2,
                        mPaint!!
                    )
                }
            }
            mPaint!!.color = floatItem.titleColor
            mPaint!!.textSize = mFontSizeTitle.toFloat()
            //画menu title
            canvas.drawText(
                floatItem.title,
                centerX - getTextWidth(floatItem.title, mPaint) / 2,
                centerY + iconH / 2 + getTextHeight(floatItem.title, mPaint) / 2,
                mPaint!!
            )
        }
    }


    fun startAnim() {
        if (mItemList.size == 0) {
            return
        }
        invalidate()
    }


    fun dismiss() {
        if (!mAlphaAnim!!.isRunning) {
            mAlphaAnim!!.start()
        }
    }

    private fun removeView() {
        val vg = this.parent as ViewGroup
        vg?.removeView(this)
    }

    override fun onWindowVisibilityChanged(visibility: Int) {
        if (visibility == GONE) {
            if (mOnMenuClickListener != null) {
                mOnMenuClickListener!!.dismiss()
            }
        }
        super.onWindowVisibilityChanged(visibility)
    }

    fun setOnMenuClickListener(onMenuClickListener: OnMenuClickListener?) {
        this.mOnMenuClickListener = onMenuClickListener
    }

    interface OnMenuClickListener {
        fun onItemClick(position: Int, title: String?)

        fun dismiss()
    }

    private abstract inner class MyAnimListener : Animator.AnimatorListener {
        override fun onAnimationStart(animation: Animator) {
        }

        override fun onAnimationCancel(animation: Animator) {
        }

        override fun onAnimationRepeat(animation: Animator) {
        }
    }


    override fun onTouchEvent(event: MotionEvent): Boolean {
        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                var i = 0
                while (i < mItemRectList.size) {
                    if (mOnMenuClickListener != null && isPointInRect(PointF(event.x, event.y), mItemRectList[i])) {
                        mOnMenuClickListener!!.onItemClick(i, mItemList[i].title)
                        return true
                    }
                    i++
                }
                dismiss()
            }
        }
        return false
    }

    private fun isPointInRect(pointF: PointF, targetRect: RectF): Boolean {
        return pointF.x >= targetRect.left && pointF.x <= targetRect.right && pointF.y >= targetRect.top && pointF.y <= targetRect.bottom
    }


    class Builder(private val mActivity: Context) {
        private var mFloatItems: MutableList<FloatItem> = ArrayList()
        private var mBgColor = Color.TRANSPARENT
        private var mStatus = STATUS_LEFT
        private var cicleBg = false
        private var mMenuBackgroundColor = -1
        private var mDrawNum = false


        fun drawNum(drawNum: Boolean): Builder {
            mDrawNum = drawNum
            return this
        }


        fun setMenuBackgroundColor(mMenuBackgroundColor: Int): Builder {
            this.mMenuBackgroundColor = mMenuBackgroundColor
            return this
        }


        fun setCicleBg(cicleBg: Boolean): Builder {
            this.cicleBg = cicleBg
            return this
        }

        fun setStatus(status: Int): Builder {
            mStatus = status
            return this
        }

        fun setFloatItems(floatItems: MutableList<FloatItem>): Builder {
            this.mFloatItems = floatItems
            return this
        }


        fun addItem(floatItem: FloatItem): Builder {
            mFloatItems.add(floatItem)
            return this
        }

        fun addItems(list: List<FloatItem>?): Builder {
            mFloatItems.addAll(list!!)
            return this
        }

        fun setBackgroundColor(color: Int): Builder {
            mBgColor = color
            return this
        }

        fun create(): FloatMenuView {
            val floatMenuView = FloatMenuView(mActivity, mStatus)
            floatMenuView.setItemList(mFloatItems)
            floatMenuView.setBackgroundColor(mBgColor)
            floatMenuView.setCircleBg(cicleBg)
            floatMenuView.startAnim()
            floatMenuView.drawNum(mDrawNum)
            floatMenuView.setMenuBackgroundColor(mMenuBackgroundColor)
            return floatMenuView
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
        const val STATUS_LEFT: Int = 3 //展开左边菜单
        const val STATUS_RIGHT: Int = 4 //展开右边菜单
    }
}
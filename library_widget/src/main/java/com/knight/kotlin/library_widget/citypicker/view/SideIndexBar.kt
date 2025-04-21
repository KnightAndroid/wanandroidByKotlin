package com.knight.kotlin.library_widget.citypicker.view


import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import android.widget.TextView
import com.knight.kotlin.library_base.util.dp2px
import com.knight.kotlin.library_widget.R
import java.util.Arrays
import kotlin.math.abs
import kotlin.math.max


/**
 * @author created by luguian
 * @organize
 * @Date 2025/4/17 11:22
 * @descript:选择器
 */
class SideIndexBar @JvmOverloads constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int = 0) :
    View(context, attrs, defStyleAttr) {
    private lateinit var mIndexItems: MutableList<String>
    private var mItemHeight = 0f //每个index的高度
    private var mTextSize = 0 //sp
    private var mTextColor = 0
    private var mTextTouchedColor = 0
    private var mCurrentIndex = -1

    private var mPaint: Paint? = null
    private var mTouchedPaint: Paint? = null

    private var mWidth = 0
    private var mHeight = 0
    private var mTopMargin = 0f //居中绘制，文字绘制起点和控件顶部的间隔

    private var mOverlayTextView: TextView? = null
    private var mOnIndexChangedListener: OnIndexTouchedChangedListener? = null

    private var navigationBarHeight = 0

    fun setNavigationBarHeight(height: Int) {
        this.navigationBarHeight = height
    }

    init {
        init(context,attrs)
    }

    private fun init(context: Context,attrs: AttributeSet) {
        mIndexItems = mutableListOf()
        mIndexItems.addAll(Arrays.asList(*DEFAULT_INDEX_ITEMS))


        val typedArray = context.obtainStyledAttributes(attrs, R.styleable.SideIndexView)
        mTextSize = typedArray.getDimensionPixelSize(R.styleable.SideIndexView_side_text_size,14.dp2px())
        mTextColor = typedArray.getColor(R.styleable.SideIndexView_side_text_color, Color.BLACK)
        mTextTouchedColor = typedArray.getColor(R.styleable.SideIndexView_side_text_touch_color, Color.BLACK)

        mPaint = Paint(Paint.ANTI_ALIAS_FLAG)
        mPaint!!.textSize = mTextSize.toFloat()
        mPaint!!.color = mTextColor

        mTouchedPaint = Paint(Paint.ANTI_ALIAS_FLAG)
        mTouchedPaint!!.textSize = mTextSize.toFloat()
        mTouchedPaint!!.color = mTextTouchedColor
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        var index: String
        for (i in mIndexItems!!.indices) {
            index = mIndexItems!![i]
            val fm = mPaint!!.fontMetrics
            canvas.drawText(
                index,
                (mWidth - mPaint!!.measureText(index)) / 2,
                mItemHeight / 2 + (fm.bottom - fm.top) / 2 - fm.bottom + mItemHeight * i + mTopMargin,
                (if (i == mCurrentIndex) mTouchedPaint else mPaint)!!
            )
        }
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        mWidth = width
        mHeight = if (abs((h - oldh).toDouble()) == navigationBarHeight.toDouble()) {
            //底部导航栏隐藏或显示
            h
        } else {
            //避免软键盘弹出时挤压
            max(height.toDouble(), oldh.toDouble()).toInt()
        }
        mItemHeight = (mHeight / mIndexItems!!.size).toFloat()
        mTopMargin = (mHeight - mItemHeight * mIndexItems!!.size) / 2
    }

    override fun performClick(): Boolean {
        return super.performClick()
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        performClick()
        when (event.action) {
            MotionEvent.ACTION_DOWN, MotionEvent.ACTION_MOVE -> {
                val y = event.y
                val indexSize = mIndexItems!!.size
                var touchedIndex = (y / mItemHeight).toInt()
                if (touchedIndex < 0) {
                    touchedIndex = 0
                } else if (touchedIndex >= indexSize) {
                    touchedIndex = indexSize - 1
                }
                if (mOnIndexChangedListener != null && touchedIndex >= 0 && touchedIndex < indexSize) {
                    if (touchedIndex != mCurrentIndex) {
                        mCurrentIndex = touchedIndex
                        if (mOverlayTextView != null) {
                            mOverlayTextView!!.visibility = VISIBLE
                            mOverlayTextView!!.text = mIndexItems!![touchedIndex]
                        }
                        mOnIndexChangedListener!!.onIndexChanged(mIndexItems[touchedIndex], touchedIndex)
                        invalidate()
                    }
                }
            }

            MotionEvent.ACTION_UP, MotionEvent.ACTION_CANCEL -> {
                mCurrentIndex = -1
                if (mOverlayTextView != null) {
                    mOverlayTextView!!.visibility = GONE
                }
                invalidate()
            }
        }
        return true
    }

    fun setOverlayTextView(overlay: TextView?): SideIndexBar {
        this.mOverlayTextView = overlay
        return this
    }

    fun setOnIndexChangedListener(listener: OnIndexTouchedChangedListener?): SideIndexBar {
        this.mOnIndexChangedListener = listener
        return this
    }

    interface OnIndexTouchedChangedListener {
        fun onIndexChanged(index: String, position: Int)
    }

    companion object {
        private val DEFAULT_INDEX_ITEMS = arrayOf(
            "当前", "热门", "A", "B", "C", "D", "E", "F", "G", "H",
            "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z", "#"
        )
    }
}
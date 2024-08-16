package com.knight.kotlin.library_widget

import android.annotation.TargetApi
import android.content.Context
import android.graphics.Color
import android.os.Build
import android.text.TextUtils
import android.util.AttributeSet
import android.util.TypedValue
import android.view.Gravity
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.Transformation
import android.widget.LinearLayout
import android.widget.TextView


/**
 * @author created by luguian
 * @organize
 * @Date 2024/8/16 11:38
 * @descript:https://github.com/shengwang520/ExpandableTextView 折叠view
 */
class ExpandableTextView : LinearLayout, View.OnClickListener {
    private var mTv: SlowShowTextView? = null
    private var mStateTv: TextView? = null// TextView to expand/collapse
    private var mRelayout = false
    private var mCollapsed = true // Show short version as default.
    private var mCollapsedHeight = 0
    private var mTextHeightWithMaxLines = 0
    private var mMaxCollapsedLines = 0
    private var mMarginBetweenTxtAndBottom = 0
    private var mStateTvGravity = 0
    private var mCollapsedString: String? = null
    private var mExpandString: String? = null
    private var mAnimationDuration = 0
    private var mContentTextSize = 0
    private var mContentTextColor = 0
    private var mContentLineSpacingMultiplier = 0f
    private var mStateTextColor = 0
    private var mAnimating = false
    private var mVisible = 0

    /* Listener for callback */
    private var mListener: OnExpandStateChangeListener? = null
    private val mRunnable = Runnable { mMarginBetweenTxtAndBottom = height - mTv!!.height }

    @JvmOverloads
    constructor(context: Context, attrs: AttributeSet? = null) : super(context, attrs) {
        init(context, attrs)
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    constructor(context: Context, attrs: AttributeSet?, defStyle: Int) : super(context, attrs, defStyle) {
        init(context, attrs)
    }

    override fun setOrientation(orientation: Int) {
        require(HORIZONTAL != orientation) { "ExpandableTextView only supports Vertical Orientation." }
        super.setOrientation(orientation)
    }

    override fun onClick(view: View) {
        if (mStateTv!!.visibility != VISIBLE) {
            return
        }
        mCollapsed = !mCollapsed
        //mStateTv!!.text = if (mCollapsed) mExpandString else mCollapsedString
        mStateTv!!.setBackgroundResource(if (mCollapsed) R.drawable.widget_tv_bitmap_arrow else R.drawable.widget_tv_bitmap_up)
        // mark that the animation is in progress
        mAnimating = true
        val animation: Animation = if (mCollapsed) {
            ExpandCollapseAnimation(this, height, mTv!!.lineHeight().toInt() * mMaxCollapsedLines  + mTextHeightWithMaxLines)
        } else {
            ExpandCollapseAnimation(
                this, height, height + (measuredHeight *  2 / 3) + mTv!!.height
            )
        }
        animation.fillAfter = true
        animation.setAnimationListener(object : Animation.AnimationListener {
            override fun onAnimationStart(animation: Animation) {}
            override fun onAnimationEnd(animation: Animation) {
                // clear animation here to avoid repeated applyTransformation() calls
                clearAnimation()
                // clear the animation flag
                mAnimating = false

                // notify the listener
                if (mListener != null) {
                    mListener!!.onExpandStateChanged(mTv, !mCollapsed)
                }
            }

            override fun onAnimationRepeat(animation: Animation) {}
        })
        clearAnimation()
        startAnimation(animation)
    }

    override fun onInterceptTouchEvent(ev: MotionEvent): Boolean {
        // while an animation is in progress, intercept all the touch events to children to
        // prevent extra clicks during the animation
        return mAnimating
    }

    override fun onFinishInflate() {
        super.onFinishInflate()
        findViews()
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        // If no change, measure and return
        if (!mRelayout || visibility == GONE) {
            super.onMeasure(widthMeasureSpec, heightMeasureSpec)
            return
        }
        mRelayout = false

        // Setup with optimistic case
        // i.e. Everything fits. No button needed
        mStateTv!!.visibility = if (mVisible == 0) GONE else INVISIBLE
        mTv!!.maxLines = Int.MAX_VALUE

        // Measure
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)

        // If the text fits in collapsed mode, we are done.
//        if (mTv!!.lineCount() <= mMaxCollapsedLines) {
//            return
//        }

        // Saves the text height w/ max lines
        mTextHeightWithMaxLines = getRealTextViewHeight(mTv!!)

        // Doesn't fit in collapsed mode. Collapse text view as needed. Show
        // button.
        if (mCollapsed) {
            mTv!!.maxLines = mMaxCollapsedLines
        }
        mStateTv!!.visibility = VISIBLE

        // Re-measure with new setup
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        if (mCollapsed) {
            // Gets the margin between the TextView's bottom and the ViewGroup's bottom
            mTv!!.post(mRunnable)
            // Saves the collapsed height of this ViewGroup
            mCollapsedHeight = measuredHeight
        }
    }

    fun setOnExpandStateChangeListener(listener: OnExpandStateChangeListener?) {
        mListener = listener
    }

    fun setText(charSequence: CharSequence?, isCollapsed: Boolean) {
        clearAnimation()
        mCollapsed = isCollapsed
        //mStateTv!!.text = if (mCollapsed) mExpandString else mCollapsedString
        mStateTv!!.setBackgroundResource(if (mCollapsed) R.drawable.widget_tv_bitmap_arrow else R.drawable.widget_tv_bitmap_up)

        text = charSequence
        layoutParams.height = ViewGroup.LayoutParams.WRAP_CONTENT
        requestLayout()
    }

    var text: CharSequence?
        get() = if (mTv == null) {
            ""
        } else mTv!!.text
        set(text) {
            mRelayout = true
            mTv!!.setDynamicText(text.toString())
            visibility = if (TextUtils.isEmpty(text)) GONE else VISIBLE
        }

    private fun init(context: Context, attrs: AttributeSet?) {
        LayoutInflater.from(context).inflate(R.layout.view_expandable_textview, this, true)
        // enforces vertical orientation
        orientation = VERTICAL

        // default visibility is gone
        visibility = GONE
        val typedArray = getContext().obtainStyledAttributes(attrs, R.styleable.ExpandableTextView)
        mMaxCollapsedLines = typedArray.getInt(R.styleable.ExpandableTextView_maxCollapsedLines, MAX_COLLAPSED_LINES)
        mAnimationDuration = typedArray.getInt(R.styleable.ExpandableTextView_animDuration, DEFAULT_ANIM_DURATION)
        mContentTextSize = typedArray.getDimensionPixelSize(R.styleable.ExpandableTextView_contentTextSize, DEFAULT_CONTENT_TEXT_SIZE)
        mContentLineSpacingMultiplier =
            typedArray.getFloat(R.styleable.ExpandableTextView_contentLineSpacingMultiplier, DEFAULT_CONTENT_TEXT_LINE_SPACING_MULTIPLIER)
        mContentTextColor = typedArray.getColor(R.styleable.ExpandableTextView_contentTextColor, Color.BLACK)
        mStateTvGravity = typedArray.getInt(R.styleable.ExpandableTextView_expandCollapseTextGravity, STATE_TV_GRAVITY_RIGHT)
        mExpandString = typedArray.getString(R.styleable.ExpandableTextView_expandText)
        mCollapsedString = typedArray.getString(R.styleable.ExpandableTextView_collapseText)
        mStateTextColor = typedArray.getColor(R.styleable.ExpandableTextView_expandCollapseTextColor, Color.BLACK)
        mVisible = typedArray.getInt(R.styleable.ExpandableTextView_expandCollapseTextVisible, STATE_TV_VISIBLE_GONE)
        if (mExpandString == null) {
            mExpandString = context.getString(R.string.widget_expand_hint)
        }
        if (mCollapsedString == null) {
            mCollapsedString = context.getString(R.string.widget_shrink_hint)
        }
        typedArray.recycle()
    }

    private fun findViews() {
        mTv = findViewById<View>(R.id.expandable_text) as SlowShowTextView
        mTv!!.setTextColor(mContentTextColor)
        mTv!!.setTextSize(TypedValue.COMPLEX_UNIT_PX, mContentTextSize.toFloat())
        mTv!!.setLineSpacing(0f, mContentLineSpacingMultiplier)
        mTv!!.setOnClickListener(this)
        mStateTv = findViewById<View>(R.id.expand_collapse) as TextView
        val params = LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT)
        when (mStateTvGravity) {
            STATE_TV_GRAVITY_LEFT -> {
                params.gravity = Gravity.START
            }
            STATE_TV_GRAVITY_CENTER -> {
                params.gravity = Gravity.CENTER_HORIZONTAL
            }
            STATE_TV_GRAVITY_RIGHT -> {
                params.gravity = Gravity.END
            }
        }
        mStateTv!!.layoutParams = params
      //  mStateTv!!.text = if (mCollapsed) mExpandString else mCollapsedString
        mStateTv!!.setTextColor(mStateTextColor)
        mStateTv!!.compoundDrawablePadding = 10

        mStateTv!!.setBackgroundResource(if (mCollapsed) R.drawable.widget_tv_bitmap_arrow else R.drawable.widget_tv_bitmap_up)

        mStateTv!!.setOnClickListener(this)
    }

    internal inner class ExpandCollapseAnimation(private val mTargetView: View, private val mStartHeight: Int, private val mEndHeight: Int) :
        Animation() {
        override fun applyTransformation(interpolatedTime: Float, t: Transformation) {
            val newHeight = ((mEndHeight - mStartHeight) * interpolatedTime + mStartHeight).toInt()
            mTv!!.maxHeight = newHeight - mMarginBetweenTxtAndBottom
            mTargetView.layoutParams.height = newHeight
            mTargetView.requestLayout()
        }

        override fun willChangeBounds(): Boolean {
            return true
        }

        init {
            duration = mAnimationDuration.toLong()
        }
    }

    interface OnExpandStateChangeListener {
        /**
         * Called when the expand/collapse animation has been finished
         *
         * @param textView   - TextView being expanded/collapsed
         * @param isExpanded - true if the TextView has been expanded
         */
        fun onExpandStateChanged(textView: TextView?, isExpanded: Boolean)
    }

    companion object {
        /* The default number of lines */ //默认显示行数
        private const val MAX_COLLAPSED_LINES = 3

        /* The default animation duration  动画时间*/
        private const val DEFAULT_ANIM_DURATION = 300

        /* The default content text size 字体大小*/
        private const val DEFAULT_CONTENT_TEXT_SIZE = 16
        private const val DEFAULT_CONTENT_TEXT_LINE_SPACING_MULTIPLIER = 1.0f
        private const val STATE_TV_GRAVITY_LEFT = 0
        private const val STATE_TV_GRAVITY_CENTER = 1
        private const val STATE_TV_GRAVITY_RIGHT = 2

        /* 状态按钮的隐藏方式 */
        private const val STATE_TV_VISIBLE_GONE = 0
        private const val STATE_TV_VISIBLE_INVISIBLE = 1

        private fun getRealTextViewHeight(textView: TextView): Int {
            val textHeight = textView.layout.getLineTop(textView.lineCount)
            val padding = textView.compoundPaddingTop + textView.compoundPaddingBottom
            return textHeight + padding
        }
    }
}
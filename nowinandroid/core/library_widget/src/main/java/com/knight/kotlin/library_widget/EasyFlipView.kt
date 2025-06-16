package com.knight.kotlin.library_widget

import android.animation.Animator
import android.animation.AnimatorInflater
import android.animation.AnimatorSet
import android.content.Context
import android.text.TextUtils
import android.util.AttributeSet
import android.view.GestureDetector.SimpleOnGestureListener
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.core.view.GestureDetectorCompat
import com.knight.kotlin.library_util.ThreadUtils


/**
 * @author created by luguian
 * @organize
 * @Date 2025/3/27 10:14
 * @descript:3D翻转卡片
 */
class EasyFlipView : FrameLayout {
    private val animFlipHorizontalOutId: Int = R.anim.widget_horizontal_flip_out
    private val animFlipHorizontalInId: Int = R.anim.widget_horizontal_flip_in
    private val animFlipHorizontalRightOutId: Int = R.anim.widget_horizontal_right_out
    private val animFlipHorizontalRightInId: Int = R.anim.widget_horizontal_right_in
    private val animFlipVerticalOutId: Int = R.anim.widget_vertical_flip_out
    private val animFlipVerticalInId: Int =R.anim.widget_vertical_flip_in
    private val animFlipVerticalFrontOutId: Int = R.anim.widget_vertical_front_out
    private val animFlipVerticalFrontInId: Int = R.anim.widget_vertical_flip_front_in

    enum class FlipState {
        FRONT_SIDE, BACK_SIDE
    }

    private var mSetRightOut: AnimatorSet? = null
    private var mSetLeftIn: AnimatorSet? = null
    private var mSetTopOut: AnimatorSet? = null
    private var mSetBottomIn: AnimatorSet? = null
    private var mIsBackVisible = false
    private var mCardFrontLayout: View? = null
    private var mCardBackLayout: View? = null
    private var flipType: String? = "vertical"
    private var flipTypeFrom: String? = "right"


    private var flipOnTouch = false
    private var flipDuration = 0
    private var flipEnabled = false
    private var flipOnceEnabled = false
    private var autoFlipBack = false
    private var autoFlipBackTime = 0

    private var context: Context
    private val x1 = 0f
    private val y1 = 0f

    private var mFlipState = FlipState.FRONT_SIDE

    private var onFlipListener: OnFlipAnimationListener? = null

    private var gestureDetector: GestureDetectorCompat? = null

    constructor(context: Context) : super(context) {
        this.context = context
        init(context, null)
    }

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        this.context = context
        init(context, attrs)
    }

    private fun init(context: Context, attrs: AttributeSet?) {
        // Setting Default Values
        flipOnTouch = true
        flipDuration = DEFAULT_FLIP_DURATION
        flipEnabled = true
        flipOnceEnabled = false
        autoFlipBack = false
        autoFlipBackTime = DEFAULT_AUTO_FLIP_BACK_TIME

        // Check for the attributes
        if (attrs != null) {
            // Attribute initialization
            val attrArray =
                context.obtainStyledAttributes(attrs, R.styleable.easy_flip_view, 0, 0)
            try {
                flipOnTouch = attrArray.getBoolean(R.styleable.easy_flip_view_flipOnTouch, true)
                flipDuration = attrArray.getInt(R.styleable.easy_flip_view_flipDuration, DEFAULT_FLIP_DURATION)
                flipEnabled = attrArray.getBoolean(R.styleable.easy_flip_view_flipEnabled, true)
                flipOnceEnabled = attrArray.getBoolean(R.styleable.easy_flip_view_flipOnceEnabled, false)
                autoFlipBack = attrArray.getBoolean(R.styleable.easy_flip_view_autoFlipBack, false)
                autoFlipBackTime = attrArray.getInt(R.styleable.easy_flip_view_autoFlipBackTime, DEFAULT_AUTO_FLIP_BACK_TIME)
                flipType = attrArray.getString(R.styleable.easy_flip_view_flipType)
                flipTypeFrom = attrArray.getString(R.styleable.easy_flip_view_flipFrom)

                if (TextUtils.isEmpty(flipType)) {
                    flipType = "vertical"
                }
                if (TextUtils.isEmpty(flipTypeFrom)) {
                    flipTypeFrom = "left"
                }
                //animFlipInId = attrArray.getResourceId(R.styleable.easy_flip_view_animFlipInId, R.animator.animation_horizontal_flip_in);
                //animFlipOutId = attrArray.getResourceId(R.styleable.easy_flip_view_animFlipOutId, R.animator.animation_horizontal_flip_out);
            } finally {
                attrArray.recycle()
            }
        }

        loadAnimations()
    }

    override fun onFinishInflate() {
        super.onFinishInflate()

        check(childCount <= 2) { "EasyFlipView can host only two direct children!" }

        findViews()
        changeCameraDistance()
        setupInitializations()
        initGestureDetector()
    }

    override fun addView(v: View?, pos: Int, params: ViewGroup.LayoutParams?) {
        check(childCount != 2) { "EasyFlipView can host only two direct children!" }

        super.addView(v, pos, params)

        findViews()
        changeCameraDistance()
    }

    override fun removeView(v: View?) {
        super.removeView(v)

        findViews()
    }

    override fun removeAllViewsInLayout() {
        super.removeAllViewsInLayout()

        // Reset the state
        mFlipState = FlipState.FRONT_SIDE

        findViews()
    }

    private fun findViews() {
        // Invalidation since we use this also on removeView
        mCardBackLayout = null
        mCardFrontLayout = null

        val childs = childCount
        if (childs < 1) {
            return
        }

        if (childs < 2) {
            // Only invalidate flip state if we have a single child
            mFlipState = FlipState.FRONT_SIDE

            mCardFrontLayout = getChildAt(0)
        } else if (childs == 2) {
            mCardFrontLayout = getChildAt(1)
            mCardBackLayout = getChildAt(0)
        }

        if (!isFlipOnTouch()) {
            mCardFrontLayout!!.visibility = VISIBLE

            if (mCardBackLayout != null) {
                mCardBackLayout!!.visibility = GONE
            }
        }
    }

    private fun setupInitializations() {
        mCardBackLayout!!.visibility = GONE
    }

    private fun initGestureDetector() {
        gestureDetector = GestureDetectorCompat(this.context, SwipeDetector())
    }

    private fun loadAnimations() {
        if (flipType.equals("horizontal", ignoreCase = true)) {
            if (flipTypeFrom.equals("left", ignoreCase = true)) {
                mSetRightOut =
                    AnimatorInflater.loadAnimator(this.context, animFlipHorizontalOutId) as AnimatorSet
                mSetLeftIn =
                    AnimatorInflater.loadAnimator(this.context, animFlipHorizontalInId) as AnimatorSet
            } else {
                mSetRightOut =
                    AnimatorInflater.loadAnimator(this.context, animFlipHorizontalRightOutId) as AnimatorSet
                mSetLeftIn =
                    AnimatorInflater.loadAnimator(this.context, animFlipHorizontalRightInId) as AnimatorSet
            }


            if (mSetRightOut == null || mSetLeftIn == null) {
                throw RuntimeException(
                    "No Animations Found! Please set Flip in and Flip out animation Ids."
                )
            }

            mSetRightOut!!.removeAllListeners()
            mSetRightOut!!.addListener(object : Animator.AnimatorListener {
                override fun onAnimationStart(animator: Animator) {
                }

                override fun onAnimationEnd(animator: Animator) {
                    if (mFlipState == FlipState.FRONT_SIDE) {
                        mCardBackLayout!!.visibility = GONE
                        mCardFrontLayout!!.visibility = VISIBLE

                        if (onFlipListener != null) onFlipListener!!.onViewFlipCompleted(this@EasyFlipView, FlipState.FRONT_SIDE)
                    } else {
                        mCardBackLayout!!.visibility = VISIBLE
                        mCardFrontLayout!!.visibility = GONE

                        if (onFlipListener != null) onFlipListener!!.onViewFlipCompleted(this@EasyFlipView, FlipState.BACK_SIDE)

                        // Auto Flip Back
                        if (autoFlipBack == true) {
                            ThreadUtils.postMainDelayed(Runnable { flipTheView() }, autoFlipBackTime.toLong())
                        }
                    }
                }

                override fun onAnimationCancel(animator: Animator) {
                }

                override fun onAnimationRepeat(animator: Animator) {
                }
            })
            setFlipDuration(flipDuration)
        } else {
            if (!TextUtils.isEmpty(flipTypeFrom) && flipTypeFrom.equals("front", ignoreCase = true)) {
                mSetTopOut = AnimatorInflater.loadAnimator(this.context, animFlipVerticalFrontOutId) as AnimatorSet
                mSetBottomIn =
                    AnimatorInflater.loadAnimator(this.context, animFlipVerticalFrontInId) as AnimatorSet
            } else {
                mSetTopOut = AnimatorInflater.loadAnimator(this.context, animFlipVerticalOutId) as AnimatorSet
                mSetBottomIn =
                    AnimatorInflater.loadAnimator(this.context, animFlipVerticalInId) as AnimatorSet
            }

            if (mSetTopOut == null || mSetBottomIn == null) {
                throw RuntimeException(
                    "No Animations Found! Please set Flip in and Flip out animation Ids."
                )
            }

            mSetTopOut!!.removeAllListeners()
            mSetTopOut!!.addListener(object : Animator.AnimatorListener {
                override fun onAnimationStart(animator: Animator) {
                }

                override fun onAnimationEnd(animator: Animator) {
                    if (mFlipState == FlipState.FRONT_SIDE) {
                        mCardBackLayout!!.visibility = GONE
                        mCardFrontLayout!!.visibility = VISIBLE

                        if (onFlipListener != null) onFlipListener!!.onViewFlipCompleted(this@EasyFlipView, FlipState.FRONT_SIDE)
                    } else {
                        mCardBackLayout!!.visibility = VISIBLE
                        mCardFrontLayout!!.visibility = GONE

                        if (onFlipListener != null) onFlipListener!!.onViewFlipCompleted(this@EasyFlipView, FlipState.BACK_SIDE)

                        // Auto Flip Back
                        if (autoFlipBack == true) {
                            ThreadUtils.postMainDelayed(Runnable { flipTheView() }, autoFlipBackTime.toLong())
                        }
                    }
                }

                override fun onAnimationCancel(animator: Animator) {
                }

                override fun onAnimationRepeat(animator: Animator) {
                }
            })
            setFlipDuration(flipDuration)
        }
    }

    private fun changeCameraDistance() {
        val distance = 8000
        val scale = resources.displayMetrics.density * distance

        if (mCardFrontLayout != null) {
            mCardFrontLayout!!.cameraDistance = scale
        }
        if (mCardBackLayout != null) {
            mCardBackLayout!!.cameraDistance = scale
        }
    }

    /**
     * Play the animation of flipping and flip the view for one side!
     */
    fun flipTheView() {
        if (!flipEnabled || childCount < 2) return

        if (flipOnceEnabled && mFlipState == FlipState.BACK_SIDE) return

        if (flipType.equals("horizontal", ignoreCase = true)) {
            if (mSetRightOut!!.isRunning || mSetLeftIn!!.isRunning) return

            mCardBackLayout!!.visibility = VISIBLE
            mCardFrontLayout!!.visibility = VISIBLE

            if (mFlipState == FlipState.FRONT_SIDE) {
                // From front to back
                mSetRightOut!!.setTarget(mCardFrontLayout)
                mSetLeftIn!!.setTarget(mCardBackLayout)
                mSetRightOut!!.start()
                mSetLeftIn!!.start()
                mIsBackVisible = true
                mFlipState = FlipState.BACK_SIDE
            } else {
                // from back to front
                mSetRightOut!!.setTarget(mCardBackLayout)
                mSetLeftIn!!.setTarget(mCardFrontLayout)
                mSetRightOut!!.start()
                mSetLeftIn!!.start()
                mIsBackVisible = false
                mFlipState = FlipState.FRONT_SIDE
            }
        } else {
            if (mSetTopOut!!.isRunning || mSetBottomIn!!.isRunning) return

            mCardBackLayout!!.visibility = VISIBLE
            mCardFrontLayout!!.visibility = VISIBLE

            if (mFlipState == FlipState.FRONT_SIDE) {
                // From front to back
                mSetTopOut!!.setTarget(mCardFrontLayout)
                mSetBottomIn!!.setTarget(mCardBackLayout)
                mSetTopOut!!.start()
                mSetBottomIn!!.start()
                mIsBackVisible = true
                mFlipState = FlipState.BACK_SIDE
            } else {
                // from back to front
                mSetTopOut!!.setTarget(mCardBackLayout)
                mSetBottomIn!!.setTarget(mCardFrontLayout)
                mSetTopOut!!.start()
                mSetBottomIn!!.start()
                mIsBackVisible = false
                mFlipState = FlipState.FRONT_SIDE
            }
        }
    }

    /**
     * Flip the view for one side with or without animation.
     *
     * @param withAnimation true means flip view with animation otherwise without animation.
     */
    fun flipTheView(withAnimation: Boolean) {
        if (childCount < 2) return

        if (flipType.equals("horizontal", ignoreCase = true)) {
            if (!withAnimation) {
                mSetLeftIn!!.setDuration(0)
                mSetRightOut!!.setDuration(0)
                val oldFlipEnabled = flipEnabled
                flipEnabled = true

                flipTheView()

                mSetLeftIn!!.setDuration(flipDuration.toLong())
                mSetRightOut!!.setDuration(flipDuration.toLong())
                flipEnabled = oldFlipEnabled
            } else {
                flipTheView()
            }
        } else {
            if (!withAnimation) {
                mSetBottomIn!!.setDuration(0)
                mSetTopOut!!.setDuration(0)
                val oldFlipEnabled = flipEnabled
                flipEnabled = true

                flipTheView()

                mSetBottomIn!!.setDuration(flipDuration.toLong())
                mSetTopOut!!.setDuration(flipDuration.toLong())
                flipEnabled = oldFlipEnabled
            } else {
                flipTheView()
            }
        }
    }

    override fun dispatchTouchEvent(ev: MotionEvent?): Boolean {
        try {
            return gestureDetector!!.onTouchEvent(ev!!) || super.dispatchTouchEvent(ev)
        } catch (throwable: Throwable) {
            throw IllegalStateException("Error in dispatchTouchEvent: ", throwable)
        }
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        return if (isEnabled && flipOnTouch) {
            gestureDetector!!.onTouchEvent(event!!)
        } else {
            super.onTouchEvent(event)
        }
    }

    /**
     * Whether view is set to flip on touch or not.
     *
     * @return true or false
     */
    fun isFlipOnTouch(): Boolean {
        return flipOnTouch
    }

    /**
     * Set whether view should be flipped on touch or not!
     *
     * @param flipOnTouch value (true or false)
     */
    fun setFlipOnTouch(flipOnTouch: Boolean) {
        this.flipOnTouch = flipOnTouch
    }

    /**
     * Returns duration of flip in milliseconds!
     *
     * @return duration in milliseconds
     */
    fun getFlipDuration(): Int {
        return flipDuration
    }

    /**
     * Sets the flip duration (in milliseconds)
     *
     * @param flipDuration duration in milliseconds
     */
    fun setFlipDuration(flipDuration: Int) {
        this.flipDuration = flipDuration
        if (flipType.equals("horizontal", ignoreCase = true)) {
            //mSetRightOut.setDuration(flipDuration);
            mSetRightOut!!.childAnimations[0].setDuration(flipDuration.toLong())
            mSetRightOut!!.childAnimations[1].startDelay = (flipDuration / 2).toLong()

            //mSetLeftIn.setDuration(flipDuration);
            mSetLeftIn!!.childAnimations[1].setDuration(flipDuration.toLong())
            mSetLeftIn!!.childAnimations[2].startDelay = (flipDuration / 2).toLong()
        } else {
            mSetTopOut!!.childAnimations[0].setDuration(flipDuration.toLong())
            mSetTopOut!!.childAnimations[1].startDelay = (flipDuration / 2).toLong()

            mSetBottomIn!!.childAnimations[1].setDuration(flipDuration.toLong())
            mSetBottomIn!!.childAnimations[2].startDelay = (flipDuration / 2).toLong()
        }
    }

    /**
     * Returns whether view can be flipped only once!
     *
     * @return true or false
     */
    fun isFlipOnceEnabled(): Boolean {
        return flipOnceEnabled
    }

    /**
     * Enable / Disable flip only once feature.
     *
     * @param flipOnceEnabled true or false
     */
    fun setFlipOnceEnabled(flipOnceEnabled: Boolean) {
        this.flipOnceEnabled = flipOnceEnabled
    }


    /**
     * Returns whether flip is enabled or not!
     *
     * @return true or false
     */
    fun isFlipEnabled(): Boolean {
        return flipEnabled
    }

    /**
     * Enable / Disable flip view.
     *
     * @param flipEnabled true or false
     */
    fun setFlipEnabled(flipEnabled: Boolean) {
        this.flipEnabled = flipEnabled
    }

    /**
     * Returns which flip state is currently on of the flip view.
     *
     * @return current state of flip view
     */
    fun getCurrentFlipState(): FlipState {
        return mFlipState
    }

    /**
     * Returns true if the front side of flip view is visible.
     *
     * @return true if the front side of flip view is visible.
     */
    fun isFrontSide(): Boolean {
        return (mFlipState == FlipState.FRONT_SIDE)
    }

    /**
     * Returns true if the back side of flip view is visible.
     *
     * @return true if the back side of flip view is visible.
     */
    fun isBackSide(): Boolean {
        return (mFlipState == FlipState.BACK_SIDE)
    }

    fun getOnFlipListener(): OnFlipAnimationListener? {
        return onFlipListener
    }

    fun setOnFlipListener(onFlipListener: OnFlipAnimationListener?) {
        this.onFlipListener = onFlipListener
    }

    /*
    public @AnimatorRes int getAnimFlipOutId() {
        return animFlipOutId;
    }

    public void setAnimFlipOutId(@AnimatorRes int animFlipOutId) {
        this.animFlipOutId = animFlipOutId;
        loadAnimations();
    }

    public @AnimatorRes int getAnimFlipInId() {
        return animFlipInId;
    }

    public void setAnimFlipInId(@AnimatorRes int animFlipInId) {
        this.animFlipInId = animFlipInId;
        loadAnimations();
    }
    */
    /**
     * Returns true if the Flip Type of animation is Horizontal?
     */
    fun isHorizontalType(): Boolean {
        return flipType == "horizontal"
    }

    /**
     * Returns true if the Flip Type of animation is Vertical?
     */
    fun isVerticalType(): Boolean {
        return flipType == "vertical"
    }

    /**
     * Sets the Flip Type of animation to Horizontal
     */
    fun setToHorizontalType() {
        flipType = "horizontal"
        loadAnimations()
    }

    /**
     * Sets the Flip Type of animation to Vertical
     */
    fun setToVerticalType() {
        flipType = "vertical"
        loadAnimations()
    }

    /**
     * Sets the flip type from direction to right
     */
    fun setFlipTypeFromRight() {
        flipTypeFrom = if (flipType == "horizontal") "right"
        else "front"
        loadAnimations()
    }

    /**
     * Sets the flip type from direction to left
     */
    fun setFlipTypeFromLeft() {
        flipTypeFrom = if (flipType == "horizontal") "left"
        else "back"
        loadAnimations()
    }

    /**
     * Sets the flip type from direction to front
     */
    fun setFlipTypeFromFront() {
        flipTypeFrom = if (flipType == "vertical") "front"
        else "right"
        loadAnimations()
    }

    /**
     * Sets the flip type from direction to back
     */
    fun setFlipTypeFromBack() {
        flipTypeFrom = if (flipType == "vertical") "back"
        else "left"
        loadAnimations()
    }

    /**
     * Returns the flip type from direction. For horizontal, it will be either right or left and for vertical, it will be front or back.
     */
    fun getFlipTypeFrom(): String? {
        return flipTypeFrom
    }

    /**
     * Returns true if Auto Flip Back is enabled
     */
    fun isAutoFlipBack(): Boolean {
        return autoFlipBack
    }

    /**
     * Set if the card should be flipped back to original front side.
     * @param autoFlipBack true if card should be flipped back to froont side
     */
    fun setAutoFlipBack(autoFlipBack: Boolean) {
        this.autoFlipBack = autoFlipBack
    }

    /**
     * Return the time in milliseconds to auto flip back to original front side.
     * @return
     */
    fun getAutoFlipBackTime(): Int {
        return autoFlipBackTime
    }

    /**
     * Set the time in milliseconds to auto flip back the view to the original front side
     * @param autoFlipBackTime The time in milliseconds
     */
    fun setAutoFlipBackTime(autoFlipBackTime: Int) {
        this.autoFlipBackTime = autoFlipBackTime
    }

    /**
     * The Flip Animation Listener for animations and flipping complete listeners
     */
    interface OnFlipAnimationListener {
        /**
         * Called when flip animation is completed.
         *
         * @param newCurrentSide After animation, the new side of the view. Either can be
         * FlipState.FRONT_SIDE or FlipState.BACK_SIDE
         */
        fun onViewFlipCompleted(easyFlipView: EasyFlipView?, newCurrentSide: FlipState?)
    }

    private inner class SwipeDetector : SimpleOnGestureListener() {
        override fun onFling(e1: MotionEvent?, e2: MotionEvent, velocityX: Float, velocityY: Float): Boolean {
            return false
        }

        override fun onSingleTapConfirmed(e: MotionEvent): Boolean {
            if (isEnabled && flipOnTouch) {
                flipTheView()
            }
            return super.onSingleTapConfirmed(e)
        }

        override fun onDown(e: MotionEvent): Boolean {
            if (isEnabled && flipOnTouch) {
                return true
            }
            return super.onDown(e)
        }
    }

    companion object {
        val TAG: String = EasyFlipView::class.java.simpleName

        const val DEFAULT_FLIP_DURATION: Int = 400
        const val DEFAULT_AUTO_FLIP_BACK_TIME: Int = 1000
    }
}
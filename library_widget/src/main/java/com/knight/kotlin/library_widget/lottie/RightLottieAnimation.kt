package com.knight.kotlin.library_widget.lottie

import android.animation.Animator
import android.animation.ObjectAnimator
import android.animation.PropertyValuesHolder
import android.animation.TimeInterpolator
import android.os.Build
import android.view.View
import android.view.ViewTreeObserver.OnGlobalLayoutListener
import com.chad.library.adapter.base.BaseQuickAdapter

/**
 * Author:Knight
 * Time:2022/4/26 10:13
 * Description:RightLottieAnimation
 */
class RightLottieAnimation constructor(builder:GuillotineBuilder) {
    private val ROTATION = "rotation"
    private val GUILLOTINE_CLOSED_ANGLE = -90f
    private val GUILLOTINE_OPENED_ANGLE = 0f
    private val DEFAULT_DURATION :Long = 625
    private val ACTION_BAR_ROTATION_ANGLE = 3f

    private val mGuillotineView: View = builder.guillotineView
    private val mDuration: Long = if (builder.duration > 0) builder.duration else DEFAULT_DURATION
    private val mOpeningAnimation: ObjectAnimator? =buildOpeningAnimation()
    private val mClosingAnimation: ObjectAnimator? = buildClosingAnimation()
    private val mListener: RightLottieListener = builder.rightLottieListener
    private val mIsRightToLeftLayout = builder.isRightToLeftLayout
    private val mInterpolator: TimeInterpolator = builder.interpolator ?: RightLottieInterpolator()
    private val mActionBarView: View = builder.actionBarView
    private val mDelay: Long = builder.startDelay

    private var isOpening = false
    private var isClosing = false

    private val mBaseQuickAdapter: BaseQuickAdapter<*, *> = builder.mBaseQuickAdapter

    init {
        setUpOpeningView(builder.openingView)
        setUpClosingView(builder.closingView)
        if (builder.isClosedOnStart) {
            mGuillotineView.rotation = GUILLOTINE_CLOSED_ANGLE
            mGuillotineView.visibility = View.INVISIBLE
        }
    }


    private fun buildOpeningAnimation(): ObjectAnimator {
        val p: PropertyValuesHolder = PropertyValuesHolder.ofFloat(
            ROTATION,
            GUILLOTINE_CLOSED_ANGLE,
            GUILLOTINE_OPENED_ANGLE
        )
        val objectAnimator = ObjectAnimator.ofPropertyValuesHolder(mGuillotineView, p)
        objectAnimator.duration = (mDuration * RightLottieInterpolator.ROTATION_TIME).toLong()
        objectAnimator.addListener(object : Animator.AnimatorListener {
            override fun onAnimationStart(animation: Animator) {
                mGuillotineView.visibility = View.VISIBLE
                isOpening = true
            }

            override fun onAnimationEnd(animation: Animator) {
                isOpening = false
                if (mListener != null) {
                    mListener.onRightLottieOpened()
                }
            }

            override fun onAnimationCancel(animation: Animator) {}
            override fun onAnimationRepeat(animation: Animator) {}
        })
        return objectAnimator
    }

    private fun buildClosingAnimation(): ObjectAnimator? {
        val p: PropertyValuesHolder = PropertyValuesHolder.ofFloat(
            ROTATION,
            GUILLOTINE_OPENED_ANGLE,
            GUILLOTINE_CLOSED_ANGLE
        )
        val objectAnimator = ObjectAnimator.ofPropertyValuesHolder(mGuillotineView, p)
        objectAnimator.duration = (mDuration * RightLottieInterpolator.ROTATION_TIME).toLong()
        objectAnimator.addListener(object : Animator.AnimatorListener {
            override fun onAnimationStart(animation: Animator) {
                mBaseQuickAdapter!!.data.clear()
                mBaseQuickAdapter.notifyDataSetChanged()
                isClosing = true
                mGuillotineView.visibility = View.VISIBLE
            }

            override fun onAnimationEnd(animation: Animator) {
                isClosing = false
                mGuillotineView.visibility = View.GONE
                //    startActionBarAnimation();
                if (mListener != null) {
                    mListener.onRightLottieClosed()
                }
            }

            override fun onAnimationCancel(animation: Animator) {}
            override fun onAnimationRepeat(animation: Animator) {}
        })
        return objectAnimator
    }


    private fun setUpOpeningView(openingView: View) {
        if (mActionBarView != null) {
            mActionBarView.viewTreeObserver.addOnGlobalLayoutListener(object :
                OnGlobalLayoutListener {
                override fun onGlobalLayout() {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                        mActionBarView.viewTreeObserver.removeOnGlobalLayoutListener(this)
                    } else {
                        mActionBarView.viewTreeObserver.removeGlobalOnLayoutListener(this)
                    }
                    mActionBarView.pivotX = calculatePivotX(openingView)
                    mActionBarView.pivotY = calculatePivotY(openingView)
                }
            })
        }
        openingView.setOnClickListener { open() }
    }

    private fun setUpClosingView(closingView: View) {
        mGuillotineView.viewTreeObserver.addOnGlobalLayoutListener(object : OnGlobalLayoutListener {
            override fun onGlobalLayout() {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                    mGuillotineView.viewTreeObserver.removeOnGlobalLayoutListener(this)
                } else {
                    mGuillotineView.viewTreeObserver.removeGlobalOnLayoutListener(this)
                }
                mGuillotineView.pivotX = calculatePivotX(closingView)
                mGuillotineView.pivotY = calculatePivotY(closingView)
            }
        })
        closingView.setOnClickListener { close() }
    }

    fun open() {
        if (!isOpening) {
            mOpeningAnimation?.start()
        }
    }

    fun close() {
        if (!isClosing) {
            // mBaseQuickAdapter.getData().clear();
            // mBaseQuickAdapter.notifyDataSetChanged();
            mClosingAnimation?.start()
        }
    }


    private fun calculatePivotY(burger: View): Float {
        return (burger.top + burger.height / 2).toFloat()
    }

    private fun calculatePivotX(burger: View): Float {
        return (burger.left + burger.width / 2).toFloat()
    }
    class GuillotineBuilder(
        val guillotineView: View,
        val closingView: View,
        val openingView: View,
        val mBaseQuickAdapter: BaseQuickAdapter<*, *>
    ) {
        lateinit var actionBarView: View
        lateinit var rightLottieListener: RightLottieListener
        var duration: Long = 0
        var startDelay: Long = 0
        var isRightToLeftLayout = false
        var interpolator: TimeInterpolator? = null
        public var isClosedOnStart = false
        fun setActionBarViewForAnimation(view: View): GuillotineBuilder {
            actionBarView = view
            return this
        }

        fun setGuillotineListener(rightLottieListener: RightLottieListener): GuillotineBuilder {
            this.rightLottieListener = rightLottieListener
            return this
        }

        fun setDuration(duration: Long): GuillotineBuilder {
            this.duration = duration
            return this
        }

        fun setStartDelay(startDelay: Long): GuillotineBuilder {
            this.startDelay = startDelay
            return this
        }

        fun setRightToLeftLayout(isRightToLeftLayout: Boolean): GuillotineBuilder {
            this.isRightToLeftLayout = isRightToLeftLayout
            return this
        }

        fun setInterpolator(interpolator: TimeInterpolator?): GuillotineBuilder {
            this.interpolator = interpolator
            return this
        }

        fun setClosedOnStart(isClosedOnStart: Boolean): GuillotineBuilder {
            this.isClosedOnStart = isClosedOnStart
            return this
        }

        fun build(): RightLottieAnimation {
            return RightLottieAnimation(this)
        }
    }
}
package com.knight.kotlin.library_widget.skeleton

import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.ColorRes
import androidx.annotation.IntRange
import androidx.annotation.LayoutRes
import androidx.core.content.ContextCompat
import com.knight.kotlin.library_widget.R
import io.supercharge.shimmerlayout.ShimmerLayout

/**
 * Author:Knight
 * Time:2022/3/31 16:12
 * Description:ViewSkeletonScreen
 */
class ViewSkeletonScreen constructor(builder:Builder) : SkeletonScreen {

    private val TAG = ViewSkeletonScreen::class.java.name
    private var mViewReplacer: ViewReplacer? = null
    private var mActualView: View? = null
    private var mSkeletonResID = 0
    private var mShimmerColor = 0
    private var mShimmer = false
    private var mShimmerDuration = 0
    private var mShimmerAngle = 0
    private val HANDLER = Handler(Looper.getMainLooper())
    init {
        mActualView = builder.mView
        mSkeletonResID = builder.mSkeletonLayoutResID
        mShimmer = builder.mShimmer
        mShimmerDuration = builder.mShimmerDuration
        mShimmerAngle = builder.mShimmerAngle
        mShimmerColor = builder.mShimmerColor
        mViewReplacer = ViewReplacer(builder.mView)
    }


    private fun generateShimmerContainerLayout(parentView: ViewGroup): ShimmerLayout {
        val shimmerLayout = LayoutInflater.from(mActualView!!.context)
            .inflate(R.layout.layout_shimmer, parentView, false) as ShimmerLayout
        shimmerLayout.setShimmerColor(mShimmerColor)
        shimmerLayout.setShimmerAngle(mShimmerAngle)
        shimmerLayout.setShimmerAnimationDuration(mShimmerDuration)
        val innerView =
            LayoutInflater.from(mActualView!!.context).inflate(mSkeletonResID, shimmerLayout, false)
        val lp = innerView.layoutParams
        if (lp != null) {
            shimmerLayout.layoutParams = lp
        }
        shimmerLayout.addView(innerView)
        shimmerLayout.addOnAttachStateChangeListener(object : View.OnAttachStateChangeListener {
            override fun onViewAttachedToWindow(v: View) {
                shimmerLayout.startShimmerAnimation()
            }

            override fun onViewDetachedFromWindow(v: View) {
                shimmerLayout.stopShimmerAnimation()
            }
        })
        shimmerLayout.startShimmerAnimation()
        return shimmerLayout
    }

    private fun generateSkeletonLoadingView(): View? {
        val viewParent = mActualView?.parent
        if (viewParent == null) {
            Log.e(TAG, "the source view have not attach to any view")
            return null
        }
        val parentView = viewParent as ViewGroup
        return if (mShimmer) {
            generateShimmerContainerLayout(parentView)
        } else LayoutInflater.from(mActualView?.context).inflate(mSkeletonResID, parentView, false)
    }



    override fun show() {
        val skeletonLoadingView = generateSkeletonLoadingView()
        if (skeletonLoadingView != null) {
            mViewReplacer?.replace(skeletonLoadingView)
        }
    }

    override fun hide() {
        if (mViewReplacer?.getTargetView() is ShimmerLayout) {
            (mViewReplacer?.getTargetView() as ShimmerLayout?)!!.stopShimmerAnimation()
        }
        mViewReplacer?.restore()
    }

    override fun delayHide(delayMillis: Int) {
        HANDLER.postDelayed(Runnable {
            if (mViewReplacer?.getTargetView() is ShimmerLayout) {
                (mViewReplacer?.getTargetView() as ShimmerLayout?)?.stopShimmerAnimation()
            }
            mViewReplacer?.restore()
        }, delayMillis.toLong())
    }


    class Builder(internal val mView: View) {
        internal var mSkeletonLayoutResID = 0
        internal var mShimmer = true
        internal var mShimmerColor: Int
        internal var mShimmerDuration = 1000
        internal var mShimmerAngle = 20

        /**
         * @param skeletonLayoutResID the loading skeleton layoutResID
         */
        fun load(@LayoutRes skeletonLayoutResID: Int): Builder {
            mSkeletonLayoutResID = skeletonLayoutResID
            return this
        }

        /**
         * @param shimmerColor the shimmer color
         */
        fun color(@ColorRes shimmerColor: Int): Builder {
            mShimmerColor = ContextCompat.getColor(mView.context, shimmerColor)
            return this
        }

        /**
         * @param shimmer whether show shimmer animation
         */
        fun shimmer(shimmer: Boolean): Builder {
            mShimmer = shimmer
            return this
        }

        /**
         * the duration of the animation , the time it will take for the highlight to move from one end of the layout
         * to the other.
         *
         * @param shimmerDuration Duration of the shimmer animation, in milliseconds
         */
        fun duration(shimmerDuration: Int): Builder {
            mShimmerDuration = shimmerDuration
            return this
        }

        /**
         * @param shimmerAngle the angle of the shimmer effect in clockwise direction in degrees.
         */
        fun angle(@IntRange(from = 0, to = 30) shimmerAngle: Int): Builder {
            mShimmerAngle = shimmerAngle
            return this
        }

        fun show(): ViewSkeletonScreen {
            val skeletonScreen = ViewSkeletonScreen(this)
            skeletonScreen.show()
            return skeletonScreen
        }

        init {
            mShimmerColor = ContextCompat.getColor(mView.context, io.supercharge.shimmerlayout.R.color.shimmer_color)
        }
    }
}
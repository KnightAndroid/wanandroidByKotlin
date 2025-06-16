package com.knight.kotlin.library_widget.skeleton

import android.os.Handler
import android.os.Looper
import androidx.annotation.ColorRes
import androidx.annotation.IntRange
import androidx.annotation.LayoutRes
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.knight.kotlin.library_widget.R

/**
 * Author:Knight
 * Time:2022/3/31 16:23
 * Description:RecyclerViewSkeletonScreen
 */
class RecyclerViewSkeletonScreen constructor(builder : Builder) : SkeletonScreen{

    private var mRecyclerView: RecyclerView? = null
    private var mActualAdapter: RecyclerView.Adapter<*>? = null
    private var mSkeletonAdapter: SkeletonAdapter? = null
    private var mRecyclerViewFrozen = false
    private val HANDLER = Handler(Looper.getMainLooper())

    init {
        mRecyclerView = builder.mRecyclerView
        mActualAdapter = builder.mActualAdapter
        mSkeletonAdapter = SkeletonAdapter()
        mSkeletonAdapter?.let {
            it.setItemCount(builder.mItemCount)
            it.setLayoutReference(builder.mItemResID)
            it.shimmer(builder.mShimmer)
            it.setShimmerColor(builder.mShimmerColor)
            it.setShimmerAngle(builder.mShimmerAngle)
            it.setShimmerDuration(builder.mShimmerDuration)
        }

        mRecyclerViewFrozen = builder.mFrozen
    }

    override fun show() {
        mRecyclerView?.adapter = mSkeletonAdapter
        if (mRecyclerView?.isComputingLayout != true && mRecyclerViewFrozen) {
            mRecyclerView?.isLayoutFrozen = true
        }
    }

    override fun hide() {
        mRecyclerView?.adapter = mActualAdapter
    }

    override fun delayHide(delayMillis: Int) {
        HANDLER.postDelayed(Runnable {
            mRecyclerView?.adapter = mActualAdapter
        }, delayMillis.toLong())
    }

    class Builder(internal val mRecyclerView: RecyclerView) {
        internal var mActualAdapter: RecyclerView.Adapter<*>? = null
        internal var mShimmer = true
        internal var mItemCount = 10
        internal var mItemResID: Int = R.layout.layout_default_item_skeleton
        internal var mShimmerColor: Int
        internal var mShimmerDuration = 1000
        internal var mShimmerAngle = 20
        internal var mFrozen = true

        /**
         * @param adapter the target recyclerView actual adapter
         */
        fun adapter(adapter: RecyclerView.Adapter<*>?): Builder {
            mActualAdapter = adapter
            return this
        }

        /**
         * @param itemCount the child item count in recyclerView
         */
        fun count(itemCount: Int): Builder {
            mItemCount = itemCount
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
         * @param shimmerColor the shimmer color
         */
        fun color(@ColorRes shimmerColor: Int): Builder {
            mShimmerColor = ContextCompat.getColor(mRecyclerView.context, shimmerColor)
            return this
        }

        /**
         * @param shimmerAngle the angle of the shimmer effect in clockwise direction in degrees.
         */
        fun angle(@IntRange(from = 0, to = 30) shimmerAngle: Int): Builder {
            mShimmerAngle = shimmerAngle
            return this
        }

        /**
         * @param skeletonLayoutResID the loading skeleton layoutResID
         */
        fun load(@LayoutRes skeletonLayoutResID: Int): Builder {
            mItemResID = skeletonLayoutResID
            return this
        }

        /**
         * @param frozen whether frozen recyclerView during skeleton showing
         * @return
         */
        fun frozen(frozen: Boolean): Builder {
            mFrozen = frozen
            return this
        }

        fun show(): RecyclerViewSkeletonScreen {
            val recyclerViewSkeleton = RecyclerViewSkeletonScreen(this)
            recyclerViewSkeleton.show()
            return recyclerViewSkeleton
        }

        init {
            mShimmerColor = ContextCompat.getColor(mRecyclerView.context, io.supercharge.shimmerlayout.R.color.shimmer_color)
        }
    }
}
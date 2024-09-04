package com.knight.kotlin.module_home.utils

import android.animation.Animator
import android.animation.ObjectAnimator
import android.animation.ValueAnimator
import android.view.View
import android.widget.ImageView
import com.knight.kotlin.module_home.adapter.BaiduHotSearchAdapter

/**
 * Author:Knight
 * Time:2022/3/9 17:13
 * Description:HomeAnimUtils
 */
object HomeAnimUtils {
    fun setArrowAnimate(
        mTopArticleAdapter: BaiduHotSearchAdapter,
        view: ImageView?,
        isShowOnlythree: Boolean
    ) {
        val animator: ObjectAnimator
        animator = if (isShowOnlythree) {
            ObjectAnimator.ofFloat(view, "rotation", 180f, 0f)
        } else {
            ObjectAnimator.ofFloat(view, "rotation", 0f, 180f)
        }
        animator.addListener(object : Animator.AnimatorListener {
            override fun onAnimationStart(animation: Animator) {}
            override fun onAnimationEnd(animation: Animator) {
                //结束之后
                //mTopArticleAdapter.setShowOnlyThree(isShowOnlythree)
            }

            override fun onAnimationCancel(animation: Animator) {}
            override fun onAnimationRepeat(animation: Animator) {}
        })
        animator.duration = 250
        animator.start()
    }


    fun height(view: View, from: Float, to: Float, duration: Int, animatorListener: Animator.AnimatorListener?): ValueAnimator {
        val animator = ValueAnimator.ofFloat(from, to)
        animator.duration = duration.toLong()
        if (animatorListener != null) {
            animator.addListener(animatorListener)
        }
        animator.addUpdateListener { animation ->
            if (view.layoutParams != null) {
                val lp = view.layoutParams
                val aFloat = animation.animatedValue as Float
                lp.height = aFloat.toInt()
                view.layoutParams = lp
            }
        }
        animator.start()
        return animator
    }




}
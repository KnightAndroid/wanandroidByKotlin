package com.knight.kotlin.module_home.utils

import android.animation.Animator
import android.animation.ObjectAnimator
import android.widget.ImageView
import com.knight.kotlin.module_home.adapter.TopArticleAdapter

/**
 * Author:Knight
 * Time:2022/3/9 17:13
 * Description:HomeAnimUtils
 */
object HomeAnimUtils {
    fun setArrowAnimate(
        mTopArticleAdapter: TopArticleAdapter,
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
                mTopArticleAdapter.setShowOnlyThree(isShowOnlythree)
            }

            override fun onAnimationCancel(animation: Animator) {}
            override fun onAnimationRepeat(animation: Animator) {}
        })
        animator.duration = 500
        animator.start()
    }
}
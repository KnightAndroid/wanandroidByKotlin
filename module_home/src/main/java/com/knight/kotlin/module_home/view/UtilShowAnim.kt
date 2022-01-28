package com.knight.kotlin.module_home.view

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.ObjectAnimator
import android.view.View
import android.view.animation.OvershootInterpolator
import androidx.viewpager2.widget.ViewPager2

/**
 * Author:Knight
 * Time:2022/1/28 15:26
 * Description:UtilShowAnim
 */
class UtilShowAnim constructor(viewPager2: ViewPager2) {
    /**
     * 动画类型 =》逐一
     */
    val ANIM_TYPE_INPROPERORDER = 1

    /**
     * 动画类型 =》展开
     */
    val ANIM_TYPE_UNFOLD = 2

    /**
     * 动画类型 =》旋转
     */
    val ANIM_TYPE_ROTATION = 3

    /**
     * ViewPager对象
     */
    private val mViewPager: ViewPager2 = viewPager2


    /**
     * 卡片动画
     *
     * @param animType 动画类型
     * 【ANIM_TYPE_INPROPERORDER：逐一】
     * 【ANIM_TYPE_UNFOLD：展开】
     * 【ANIM_TYPE_ROTATION：旋转】
     */
    fun cardAnim(animType: Int) {
        if (mViewPager == null) {
            return
        }
        if (animType == 0) {
            return
        }
        mViewPager.alpha = 0f
        // 等待View加载完成，否则动画可能会受影响
        mViewPager.post(Runnable {
            val count = mViewPager.childCount
            if (count == 0) {
                mViewPager.alpha = 1f
                return@Runnable
            }
            when (animType) {
                ANIM_TYPE_INPROPERORDER ->      // 动画效果 =》逐一
                    cardAnimInproperorder()
                ANIM_TYPE_UNFOLD ->             // 动画效果 =》展开
                    cardAnimUnfold()
                ANIM_TYPE_ROTATION ->           // 动画效果 =》旋转
                    cardAnimRotation()
            }
            mViewPager.alpha = 1f
        })
    }


    /**
     * 动画效果 =》逐一
     */
    private fun cardAnimInproperorder() {
        // 可见的卡片数量
        val offscreen = mViewPager.offscreenPageLimit
        // 卡片总数量
        val count = mViewPager.adapter?.itemCount ?: 0
        for (i in 0..offscreen) {

            // 越界判断
            if (i >= count) {
                return
            }

            // 获取卡片
            val card = mViewPager.getChildAt(i) ?: return
            card.alpha = 0f

            // 卡片透明度渐变
            val animAlpha = ObjectAnimator.ofFloat(
                card,
                "alpha", 0f, 1f
            )
            animAlpha.duration = 200
            animAlpha.startDelay = (i * 150).toLong()
            animAlpha.start()

            // 卡片Y轴位移动画
            val animTY = ObjectAnimator.ofFloat(
                card,
                "translationY",
                card.translationY + 350,
                card.translationY
            )
            animTY.duration = 450
            animTY.startDelay = (i * 150).toLong()
            animTY.interpolator = OvershootInterpolator(2f)
            animTY.start()
        }
    }

    /**
     * 动画效果 =》展开
     */
    private fun cardAnimUnfold() {
        // 可见的卡片数量
        val offscreen = mViewPager.offscreenPageLimit
        // 卡片总数量
        val count = mViewPager.adapter?.itemCount ?: 0

        // 获取第一张卡片
        val cardOne = mViewPager.getChildAt(0)

        // 卡片透明度渐变
        val animAlpha = ObjectAnimator.ofFloat(
            cardOne,
            "alpha", 0f, 1f
        )
        animAlpha.duration = 200
        animAlpha.start()

        // 卡片Y轴位移动画
        val animTY = ObjectAnimator.ofFloat(
            cardOne,
            "translationY",
            cardOne.translationY + 350,
            cardOne.translationY
        )
        animTY.duration = 450
        animTY.interpolator = OvershootInterpolator(2f)
        animTY.start()
        if (count > 1) {
            for (i in 1..offscreen) {
                if (i >= count) {
                    return
                }

                // 获取卡片
                val card = mViewPager.getChildAt(i) ?: return

                // 隐藏卡片，动画开始时再设为可见
                card.visibility = View.INVISIBLE

                // 卡片X轴位移动画
                val animTX = ObjectAnimator.ofFloat(
                    card,
                    "translationX",
                    card.translationX - card.width * 0.075f,
                    card.translationX + card.width * 0.035f,
                    card.translationX
                )
                animTX.duration = 350
                animTX.startDelay = (180 + i * 100).toLong()
                animTX.addListener(object : AnimatorListenerAdapter() {
                    override fun onAnimationStart(animation: Animator) {
                        super.onAnimationStart(animation)

                        // 卡片动画开始时，设置卡片可见
                        card.visibility = View.VISIBLE
                    }
                })
                animTX.start()
            }
        }
    }

    /**
     * 动画效果 =》旋转
     */
    private fun cardAnimRotation() {
        // 可见的卡片数量
        val offscreen = mViewPager.offscreenPageLimit
        // 卡片总数量
        val count = mViewPager.adapter?.itemCount ?:0
        for (i in 0..offscreen) {
            if (i >= count) {
                return
            }

            // 获取卡片
            val card = mViewPager.getChildAt(i) ?: return
            card.alpha = 0f

            // 卡片透明度渐变
            val animAlpha = ObjectAnimator.ofFloat(
                card,
                "alpha", 0f, 1f
            )
            animAlpha.duration = 200
            animAlpha.startDelay = (i * 150).toLong()
            animAlpha.start()

            // 卡片旋转动画
            val animTY = ObjectAnimator.ofFloat(
                card,
                "rotation",
                card.rotation - 30,
                card.rotation
            )
            animTY.duration = 450
            animTY.startDelay = (i * 150).toLong()
            animTY.interpolator = OvershootInterpolator(2f)
            animTY.start()
        }
    }

}
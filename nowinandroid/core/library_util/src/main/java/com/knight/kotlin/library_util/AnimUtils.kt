package com.knight.kotlin.library_util

import android.view.animation.AlphaAnimation
import android.view.animation.Animation
import android.view.animation.DecelerateInterpolator
import android.view.animation.RotateAnimation
import android.view.animation.ScaleAnimation
import android.view.animation.TranslateAnimation

/**
 * Author:Knight
 * Time:2024/3/29 16:38
 * Description:AnimUtils
 */
object AnimUtils {


    /**
     * 以中心缩放动画
     *
     * @param from
     * @param to
     */
    fun scaleAnim(time: Long, from: Float, to: Float, offsetTime: Long): ScaleAnimation {
        val scaleAnimation = ScaleAnimation(from, to, from, to,
            Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f)
        scaleAnimation.startOffset = offsetTime
        scaleAnimation.interpolator = DecelerateInterpolator()
        scaleAnimation.duration = time
        return scaleAnimation
    }

    /**
     * 旋转动画
     *
     * @param time
     */
    fun rotateAnim(time: Long, fromDegrees: Int, toDegrees: Float): RotateAnimation {
        val rotateAnimation = RotateAnimation(fromDegrees.toFloat(), toDegrees,
            Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f)
        rotateAnimation.duration = time
        return rotateAnimation
    }

    /**
     * 移动动画
     *
     * @param fromX
     * @param toX
     * @param fromY
     * @param toY
     */
    fun translationAnim(time: Long, fromX: Float, toX: Float, fromY: Float, toY: Float, offsetTime: Long): TranslateAnimation {
        val anim = TranslateAnimation(fromX, toX, fromY, toY)
        anim.duration = time
        anim.interpolator = DecelerateInterpolator()
        anim.startOffset = offsetTime
        return anim
    }

    /**
     * 透明度动画
     *
     * @param fromAlpha
     * @param toAlpha
     * @param duration
     */
    fun alphaAnim(fromAlpha: Float, toAlpha: Float, duration: Long, offsetTime: Long): AlphaAnimation {
        val anim = AlphaAnimation(fromAlpha, toAlpha)
        anim.duration = duration
        anim.startOffset = offsetTime
        return anim
    }
}
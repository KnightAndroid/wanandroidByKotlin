package com.knight.kotlin.library_widget.lottie

import android.animation.TimeInterpolator

/**
 * Author:Knight
 * Time:2022/4/26 10:09
 * Description:RightLottieInterpolator
 */
class RightLottieInterpolator : TimeInterpolator {

    companion object {
        const val ROTATION_TIME = 0.46667f
        const val FIRST_BOUNCE_TIME = 0.26666f
        const val SECOND_BOUNCE_TIME = 0.26667f

    }


    override fun getInterpolation(input: Float): Float {
        return if (input < ROTATION_TIME) {
            rotation(input)
        } else if (input < ROTATION_TIME + FIRST_BOUNCE_TIME) {
            firstBounce(input)
        } else {
            secondBounce(input)
        }
    }

    private fun rotation(t: Float): Float {
        return 4.592f * t * t
    }

    private fun firstBounce(t: Float): Float {
        return 2.5f * t * t - 3f * t + 1.85556f
    }

    private fun secondBounce(t: Float): Float {
        return 0.625f * t * t - 1.083f * t + 1.458f
    }
}
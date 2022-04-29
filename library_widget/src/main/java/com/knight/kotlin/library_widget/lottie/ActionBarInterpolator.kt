package com.knight.kotlin.library_widget.lottie

import android.animation.TimeInterpolator

/**
 * Author:Knight
 * Time:2022/4/26 10:03
 * Description:ActionBarInterpolator
 */
class ActionBarInterpolator : TimeInterpolator {

    private val FIRST_BOUNCE_PART = 0.375f
    private val SECOND_BOUNCE_PART = 0.625f

    override fun getInterpolation(input: Float): Float {
        return if (input < FIRST_BOUNCE_PART) {
            -28.4444f * input * input + 10.66667f * input
        } else if (input < SECOND_BOUNCE_PART) {
            21.33312f * input * input - 21.33312f * input + 4.999950f
        } else {
            -9.481481f * input * input + 15.40741f * input - 5.925926f
        }
    }
}
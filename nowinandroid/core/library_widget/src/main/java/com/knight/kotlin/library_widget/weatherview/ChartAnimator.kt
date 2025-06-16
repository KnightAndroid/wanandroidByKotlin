package com.knight.kotlin.library_widget.weatherview

import android.animation.ObjectAnimator
import android.animation.ValueAnimator.AnimatorUpdateListener


/**
 * @Description
 * @Author knight
 * @Time 2025/3/5 22:25
 *
 */

class ChartAnimator(listener: AnimatorUpdateListener?) {

    protected var mPhaseY: Float = 1f //0f-1f
    protected var mPhaseX: Float = 1f //0f-1f

    private var mListener: AnimatorUpdateListener? = null

    init {
        mListener = listener
    }



    /**
     * Y轴动画
     *
     * @param durationMillis
     */
    fun animateY(durationMillis: Int) {
        val animatorY = ObjectAnimator.ofFloat(this, "phaseY", 0f, 1f)
        animatorY.setDuration(durationMillis.toLong())
        animatorY.addUpdateListener(mListener)
        animatorY.start()
    }


    /**
     * X轴动画
     *
     * @param durationMillis
     */
    fun animateX(durationMillis: Int) {
        val animatorY = ObjectAnimator.ofFloat(this, "phaseX", 0f, 1f)
        animatorY.setDuration(durationMillis.toLong())
        animatorY.addUpdateListener(mListener)
        animatorY.start()
    }


    /**
     * This gets the y-phase that is used to animate the values.
     *
     * @return
     */
    fun getPhaseY(): Float {
        return mPhaseY
    }

    /**
     * This modifys the y-phase that is used to animate the values.
     *
     * @param phase
     */
    fun setPhaseY(phase: Float) {
        mPhaseY = phase
    }


    /**
     * This modifys the X-phase that is used to animate the values.
     *
     * @param phase
     */
    fun setPhaseX(phase: Float) {
        mPhaseX = phase
    }


    /**
     * This gets the x-phase that is used to animate the values.
     *
     * @return
     */
    fun getPhaseX(): Float {
        return mPhaseX
    }
}
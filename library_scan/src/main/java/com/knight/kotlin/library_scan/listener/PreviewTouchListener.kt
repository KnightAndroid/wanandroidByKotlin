package com.knight.kotlin.library_scan.listener

import android.content.Context
import android.view.MotionEvent
import android.view.ScaleGestureDetector
import android.view.ScaleGestureDetector.OnScaleGestureListener
import android.view.ScaleGestureDetector.SimpleOnScaleGestureListener
import android.view.View

/**
 * Author:Knight
 * Time:2022/2/14 13:51
 * Description:PreviewTouchListener
 */
class PreviewTouchListener constructor(context: Context) : View.OnTouchListener{

    private var mScaleGestureDetector: ScaleGestureDetector? = null

    init {
        /**
         * 缩放监听
         */
        var onScaleGestureListener: OnScaleGestureListener = object : SimpleOnScaleGestureListener() {
            override fun onScale(detector: ScaleGestureDetector): Boolean {
                val delta = detector.scaleFactor
                if (mCustomTouchListener != null) {
                    mCustomTouchListener!!.zoom(delta)
                }
                return true
            }
        }
        mScaleGestureDetector = ScaleGestureDetector(context, onScaleGestureListener)
    }


    override fun onTouch(v: View?, event: MotionEvent): Boolean {
        mScaleGestureDetector?.onTouchEvent(event)
        return true
    }

    interface CustomTouchListener {
        /**
         * 放大
         */
        fun zoom(delta: Float)
    }

    private var mCustomTouchListener: CustomTouchListener? = null

    fun setCustomTouchListener(customTouchListener: CustomTouchListener?) {
        mCustomTouchListener = customTouchListener
    }


}
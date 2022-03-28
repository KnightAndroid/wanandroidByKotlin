package com.knight.kotlin.library_widget.loadcircleview

import android.content.Context
import android.graphics.Canvas
import android.util.AttributeSet
import com.knight.kotlin.library_widget.R

/**
 * Author:Knight
 * Time:2022/3/28 16:02
 * Description:CirView
 */
class CirView : androidx.appcompat.widget.AppCompatImageView {


    private var mRotateDegrees = 0f
    private var mFrameTime = 0
    private var mNeedToUpdateView = false
    private var mUpdateViewRunnable: Runnable? = null
    @JvmOverloads
    constructor(context: Context, attributeSet: AttributeSet? = null)
            : super(context, attributeSet) {
        init()
    }

    private fun init() {
        //        设置图片
        setImageResource(R.drawable.widget_icon_loading)
        mFrameTime = 1000 / 12
        //        转圈速度
        mUpdateViewRunnable = object : Runnable {
            override fun run() {
                mRotateDegrees += 30f
                mRotateDegrees = if (mRotateDegrees < 360) mRotateDegrees else mRotateDegrees - 360
                invalidate()
                if (mNeedToUpdateView) {
                    postDelayed(this, mFrameTime.toLong())
                }
            }
        }
    }

    override fun onDraw(canvas: Canvas) {
        canvas.rotate(mRotateDegrees, (width / 2).toFloat(), (height / 2).toFloat())
        super.onDraw(canvas)
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        mNeedToUpdateView = true
        post(mUpdateViewRunnable)
    }

    override fun onDetachedFromWindow() {
        mNeedToUpdateView = false
        super.onDetachedFromWindow()
    }
}
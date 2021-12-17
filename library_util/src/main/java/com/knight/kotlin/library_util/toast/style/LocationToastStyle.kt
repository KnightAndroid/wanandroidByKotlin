package com.knight.kotlin.library_util.toast.style

import android.content.Context
import android.view.View
import com.knight.kotlin.library_util.toast.callback.ToastStyleInterface


/**
 * Author:Knight
 * Time:2021/12/17 13:52
 * Description:LocationToastStyle
 */
class LocationToastStyle : ToastStyleInterface<View?> {
    private var mStyle: ToastStyleInterface<*>? = null

    private var mGravity = 0
    private var mXOffset = 0
    private var mYOffset = 0
    private var mHorizontalMargin = 0f
    private var mVerticalMargin = 0f
    fun LocationToastStyle(
        style: ToastStyleInterface<*>?,
        gravity: Int,
        xOffset: Int = 0,
        yOffset: Int = 0,
        horizontalMargin: Float = 0f,
        verticalMargin: Float = 0f
    ) {
        mStyle = style
        mGravity = gravity
        mXOffset = xOffset
        mYOffset = yOffset
        mHorizontalMargin = horizontalMargin
        mVerticalMargin = verticalMargin
    }
    override fun createView(context: Context): View? {
        return mStyle?.createView(context)
    }

    override fun getGravity(): Int {
        return mGravity
    }

    override fun getXOffset(): Int {
        return mXOffset
    }

    override fun getYOffset(): Int {
        return mYOffset
    }

    override fun getHorizontalMargin(): Float {
        return mHorizontalMargin
    }

    override fun getVerticalMargin(): Float {
        return mVerticalMargin
    }
}
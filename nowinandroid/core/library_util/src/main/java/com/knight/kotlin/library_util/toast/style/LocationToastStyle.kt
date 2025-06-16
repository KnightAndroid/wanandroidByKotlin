package com.knight.kotlin.library_util.toast.style

import android.content.Context
import android.view.View
import com.knight.kotlin.library_util.toast.callback.IToastStyle


/**
 * Author:Knight
 * Time:2021/12/17 13:52
 * Description:LocationToastStyle
 */
class LocationToastStyle constructor(style: IToastStyle<*>?,
                                     gravity: Int,
                                     xOffset: Int = 0,
                                     yOffset: Int = 0,
                                     horizontalMargin: Float = 0f,
                                     verticalMargin: Float = 0f) : IToastStyle<View?> {
    private val mStyle:IToastStyle<*>? = style

    private val mGravity = gravity
    private val mXOffset = xOffset
    private val mYOffset = yOffset
    private val mHorizontalMargin = horizontalMargin
    private val mVerticalMargin = verticalMargin

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
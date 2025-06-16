package com.knight.kotlin.library_util.toast.style

import android.content.Context
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import com.knight.kotlin.library_util.toast.callback.IToastStyle

/**
 * Author:Knight
 * Time:2021/12/17 13:58
 * Description:ViewToastStyle
 */
class CustomToastStyle @JvmOverloads constructor(
    id: Int,
    gravity: Int = Gravity.CENTER,
    xOffset: Int = 0,
    yOffset: Int = 0,
    horizontalMargin: Float = 0f,
    verticalMargin: Float = 0f
)  : IToastStyle<View> {

    private val mLayoutId = id
    private val mGravity = gravity
    private val mXOffset = xOffset
    private val mYOffset = yOffset
    private val mHorizontalMargin = horizontalMargin
    private val mVerticalMargin = verticalMargin


    override fun createView(context: Context): View {
        return LayoutInflater.from(context).inflate(mLayoutId,null)
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
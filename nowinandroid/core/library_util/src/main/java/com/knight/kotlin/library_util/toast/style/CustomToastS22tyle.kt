package com.knight.kotlin.library_util.toast.style

import android.content.Context
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import com.knight.kotlin.library_util.toast.callback.IToastStyle


/**
 * @author created by luguian
 * @organize
 * @Date 2025/2/13 15:00
 * @descript:自自定义 View 包装样式实现
 */
class CustomToastS22tyle @JvmOverloads constructor(
    id: Int,
    gravity: Int = Gravity.CENTER,
    xOffset: Int = 0,
    yOffset: Int = 0,
    horizontalMargin: Float = 0f,
    verticalMargin: Float = 0f
) :
    IToastStyle<View?> {
    private val mLayoutId = id
    private val mGravity = gravity
    private val mXOffset = xOffset
    private val mYOffset = yOffset
    private val mHorizontalMargin = horizontalMargin
    private val mVerticalMargin = verticalMargin

    override fun createView(context: Context): View? {
        return LayoutInflater.from(context).inflate(mLayoutId, null)
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
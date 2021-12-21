package com.knight.kotlin.library_util.toast.style

import android.content.Context
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import com.knight.kotlin.library_util.toast.callback.ToastStyleInterface

/**
 * Author:Knight
 * Time:2021/12/17 13:58
 * Description:ViewToastStyle
 */
class ViewToastStyle constructor(id:Int,style :ToastStyleInterface<*>?) : ToastStyleInterface<View> {

    private val mLayoutId:Int = id
    private val mStyle :ToastStyleInterface<*>? = style


    override fun createView(context: Context): View {
        return LayoutInflater.from(context).inflate(mLayoutId,null)
    }

    override fun getGravity():Int{
        if (mStyle == null) {
            return Gravity.CENTER
        }
        return mStyle.getGravity()
    }

    override fun getXOffset():Int {
        if (mStyle == null) {
            return 0
        }
        return mStyle.getXOffset()
    }

    override fun getYOffset(): Int {
        return mStyle?.getYOffset()!!
    }

    override fun getHorizontalMargin(): Float {
        return mStyle?.getHorizontalMargin() ?: 0f
    }

    override fun getVerticalMargin(): Float {
        return mStyle?.getVerticalMargin() ?: 0f
    }
}
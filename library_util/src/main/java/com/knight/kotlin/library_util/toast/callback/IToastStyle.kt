package com.knight.kotlin.library_util.toast.callback

import android.content.Context
import android.view.Gravity
import android.view.View


/**
 * Author:Knight
 * Time:2021/12/17 11:02
 * Description:ToastStyleInterface
 */
open interface IToastStyle<V : View?> {
    /**
     * 创建 Toast 视图
     */
    fun createView(context: Context): V

    /**
     * 获取 Toast 显示重心
     */
    fun getGravity(): Int {
        return Gravity.CENTER
    }

    /**
     * 获取 Toast 水平偏移
     */
    fun getXOffset(): Int {
        return 0
    }

    /**
     * 获取 Toast 垂直偏移
     */
    fun getYOffset(): Int {
        return 0
    }

    /**
     * 获取 Toast 水平间距
     */
    fun getHorizontalMargin(): Float {
        return 0.0f
    }

    /**
     * 获取 Toast 垂直间距
     */
    fun getVerticalMargin(): Float {
        return 0.0f
    }
}
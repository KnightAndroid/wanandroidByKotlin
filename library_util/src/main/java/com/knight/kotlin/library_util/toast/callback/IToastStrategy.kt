package com.knight.kotlin.library_util.toast.callback

import android.app.Application
import com.knight.kotlin.library_util.toast.ToastParams


/**
 * Author:Knight
 * Time:2021/12/17 10:59
 * Description:ToastStrategyInterface
 */
interface IToastStrategy {
    /**
     *
     * 注册策略
     */
    fun registerStrategy(application:Application)
    /**
     * 绑定样式
     */
    /**
     * 计算 Toast 显示时长
     */
    fun computeShowDuration(text: CharSequence): Int


    /**
     * 创建 Toast
     */
    fun createToast(params: ToastParams): IToast?



    /**
     * 显示 Toast
     */
    fun showToast(params: ToastParams)

    /**
     * 取消Toast
     */
    fun cancelToast()



}
package com.knight.kotlin.library_util.toast.callback

import android.app.Application

/**
 * Author:Knight
 * Time:2021/12/17 10:59
 * Description:ToastStrategyInterface
 */
interface ToastStrategyInterface {
    /**
     *
     * 注册策略
     */
    fun registerStrategy(application:Application)
    /**
     * 绑定样式
     */
    fun bindStyle(style: ToastStyleInterface<*>)
    /**
     * 创建 Toast
     */
    fun createToast(application:Application) : ToastInterface
    /**
     * 显示 Toast
     *
     */
    fun showToast(text: CharSequence)

    /**
     * 取消Toast
     */
    fun cancelToast()



}
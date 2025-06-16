package com.knight.kotlin.library_util.toast.callback

import com.knight.kotlin.library_util.toast.ToastParams



/**
 * Author:Knight
 * Time:2021/12/17 10:24
 * Description:ToastInterceptorInterface
 */
interface IToastInterceptor {

    /**
     * 根据显示的文本决定是否拦截该 Toast
     */
    fun intercept(params: ToastParams): Boolean
}
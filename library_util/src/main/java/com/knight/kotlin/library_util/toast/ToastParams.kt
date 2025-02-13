package com.knight.kotlin.library_util.toast

import com.knight.kotlin.library_util.toast.callback.IToastInterceptor
import com.knight.kotlin.library_util.toast.callback.IToastStrategy
import com.knight.kotlin.library_util.toast.callback.IToastStyle




/**
 * @author created by luguian
 * @organize
 * @Date 2025/2/13 15:17
 * @descript: Toast 参数类
 */
class ToastParams {
    /** 显示的文本  */
    var text: CharSequence? = null

    /**
     * Toast 显示时长，有两种值可选
     *
     * 短吐司：[android.widget.Toast.LENGTH_SHORT]
     * 长吐司：[android.widget.Toast.LENGTH_LONG]
     */
    var duration: Int = -1

    /** 延迟显示时间  */
    var delayMillis: Long = 0

    /** 是否跨页面展示（如果为 true 则优先用系统 Toast 实现）  */
    var crossPageShow: Boolean = false

    /** Toast 样式  */
    var style: IToastStyle<*>? = null

    /** Toast 处理策略  */
    var strategy: IToastStrategy? = null

    /** Toast 拦截器  */
    var interceptor: IToastInterceptor? = null
}
package com.knight.kotlin.library_util.toast


import com.knight.kotlin.library_util.BuildConfig
import com.knight.kotlin.library_util.LogUtils
import com.knight.kotlin.library_util.toast.callback.ToastInterceptorInterface

/**
 * Author:Knight
 * Time:2021/12/20 11:12
 * Description:ToastInterceptor
 */
class ToastInterceptor : ToastInterceptorInterface {

    override fun intercept(text: CharSequence): Boolean {
        if (BuildConfig.DEBUG) {
            //获取调用的堆栈信息
            val stackTrace = Throwable().stackTrace
            //跳过最前面的两个堆栈
            var i = 2
            while (stackTrace.size > 2 && i < stackTrace.size) {

                // 获取代码行数
                val lineNumber = stackTrace[i].lineNumber
                // 获取类的全路径
                val className = stackTrace[i].className
                if (lineNumber <= 0 || className.startsWith(ToastUtils::class.java.getName())) {
                    i++
                    continue
                }
                LogUtils.d("Toast" + stackTrace[i].fileName.toString() + ":" + lineNumber.toString() + ") " + text.toString())
                break
                i++
            }

        }
        return false
    }
}
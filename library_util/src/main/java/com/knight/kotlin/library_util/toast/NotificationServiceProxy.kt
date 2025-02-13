package com.knight.kotlin.library_util.toast

import java.lang.reflect.InvocationHandler
import java.lang.reflect.Method


/**
 * @author created by luguian
 * @organize
 * @Date 2025/2/13 15:11
 * @descript:通知服务代理对象
 */
internal class NotificationServiceProxy(realObject: Any) : InvocationHandler {
    /** 被代理的对象  */
    private val mRealObject = realObject

    @Throws(Throwable::class)
    override fun invoke(proxy: Any?, method: Method, args: Array<Any?>): Any {
        when (method.name) {
            "enqueueToast", "enqueueToastEx", "cancelToast" ->                 // 将包名修改成系统包名，这样就可以绕过系统的拦截
                // 部分华为机将 enqueueToast 方法名修改成了 enqueueToastEx
                args[0] = "android"

            else -> {}
        }
        // 使用动态代理
        return method.invoke(mRealObject, *args)
    }
}
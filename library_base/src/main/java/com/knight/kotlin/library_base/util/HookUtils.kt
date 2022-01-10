package com.knight.kotlin.library_base.util

import android.content.Context
import java.lang.reflect.Field
import java.lang.reflect.Method

/**
 * Author:Knight
 * Time:2022/1/5 14:54
 * Description:HookUtils
 * Hook Provider 控制Provider初始时机
 */
object HookUtils {
    private var providers: Any? = null

    private var installContentProvidersMethod: Method? = null
    private var currentActivityThread: Any? = null


    /**
     * 用户同意之后再初始化contentProvider框架
     * @param context
     */
    fun initProvider(context: Context?) {
        try {
            installContentProvidersMethod?.invoke(currentActivityThread, context, providers)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    @Throws(java.lang.Exception::class)
    fun attachContext() {
        // 先获取到当前的ActivityThread对象
        val activityThreadClass = Class.forName("android.app.ActivityThread")
        val currentActivityThreadMethod =
            activityThreadClass.getDeclaredMethod("currentActivityThread")
        currentActivityThreadMethod.isAccessible = true
        //currentActivityThread是一个static函数所以可以直接invoke，不需要带实例参数
        currentActivityThread = currentActivityThreadMethod.invoke(null)
        hookInstallContentProvider(activityThreadClass)
    }

    /**
     *
     * hookContentProvider
     * @param activityThreadClass
     * @throws Exception
     */
    @Throws(java.lang.Exception::class)
    private fun hookInstallContentProvider(activityThreadClass: Class<*>) {
        val appDataField: Field = activityThreadClass.getDeclaredField("mBoundApplication")
        appDataField.isAccessible = true
        val appData: Any = appDataField.get(currentActivityThread)
        val providersField: Field = appData.javaClass.getDeclaredField("providers")
        providersField.isAccessible = true
        providers = providersField.get(appData)
        //清空provider，避免有些sdk通过provider来初始化
        providersField.set(appData, null)
        installContentProvidersMethod = activityThreadClass.getDeclaredMethod(
            "installContentProviders",
            Context::class.java,
            MutableList::class.java
        )
        installContentProvidersMethod?.isAccessible = true
    }
}
package com.knight.kotlin.library_util

import android.app.Activity
import java.util.*

/**
 * Author:Knight
 * Time:2021/12/15 16:48
 * Description:ActivityManager
 */
object ActivityManager {


    //Activity栈
    val activityStack by lazy { Stack<Activity>() }


    /**
     * 添加Activity 到管理栈
     * @param activity Activity
     * 压入栈
     */
    fun addActivityToStack(activity:Activity) {
        activityStack.push(activity)
    }

    /**
     * 弹出栈内 不finish
     * @param activity Activity
     */
    fun popActivityToStack(activity: Activity) {
        if (!activityStack.empty()) {
            activityStack.forEach{
                if (it == activity) {
                    activityStack.remove(activity)
                    return
                }
            }

        }
    }

    /**
     * 返回上一个Activity 并结束当前Activity
     *
     */
    fun backToPreviousActivity() {
        if (!activityStack.empty()) {
            val activity = activityStack.pop()
            if (!activity.isFinishing) activity.finish()
        }
    }

    fun isCurrentActivity(cls:Class<*>):Boolean {
        val currentActivity = getCurrentActivity()
        return if (currentActivity != null) currentActivity.javaClass == cls else false
    }

    /**
     *
     * 获取当前的Activity
     */
    fun getCurrentActivity():Activity? =
        if (!activityStack.empty()) activityStack.lastElement() else null


    /**
     * 结束一个栈内指定类名的Activity
     * @param cls Class<*>
     */
    fun finishActivity(cls:Class<*>) {
        activityStack.forEach {
            if (it.javaClass == cls) {
                if (!it.isFinishing) it.finish()
                return
            }
        }
    }

    /**
     *
     * 弹出除了这个Activity的所有Activity
     */
    fun popOtherActivty() {
        val activtyList = activityStack.toList()
        getCurrentActivity()?.run {
            activtyList.forEach { activity ->
                if (this != activity) {
                    activityStack.remove(activity)
                    activity.finish()
                }
            }
        }
    }

    /**
     *
     * 返回指定Activity
     */
    fun backToSpecifyActivty(activtyClass : Class<*>) {
        val activtyList = activityStack.toList().reversed()
        activtyList.forEach {
            if (it.javaClass == activtyClass) {
                return
            } else {
                activityStack.pop()
                it.finish()
            }
        }

    }

}
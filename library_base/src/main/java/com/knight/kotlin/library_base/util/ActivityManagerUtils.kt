package com.knight.kotlin.library_base.util

import android.app.Activity
import android.app.Application
import android.os.Bundle
import android.util.ArrayMap

/**
 * Author:Knight
 * Time:2021/12/20 15:15
 * Description:ActivityManagerUtils
 */
class ActivityManagerUtils : Application.ActivityLifecycleCallbacks {


    companion object {
        @Volatile
        private var instance: ActivityManagerUtils? = null
        /**
         * 单例模式
         * 双重校验锁
         * @return
         */
        fun getInstance(): ActivityManagerUtils? {
            if (instance == null) {
                synchronized(ActivityManagerUtils::class.java) {
                    if (instance == null) {
                        instance = ActivityManagerUtils()
                    }
                }
            }
            return instance
        }
    }


    private val mActivityArrayMap: ArrayMap<String, Activity> = ArrayMap()
    private  var mCurrentTag:String? = null



    /**
     *
     * 获取栈顶的Activity
     */
    fun getTopActivity():Activity? {
        return mActivityArrayMap.get(mCurrentTag)
    }

    /**
     * 销毁所有Activity
     */
    fun finishAllActivity() {
        val keys: Array<String> = mActivityArrayMap.keys.toTypedArray()
        for (key in keys) {
            val activity = mActivityArrayMap[key]
            activity?.finish()
            mActivityArrayMap.remove(key)
        }
    }


    @SafeVarargs
    fun finishAllActivities(vararg classArray: Class<out Activity?>) {
        val keys: Array<String> = mActivityArrayMap.keys.toTypedArray()
        for (key in keys) {
            val activity = mActivityArrayMap[key]
            if (activity != null && !activity.isFinishing) {
                var whiteClazz = false
                if (classArray != null) {
                    for (clazz in classArray) {
                        if (activity.javaClass == clazz) {
                            whiteClazz = true
                        }
                    }
                }
                //如果不是白名单上的Activity就销毁掉
                if (!whiteClazz) {
                    activity.finish()
                    mActivityArrayMap.remove(key)
                }
            }
        }
    }

    /**
     * 获取一个独一无二的对象标记
     *
     */
    fun getObjectTag(ob:Any):String {
         //对象所在的包名+对象的内存hash地址
         return ob.javaClass.getName().toString() + Integer.toHexString(ob.hashCode())
    }

    override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {
        mCurrentTag = getObjectTag(activity)
        mActivityArrayMap.put(getObjectTag(activity),activity)
    }

    override fun onActivityStarted(activity: Activity) {
        mCurrentTag = getObjectTag(activity)
    }

    override fun onActivityResumed(activity: Activity) {
        mCurrentTag = getObjectTag(activity)
    }

    override fun onActivityPaused(activity: Activity) {
    }

    override fun onActivityStopped(activity: Activity) {
    }

    override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle) {
    }

    override fun onActivityDestroyed(activity: Activity) {
        mActivityArrayMap.remove(getObjectTag(activity))
        if (getObjectTag(activity).equals(mCurrentTag)) {
            //清除当前标记
            mCurrentTag = null

        }
    }
}
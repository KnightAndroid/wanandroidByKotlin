package com.knight.kotlin.library_aop.loginintercept

import android.content.Context

/**
 * Author:Knight
 * Time:2022/1/17 14:44
 * Description:LoginManager
 */
class LoginManager {


    companion object {
        private var instance: LoginManager? = null
        fun getInstance(): LoginManager? {
            if (null == instance) {
                synchronized(LoginManager::class.java) {
                    if (null == instance) {
                        instance = LoginManager()
                    }
                }
            }
            return instance
        }
    }


    /**
     * 初始化
     * @param context
     * @param iLoginFilter
     */
    fun init(context: Context?, iLoginFilter: ILoginFilter?) {
        LoginAssistant.getInstance()!!.setApplicationContext(context)
        LoginAssistant.getInstance()!!.setiLoginFilter(iLoginFilter)
    }

}
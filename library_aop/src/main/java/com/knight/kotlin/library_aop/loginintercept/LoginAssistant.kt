package com.knight.kotlin.library_aop.loginintercept

import android.content.Context

/**
 * Author:Knight
 * Time:2022/1/17 14:40
 * Description:LoginAssistant
 */
class LoginAssistant {

    companion object {
        private var instance: LoginAssistant? = null
        fun getInstance(): LoginAssistant? {
            if (instance == null) {
                synchronized(LoginAssistant::class.java) {
                    if (instance == null) {
                        instance = LoginAssistant()
                    }
                }
            }
            return instance
        }
    }


    private var iLoginFilter: ILoginFilter? = null
    private var applicationContext: Context? = null

    fun getiLoginFilter(): ILoginFilter? {
        return iLoginFilter
    }

    fun setiLoginFilter(iLoginFilter: ILoginFilter?) {
        this.iLoginFilter = iLoginFilter
    }


    fun setApplicationContext(applicationContext: Context?) {
        this.applicationContext = applicationContext
    }

    fun getApplicationContext(): Context? {
        return applicationContext
    }


}
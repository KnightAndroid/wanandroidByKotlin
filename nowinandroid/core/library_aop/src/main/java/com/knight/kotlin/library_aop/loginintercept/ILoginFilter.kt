package com.knight.kotlin.library_aop.loginintercept

import android.content.Context

/**
 * Author:Knight
 * Time:2022/1/17 14:40
 * Description:ILoginFilter
 */
interface ILoginFilter {
    fun login(applicationContext: Context?, loginDefine: Boolean)
    fun isLogin(applicationContext: Context?): Boolean
}
package com.knight.kotlin.library_aop.loginintercept

import android.content.Context
import com.flyjingfish.android_aop_annotation.ProceedJoinPoint
import com.flyjingfish.android_aop_annotation.base.BasePointCut

/**
 * Author:Knight
 * Time:2024/1/9 15:54
 * Description:LoginInterceptCut
 */
class LoginInterceptCut : BasePointCut<LoginCheck> {
    override fun invoke(joinPoint: ProceedJoinPoint, anno: LoginCheck): Any? {
        //获取用户实现的ILogin类，如果没有调用init设置初始化就抛出异常
        val iLoginFilter = LoginAssistant.getInstance()?.getiLoginFilter()
            ?: throw RuntimeException("LoginManager没有初始化")
        val mContext: Context? = LoginAssistant.getInstance()?.getApplicationContext()
        val skipLogin = anno.isSkipLogin
        return if (skipLogin) {
            //可以跳过登录
            joinPoint.proceed()
        } else {
            if (iLoginFilter.isLogin(mContext)) {
                joinPoint.proceed()
            } else {
                //提示 需要登录
                iLoginFilter.login(mContext, false)
            }
        }

    }
}
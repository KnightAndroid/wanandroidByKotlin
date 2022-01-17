package com.knight.kotlin.library_aop.loginintercept

import android.content.Context
import org.aspectj.lang.ProceedingJoinPoint
import org.aspectj.lang.Signature
import org.aspectj.lang.annotation.Around
import org.aspectj.lang.annotation.Aspect
import org.aspectj.lang.annotation.Pointcut
import org.aspectj.lang.reflect.MemberSignature
import org.aspectj.lang.reflect.MethodSignature


/**
 * Author:Knight
 * Time:2022/1/17 14:42
 * Description:LoginCheckAspect
 */
@Aspect
class LoginCheckAspect {
    @Pointcut("execution(@com.knight.kotlin.library_aop.loginintercept.LoginCheck * * (..))")
    fun LoginCheck() {
    }


    @Around("LoginCheck()")
    @Throws(Throwable::class)
    fun aroundLoginPoint(joinPoint: ProceedingJoinPoint) {

        //获取用户实现的ILogin类，如果没有调用init设置初始化就抛出异常
        val iLoginFilter = LoginAssistant.getInstance()?.getiLoginFilter()
            ?: throw RuntimeException("LoginManager没有初始化")

        //先得到方法得签名methodSignature,然后得到@LoginFilter注解，如果注解为空，就不再往下走
        val signature: Signature =
            joinPoint.signature as? MemberSignature ?: throw RuntimeException("该注解只能用于方法上")
        val methodSignature = signature as MethodSignature
        val loginCheck = methodSignature.method.getAnnotation(LoginCheck::class.java) ?: return
        val mContext: Context? = LoginAssistant.getInstance()?.getApplicationContext()
        val skipLogin = loginCheck.isSkipLogin
        if (skipLogin) {
            //可以跳过登录
            joinPoint.proceed()
        } else {
            if (iLoginFilter.isLogin(mContext)) {
                joinPoint.proceed()
            } else {
                //提示 需要登录
                iLoginFilter.login(mContext, loginCheck.isSkipLogin)
            }
        }
    }
}
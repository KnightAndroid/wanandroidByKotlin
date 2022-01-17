package com.knight.kotlin.library_aop.loginintercept

/**
 * Author:Knight
 * Time:2022/1/17 14:41
 * Description:LoginCHeck
 */
@kotlin.annotation.Retention(AnnotationRetention.RUNTIME)
@Target(AnnotationTarget.FUNCTION)
annotation class LoginCheck(
    val isSkipLogin: Boolean = false //增加额外的信息，决定要不要跳过检查，默认不跳过
)

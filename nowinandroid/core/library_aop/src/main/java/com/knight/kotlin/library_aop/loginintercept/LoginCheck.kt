package com.knight.kotlin.library_aop.loginintercept

import com.flyjingfish.android_aop_annotation.anno.AndroidAopPointCut

/**
 * Author:Knight
 * Time:2022/1/17 14:41
 * Description:LoginCHeck
 */

//@AndroidAopPointCut(LoginInterceptCut::class)。最新不能这么用
@Retention(AnnotationRetention.RUNTIME)
@Target(AnnotationTarget.FUNCTION)
annotation class LoginCheck(
    val isSkipLogin: Boolean = false //增加额外的信息，决定要不要跳过检查，默认不跳过
)

package com.knight.kotlin.library_aop.clickintercept

/**
 * Author:Knight
 * Time:2022/1/14 17:50
 * Description:SingleClick
 */
@kotlin.annotation.Retention(AnnotationRetention.RUNTIME)
@Target(
    AnnotationTarget.FUNCTION,
    AnnotationTarget.PROPERTY_GETTER,
    AnnotationTarget.PROPERTY_SETTER
)
annotation class SingleClick constructor(
    /**
     * 快速点击的间隔
     */
    val value: Long = 1000
)
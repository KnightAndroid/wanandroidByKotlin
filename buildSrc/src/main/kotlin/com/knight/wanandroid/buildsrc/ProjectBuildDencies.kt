package com.knight.wanandroid.buildsrc

/**
 *Author:Knight
 *Time:2021/12/14 15:37
 *Description:ProjectBuildDencies
 */
object ProjectBuildDencies {
    const val COMPILE_SDK_VERSION = 35
    const val BUILD_TOOLS_VERSION = "35.0.0"
    const val APPLICATION_ID = "com.knight.kotlin.wanandroid"
    const val MIN_SDK_VERSION = 22
    const val TARGET_SDK_VERSION = 35
    const val VERSION_CODE = 11
    const val VERSION_NAME = "2.2.0"
    const val APP_MODE = false


    /**
     * 环境版本
     * 开发版本：DEVELOP
     * 测试版本：RELEASE
     * 正式版本：MASTER
     */
    object Variants {
        //开发版本
        const val DEVELOP = "DEVELOP"
        //测试版本
        const val RELEASE = "RELEASE"
        //正式版本
        const val MASTER = "MASTER"
    }
}
package com.knight.wanandroid.buildsrc

/**
 *Author:Knight
 *Time:2021/12/14 15:37
 *Description:ProjectBuildDencies
 */
object ProjectBuildDencies {
    const val compileSdkVersion = 34
    const val buildToolsVersion = "34.0.0"
    const val applicationId = "com.knight.kotlin.wanandroid"
    const val minSdkVersion = 21
    const val targetSdkVersion = 34
    const val versionCode = 5
    const val versionName = "1.1.0"
    const val isAppMode = false


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
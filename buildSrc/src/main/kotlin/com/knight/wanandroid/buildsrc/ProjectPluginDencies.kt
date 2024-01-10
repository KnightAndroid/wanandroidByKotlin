package com.knight.wanandroid.buildsrc

/**
 * 项目级插件管理
 *
 * @author Knight
 * @since 14/14/21 14:53 PM
 */
object ProjectPluginDencies {
    const val AndroidToolsPlugin = "com.android.tools.build:gradle:7.0.2"
    const val KotlinPlugin = "org.jetbrains.kotlin:kotlin-gradle-plugin:1.8.10"
    const val GoRouterRegister = "com.github.wyjsonGo.GoRouter:GoRouter-Gradle-Plugin:2.4.5"
    const val HiltPlugin = "com.google.dagger:hilt-android-gradle-plugin:2.47"
    const val AspectJxPlugin = "com.hujiang.aspectjx:gradle-android-plugin-aspectjx:2.0.10"
    const val AndroidAop = "io.github.FlyJingFish.AndroidAop:android-aop-plugin:1.3.1"
    const val AndroidKsp = "com.google.devtools.ksp:1.8.10-1.0.9"
}


/**
 * 一、什么是Gradle、Gradle插件
 * Gradle：gradle-wrapper.properties中配置的是Gradle的版本  distributionUrl=https://services.gradle.org/distributions/gradle-6.2.2-all.zip
 * 一个构建系统，构建项目的工具，用来编译Android app，能够简化你的编译、打包、测试过程 构建工具就是对你的项目进行编译、运行、签名、打包、依赖管理等一系列功能的合集
 *
 * Gradle插件：Android Plugin for Gradle，工程根目录build.gradle中的依赖指定的是Gradle插件的版本 classpath 'com.android.tools.build:gradle:3.6.3'
 * Android Plugin for Gradle，它本质就是一个AS的插件，它一边调用 Gradle本身的代码和批处理工具来构建项目，一边调用Android SDK的编译、打包功能，从而让我们能够顺畅地在AS上进行开发
 *
 * 没错，这就是Gradle替我们做的事。Gradle通过编写一个名为build.gradle的脚本文件对项目进行设置，再根据这个脚本对项目进行构建。
 * 那么，如何来提供这个Gradle构建环境呢？这就需要通过安装Gradle插件来使系统能支持运行Gradle。
 *
 */
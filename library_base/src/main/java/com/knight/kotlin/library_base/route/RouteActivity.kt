package com.knight.kotlin.library_base.route

/**
 * Author:Knight
 * Time:2021/12/21 16:46
 * Description:RouteActivity
 */
object RouteActivity {

    /**
     * 容器模块
     *
     */
    object Main {
        private const val MAIN = "/module_main"
        const val MainActivity = "$MAIN/MainActivity"
    }

    /**
     * 闪屏模块 欢迎模块
     *
     */
    object Weclome {
        private const val WECLOME = "/module_welcome"
        const val WeclmeActivity = "$WECLOME/WelcomeActivity"
    }
}
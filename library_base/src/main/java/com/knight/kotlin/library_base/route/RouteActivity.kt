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
     *
     * 首页模块
     */
    object Home {
        private const val HOME = "/module_home"
        const val HomeArticlesTabActivity = "$HOME/ArticlesTabActivity"
    }

    /**
     * 闪屏模块 欢迎模块
     *
     */
    object Weclome {
        private const val WECLOME = "/module_welcome"
        const val WeclmeActivity = "$WECLOME/WelcomeActivity"
    }


    /**
     * H5网页模块
     */
    object Web {
        private const val WEB = "/module_web"
        const val WebPager = "$WEB/WebPagerActivity"
        const val WebArticlePager = "$WEB/WebArticleActivity"
        const val WebPreviewPhotoPager = "$WEB/WebPreviewActivity"
        const val WebTransitionPager = "$WEB/WebTransitActivity"
    }



}
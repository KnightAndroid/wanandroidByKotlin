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
        const val HomeSearchActivity = "$HOME/HomeSearchActivity"
        const val HomeSearchResultActivity = "$HOME/HomeSearchResultActivity"
        const val HomeKnowLedgeLabelActivity = "$HOME/KnowLedgeLabelActivity"
        const val AddKnowLedgeLableActivity = "$HOME/AddKnowLedgeLabelActivity"
    }


    /**
     *
     * 广场模块
     */
    object Square {
        private const val SQUARE = "/module_square"
        const val SquareShareArticleActivity = "$SQUARE/ShareArticleActivity"
    }


    /**
     * 公众号模块
     *
     */
    object Wechat {
        private const val WECHAT = "/module_wechat"
        const val WechatTabActivity = "$WECHAT/WechatTabActivity"
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
     * 导航模块
     *
     */
    object Navigate {
        private const val NAVIGATE = "/module_navigate"
        const val HierachyTabActivity = "$NAVIGATE/HierachyTabActivity"
        const val HierachyDetailActivity = "$NAVIGATE/HierachyDetailActivity"
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


    /**
     * 我的模块
     *
     */
    object Mine {
        private const val MINE = "/module_mine"
        const val QuickLoginActivity = "$MINE/QuickLoginActivity"
        const val LoginActivity = "$MINE/LoginActivity"
        const val RegisterActivity = "$MINE/RegisterActivity"
        const val UserCoinRankActivity = "$MINE/UserCoinRankActivity"
        const val OtherShareArticleActivity = "$MINE/OtherShareArticleActivity"
        const val MyPointsActivity = "$MINE/MyPointsActivity"
        const val MyCollectArticleActivity = "$MINE/MyCollectArticleActivity"
        const val MyShareArticlesActivity = "$MINE/MySHareArticlesActivity"
        const val HistoryRecordActivity = "$MINE/HistoryRecordActivity"
    }

    /**
     *
     * 设置模块
     */
    object Set {
        private const val SET = "/module_set"
        const val SetActivity = "$SET/SetActivity"
        const val DarkModelActivity = "$SET/DarkModelActivity"
        const val SetLanguageActivity = "$SET/SetLanguageActivity"
        const val SetAutoNightActivity = "$SET/SetAutoNightActivity"
        const val SetChangeTextSizeActivity = "$SET/SetChangeTextSizeActivity"
        const val SetGestureLockActivity = "$SET/SetGestureLockActivity"
        const val AboutActivity = "$SET/AboutActivity"
        const val PersonalDeviceMessage = "$SET/SetPersonalDeviceActivity"
        const val PersonalMessage = "$SET/SetPersonalMessage"
        const val DeviceMessage = "$SET/DeviceMessage"
        const val AppRecordMessageActivity = "$SET/AppRecordMessageActivity"

    }


    /***
     *
     * 工具类
     *
     */
    object Utils {
        private const val Utils = "/module_utils"
        const val UtilsActivity = "$Utils/UtilsActivity"
    }

    /**
     *
     * 课程模块
     *
     */
    object Course {
        private const val Course = "/module_course"
        const val CourseListActivity = "$Course/CourseListActivity"
        const val CourseDetailListActivity = "$Course/CourseDetailListActivity"
    }


    /**
     *
     * 消息模块
     */
    object Message {
        private const val Message = "/module_message"
        const val MessageActivity = "$Message/MessageActivity"

    }



}
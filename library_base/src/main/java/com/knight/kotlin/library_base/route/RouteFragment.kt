package com.knight.kotlin.library_base.route

/**
 * Author:Knight
 * Time:2021/12/22 16:21
 * Description:RouteFragment
 */
object RouteFragment {

    /**
     *
     * 首页
     */
    object Home {
        private const val HOME = "/module_home"
        const val HomeFragment = "$HOME/HomeFragment"
        const val RecommendFragment = "$HOME/RecommendFragment"
        const val HomeTopArticleFragment = "$HOME/TopArticleFragment"
        const val HomeEyeClassifyFragment = "$HOME/EyeClassifyFragment"
    }


    /**
     *
     * 微信
     *
     */
    object Wechat {
        private const val WECHAT = "/module_wechat"
        const val WechatOfficialAccountFragment = "$WECHAT/WechatOfficialAccountFragment"

    }

    /**
     *
     * 广场
     */
    object Square {
        private const val SQUARE = "/module_square"
        const val SquareFragment = "$SQUARE/SquareFragment"
        const val SquareShareListFragment = "$SQUARE/SquareShareListFragment"
        const val SquareArticleFragment = "$SQUARE/ArticleFragment"
    }


    /**
     * 项目
     *
     */
    object Project {
        private const val PROJECT = "/module_project"
        const val ProjectFragment = "$PROJECT/ProjectFragment"
        const val ProjectArticleFragment = "$PROJECT/ProjectArticleFragment"
    }


    /***
     *
     * 导航
     *
     */
    object Navigate {
        private const val NAVIGATE = "/module_navigate"
        const val NavigateHomeFragment = "$NAVIGATE/NavigateHomeFragment"
        const val NavigateFragment = "$NAVIGATE/NavigateFragment"
        const val HierachyFragment = "$NAVIGATE/HierachyFragment"
        const val NavigateRightTreeFragment = "$NAVIGATE/NavigateRightTreeFragment"
        const val HierachyTabArticleFragment = "$NAVIGATE/HierachyTabArticleFragment"

    }


    /**
     *
     * 我的
     */
    object Mine {
        private const val MINE = "/module_mine"
        const val MineFragment = "$MINE/MineFragment"

    }

    /**
     *
     * 信息
     */
    object Message {
        private const val MESSAGE = "/module_message"
        const val MessageFragment = "$MESSAGE/MessageFragment"
    }




}
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
        const val HomeArticleFragment = "$HOME/ArticleFragment"
    }

    /**
     *
     * 广场
     */
    object Square {
        private const val SQUARE = "/module_square"
        const val SquareFragment = "$SQUARE/SquareFragment"
    }


    /**
     * 项目
     *
     */
    object Project {
        private const val PROJECT = "/module_project"
        const val ProjectFragment = "$PROJECT/ProjectFragment"
    }


    /***
     *
     * 导航
     *
     */
    object Navigate {
        private const val NAVIGATE = "/module_navigate"
        const val NavigateFragment = "$NAVIGATE/NavigateFragment"
    }


    /**
     *
     * 我的
     */
    object Mine {
        private const val MINE = "/module_mine"
        const val MineFragment = "$MINE/MineFragment"

    }




}
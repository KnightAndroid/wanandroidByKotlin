package com.knight.kotlin.module_mine.contract

import com.core.library_base.vm.ViewEvent
import com.core.library_base.vm.ViewSideEffect
import com.core.library_base.vm.ViewState
import com.knight.kotlin.module_mine.entity.MyArticleEntity


/**
 * @author created by luguian
 * @organize
 * @Date 2026/6/8 16:28
 * @descript:我的分享启动契约
 */
class MyShareArticlesContract {

    /**
     * Event
     *
     * 用户行为
     */
    sealed class Event : ViewEvent {

        /**
         * 初始化加载
         */
        data object GetMyShareArticles : Event()

        /**
         * 下拉刷新
         */
        data object Refresh : Event()

        /**
         * 上拉加载更多
         */
        data object LoadMore : Event()

        /**
         * 删除分享文章
         */
        data class DeleteMyShareArticle(
            val articleId: Int,
            val position: Int
        ) : Event()
    }

    /**
     * State
     *
     * 页面状态
     */
    data class State(

        /**
         * 是否显示Loading
         */
        val isLoading: Boolean = false,

        /**
         * 当前页码
         */
        val page: Int = 1,

        /**
         * 我的分享文章列表
         */
        val articleList: List<MyArticleEntity> = emptyList(),

        /**
         * 是否还有下一页
         */
        val hasMore: Boolean = true,

        /**
         * 是否刷新成功
         */
        val refreshSuccess: Boolean = false,

        /**
         * 是否加载更多成功
         */
        val loadMoreSuccess: Boolean = false,

        /**
         * 是否请求成功
         */
        val requestSuccess: Boolean = false,

        /**
         * 是否空数据
         */
        val isEmpty: Boolean = false

    ) : ViewState

    /**
     * Effect
     *
     * 一次性事件
     */
    sealed class Effect : ViewSideEffect {

        /**
         * 删除成功
         */
        data class DeleteSuccess(
            val position: Int
        ) : Effect()

        /**
         * Toast
         */
        data class ShowToast(
            val msg: String
        ) : Effect()

        /**
         * 打开文章详情
         */
        data class OpenArticle(
            val article: MyArticleEntity
        ) : Effect()
    }
}
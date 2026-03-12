package com.knight.kotlin.module_square.contract

import com.core.library_base.vm.ViewEvent
import com.core.library_base.vm.ViewSideEffect
import com.core.library_base.vm.ViewState
import com.knight.kotlin.module_square.entity.SquareArticleListBean


/**
 * @author created by luguian
 * @organize
 * @Date 2026/3/12 14:59
 * @descript:广场文章契约 用户意图 事件 状态整合
 */
class SquareArticleContract {

    /** =========================
     * 用户意图
     * ========================= */
    sealed class Event : ViewEvent {

        /**
         * 加载文章
         */
        data class GetArticleByTag(
            val page: Int,
            val keyword: String
        ) : Event()

        /**
         * 收藏
         */
        data class CollectArticle(
            val id: Int
        ) : Event()

        /**
         * 取消收藏
         */
        data class UnCollectArticle(
            val id: Int
        ) : Event()
    }

    /** =========================
     * 页面状态（唯一）
     * ========================= */
    data class State(

        /** 文章分页数据 */
        val articleList: SquareArticleListBean? = null,

        /** loading */
        val loading: Boolean = false,

        /** 是否刷新 */
        val isRefreshing: Boolean = false

    ) : ViewState


    /** =========================
     * 一次性事件
     * ========================= */
    sealed class Effect : ViewSideEffect {

        /** toast */
        data class ShowError(val msg: String) : Effect()

        /** 收藏成功 */
        object CollectSuccess : Effect()

        /** 取消收藏成功 */
        object UnCollectSuccess : Effect()
    }
}
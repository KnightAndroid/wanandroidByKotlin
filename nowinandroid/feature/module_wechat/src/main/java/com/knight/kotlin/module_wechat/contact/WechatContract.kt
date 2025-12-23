package com.knight.kotlin.module_wechat.contact

import com.core.library_base.vm.ViewEvent
import com.core.library_base.vm.ViewSideEffect
import com.core.library_base.vm.ViewState
import com.knight.kotlin.module_wechat.entity.WechatArticleListEntity


/**
 * @author created by luguian
 * @organize
 * @Date 2025/12/23 10:10
 * @descript:微信公众号 契约 用户意图 事件 状态整合
 */
class WechatContract {

    sealed class Event : ViewEvent {

        data class LoadArticles(
            val cid: Int,
            val page: Int,
            val keyword: String? = null,
            val isRefresh: Boolean = false
        ) : Event()

        data class CollectArticle(
            val articleId: Int,
            val position: Int
        ) : Event()

        data class UnCollectArticle(
            val articleId: Int,
            val position: Int
        ) : Event()
    }

    data class State(
        val isLoading: Boolean = false,
        val isRefreshing: Boolean = false,

        /** 接口返回的完整数据 */
        val articlePage: WechatArticleListEntity? = null,
        val isError: Boolean = false
    ) : ViewState

    sealed class Effect : ViewSideEffect {
        data class ShowToast(val msg: String) : Effect()

        /** 收藏状态是一次性 UI 行为 */
        data class UpdateCollect(
            val position: Int,
            val collect: Boolean
        ) : Effect()
    }
}
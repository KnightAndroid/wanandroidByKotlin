package com.knight.kotlin.module_square.contract

import com.core.library_base.vm.ViewEvent
import com.core.library_base.vm.ViewSideEffect
import com.core.library_base.vm.ViewState
import com.knight.kotlin.module_square.entity.SquareShareArticleListBean


/**
 * @author created by luguian
 * @organize
 * @Date 2026/3/13 15:11
 * @descript:MVI 契约：广场文章列表
 */
class SquareListContract {

    /** =========================
     * 用户意图
     * ========================= */
    sealed class Event : ViewEvent {

        data class GetSquareArticles(val page: Int) : Event()

        data class CollectArticle(val id: Int) : Event()

        data class UnCollectArticle(val id: Int) : Event()
    }

    /** =========================
     * 页面状态
     * ========================= */
    data class State(
        val articleList: SquareShareArticleListBean? = null,
        val loading: Boolean = false
    ) : ViewState

    /** =========================
     * 一次性事件
     * ========================= */
    sealed class Effect : ViewSideEffect {
        data class ShowError(val msg: String) : Effect()
        object CollectSuccess : Effect()
        object UnCollectSuccess : Effect()
    }
}
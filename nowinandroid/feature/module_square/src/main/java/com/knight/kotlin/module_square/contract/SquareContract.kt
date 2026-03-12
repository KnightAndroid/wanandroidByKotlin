package com.knight.kotlin.module_square.contract

import com.core.library_base.vm.ViewEvent
import com.core.library_base.vm.ViewSideEffect
import com.core.library_base.vm.ViewState
import com.knight.kotlin.module_square.entity.SquareQuestionListBean


/**
 * @author created by luguian
 * @organize
 * @Date 2026/3/12 15:19
 * @descript:广场 契约 用户意图 事件 状态整合
 */
/**
 * Author: Knight
 * Time: 2026/3/12
 * Description: SquareFragment MVI Contract
 */
class SquareContract {

    /** 用户意图 / Event */
    sealed class Event : ViewEvent {

        /** 请求问答列表（分页） */
        data class LoadQuestions(
            val page: Int,
            val isRefresh: Boolean = false
        ) : Event()

        /** 收藏文章 */
        data class CollectArticle(
            val articleId: Int,
            val position: Int
        ) : Event()

        /** 取消收藏文章 */
        data class UnCollectArticle(
            val articleId: Int,
            val position: Int
        ) : Event()

        /** 更新知识标签（可选，将 ChangeLabel 也通过 Event 处理） */
        data class UpdateKnowledgeLabel(
            val labels: List<String>
        ) : Event()
    }

    /** 页面状态 */
    data class State(

        /** 问答列表分页数据 */
        val questionPage: SquareQuestionListBean? = null,

        /** 是否正在加载（首次进入或重试 Loading） */
        val isLoading: Boolean = false,

        /** 是否刷新中 */
        val isRefreshing: Boolean = false

    ) : ViewState

    /** 一次性副作用 / Effect */
    sealed class Effect : ViewSideEffect {

        /** 展示错误信息 */
        data class ShowError(val msg: String) : Effect()

        /** 展示吐司 */
        data class ShowToast(val msg: String) : Effect()

        /** 更新收藏状态（Fragment 根据 position 更新 RecyclerView） */
        data class UpdateCollect(
            val position: Int,
            val collect: Boolean
        ) : Effect()

        /** 可选：加载更多或刷新成功提示 */
        object RefreshSuccess : Effect()
        object LoadMoreSuccess : Effect()
    }
}
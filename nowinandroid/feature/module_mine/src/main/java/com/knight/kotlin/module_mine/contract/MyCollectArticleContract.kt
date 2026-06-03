package com.knight.kotlin.module_mine.contract

import com.core.library_base.vm.ViewEvent
import com.core.library_base.vm.ViewSideEffect
import com.core.library_base.vm.ViewState
import com.knight.kotlin.module_mine.entity.MyCollectArticleListEntity


/**
 * @author created by luguian
 * @organize
 * @Date 2026/6/3 16:30
 * @descript:我的收藏契约
 */
class MyCollectArticleContract {

    sealed class Event : ViewEvent {

        data object LoadData : Event()

        data object Refresh : Event()

        data object LoadMore : Event()

        data class UnCollectArticle(
            val articleId: Int,
            val position: Int
        ) : Event()
    }

    data class State(

        val isLoading: Boolean = false,

        val page: Int = 0,

        val articleData: MyCollectArticleListEntity? = null,

        val noMoreData: Boolean = false

    ) : ViewState

    sealed class Effect : ViewSideEffect {

        data class ShowToast(
            val msg: String
        ) : Effect()

        data class RemoveItem(
            val position: Int
        ) : Effect()
    }
}
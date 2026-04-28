package com.knight.kotlin.module_navigate.contract

import com.core.library_base.vm.ViewEvent
import com.core.library_base.vm.ViewSideEffect
import com.core.library_base.vm.ViewState
import com.knight.kotlin.module_navigate.entity.HierachyTabArticleEntity


/**
 * @author created by luguian
 * @organize
 * @Date 2026/4/28 16:11
 * @descript:
 */
class HierachyArticleContract {

    sealed class Event : ViewEvent {
        data class LoadData(val isRefresh: Boolean) : Event()
        data class Collect(val id: Int, val position: Int) : Event()
        data class UnCollect(val id: Int, val position: Int) : Event()
    }

    data class State(
        val isLoading: Boolean = false,
        val list: MutableList<HierachyTabArticleEntity> = mutableListOf(),
        val page: Int = 0,
        val hasMore: Boolean = true
    ) : ViewState

    sealed class Effect : ViewSideEffect {
        data class ShowToast(val msg: String) : Effect()
        object FinishRefresh : Effect()
        object FinishLoadMore : Effect()
        data class UpdateItem(val position: Int, val collect: Boolean) : Effect()
    }
}
package com.knight.kotlin.module_project.contract

import com.core.library_base.vm.ViewEvent
import com.core.library_base.vm.ViewSideEffect
import com.core.library_base.vm.ViewState
import com.knight.kotlin.module_project.entity.ProjectArticleBean


/**
 * @author created by luguian
 * @organize
 * @Date 2026/4/9 16:06
 * @descript:项目文章契约
 */
class ProjectArticleContract {

    sealed class Event : ViewEvent {
        object Refresh : Event()
        object LoadMore : Event()

        data class Collect(val id: Int, val position: Int) : Event()
        data class UnCollect(val id: Int, val position: Int) : Event()
    }

    data class State(
        val isLoading: Boolean = false,
        val list: List<ProjectArticleBean> = emptyList(),
        val page: Int = 1,
        val hasMore: Boolean = true,
        val isRefresh: Boolean = true,
        val error: String? = null
    ) : ViewState

    sealed class Effect : ViewSideEffect {
        data class Toast(val msg: String) : Effect()
    }
}
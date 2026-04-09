package com.knight.kotlin.module_project.contract

import com.core.library_base.vm.ViewEvent
import com.core.library_base.vm.ViewSideEffect
import com.core.library_base.vm.ViewState
import com.knight.kotlin.module_project.entity.ProjectTypeBean


/**
 * @author created by luguian
 * @organize
 * @Date 2026/4/9 15:24
 * @descript:项目契约
 */
class ProjectContract {

    sealed class Event : ViewEvent {
        object LoadProjectTypes : Event()
        object Retry : Event()
    }

    data class State(
        val isLoading: Boolean = false,
        val projectTypes: List<ProjectTypeBean> = emptyList(),
        val error: String? = null
    ) : ViewState

    sealed class Effect : ViewSideEffect {
        data class ShowToast(val msg: String) : Effect()

        // ✅ 新增：一次性初始化 ViewPager
        data class InitViewPager(val list: List<ProjectTypeBean>) : Effect()
    }
}
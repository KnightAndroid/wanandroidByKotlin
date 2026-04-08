package com.knight.kotlin.module_realtime.contract

import com.core.library_base.vm.ViewEvent
import com.core.library_base.vm.ViewSideEffect
import com.core.library_base.vm.ViewState
import com.knight.kotlin.library_base.entity.BaiduContent


/**
 * @author created by luguian
 * @organize
 * @Date 2026/4/8 17:05
 * @descript:热搜main 契约
 */
class RealTimeMainContract {

    sealed class Event : ViewEvent {
        object Init : Event()
    }

    data class State(
        val isLoading: Boolean = false,
        val hotList: List<BaiduContent> = emptyList(),
        val novelList: List<BaiduContent> = emptyList(),
        val movieList: List<BaiduContent> = emptyList(),
        val isError: Boolean = false
    ) : ViewState

    sealed class Effect : ViewSideEffect
}
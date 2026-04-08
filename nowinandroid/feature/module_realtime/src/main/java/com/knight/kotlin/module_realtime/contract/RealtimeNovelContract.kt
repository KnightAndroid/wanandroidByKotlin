package com.knight.kotlin.module_realtime.contract

import com.core.library_base.vm.ViewEvent
import com.core.library_base.vm.ViewSideEffect
import com.core.library_base.vm.ViewState


/**
 * @author created by luguian
 * @organize
 * @Date 2026/4/8 17:45
 * @descript:小说契约
 */
class RealtimeNovelContract {

    sealed class Event : ViewEvent {
        object Init : Event()
    }

    data class State(
        val isLoading: Boolean = false,
        val tabs: List<String> = emptyList()
    ) : ViewState

    sealed class Effect : ViewSideEffect
}
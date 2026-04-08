package com.knight.kotlin.module_realtime.contract

import com.core.library_base.vm.ViewEvent
import com.core.library_base.vm.ViewSideEffect
import com.core.library_base.vm.ViewState
import com.knight.kotlin.library_base.entity.BaiduContent


/**
 * @author created by luguian
 * @organize
 * @Date 2026/4/8 17:36
 * @descript:百度热搜电影子页面契约
 */
class RealtimeChildNovelContract {

    sealed class Event : ViewEvent {
        data class Init(val category: String) : Event()
    }

    data class State(
        val isLoading: Boolean = false,
        val list: List<BaiduContent> = emptyList(),
        val isEmpty: Boolean = false
    ) : ViewState

    sealed class Effect : ViewSideEffect
}
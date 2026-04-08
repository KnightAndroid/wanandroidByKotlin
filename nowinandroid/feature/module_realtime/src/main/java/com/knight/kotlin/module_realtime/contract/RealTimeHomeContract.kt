package com.knight.kotlin.module_realtime.contract

import com.core.library_base.vm.ViewEvent
import com.core.library_base.vm.ViewSideEffect
import com.core.library_base.vm.ViewState
import com.knight.kotlin.library_base.entity.BaiduTabBoard


/**
 * @author created by luguian
 * @organize
 * @Date 2026/4/8 16:49
 * @descript:百度热搜home页面契约
 */
class RealTimeHomeContract {

    sealed class Event : ViewEvent {
        object Init : Event()
    }

    data class State(
        val isLoading: Boolean = false,
        val tabList: List<BaiduTabBoard> = emptyList(),
        val isError: Boolean = false
    ) : ViewState

    sealed class Effect : ViewSideEffect
}
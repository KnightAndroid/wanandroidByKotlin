package com.knight.kotlin.module_realtime.contract

import com.core.library_base.vm.ViewEvent
import com.core.library_base.vm.ViewSideEffect
import com.core.library_base.vm.ViewState
import com.knight.kotlin.library_base.entity.BaiduContent


/**
 * @author created by luguian
 * @organize
 * @Date 2026/4/8 17:50
 * @descript:电视剧契约
 */
class RealtimeTeleplayContract {

    sealed class Event : ViewEvent {
        object Init : Event()
        data class FilterChanged(val category: String, val country: String) : Event()
    }

    data class State(
        val isLoading: Boolean = false,
        val categoryList: List<String> = emptyList(),
        val countryList: List<String> = emptyList(),
        val list: List<BaiduContent> = emptyList(),
        val isEmpty: Boolean = false
    ) : ViewState

    sealed class Effect : ViewSideEffect
}
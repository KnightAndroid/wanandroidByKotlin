package com.knight.kotlin.module_realtime.contract

import com.core.library_base.vm.ViewEvent
import com.core.library_base.vm.ViewSideEffect
import com.core.library_base.vm.ViewState
import com.knight.kotlin.library_base.entity.BaiduContent


/**
 * @author created by luguian
 * @organize
 * @Date 2026/4/8 15:39
 * @descript:汽车榜契约
 */
class RealTimeCarContract {

    sealed class Event : ViewEvent {
        data class LoadData(val category: String) : Event()
        data class ChangeCategory(val category: String) : Event()
    }

    data class State(
        val isLoading: Boolean = false,
        val categoryList: List<String> = emptyList(),
        val carList: List<BaiduContent> = emptyList(),
        val isEmpty: Boolean = false
    ) : ViewState

    sealed class Effect : ViewSideEffect
}
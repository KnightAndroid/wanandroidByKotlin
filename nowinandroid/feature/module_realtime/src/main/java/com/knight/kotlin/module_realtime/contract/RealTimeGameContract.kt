package com.knight.kotlin.module_realtime.contract

import com.core.library_base.vm.ViewEvent
import com.core.library_base.vm.ViewSideEffect
import com.core.library_base.vm.ViewState
import com.knight.kotlin.library_base.entity.BaiduContent


/**
 * @author created by luguian
 * @organize
 * @Date 2026/4/8 16:10
 * @descript:游戏界面契约
 */
class RealTimeGameContract {

    sealed class Event : ViewEvent {
        object Init : Event()
        data class SelectCategory(val category: String) : Event()
        object Retry : Event()
    }

    data class State(
        val isLoading: Boolean = false,
        val isError: Boolean = false, // ✅ 新增
        val categoryList: List<String> = emptyList(),
        val gameList: List<BaiduContent> = emptyList(),
        val currentCategory: String = "全部类型", // ✅ 新增
        val isEmpty: Boolean = false
    ) : ViewState

    sealed class Effect : ViewSideEffect {
        data class ShowToast(val msg: String) : Effect()
    }
}
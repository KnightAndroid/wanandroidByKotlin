package com.knight.kotlin.module_utils.contract

import com.core.library_base.vm.ViewEvent
import com.core.library_base.vm.ViewSideEffect
import com.core.library_base.vm.ViewState
import com.knight.kotlin.module_utils.entity.UtilsEntity

/**
 * @Description 工具类页面契约 用户意图 事件 状态整合
 * @Author knight
 * @Time 2026/2/26 21:50
 *
 */

/**
 * Utils 页面契约
 */
class UtilsContract {

    /** 用户意图 */
    sealed class Event : ViewEvent {
        object LoadUtils : Event()
    }

    /** 页面唯一状态 */
    data class State(
        val isLoading: Boolean = false,
        val utils: List<UtilsEntity> = emptyList(),
        val error: String? = null
    ) : ViewState

    /** 一次性副作用 */
    sealed class Effect : ViewSideEffect {
        data class ShowError(val message: String) : Effect()
    }
}
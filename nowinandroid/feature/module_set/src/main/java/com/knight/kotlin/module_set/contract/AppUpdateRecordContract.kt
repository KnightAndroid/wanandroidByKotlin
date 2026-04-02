package com.knight.kotlin.module_set.contract

import com.core.library_base.vm.ViewEvent
import com.core.library_base.vm.ViewSideEffect
import com.core.library_base.vm.ViewState
import com.knight.kotlin.module_set.entity.VersionRecordListEntity


/**
 * @author created by luguian
 * @organize
 * @Date 2026/4/2 15:42
 * @descript:版本更新记录契约
 */
class AppUpdateRecordContract {

    /** ========= Event ========= */
    sealed class Event : ViewEvent {
        object LoadData : Event()
    }

    /** ========= State ========= */
    data class State(
        val isLoading: Boolean = false,
        val listData: VersionRecordListEntity? = null
    ) : ViewState

    /** ========= Effect ========= */
    sealed class Effect : ViewSideEffect {
        data class ShowError(val msg: String) : Effect()
    }
}
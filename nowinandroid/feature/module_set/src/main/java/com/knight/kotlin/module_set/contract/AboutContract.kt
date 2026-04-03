package com.knight.kotlin.module_set.contract

import com.core.library_base.vm.ViewEvent
import com.core.library_base.vm.ViewSideEffect
import com.core.library_base.vm.ViewState
import com.knight.kotlin.library_base.entity.AppUpdateBean


/**
 * @author created by luguian
 * @organize
 * @Date 2026/4/2 15:24
 * @descript:关于页面契约
 */
class AboutContract {

    /** ========= Event ========= */
    sealed class Event : ViewEvent {
        data class CheckAppUpdate(
            val currentVersionCode: Long,
            val currentVersionName: String
        ) : Event()
    }

    /** ========= State ========= */
    data class State(
        val isLoading: Boolean = false
    ) : ViewState

    /** ========= Effect ========= */
    sealed class Effect : ViewSideEffect {

        data class ShowUpdateDialog(val data: AppUpdateBean) : Effect()

        object ShowNoUpdateToast : Effect()

        data class ShowError(val msg: String) : Effect()
    }
}
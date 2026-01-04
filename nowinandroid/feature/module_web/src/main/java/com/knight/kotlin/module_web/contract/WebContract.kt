package com.knight.kotlin.module_web.contract

import com.core.library_base.vm.ViewEvent
import com.core.library_base.vm.ViewSideEffect
import com.core.library_base.vm.ViewState

/**
 * @Description
 * @Author knight
 * @Time 2025/12/23 21:05
 *
 */

object WebContract {

    // ========================
    // Event
    // ========================
    sealed class Event : ViewEvent {

        data class CollectArticle(
            val articleId: Int
        ) : Event()
    }

    // ========================
    // State
    // ========================
    data class State(
        val placeholder: Boolean = false
    ) : ViewState

    // ========================
    // Effect
    // ========================
    sealed class Effect : ViewSideEffect {

        object CollectSuccess : Effect()

        data class ShowToast(
            val msg: String
        ) : Effect()
    }
}
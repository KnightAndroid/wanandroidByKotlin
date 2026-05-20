package com.knight.kotlin.module_navigate.contract

import com.core.library_base.vm.ViewEvent
import com.core.library_base.vm.ViewSideEffect
import com.core.library_base.vm.ViewState
import com.knight.kotlin.module_navigate.entity.HierachyListEntity


/**
 * @author created by luguian
 * @organize
 * @Date 2026/5/20 16:17
 * @descript:
 */
class HierachyContract {

    sealed class Event : ViewEvent {

        /**
         * 加载数据
         */
        data object LoadData : Event()

        /**
         * 左侧点击
         */
        data class SelectLeftTab(
            val position: Int
        ) : Event()
    }

    data class State(

        /**
         * loading
         */
        val isLoading: Boolean = false,

        /**
         * 数据
         */
        val list: List<HierachyListEntity> = emptyList(),

        /**
         * 当前选中位置
         */
        val currentPosition: Int = 0

    ) : ViewState

    sealed class Effect : ViewSideEffect {

        data class ShowToast(
            val msg: String
        ) : Effect()
    }
}
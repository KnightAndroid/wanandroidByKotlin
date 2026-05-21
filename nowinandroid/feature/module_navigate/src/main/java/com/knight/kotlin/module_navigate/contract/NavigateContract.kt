package com.knight.kotlin.module_navigate.contract

import com.core.library_base.vm.ViewEvent
import com.core.library_base.vm.ViewSideEffect
import com.core.library_base.vm.ViewState
import com.knight.kotlin.module_navigate.entity.NavigateListEntity


/**
 * @author created by luguian
 * @organize
 * @Date 2026/5/21 15:15
 * @descript:导航页面 Contract
 */
class NavigateContract {


    sealed class Event : ViewEvent {

        /**
         * 加载数据
         */
        data object LoadData : Event()

        /**
         * 左侧 tab 选中
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
         * 导航数据
         */
        val list: List<NavigateListEntity> = emptyList(),

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
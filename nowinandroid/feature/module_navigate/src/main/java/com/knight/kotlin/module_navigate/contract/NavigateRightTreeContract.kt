package com.knight.kotlin.module_navigate.contract

import com.core.library_base.vm.ViewEvent
import com.core.library_base.vm.ViewSideEffect
import com.core.library_base.vm.ViewState
import com.knight.kotlin.module_navigate.entity.HierachyListEntity
import com.knight.kotlin.module_navigate.entity.NavigateListEntity


/**
 * @author created by luguian
 * @organize
 * @Date 2026/5/21 16:19
 * @descript:
 */
class NavigateRightTreeContract {

    sealed class Event : ViewEvent {

        /**
         * 加载数据
         */
        data class LoadData(
            val isNavigate: Boolean
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
        val navigateList: List<NavigateListEntity> = emptyList(),

        /**
         * 体系数据
         */
        val hierachyList: List<HierachyListEntity> = emptyList()

    ) : ViewState

    sealed class Effect : ViewSideEffect {

        data class ShowToast(
            val msg: String
        ) : Effect()
    }

}
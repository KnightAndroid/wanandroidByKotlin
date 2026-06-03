package com.knight.kotlin.module_mine.contract

import com.core.library_base.vm.ViewEvent
import com.core.library_base.vm.ViewSideEffect
import com.core.library_base.vm.ViewState
import com.knight.kotlin.module_mine.entity.MyDetailCoinListEntity


/**
 * @author created by luguian
 * @organize
 * @Date 2026/6/3 15:45
 * @descript:我的积分契约
 */
class MyDetailCoinsContract {

    /**
     * 用户事件
     */
    sealed class Event : ViewEvent {

        /**
         * 首次加载
         */
        data object LoadData : Event()

        /**
         * 下拉刷新
         */
        data object Refresh : Event()

        /**
         * 上拉加载更多
         */
        data object LoadMore : Event()
    }

    /**
     * 页面状态
     */
    data class State(

        /**
         * loading
         */
        val isLoading: Boolean = false,

        /**
         * 当前页
         */
        val page: Int = 1,

        /**
         * 当前返回数据
         */
        val coinData: MyDetailCoinListEntity? = null,

        /**
         * 是否没有更多
         */
        val noMoreData: Boolean = false

    ) : ViewState

    /**
     * 一次性事件
     */
    sealed class Effect : ViewSideEffect {

        data class ShowToast(
            val msg: String
        ) : Effect()
    }
}
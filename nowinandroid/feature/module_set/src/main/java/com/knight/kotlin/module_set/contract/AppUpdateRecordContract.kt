package com.knight.kotlin.module_set.contract

import com.core.library_base.vm.ViewEvent
import com.core.library_base.vm.ViewSideEffect
import com.core.library_base.vm.ViewState
import com.knight.kotlin.module_set.entity.VersionRecordEntity


/**
 * @author created by luguian
 * @organize
 * @Date 2026/4/2 15:42
 * @descript:版本更新记录契约
 */
class AppUpdateRecordContract {

    /** ========= Event ========= */
    sealed class Event : ViewEvent {

        /** 首次加载 / 下拉刷新 */
        object LoadData : Event()

        /** 手动刷新（可扩展） */
        object Refresh : Event()
    }

    /** ========= State ========= */
    data class State(

        /** 页面是否首次 loading（LoadSir 用） */
        val isLoading: Boolean = false,

        /** 是否正在下拉刷新 */
        val isRefreshing: Boolean = false,

        /** 列表数据（直接给UI用，避免嵌套） */
        val list: List<VersionRecordEntity> = emptyList(),

        /** 是否为空页面 */
        val isEmpty: Boolean = false

    ) : ViewState

    /** ========= Effect ========= */
    sealed class Effect : ViewSideEffect {

        /** 停止刷新（一次性行为） */
        object StopRefresh : Effect()

        /** 错误提示 */
        data class ShowError(val msg: String) : Effect()
    }
}
package com.knight.kotlin.module_set.contract

import com.core.library_base.vm.ViewEvent
import com.core.library_base.vm.ViewSideEffect
import com.core.library_base.vm.ViewState


/**
 * @author created by luguian
 * @organize
 * @Date 2026/4/2 16:50
 * @descript:设置页面契约
 */
class SetContract {

    /** ========= Event ========= */
    sealed class Event : ViewEvent {

        /** 退出登录 */
        object Logout : Event()

        /** 清缓存 */
        object ClearCache : Event()

        /** 修改主题 */
        data class ChangeTheme(val color: Int) : Event()
    }

    /** ========= State ========= */
    data class State(

        /** loading */
        val isLoading: Boolean = false,

        /** 是否登录 */
        val isLogin: Boolean = false,

        /** 缓存大小 */
        val cacheSize: String = ""

    ) : ViewState

    /** ========= Effect ========= */
    sealed class Effect : ViewSideEffect {

        /** 退出成功 */
        object LogoutSuccess : Effect()

        /** 清缓存完成 */
        object CacheCleared : Effect()

        /** 主题改变 */
        object ThemeChanged : Effect()

        /** 错误提示 */
        data class ShowError(val msg: String) : Effect()
    }
}
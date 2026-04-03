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
/**
 * @author Knight
 * @descript 设置页面契约（MVI）
 */
class SetContract {

    /**
     * ===================== State（页面状态） =====================
     *
     * 👉 原则：
     * UI = f(State)
     */
    data class State(
        /** 是否加载中（可扩展用） */
        val isLoading: Boolean = false,

        /** 登录状态 */
        val isLogin: Boolean = false,

        /** 缓存大小（由 UI 计算后通过 Event 注入） */
        val cacheSize: String = "",

        /** 当前主题色（核心状态） */
        val themeColor: Int = 0,

        /** 护眼模式 */
        val isEyeCare: Boolean = false,

        /** 状态栏是否跟随主题 */
        val isStatusWithTheme: Boolean = false
    ) : ViewState


    /**
     * ===================== Event（用户行为） =====================
     *
     * 👉 原则：
     * 所有 UI 行为必须通过 Event 进入
     */
    sealed class Event : ViewEvent {

        /** 初始化 */
        object Init : Event()

        /** 退出登录 */
        object Logout : Event()

        /** 护眼模式切换 */
        data class ChangeEyeCare(val enable: Boolean) : Event()

        /** 状态栏主题切换 */
        data class ChangeStatusTheme(val enable: Boolean) : Event()

        /** 主题色变更 */
        data class ChangeThemeColor(val color: Int) : Event()

        /** 缓存大小更新（UI计算后传入） */
        data class UpdateCacheSize(val size: String) : Event()

        /**
         * ⭐（可选扩展）
         * 刷新全部状态（例如从其他页面回来）
         */
        object Refresh : Event()
    }


    /**
     * ===================== Effect（一次性行为） =====================
     *
     * 👉 原则：
     * 不进入 State 的 UI 行为（Toast / 导航 / Dialog）
     */
    sealed class Effect : ViewSideEffect {

        /** 显示 loading */
        object ShowLoading : Effect()

        /** 隐藏 loading */
        object HideLoading : Effect()

        /** 退出登录成功 */
        object LogoutSuccess : Effect()

        /** Toast 提示 */
        data class ShowToast(val msg: String) : Effect()
    }
}
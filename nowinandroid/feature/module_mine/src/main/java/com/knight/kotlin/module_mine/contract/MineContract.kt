package com.knight.kotlin.module_mine.contract

import com.core.library_base.vm.ViewEvent
import com.core.library_base.vm.ViewSideEffect
import com.core.library_base.vm.ViewState
import com.knight.kotlin.library_common.entity.UserInfoEntity
import com.knight.kotlin.module_mine.entity.UserInfoMessageEntity


/**
 * @author created by luguian
 * @organize
 * @Date 2026/5/22 17:25
 * @descript:
 */
class MineContract {

    /**
     * =========================
     * Event
     * 用户主动触发的行为
     * =========================
     */
    sealed class Event : ViewEvent {

        /**
         * 初始化页面
         */
        data object Init : Event()

        /**
         * 刷新用户信息
         */
        data object RefreshUserInfo : Event()

        /**
         * 登录
         */
        data class Login(
            val userName: String,
            val passWord: String
        ) : Event()

        /**
         * 退出登录
         */
        data object Logout : Event()
    }

    /**
     * =========================
     * State
     * 页面UI状态
     * =========================
     */
    data class State(

        /**
         * 是否加载中
         */
        val isLoading: Boolean = false,

        /**
         * 当前登录用户信息
         */
        val userInfo: UserInfoEntity? = null,

        /**
         * 用户积分/等级/排名
         */
        val userCoinInfo: UserInfoMessageEntity? = null,

        /**
         * 是否登录
         */
        val isLogin: Boolean = false,

        /**
         * 错误信息
         */
        val errorMsg: String? = null

    ) : ViewState

    /**
     * =========================
     * Effect
     * 一次性事件
     * =========================
     */
    sealed class Effect : ViewSideEffect {

        /**
         * Toast提示
         */
        data class ShowToast(
            val msg: String
        ) : Effect()

        /**
         * 登录成功
         */
        data object LoginSuccess : Effect()

        /**
         * 登录失败
         */
        data class LoginFailure(
            val msg: String?
        ) : Effect()

        /**
         * 退出登录成功
         */
        data object LogoutSuccess : Effect()

        /**
         * 请求失败
         */
        data class RequestFailure(
            val msg: String?
        ) : Effect()
    }

}
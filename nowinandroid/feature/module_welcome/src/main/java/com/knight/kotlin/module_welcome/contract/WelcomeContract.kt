package com.knight.kotlin.module_welcome.contract

import com.core.library_base.vm.ViewEvent
import com.core.library_base.vm.ViewSideEffect
import com.core.library_base.vm.ViewState
import com.knight.kotlin.library_common.entity.AppThemeBean


/**
 * @author created by luguian
 * @organize
 * @Date 2025/12/22 9:40
 * @descript:启动页契约 用户意图 事件 状态整合
 */
class WelcomeContract {

    /** 用户意图 */
    sealed class Event : ViewEvent {
        object LoadAppTheme : Event()
        object LogoAnimFinished : Event()
    }

    /** 页面唯一状态
     *
     *
     * */
    data class State(
        val theme: AppThemeBean? = null
    ) : ViewState

    /** 一次性副作用 */
    sealed class Effect : ViewSideEffect {
        object GoMain : Effect()
        object ShowPrivacyDialog : Effect()
        data class ShowError(val msg: String) : Effect()
    }
}
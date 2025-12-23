package com.core.library_base.contact

import com.core.library_base.vm.ViewEvent
import com.core.library_base.vm.ViewSideEffect
import com.core.library_base.vm.ViewState


/**
 * @author created by luguian
 * @organize
 * @Date 2025/12/23 14:50
 * @descript:
 */
/**
 * 空 MVI Contract
 * 用于「纯 UI 容器页面 / 占位页面 / 路由页面」
 */
object EmptyContract {

    object Event : ViewEvent

    object State : ViewState

    object Effect : ViewSideEffect
}
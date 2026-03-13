package com.knight.kotlin.module_square.contract

import com.core.library_base.vm.ViewEvent
import com.core.library_base.vm.ViewSideEffect
import com.core.library_base.vm.ViewState


/**
 * @author created by luguian
 * @organize
 * @Date 2026/3/13 15:39
 * @descript:分享文章契约
 */
class SquareShareArticleContract {

    /** 用户行为 */
    sealed class Event : ViewEvent {

        data class ShareArticle(
            val title: String,
            val link: String
        ) : Event()
    }

    /** 页面状态 */
    data class State(
        val loading: Boolean = false
    ) : ViewState

    /** 一次性事件 */
    sealed class Effect : ViewSideEffect {

        object ShareSuccess : Effect()

        data class ShowError(val msg: String) : Effect()
    }
}
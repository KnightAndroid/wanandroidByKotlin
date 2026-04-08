package com.knight.kotlin.module_realtime.contract

import android.graphics.Color
import com.core.library_base.vm.ViewEvent
import com.core.library_base.vm.ViewSideEffect
import com.core.library_base.vm.ViewState


/**
 * @author created by luguian
 * @organize
 * @Date 2026/4/8 14:28
 * @descript:热搜主界面契约
 */
class RealTimeContract {


    sealed class Event : ViewEvent {
        data class OnAppBarOffsetChanged(
            val offset: Int,
            val orientation: Int
        ) : Event()
    }

    data class State(
        val alpha: Int = 0,
        val backgroundColor: Int = Color.TRANSPARENT,
        val iconColor: Int = Color.WHITE,
        val titleAlpha: Float = 0f
    ) : ViewState

    sealed class Effect : ViewSideEffect
}
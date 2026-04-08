package com.knight.kotlin.module_realtime.vm

import android.graphics.Color
import com.core.library_base.vm.BaseMviViewModel
import com.core.library_common.util.dp2px
import com.knight.kotlin.library_common.util.CacheUtils
import com.knight.kotlin.module_realtime.contract.RealTimeContract
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import android.animation.ArgbEvaluator
/**
 * @author created by luguian
 * @organize
 * @Date 2026/4/8 15:04
 * @descript:热搜主页面vm
 */
@HiltViewModel
class RealTimeViewModel @Inject constructor() : BaseMviViewModel<
        RealTimeContract.Event,
        RealTimeContract.State,
        RealTimeContract.Effect>() {

    override fun initialState(): RealTimeContract.State = RealTimeContract.State()

    override fun handleEvent(event: RealTimeContract.Event) {
        when (event) {
            is RealTimeContract.Event.OnAppBarOffsetChanged -> {
                handleOffsetChanged(event)
            }
        }
    }

    private fun handleOffsetChanged(event: RealTimeContract.Event.OnAppBarOffsetChanged) {
        val mHeight = 320f.dp2px()
        val mOffsetY = kotlin.math.abs(event.offset).toFloat()
        val scale = (mOffsetY / mHeight).coerceAtMost(1f)
        val alpha = (scale * 255).toInt()

        val isDark = CacheUtils.getNormalDark()

        val bgColor = if (isDark) {
            Color.argb(alpha, 186, 5, 2)
        } else {
            Color.argb(alpha, 254, 79, 76)
        }

        val iconColor = ArgbEvaluator().evaluate(
            scale,
            Color.WHITE,
            Color.argb(255, 34, 22, 19)
        ) as Int

        setState {
            copy(
                alpha = alpha,
                backgroundColor = bgColor,
                iconColor = iconColor,
                titleAlpha = if (event.orientation == 0) 0f else alpha / 255f
            )
        }
    }
}
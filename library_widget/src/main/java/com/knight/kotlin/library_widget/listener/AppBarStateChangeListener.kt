package com.knight.kotlin.library_widget.listener

import com.google.android.material.appbar.AppBarLayout
import com.google.android.material.appbar.AppBarLayout.OnOffsetChangedListener
import kotlin.math.abs


/**
 * @author created by luguian
 * @organize
 * @Date 2024/8/16 10:25
 * @descript:
 */
abstract class AppBarStateChangeListener : OnOffsetChangedListener {
    enum class State {
        EXPANDED,//展开
        COLLAPSED,//折叠
        IDLE //中间
    }

    private var mCurrentState = State.IDLE

    override fun onOffsetChanged(appBarLayout: AppBarLayout, i: Int) {
        if (i == 0) {
            if (mCurrentState != State.EXPANDED) {
                onStateChanged(appBarLayout, State.EXPANDED)
            }
            mCurrentState = State.EXPANDED
        } else if (abs(i.toDouble()) >= appBarLayout.totalScrollRange) {
            if (mCurrentState != State.COLLAPSED) {
                onStateChanged(appBarLayout, State.COLLAPSED)
            }
            mCurrentState = State.COLLAPSED
        } else {
            if (mCurrentState != State.IDLE) {
                onStateChanged(appBarLayout, State.IDLE)
            }
            mCurrentState = State.IDLE
        }
    }

    abstract fun onStateChanged(appBarLayout: AppBarLayout?, state: State?)
}
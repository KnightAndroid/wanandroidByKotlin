package com.knight.kotlin.library_widget.citypicker

import androidx.annotation.IntDef


/**
 * @author created by luguian
 * @organize
 * @Date 2025/4/17 10:56
 * @descript:
 */
@Retention(AnnotationRetention.SOURCE)
@IntDef(LocateState.SUCCESS, LocateState.FAILURE)
annotation class State

object LocateState {
    const val LOCATING = 123
    const val SUCCESS = 132
    const val FAILURE = 321
}
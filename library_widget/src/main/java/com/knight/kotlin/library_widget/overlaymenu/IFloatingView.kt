package com.knight.kotlin.library_widget.overlaymenu

import android.app.Activity
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.annotation.DrawableRes
import androidx.annotation.LayoutRes


/**
 * @author created by luguian
 * @organize
 * @Date 2025/4/23 14:47
 * @descript:
 */
interface IFloatingView {
    fun remove(): FloatingView

    fun add(): FloatingView

    fun attach(activity: Activity): FloatingView

    fun attach(container: FrameLayout?): FloatingView

    fun detach(activity: Activity): FloatingView

    fun detach(container: FrameLayout?): FloatingView

    fun getView(): FloatingMagnetView?

    fun icon(@DrawableRes resId: Int): FloatingView

    fun customView(viewGroup: FloatingMagnetView): FloatingView

    fun customView(@LayoutRes resource: Int): FloatingView

    fun layoutParams(params: ViewGroup.LayoutParams): FloatingView

    fun listener(magnetViewListener: MagnetViewListener?): FloatingView

    fun dragEnable(dragEnable: Boolean): FloatingView

    fun setAutoMoveToEdge(autoMoveToEdge: Boolean): FloatingView
}
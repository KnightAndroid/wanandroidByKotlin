package com.knight.kotlin.library_widget.skeleton

/**
 * Author:Knight
 * Time:2022/3/31 15:47
 * Description:SkeletonScreen
 */
interface SkeletonScreen {
    fun show()

    fun hide()

    fun delayHide(delayMillis: Int)
}
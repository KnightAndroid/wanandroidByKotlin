package com.knight.kotlin.module_home.view

import android.content.Context
import net.lucode.hackware.magicindicator.buildins.commonnavigator.titles.ColorTransitionPagerTitleView

/**
 * Author:Knight
 * Time:2022/1/28 15:22
 * Description:ScaleTransitionPagerTitleView
 */
class ScaleTransitionPagerTitleView constructor(context: Context):ColorTransitionPagerTitleView(context) {
    private val minScale = 0.8f

    override fun onEnter(index: Int, totalCount: Int, enterPercent: Float, leftToRight: Boolean) {
        super.onEnter(index, totalCount, enterPercent, leftToRight)
        this.scaleX = minScale + (1.0f - minScale) * enterPercent
        this.scaleY = minScale + (1.0f - minScale) * enterPercent
    }

    override fun onLeave(index: Int, totalCount: Int, leavePercent: Float, leftToRight: Boolean) {
        super.onLeave(index, totalCount, leavePercent, leftToRight)
        this.scaleX = 1.0f + (minScale - 1.0f) * leavePercent
        this.scaleY = 1.0f + (minScale - 1.0f) * leavePercent
    }
}
package com.knight.kotlin.library_widget

import android.content.Context
import net.lucode.hackware.magicindicator.buildins.commonnavigator.titles.ColorTransitionPagerTitleView

/**
 *
 * @ProjectName:    wanandroid
 * @Package:        com.knight.kotlin.library_widget
 * @ClassName:      ScaleTransitionPagerTitleView
 * @Description:    java类作用描述
 * @Author:         knight
 * @CreateDate:     2022/3/22 9:58 下午
 * @UpdateUser:     更新者
 * @UpdateDate:     2022/3/22 9:58 下午
 * @UpdateRemark:   更新说明
 * @Version:        1.0
 */

class ScaleTransitionPagerTitleView : ColorTransitionPagerTitleView{

    private val minScale = 0.8f

    @JvmOverloads
    constructor(context: Context): super(context){

    }

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
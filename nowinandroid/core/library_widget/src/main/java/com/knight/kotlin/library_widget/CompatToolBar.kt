package com.knight.kotlin.library_widget

import android.content.Context
import android.content.res.Resources
import android.os.Build
import android.util.AttributeSet
import android.widget.Toolbar

/**
 * Author:Knight
 * Time:2021/12/24 16:08
 * Description:CompatToolBar
 */
class CompatToolBar : Toolbar {

    @JvmOverloads
    constructor(context: Context, attributeSet: AttributeSet? = null, defAttrStyle: Int = 0)
            : super(context, attributeSet, defAttrStyle) {
        setUp()
    }

    private fun setUp() {
        var compatPaddingTop = 0
        // android 4.4以上将Toolbar添加状态栏高度的上边距，沉浸到状态栏下方
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            compatPaddingTop = getStatusBarHeight()
        }
        this.setPadding(
            getPaddingLeft(),
            dp2px(10f) + compatPaddingTop,
            getPaddingRight(),
            getPaddingBottom() + compatPaddingTop / 2
        );
    }

    private fun getStatusBarHeight(): Int {
        var result = 0
        try {
            val resourceId: Int =
                Resources.getSystem().getIdentifier("status_bar_height", "dimen", "android")
            if (resourceId > 0) {
                result = Resources.getSystem().getDimensionPixelSize(resourceId)
            }
        } catch (e: Resources.NotFoundException) {
            e.printStackTrace()
        }
        return result
    }

    private fun dp2px(dpValue: Float): Int {
        val scale = Resources.getSystem().displayMetrics.density
        return (dpValue * scale + 0.5f).toInt()
    }

}
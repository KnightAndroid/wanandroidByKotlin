package com.knight.kotlin.library_scan.widget

import android.animation.ValueAnimator
import android.content.Context
import android.util.AttributeSet
import android.view.View

/**
 * Author:Knight
 * Time:2022/2/11 17:14
 * Description:BaseScanView
 */
open class BaseScanView: View {


    protected var valueAnimator:ValueAnimator?=null
    @JvmOverloads
    constructor(context: Context, attributeSet: AttributeSet? = null, defAttrStyle: Int = 0)
            : super(context, attributeSet, defAttrStyle) {

    }


    open fun startAnim(){}

    open fun cancelAnim(){}
}
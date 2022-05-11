package com.knight.kotlin.library_widget

import android.animation.ObjectAnimator
import android.content.Context
import android.text.TextUtils
import android.util.AttributeSet
import android.view.animation.AccelerateDecelerateInterpolator
import androidx.appcompat.widget.AppCompatTextView

/**
 * Author:Knight
 * Time:2022/5/11 10:38
 * Description:CountNumberView
 */
class CountNumberView:AppCompatTextView {
    /**动画时长 */
    private var duration = 1800

    /**显示数字 */
    private var number = 0f

    /**显示表达式 */
    private lateinit var regex: String

    companion object {
        /**不保留小数，整数 */
        val INTREGEX = "%1$01.0f"

        /**保留2位小数 */
        val FLOATREGEX = "%1$01.2f"
    }


    @JvmOverloads
    constructor(context: Context, attributeSet: AttributeSet? = null)
            : super(context, attributeSet) {
    }

    /**
     * 显示带有动画效果的数字
     * @param number
     * @param regex
     */
    fun showNumberWithAnimation(number: Float, regex: String) {
        if (TextUtils.isEmpty(regex)) {
            //默认为整数
            this.regex = INTREGEX
        } else {
            this.regex = regex
        }
        //修改number属性，会调用setNumber方法
        val objectAnimator = ObjectAnimator.ofFloat(this, "number", 0f, number)
        objectAnimator.duration = duration.toLong()
        //加速器，从慢到快到再到慢
        objectAnimator.interpolator = AccelerateDecelerateInterpolator()
        objectAnimator.start()
    }

    /**
     * 获取当前数字
     * @return
     */
    fun getNumber(): Float {
        return number
    }

    /**
     * 根据正则表达式，显示对应数字样式
     * @param number
     */
    fun setNumber(number: Float) {
        this.number = number
        text = String.format(regex, number)
    }
}
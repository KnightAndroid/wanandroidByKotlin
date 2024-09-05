package com.knight.kotlin.library_widget.shadeview.config


/**
 * @author created by luguian
 * @organize
 * @Date 2024/9/5 9:12
 * @descript:CompoundButton View 属性收集接口
 */
interface ICompoundButtonStyleable {
    fun getButtonDrawableStyleable(): Int

    fun getButtonPressedDrawableStyleable(): Int

    fun getButtonCheckedDrawableStyleable(): Int

    fun getButtonDisabledDrawableStyleable(): Int

    fun getButtonFocusedDrawableStyleable(): Int

    fun getButtonSelectedDrawableStyleable(): Int
}
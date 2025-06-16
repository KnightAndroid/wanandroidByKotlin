package com.knight.kotlin.library_widget.shadeview.config


/**
 * @author created by luguian
 * @organize
 * @Date 2024/9/5 9:32
 * @descript:文本颜色 View 属性收集接口
 */
interface ITextColorStyleable {
    fun getTextColorStyleable(): Int

    fun getTextPressedColorStyleable(): Int

    fun getTextCheckedColorStyleable(): Int {
        return 0
    }

    fun getTextDisabledColorStyleable(): Int

    fun getTextFocusedColorStyleable(): Int

    fun getTextSelectedColorStyleable(): Int

    fun getTextStartColorStyleable(): Int

    fun getTextCenterColorStyleable(): Int

    fun getTextEndColorStyleable(): Int

    fun getTextGradientOrientationStyleable(): Int

    fun getTextStrokeColorStyleable(): Int

    fun getTextStrokeSizeStyleable(): Int
}
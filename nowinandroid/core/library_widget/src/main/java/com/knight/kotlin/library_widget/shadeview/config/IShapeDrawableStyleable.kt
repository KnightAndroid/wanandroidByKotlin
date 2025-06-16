package com.knight.kotlin.library_widget.shadeview.config


/**
 * @author created by luguian
 * @organize
 * @Date 2024/9/5 9:30
 * @descript:ShapeDrawable View 属性收集接口
 */
interface IShapeDrawableStyleable {
    fun getShapeTypeStyleable(): Int

    fun getShapeWidthStyleable(): Int

    fun getShapeHeightStyleable(): Int

    fun getRadiusStyleable(): Int

    fun getRadiusInTopLeftStyleable(): Int

    fun getRadiusInTopStartStyleable(): Int

    fun getRadiusInTopRightStyleable(): Int

    fun getRadiusInTopEndStyleable(): Int

    fun getRadiusInBottomLeftStyleable(): Int

    fun getRadiusInBottomStartStyleable(): Int

    fun getRadiusInBottomRightStyleable(): Int

    fun getRadiusInBottomEndStyleable(): Int

    fun getSolidColorStyleable(): Int

    fun getSolidPressedColorStyleable(): Int

    fun getSolidCheckedColorStyleable(): Int {
        return 0
    }

    fun getSolidDisabledColorStyleable(): Int

    fun getSolidFocusedColorStyleable(): Int

    fun getSolidSelectedColorStyleable(): Int

    fun getSolidGradientStartColorStyleable(): Int

    fun getSolidGradientCenterColorStyleable(): Int

    fun getSolidGradientEndColorStyleable(): Int

    fun getSolidGradientOrientationStyleable(): Int

    fun getSolidGradientTypeStyleable(): Int

    fun getSolidGradientCenterXStyleable(): Int

    fun getSolidGradientCenterYStyleable(): Int

    fun getSolidGradientRadiusStyleable(): Int

    fun getStrokeColorStyleable(): Int

    fun getStrokePressedColorStyleable(): Int

    fun getStrokeCheckedColorStyleable(): Int {
        return 0
    }

    fun getStrokeDisabledColorStyleable(): Int

    fun getStrokeFocusedColorStyleable(): Int

    fun getStrokeSelectedColorStyleable(): Int

    fun getStrokeGradientStartColorStyleable(): Int

    fun getStrokeGradientCenterColorStyleable(): Int

    fun getStrokeGradientEndColorStyleable(): Int

    fun getStrokeGradientOrientationStyleable(): Int

    fun getStrokeSizeStyleable(): Int

    fun getStrokeDashSizeStyleable(): Int

    fun getStrokeDashGapStyleable(): Int

    fun getShadowSizeStyleable(): Int

    fun getShadowColorStyleable(): Int

    fun getShadowOffsetXStyleable(): Int

    fun getShadowOffsetYStyleable(): Int

    fun getRingInnerRadiusSizeStyleable(): Int

    fun getRingInnerRadiusRatioStyleable(): Int

    fun getRingThicknessSizeStyleable(): Int

    fun getRingThicknessRatioStyleable(): Int

    fun getLineGravityStyleable(): Int
}
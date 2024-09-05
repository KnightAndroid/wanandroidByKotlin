package com.knight.kotlin.library_widget.shadeview.styleable

import com.knight.kotlin.library_widget.R
import com.knight.kotlin.library_widget.shadeview.config.IShapeDrawableStyleable




/**
 * @author created by luguian
 * @organize
 * @Date 2024/9/5 11:06
 * @descript:View 的 Shape 属性值
 */
class ShapeViewStyleable : IShapeDrawableStyleable {
    /**
     * [IShapeDrawableStyleable]
     */
    override fun getShapeTypeStyleable(): Int {
        return R.styleable.ShapeView_shape_type
    }

    override fun getShapeWidthStyleable(): Int {
        return R.styleable.ShapeView_shape_width
    }

    override fun getShapeHeightStyleable(): Int {
        return R.styleable.ShapeView_shape_height
    }

    override fun getRadiusStyleable(): Int {
        return R.styleable.ShapeView_shape_radius
    }

    override fun getRadiusInTopLeftStyleable(): Int {
        return R.styleable.ShapeView_shape_radiusInTopLeft
    }

    override fun getRadiusInTopStartStyleable(): Int {
        return R.styleable.ShapeView_shape_radiusInTopStart
    }

    override fun getRadiusInTopRightStyleable(): Int {
        return R.styleable.ShapeView_shape_radiusInTopRight
    }

    override fun getRadiusInTopEndStyleable(): Int {
        return R.styleable.ShapeView_shape_radiusInTopEnd
    }

    override fun getRadiusInBottomLeftStyleable(): Int {
        return R.styleable.ShapeView_shape_radiusInBottomLeft
    }

    override fun getRadiusInBottomStartStyleable(): Int {
        return R.styleable.ShapeView_shape_radiusInBottomStart
    }

    override fun getRadiusInBottomRightStyleable(): Int {
        return R.styleable.ShapeView_shape_radiusInBottomRight
    }

    override fun getRadiusInBottomEndStyleable(): Int {
        return R.styleable.ShapeView_shape_radiusInBottomEnd
    }

    override fun getSolidColorStyleable(): Int {
        return R.styleable.ShapeView_shape_solidColor
    }

    override fun getSolidPressedColorStyleable(): Int {
        return R.styleable.ShapeView_shape_solidPressedColor
    }

    override fun getSolidDisabledColorStyleable(): Int {
        return R.styleable.ShapeView_shape_solidDisabledColor
    }

    override fun getSolidFocusedColorStyleable(): Int {
        return R.styleable.ShapeView_shape_solidFocusedColor
    }

    override fun getSolidSelectedColorStyleable(): Int {
        return R.styleable.ShapeView_shape_solidSelectedColor
    }

    override fun getSolidGradientStartColorStyleable(): Int {
        return R.styleable.ShapeView_shape_solidGradientStartColor
    }

    override fun getSolidGradientCenterColorStyleable(): Int {
        return R.styleable.ShapeView_shape_solidGradientCenterColor
    }

    override fun getSolidGradientEndColorStyleable(): Int {
        return R.styleable.ShapeView_shape_solidGradientEndColor
    }

    override fun getSolidGradientOrientationStyleable(): Int {
        return R.styleable.ShapeView_shape_solidGradientOrientation
    }

    override fun getSolidGradientTypeStyleable(): Int {
        return R.styleable.ShapeView_shape_solidGradientType
    }

    override fun getSolidGradientCenterXStyleable(): Int {
        return R.styleable.ShapeView_shape_solidGradientCenterX
    }

    override fun getSolidGradientCenterYStyleable(): Int {
        return R.styleable.ShapeView_shape_solidGradientCenterY
    }

    override fun getSolidGradientRadiusStyleable(): Int {
        return R.styleable.ShapeView_shape_solidGradientRadius
    }

    override fun getStrokeColorStyleable(): Int {
        return R.styleable.ShapeView_shape_strokeColor
    }

    override fun getStrokePressedColorStyleable(): Int {
        return R.styleable.ShapeView_shape_strokePressedColor
    }

    override fun getStrokeDisabledColorStyleable(): Int {
        return R.styleable.ShapeView_shape_strokeDisabledColor
    }

    override fun getStrokeFocusedColorStyleable(): Int {
        return R.styleable.ShapeView_shape_strokeFocusedColor
    }

    override fun getStrokeSelectedColorStyleable(): Int {
        return R.styleable.ShapeView_shape_strokeSelectedColor
    }

    override fun getStrokeGradientStartColorStyleable(): Int {
        return R.styleable.ShapeView_shape_strokeGradientStartColor
    }

    override fun getStrokeGradientCenterColorStyleable(): Int {
        return R.styleable.ShapeView_shape_strokeGradientCenterColor
    }

    override fun getStrokeGradientEndColorStyleable(): Int {
        return R.styleable.ShapeView_shape_strokeGradientColor
    }

    override fun getStrokeGradientOrientationStyleable(): Int {
        return R.styleable.ShapeView_shape_strokeGradientOrientation
    }

    override fun getStrokeSizeStyleable(): Int {
        return R.styleable.ShapeView_shape_strokeSize
    }

    override fun getStrokeDashSizeStyleable(): Int {
        return R.styleable.ShapeView_shape_strokeDashSize
    }

    override fun getStrokeDashGapStyleable(): Int {
        return R.styleable.ShapeView_shape_strokeDashGap
    }

    override fun getShadowSizeStyleable(): Int {
        return R.styleable.ShapeView_shape_shadowSize
    }

    override fun getShadowColorStyleable(): Int {
        return R.styleable.ShapeView_shape_shadowColor
    }

    override fun getShadowOffsetXStyleable(): Int {
        return R.styleable.ShapeView_shape_shadowOffsetX
    }

    override fun getShadowOffsetYStyleable(): Int {
        return R.styleable.ShapeView_shape_shadowOffsetY
    }

    override fun getRingInnerRadiusSizeStyleable(): Int {
        return R.styleable.ShapeView_shape_ringInnerRadiusSize
    }

    override fun getRingInnerRadiusRatioStyleable(): Int {
        return R.styleable.ShapeView_shape_ringInnerRadiusRatio
    }

    override fun getRingThicknessSizeStyleable(): Int {
        return R.styleable.ShapeView_shape_ringThicknessSize
    }

    override fun getRingThicknessRatioStyleable(): Int {
        return R.styleable.ShapeView_shape_ringThicknessRatio
    }

    override fun getLineGravityStyleable(): Int {
        return R.styleable.ShapeView_shape_lineGravity
    }
}
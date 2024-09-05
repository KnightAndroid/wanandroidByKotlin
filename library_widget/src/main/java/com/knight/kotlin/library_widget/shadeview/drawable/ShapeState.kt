package com.knight.kotlin.library_widget.shadeview.drawable

import android.content.res.Resources
import android.graphics.Rect
import android.graphics.drawable.Drawable
import android.graphics.drawable.Drawable.ConstantState
import android.view.Gravity


/**
 * @author created by luguian
 * @organize
 * @Date 2024/9/5 15:57
 * @descript:ShapeDrawable 参数构建
 */
class ShapeState : ConstantState {
    var shapeChangingConfigurations: Int = 0

    @ShapeTypeLimit
    var shapeType: Int = ShapeType.RECTANGLE

    @ShapeGradientTypeLimit
    var solidGradientType: Int = ShapeGradientType.LINEAR_GRADIENT
    var solidGradientOrientation: ShapeGradientOrientation = ShapeGradientOrientation.TOP_TO_BOTTOM
    var solidColors: IntArray? = intArrayOf()
    var strokeColors: IntArray? = intArrayOf()
    lateinit var tempSolidColors: IntArray // no need to copy
    lateinit var tempSolidPositions: FloatArray // no need to copy
    var positions: FloatArray? = null
    var hasSolidColor: Boolean = false
    var hasStrokeColor: Boolean = false
    var solidColor: Int = 0
    var strokeSize: Int = -1 // if >= 0 use stroking.
    var strokeGradientOrientation: ShapeGradientOrientation = ShapeGradientOrientation.TOP_TO_BOTTOM
    var strokeColor: Int = 0
    var strokeDashSize: Float = 0f
    var strokeDashGap: Float = 0f
    var radius: Float = 0f // use this if mRadiusArray is null
    var radiusArray: FloatArray? = null
    var padding: Rect? = null
    var width: Int = -1
    var height: Int = -1
    var ringInnerRadiusRatio: Float = 0f
    var ringThicknessRatio: Float = 0f
    var ringInnerRadiusSize: Int = -1
    var ringThicknessSize: Int = -1
    var solidCenterX: Float = 0.5f
    var solidCenterY: Float = 0.5f
    var gradientRadius: Float = 0.5f
    var useLevel: Boolean = false
    var useLevelForShape: Boolean = false
    var opaque: Boolean = false

    var shadowSize: Int = 0
    var shadowColor: Int = 0
    var shadowOffsetX: Int = 0
    var shadowOffsetY: Int = 0

    var lineGravity: Int = Gravity.CENTER

    constructor()

    constructor(state: ShapeState) {
        shapeChangingConfigurations = state.changingConfigurations
        shapeType = state.shapeType
        solidGradientType = state.solidGradientType
        solidGradientOrientation = state.solidGradientOrientation
        if (state.solidColors != null) {
            solidColors = state.solidColors!!.clone()
        }
        if (state.strokeColors != null) {
            strokeColors = state.strokeColors!!.clone()
        }
        if (state.positions != null) {
            positions = state.positions!!.clone()
        }
        hasSolidColor = state.hasSolidColor
        hasStrokeColor = state.hasStrokeColor
        solidColor = state.solidColor
        strokeSize = state.strokeSize
        strokeColor = state.strokeColor
        strokeDashSize = state.strokeDashSize
        strokeDashGap = state.strokeDashGap
        radius = state.radius
        if (state.radiusArray != null) {
            radiusArray = state.radiusArray!!.clone()
        }
        if (state.padding != null) {
            padding = Rect(state.padding)
        }
        width = state.width
        height = state.height
        ringInnerRadiusRatio = state.ringInnerRadiusRatio
        ringThicknessRatio = state.ringThicknessRatio
        ringInnerRadiusSize = state.ringInnerRadiusSize
        ringThicknessSize = state.ringThicknessSize
        solidCenterX = state.solidCenterX
        solidCenterY = state.solidCenterY
        gradientRadius = state.gradientRadius
        useLevel = state.useLevel
        useLevelForShape = state.useLevelForShape
        opaque = state.opaque

        shadowSize = state.shadowSize
        shadowColor = state.shadowColor
        shadowOffsetX = state.shadowOffsetX
        shadowOffsetY = state.shadowOffsetY

        lineGravity = state.lineGravity
    }

    override fun newDrawable(): Drawable {
        return ShapeDrawable(this)
    }

    override fun newDrawable(res: Resources?): Drawable {
        return ShapeDrawable(this)
    }

    override fun getChangingConfigurations(): Int {
        return shapeChangingConfigurations
    }

    fun setType(shape: Int) {
        shapeType = shape
        computeOpacity()
    }

    fun setShapeSolidGradientType(gradientType: Int) {
        this.solidGradientType = gradientType
    }

    fun setShapeSolidColor(vararg colors: Int) {
        if (colors == null) {
            solidColor = 0
            hasSolidColor = true
            computeOpacity()
            return
        }

        if (colors.size == 1) {
            hasSolidColor = true
            solidColor = colors[0]
            solidColors = null
        } else {
            hasSolidColor = false
            solidColor = 0
            solidColors = colors
        }
        computeOpacity()
    }

    fun setShapeSolidColor(argb: Int) {
        hasSolidColor = true
        solidColor = argb
        solidColors = null
        computeOpacity()
    }

    private fun computeOpacity() {
        if (shapeType != ShapeType.RECTANGLE) {
            opaque = false
            return
        }

        if (radius > 0 || radiusArray != null) {
            opaque = false
            return
        }

        if (shadowSize > 0) {
            opaque = false
            return
        }

        if (strokeSize > 0 && !isOpaque(strokeColor)) {
            opaque = false
            return
        }

        if (hasSolidColor) {
            opaque = isOpaque(solidColor)
            return
        }

        if (solidColors != null) {
            for (color in solidColors!!) {
                if (!isOpaque(color)) {
                    opaque = false
                    return
                }
            }
        }

        if (hasStrokeColor) {
            opaque = isOpaque(strokeColor)
            return
        }

        if (strokeColors != null) {
            for (color in strokeColors!!) {
                if (!isOpaque(color)) {
                    opaque = false
                    return
                }
            }
        }

        opaque = true
    }

    fun setShapeStrokeSize(size: Int) {
        strokeSize = size
        computeOpacity()
    }

    fun setShapeStrokeColor(vararg colors: Int) {
        if (colors == null) {
            strokeColor = 0
            hasStrokeColor = true
            computeOpacity()
            return
        }

        if (colors.size == 1) {
            hasStrokeColor = true
            strokeColor = colors[0]
            strokeColors = null
        } else {
            hasStrokeColor = false
            strokeColor = 0
            strokeColors = colors
        }
        computeOpacity()
    }

    fun setCornerRadius(radius: Float) {
        var radius = radius
        if (radius < 0) {
            radius = 0f
        }
        this.radius = radius
        radiusArray = null
    }

    fun setCornerRadii(radii: FloatArray?) {
        radiusArray = radii
        if (radii == null) {
            radius = 0f
        }
    }

    companion object {
        private fun isOpaque(color: Int): Boolean {
            return ((color shr 24) and 0xff) == 0xff
        }
    }
}
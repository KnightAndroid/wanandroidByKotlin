package com.knight.kotlin.library_widget.shadeview.builder

import android.content.res.TypedArray
import android.graphics.Color
import android.graphics.drawable.Drawable


import android.view.Gravity
import android.view.View
import androidx.annotation.NonNull
import androidx.annotation.Nullable
import com.knight.kotlin.library_widget.shadeview.config.IShapeDrawableStyleable
import com.knight.kotlin.library_widget.shadeview.drawable.ShapeDrawable
import com.knight.kotlin.library_widget.shadeview.drawable.ShapeGradientOrientation
import com.knight.kotlin.library_widget.shadeview.drawable.ShapeGradientType
import com.knight.kotlin.library_widget.shadeview.drawable.ShapeGradientTypeLimit
import com.knight.kotlin.library_widget.shadeview.drawable.ShapeType
import com.knight.kotlin.library_widget.shadeview.drawable.ShapeTypeLimit
import com.knight.kotlin.library_widget.shadeview.other.ExtendStateListDrawable


/**
 * @author created by luguian
 * @organize
 * @Date 2024/9/5 9:09
 * @descript:
 */
class ShapeDrawableBuilder(view: View, typedArray: TypedArray, styleable: IShapeDrawableStyleable) {
    private val mView = view

    @ShapeTypeLimit
    private var mType: Int
    private var mWidth: Int
    private var mHeight: Int

    private var mSolidColor: Int
    private var mSolidPressedColor: Int? = null
    private var mSolidCheckedColor: Int? = null
    private var mSolidDisabledColor: Int? = null
    private var mSolidFocusedColor: Int? = null
    private var mSolidSelectedColor: Int? = null

    private var mTopLeftRadius: Float
    private var mTopRightRadius: Float
    private var mBottomLeftRadius: Float
    private var mBottomRightRadius: Float

    private var mSolidGradientColors: IntArray? = intArrayOf()
    private var mSolidGradientOrientation: ShapeGradientOrientation

    @ShapeGradientTypeLimit
    private var mSolidGradientType: Int
    private var mSolidGradientCenterX: Float
    private var mSolidGradientCenterY: Float
    private var mSolidGradientRadius: Int

    private var mStrokeColor: Int
    private var mStrokePressedColor: Int? = null
    private var mStrokeCheckedColor: Int? = null
    private var mStrokeDisabledColor: Int? = null
    private var mStrokeFocusedColor: Int? = null
    private var mStrokeSelectedColor: Int? = null

    private var mStrokeGradientColors: IntArray? = intArrayOf()
    private var mStrokeGradientOrientation: ShapeGradientOrientation

    private var mStrokeSize: Int
    private var mStrokeDashSize: Int
    private var mStrokeDashGap: Int

    private var mShadowSize: Int
    private var mShadowColor: Int
    private var mShadowOffsetX: Int
    private var mShadowOffsetY: Int

    private var mRingInnerRadiusSize: Int
    private var mRingInnerRadiusRatio: Float
    private var mRingThicknessSize: Int
    private var mRingThicknessRatio: Float

    private var mLineGravity: Int

    init {
        mType = typedArray.getInt(styleable.getShapeTypeStyleable(), ShapeType.RECTANGLE)
        mWidth = typedArray.getDimensionPixelSize(styleable.getShapeWidthStyleable(), -1)
        mHeight = typedArray.getDimensionPixelSize(styleable.getShapeHeightStyleable(), -1)

        mSolidColor = typedArray.getColor(styleable.getSolidColorStyleable(), NO_COLOR)
        if (typedArray.hasValue(styleable.getSolidPressedColorStyleable())) {
            mSolidPressedColor = typedArray.getColor(styleable.getSolidPressedColorStyleable(), NO_COLOR)
        }
        if (styleable.getSolidCheckedColorStyleable() > 0 && typedArray.hasValue(styleable.getSolidCheckedColorStyleable())) {
            mSolidCheckedColor = typedArray.getColor(styleable.getSolidCheckedColorStyleable(), NO_COLOR)
        }
        if (typedArray.hasValue(styleable.getSolidDisabledColorStyleable())) {
            mSolidDisabledColor = typedArray.getColor(styleable.getSolidDisabledColorStyleable(), NO_COLOR)
        }
        if (typedArray.hasValue(styleable.getSolidFocusedColorStyleable())) {
            mSolidFocusedColor = typedArray.getColor(styleable.getSolidFocusedColorStyleable(), NO_COLOR)
        }
        if (typedArray.hasValue(styleable.getSolidSelectedColorStyleable())) {
            mSolidSelectedColor = typedArray.getColor(styleable.getSolidSelectedColorStyleable(), NO_COLOR)
        }

        val layoutDirection = view.layoutDirection

        val radius = typedArray.getDimensionPixelSize(styleable.getRadiusStyleable(), 0)
        mBottomRightRadius = radius.toFloat()
        mBottomLeftRadius = mBottomRightRadius
        mTopRightRadius = mBottomLeftRadius
        mTopLeftRadius = mTopRightRadius

        if (typedArray.hasValue(styleable.getRadiusInTopStartStyleable())) {
            when (layoutDirection) {
                View.LAYOUT_DIRECTION_RTL -> mTopRightRadius = typedArray.getDimensionPixelSize(styleable.getRadiusInTopStartStyleable(), radius).toFloat()
                View.LAYOUT_DIRECTION_LTR -> mTopLeftRadius = typedArray.getDimensionPixelSize(styleable.getRadiusInTopStartStyleable(), radius).toFloat()
                else -> mTopLeftRadius = typedArray.getDimensionPixelSize(styleable.getRadiusInTopStartStyleable(), radius).toFloat()
            }
        }
        if (typedArray.hasValue(styleable.getRadiusInTopEndStyleable())) {
            when (layoutDirection) {
                View.LAYOUT_DIRECTION_RTL -> mTopLeftRadius = typedArray.getDimensionPixelSize(styleable.getRadiusInTopEndStyleable(), radius).toFloat()
                View.LAYOUT_DIRECTION_LTR -> mTopRightRadius = typedArray.getDimensionPixelSize(styleable.getRadiusInTopEndStyleable(), radius).toFloat()
                else -> mTopRightRadius = typedArray.getDimensionPixelSize(styleable.getRadiusInTopEndStyleable(), radius).toFloat()
            }
        }
        if (typedArray.hasValue(styleable.getRadiusInBottomStartStyleable())) {
            when (layoutDirection) {
                View.LAYOUT_DIRECTION_RTL -> mBottomRightRadius =
                    typedArray.getDimensionPixelSize(styleable.getRadiusInBottomStartStyleable(), radius).toFloat()

                View.LAYOUT_DIRECTION_LTR -> mBottomLeftRadius = typedArray.getDimensionPixelSize(styleable.getRadiusInBottomStartStyleable(), radius).toFloat()
                else -> mBottomLeftRadius = typedArray.getDimensionPixelSize(styleable.getRadiusInBottomStartStyleable(), radius).toFloat()
            }
        }
        if (typedArray.hasValue(styleable.getRadiusInBottomEndStyleable())) {
            when (layoutDirection) {
                View.LAYOUT_DIRECTION_RTL -> mBottomLeftRadius = typedArray.getDimensionPixelSize(styleable.getRadiusInBottomEndStyleable(), radius).toFloat()
                View.LAYOUT_DIRECTION_LTR -> mBottomRightRadius = typedArray.getDimensionPixelSize(styleable.getRadiusInBottomEndStyleable(), radius).toFloat()
                else -> mBottomRightRadius = typedArray.getDimensionPixelSize(styleable.getRadiusInBottomEndStyleable(), radius).toFloat()
            }
        }

        if (typedArray.hasValue(styleable.getRadiusInTopLeftStyleable())) {
            mTopLeftRadius = typedArray.getDimensionPixelSize(styleable.getRadiusInTopLeftStyleable(), radius).toFloat()
        }
        if (typedArray.hasValue(styleable.getRadiusInTopRightStyleable())) {
            mTopRightRadius = typedArray.getDimensionPixelSize(styleable.getRadiusInTopRightStyleable(), radius).toFloat()
        }
        if (typedArray.hasValue(styleable.getRadiusInBottomLeftStyleable())) {
            mBottomLeftRadius = typedArray.getDimensionPixelSize(styleable.getRadiusInBottomLeftStyleable(), radius).toFloat()
        }
        if (typedArray.hasValue(styleable.getRadiusInBottomRightStyleable())) {
            mBottomRightRadius = typedArray.getDimensionPixelSize(styleable.getRadiusInBottomRightStyleable(), radius).toFloat()
        }

        if (typedArray.hasValue(styleable.getSolidGradientStartColorStyleable()) && typedArray.hasValue(styleable.getSolidGradientEndColorStyleable())) {
            mSolidGradientColors = if (typedArray.hasValue(styleable.getSolidGradientCenterColorStyleable())) {
                intArrayOf(
                    typedArray.getColor(styleable.getSolidGradientStartColorStyleable(), NO_COLOR),
                    typedArray.getColor(styleable.getSolidGradientCenterColorStyleable(), NO_COLOR),
                    typedArray.getColor(styleable.getSolidGradientEndColorStyleable(), NO_COLOR)
                )
            } else {
                intArrayOf(
                    typedArray.getColor(styleable.getSolidGradientStartColorStyleable(), NO_COLOR),
                    typedArray.getColor(styleable.getSolidGradientEndColorStyleable(), NO_COLOR)
                )
            }
        }

        mSolidGradientOrientation = transformGradientOrientation(typedArray.getInt(styleable.getSolidGradientOrientationStyleable(), 0))
        mSolidGradientType = typedArray.getInt(styleable.getSolidGradientTypeStyleable(), ShapeGradientType.LINEAR_GRADIENT)
        mSolidGradientCenterX = typedArray.getFloat(styleable.getSolidGradientCenterXStyleable(), 0.5f)
        mSolidGradientCenterY = typedArray.getFloat(styleable.getSolidGradientCenterYStyleable(), 0.5f)
        mSolidGradientRadius = typedArray.getDimensionPixelSize(styleable.getSolidGradientRadiusStyleable(), radius)

        mStrokeColor = typedArray.getColor(styleable.getStrokeColorStyleable(), NO_COLOR)
        if (typedArray.hasValue(styleable.getStrokePressedColorStyleable())) {
            mStrokePressedColor = typedArray.getColor(styleable.getStrokePressedColorStyleable(), NO_COLOR)
        }
        if (styleable.getStrokeCheckedColorStyleable() > 0 && typedArray.hasValue(styleable.getStrokeCheckedColorStyleable())) {
            mStrokeCheckedColor = typedArray.getColor(styleable.getStrokeCheckedColorStyleable(), NO_COLOR)
        }
        if (typedArray.hasValue(styleable.getStrokeDisabledColorStyleable())) {
            mStrokeDisabledColor = typedArray.getColor(styleable.getStrokeDisabledColorStyleable(), NO_COLOR)
        }
        if (typedArray.hasValue(styleable.getStrokeFocusedColorStyleable())) {
            mStrokeFocusedColor = typedArray.getColor(styleable.getStrokeFocusedColorStyleable(), NO_COLOR)
        }
        if (typedArray.hasValue(styleable.getStrokeSelectedColorStyleable())) {
            mStrokeSelectedColor = typedArray.getColor(styleable.getStrokeSelectedColorStyleable(), NO_COLOR)
        }

        if (typedArray.hasValue(styleable.getStrokeGradientStartColorStyleable()) && typedArray.hasValue(styleable.getStrokeGradientEndColorStyleable())) {
            mStrokeGradientColors = if (typedArray.hasValue(styleable.getStrokeGradientCenterColorStyleable())) {
                intArrayOf(
                    typedArray.getColor(styleable.getStrokeGradientStartColorStyleable(), NO_COLOR),
                    typedArray.getColor(styleable.getStrokeGradientCenterColorStyleable(), NO_COLOR),
                    typedArray.getColor(styleable.getStrokeGradientEndColorStyleable(), NO_COLOR)
                )
            } else {
                intArrayOf(
                    typedArray.getColor(styleable.getStrokeGradientStartColorStyleable(), NO_COLOR),
                    typedArray.getColor(styleable.getStrokeGradientEndColorStyleable(), NO_COLOR)
                )
            }
        }

        mStrokeGradientOrientation = transformGradientOrientation(typedArray.getInt(styleable.getStrokeGradientOrientationStyleable(), 0))

        mStrokeSize = typedArray.getDimensionPixelSize(styleable.getStrokeSizeStyleable(), 0)
        mStrokeDashSize = typedArray.getDimensionPixelSize(styleable.getStrokeDashSizeStyleable(), 0)
        mStrokeDashGap = typedArray.getDimensionPixelSize(styleable.getStrokeDashGapStyleable(), 0)

        mShadowSize = typedArray.getDimensionPixelSize(styleable.getShadowSizeStyleable(), 0)
        mShadowColor = typedArray.getColor(styleable.getShadowColorStyleable(), 0x10000000)
        mShadowOffsetX = typedArray.getDimensionPixelOffset(styleable.getShadowOffsetXStyleable(), 0)
        mShadowOffsetY = typedArray.getDimensionPixelOffset(styleable.getShadowOffsetYStyleable(), 0)

        mRingInnerRadiusSize = typedArray.getDimensionPixelOffset(styleable.getRingInnerRadiusSizeStyleable(), -1)
        mRingInnerRadiusRatio = typedArray.getFloat(styleable.getRingInnerRadiusRatioStyleable(), 3.0f)
        mRingThicknessSize = typedArray.getDimensionPixelOffset(styleable.getRingThicknessSizeStyleable(), -1)
        mRingThicknessRatio = typedArray.getFloat(styleable.getRingThicknessRatioStyleable(), 9.0f)

        mLineGravity = typedArray.getInt(styleable.getLineGravityStyleable(), Gravity.CENTER)
    }

    fun setType(@ShapeTypeLimit type: Int): ShapeDrawableBuilder {
        mType = type
        return this
    }

    @ShapeTypeLimit
    fun getType(): Int {
        return mType
    }

    fun setWidth(width: Int): ShapeDrawableBuilder {
        mWidth = width
        return this
    }

    fun getWidth(): Int {
        return mWidth
    }

    fun setHeight(height: Int): ShapeDrawableBuilder {
        mHeight = height
        return this
    }

    fun getHeight(): Int {
        return mHeight
    }

    fun setRadius(radius: Float): ShapeDrawableBuilder {
        return setRadius(radius, radius, radius, radius)
    }

    fun setRadius(
        topLeftRadius: Float, topRightRadius: Float,
        bottomLeftRadius: Float, bottomRightRadius: Float
    ): ShapeDrawableBuilder {
        mTopLeftRadius = topLeftRadius
        mTopRightRadius = topRightRadius
        mBottomLeftRadius = bottomLeftRadius
        mBottomRightRadius = bottomRightRadius
        return this
    }

    fun setRadiusRelative(
        topStartRadius: Float, topEndRadius: Float,
        bottomStartRadius: Float, bottomEndRadius: Float
    ): ShapeDrawableBuilder {
        val layoutDirection = mView.layoutDirection
        when (layoutDirection) {
            View.LAYOUT_DIRECTION_RTL -> {
                mTopLeftRadius = topEndRadius
                mTopRightRadius = topStartRadius
                mBottomLeftRadius = bottomEndRadius
                mBottomRightRadius = bottomStartRadius
            }

            View.LAYOUT_DIRECTION_LTR -> {
                mTopLeftRadius = topStartRadius
                mTopRightRadius = topEndRadius
                mBottomLeftRadius = bottomStartRadius
                mBottomRightRadius = bottomEndRadius
            }

            else -> {
                mTopLeftRadius = topStartRadius
                mTopRightRadius = topEndRadius
                mBottomLeftRadius = bottomStartRadius
                mBottomRightRadius = bottomEndRadius
            }
        }
        return this
    }

    fun setTopLeftRadius(radius: Float): ShapeDrawableBuilder {
        mTopLeftRadius = radius
        return this
    }

    fun getTopLeftRadius(): Float {
        return mTopLeftRadius
    }

    fun setTopRightRadius(radius: Float): ShapeDrawableBuilder {
        mTopRightRadius = radius
        return this
    }

    fun getTopRightRadius(): Float {
        return mTopRightRadius
    }

    fun setBottomLeftRadius(radius: Float): ShapeDrawableBuilder {
        mBottomLeftRadius = radius
        return this
    }

    fun getBottomLeftRadius(): Float {
        return mBottomLeftRadius
    }

    fun setBottomRightRadius(radius: Float): ShapeDrawableBuilder {
        mBottomRightRadius = radius
        return this
    }

    fun getBottomRightRadius(): Float {
        return mBottomRightRadius
    }

    fun setSolidColor(color: Int): ShapeDrawableBuilder {
        mSolidColor = color
        clearSolidGradientColors()
        return this
    }

    fun getSolidColor(): Int {
        return mSolidColor
    }

    fun setSolidPressedColor(color: Int?): ShapeDrawableBuilder {
        mSolidPressedColor = color
        return this
    }

    @Nullable
    fun getSolidPressedColor(): Int? {
        return mSolidPressedColor
    }

    fun setSolidCheckedColor(color: Int?): ShapeDrawableBuilder {
        mSolidCheckedColor = color
        return this
    }

    @Nullable
    fun getSolidCheckedColor(): Int? {
        return mSolidCheckedColor
    }

    fun setSolidDisabledColor(color: Int?): ShapeDrawableBuilder {
        mSolidDisabledColor = color
        return this
    }

    @Nullable
    fun getSolidDisabledColor(): Int? {
        return mSolidDisabledColor
    }

    fun setSolidFocusedColor(color: Int?): ShapeDrawableBuilder {
        mSolidFocusedColor = color
        return this
    }

    @Nullable
    fun getSolidFocusedColor(): Int? {
        return mSolidFocusedColor
    }

    fun setSolidSelectedColor(color: Int?): ShapeDrawableBuilder {
        mSolidSelectedColor = color
        return this
    }

    @Nullable
    fun getSolidSelectedColor(): Int? {
        return mSolidSelectedColor
    }

    fun setSolidGradientColors(startColor: Int, endColor: Int): ShapeDrawableBuilder {
        return setSolidGradientColors(intArrayOf(startColor, endColor))
    }

    fun setSolidGradientColors(startColor: Int, centerColor: Int, endColor: Int): ShapeDrawableBuilder {
        return setSolidGradientColors(intArrayOf(startColor, centerColor, endColor))
    }

    fun setSolidGradientColors(colors: IntArray?): ShapeDrawableBuilder {
        mSolidGradientColors = colors
        return this
    }

    @Nullable
    fun getSolidGradientColors(): IntArray? {
        return mSolidGradientColors
    }

    fun isSolidGradientColorsEnable(): Boolean {
        return mSolidGradientColors != null &&
                mSolidGradientColors!!.size > 0
    }

    fun clearSolidGradientColors() {
        mSolidGradientColors = null
    }

    fun setSolidGradientOrientation(orientation: ShapeGradientOrientation): ShapeDrawableBuilder {
        mSolidGradientOrientation = orientation
        return this
    }

    fun getSolidGradientOrientation(): ShapeGradientOrientation {
        return mSolidGradientOrientation
    }

    fun setSolidGradientType(@ShapeGradientTypeLimit type: Int): ShapeDrawableBuilder {
        mSolidGradientType = type
        return this
    }

    @ShapeGradientTypeLimit
    fun getSolidGradientType(): Int {
        return mSolidGradientType
    }

    fun setSolidGradientCenterX(centerX: Float): ShapeDrawableBuilder {
        mSolidGradientCenterX = centerX
        return this
    }

    fun getSolidGradientCenterX(): Float {
        return mSolidGradientCenterX
    }

    fun setSolidGradientCenterY(centerY: Float): ShapeDrawableBuilder {
        mSolidGradientCenterY = centerY
        return this
    }

    fun getSolidGradientCenterY(): Float {
        return mSolidGradientCenterY
    }

    fun setSolidGradientRadius(radius: Int): ShapeDrawableBuilder {
        mSolidGradientRadius = radius
        return this
    }

    fun getSolidGradientRadius(): Int {
        return mSolidGradientRadius
    }

    fun setStrokeColor(color: Int): ShapeDrawableBuilder {
        mStrokeColor = color
        clearStrokeGradientColors()
        return this
    }

    fun getStrokeColor(): Int {
        return mStrokeColor
    }

    fun setStrokePressedColor(color: Int?): ShapeDrawableBuilder {
        mStrokePressedColor = color
        return this
    }

    @Nullable
    fun getStrokePressedColor(): Int? {
        return mStrokePressedColor
    }

    fun setStrokeCheckedColor(color: Int?): ShapeDrawableBuilder {
        mStrokeCheckedColor = color
        return this
    }

    @Nullable
    fun getStrokeCheckedColor(): Int? {
        return mStrokeCheckedColor
    }

    fun setStrokeDisabledColor(color: Int?): ShapeDrawableBuilder {
        mStrokeDisabledColor = color
        return this
    }

    @Nullable
    fun getStrokeDisabledColor(): Int? {
        return mStrokeDisabledColor
    }

    fun setStrokeFocusedColor(color: Int?): ShapeDrawableBuilder {
        mStrokeFocusedColor = color
        return this
    }

    @Nullable
    fun getStrokeFocusedColor(): Int? {
        return mStrokeFocusedColor
    }

    fun setStrokeSelectedColor(color: Int?): ShapeDrawableBuilder {
        mStrokeSelectedColor = color
        return this
    }

    @Nullable
    fun getStrokeSelectedColor(): Int? {
        return mStrokeSelectedColor
    }

    fun setStrokeGradientColors(startColor: Int, endColor: Int): ShapeDrawableBuilder {
        return setStrokeGradientColors(intArrayOf(startColor, endColor))
    }

    fun setStrokeGradientColors(startColor: Int, centerColor: Int, endColor: Int): ShapeDrawableBuilder {
        return setStrokeGradientColors(intArrayOf(startColor, centerColor, endColor))
    }

    fun setStrokeGradientColors(colors: IntArray?): ShapeDrawableBuilder {
        mStrokeGradientColors = colors
        return this
    }

    @Nullable
    fun getStrokeGradientColors(): IntArray? {
        return mStrokeGradientColors
    }

    fun isStrokeGradientColorsEnable(): Boolean {
        return mStrokeGradientColors != null &&
                mStrokeGradientColors!!.size > 0
    }

    fun clearStrokeGradientColors() {
        mStrokeGradientColors = null
    }

    fun setStrokeGradientOrientation(orientation: ShapeGradientOrientation): ShapeDrawableBuilder {
        mStrokeGradientOrientation = orientation
        return this
    }

    fun getStrokeGradientOrientation(): ShapeGradientOrientation {
        return mStrokeGradientOrientation
    }

    fun setStrokeSize(size: Int): ShapeDrawableBuilder {
        mStrokeSize = size
        return this
    }

    fun getStrokeSize(): Int {
        return mStrokeSize
    }

    fun setStrokeDashSize(size: Int): ShapeDrawableBuilder {
        mStrokeDashSize = size
        return this
    }

    fun getStrokeDashSize(): Int {
        return mStrokeDashSize
    }

    fun setStrokeDashGap(gap: Int): ShapeDrawableBuilder {
        mStrokeDashGap = gap
        return this
    }

    fun getStrokeDashGap(): Int {
        return mStrokeDashGap
    }

    fun isStrokeDashLineEnable(): Boolean {
        return mStrokeDashGap > 0
    }

    fun setRingInnerRadiusSize(size: Int): ShapeDrawableBuilder {
        mRingInnerRadiusSize = size
        return this
    }

    fun getRingInnerRadiusSize(): Int {
        return mRingInnerRadiusSize
    }

    fun setRingInnerRadiusRatio(ratio: Float): ShapeDrawableBuilder {
        mRingInnerRadiusRatio = ratio
        return this
    }

    fun getRingInnerRadiusRatio(): Float {
        return mRingInnerRadiusRatio
    }

    fun setRingThicknessSize(size: Int): ShapeDrawableBuilder {
        mRingThicknessSize = size
        return this
    }

    fun getRingThicknessSize(): Int {
        return mRingThicknessSize
    }

    fun setRingThicknessRatio(ratio: Float): ShapeDrawableBuilder {
        mRingThicknessRatio = ratio
        return this
    }

    fun getRingThicknessRatio(): Float {
        return mRingThicknessRatio
    }

    fun isShadowEnable(): Boolean {
        return mShadowSize > 0
    }

    fun setShadowSize(size: Int): ShapeDrawableBuilder {
        mShadowSize = size
        return this
    }

    fun getShadowSize(): Int {
        return mShadowSize
    }

    fun setShadowColor(color: Int): ShapeDrawableBuilder {
        mShadowColor = color
        return this
    }

    fun getShadowColor(): Int {
        return mShadowColor
    }

    fun setShadowOffsetX(offsetX: Int): ShapeDrawableBuilder {
        mShadowOffsetX = offsetX
        return this
    }

    fun getShadowOffsetX(): Int {
        return mShadowOffsetX
    }

    fun setShadowOffsetY(offsetY: Int): ShapeDrawableBuilder {
        mShadowOffsetY = offsetY
        return this
    }

    fun getShadowOffsetY(): Int {
        return mShadowOffsetY
    }

    fun getLineGravity(): Int {
        return mLineGravity
    }

    fun setLineGravity(gravity: Int): ShapeDrawableBuilder {
        mLineGravity = gravity
        return this
    }

    fun buildBackgroundDrawable(): Drawable? {
        val hasSolidColorState =
            mSolidPressedColor != null || mSolidCheckedColor != null || mSolidDisabledColor != null || mSolidFocusedColor != null || mSolidSelectedColor != null

        val hasStrokeColorState =
            mStrokePressedColor != null || mStrokeCheckedColor != null || mStrokeDisabledColor != null || mStrokeFocusedColor != null || mStrokeSelectedColor != null

        if (!isSolidGradientColorsEnable() && !isStrokeGradientColorsEnable() && mSolidColor == NO_COLOR && !hasSolidColorState && mStrokeColor == NO_COLOR && !hasStrokeColorState) {
            // 啥都没有设置，直接 return
            return null
        }

        val defaultDrawable: ShapeDrawable

        val viewBackground = mView.background
        defaultDrawable = if (viewBackground is ExtendStateListDrawable) {
            convertShapeDrawable((viewBackground as ExtendStateListDrawable).getDefaultDrawable())
        } else {
            convertShapeDrawable(viewBackground)
        }

        refreshShapeDrawable(defaultDrawable, null, null)

        if (!hasSolidColorState && !hasStrokeColorState) {
            return defaultDrawable
        }

        val stateListDrawable: ExtendStateListDrawable = ExtendStateListDrawable()
        if (mSolidPressedColor != null || mStrokePressedColor != null) {
            val drawable = convertShapeDrawable(stateListDrawable.getPressedDrawable())
            refreshShapeDrawable(drawable, mSolidPressedColor, mStrokePressedColor)
            stateListDrawable.setPressedDrawable(drawable)
        }

        if (mSolidCheckedColor != null || mStrokeCheckedColor != null) {
            val drawable = convertShapeDrawable(stateListDrawable.getCheckDrawable())
            refreshShapeDrawable(drawable, mSolidCheckedColor, mStrokeCheckedColor)
            stateListDrawable.setCheckDrawable(drawable)
        }

        if (mSolidDisabledColor != null || mStrokeDisabledColor != null) {
            val drawable = convertShapeDrawable(stateListDrawable.getDisabledDrawable())
            refreshShapeDrawable(drawable, mSolidDisabledColor, mStrokeDisabledColor)
            stateListDrawable.setDisabledDrawable(drawable)
        }

        if (mSolidFocusedColor != null || mStrokeFocusedColor != null) {
            val drawable = convertShapeDrawable(stateListDrawable.getFocusedDrawable())
            refreshShapeDrawable(drawable, mSolidFocusedColor, mStrokeFocusedColor)
            stateListDrawable.setFocusedDrawable(drawable)
        }

        if (mSolidSelectedColor != null || mStrokeSelectedColor != null) {
            val drawable = convertShapeDrawable(stateListDrawable.getSelectDrawable())
            refreshShapeDrawable(drawable, mSolidSelectedColor, mStrokeSelectedColor)
            stateListDrawable.setSelectDrawable(drawable)
        }

        stateListDrawable.setDefaultDrawable(defaultDrawable)
        return stateListDrawable
    }

    fun refreshShapeDrawable(
        drawable: ShapeDrawable,
        @Nullable solidStateColor: Int?,
        @Nullable strokeStateColor: Int?
    ) {
        drawable.setType(mType)
            .setWidth(mWidth)
            .setHeight(mHeight)
            .setRadius(
                mTopLeftRadius, mTopRightRadius,
                mBottomLeftRadius, mBottomRightRadius
            )

        drawable.setSolidGradientType(mSolidGradientType)
            .setSolidGradientOrientation(mSolidGradientOrientation)
            .setSolidGradientRadius(mSolidGradientRadius.toFloat())
            .setSolidGradientCenterX(mSolidGradientCenterX)
            .setSolidGradientCenterY(mSolidGradientCenterY)

        drawable.setStrokeGradientOrientation(mStrokeGradientOrientation)
            .setStrokeSize(mStrokeSize)
            .setStrokeDashSize(mStrokeDashSize.toFloat())
            .setStrokeDashGap(mStrokeDashGap.toFloat())

        drawable.setShadowSize(mShadowSize)
            .setShadowColor(mShadowColor)
            .setShadowOffsetX(mShadowOffsetX)
            .setShadowOffsetY(mShadowOffsetY)

        if (mRingInnerRadiusRatio > 0) {
            drawable.setRingInnerRadiusRatio(mRingInnerRadiusRatio)
        } else if (mRingInnerRadiusSize > -1) {
            drawable.setRingInnerRadiusSize(mRingInnerRadiusSize)
        }

        if (mRingThicknessRatio > 0) {
            drawable.setRingThicknessRatio(mRingThicknessRatio)
        } else if (mRingThicknessSize > -1) {
            drawable.setRingThicknessSize(mRingThicknessSize)
        }

        drawable.setLineGravity(mLineGravity)

        // 填充色设置
        if (solidStateColor != null) {
            drawable.setSolidColor(solidStateColor)
        } else if (isSolidGradientColorsEnable()) {
            mSolidGradientColors?.let { drawable.setSolidColor(*it) }
        } else {
            drawable.setSolidColor(mSolidColor)
        }

        // 边框色设置
        if (strokeStateColor != null) {
            drawable.setStrokeColor(strokeStateColor)
        } else if (isStrokeGradientColorsEnable()) {
            mStrokeGradientColors?.let { drawable.setStrokeColor(*it) }
        } else {
            drawable.setStrokeColor(mStrokeColor)
        }
    }

    @NonNull
    fun convertShapeDrawable(drawable: Drawable?): ShapeDrawable {
        if (drawable is ShapeDrawable) {
            return drawable
        }
        return ShapeDrawable()
    }

    fun intoBackground() {
        // 获取到的 Drawable 有可能为空
        val drawable = buildBackgroundDrawable()
        if (isStrokeDashLineEnable() || isShadowEnable()) {
            // 需要关闭硬件加速，否则虚线或者阴影在某些手机上面无法生效
            // https://developer.android.com/guide/topics/graphics/hardware-accel?hl=zh-cn
            mView.setLayerType(View.LAYER_TYPE_SOFTWARE, null)
        }
        mView.background = drawable
    }

    /**
     * 将 ShapeView 框架中渐变色的 xml 属性值转换成 ShapeDrawable 中的枚举值
     */
    private fun transformGradientOrientation(value: Int): ShapeGradientOrientation {
        return when (value) {
            10 -> ShapeGradientOrientation.START_TO_END
            180 -> ShapeGradientOrientation.RIGHT_TO_LEFT
            1800 -> ShapeGradientOrientation.END_TO_START
            90 -> ShapeGradientOrientation.BOTTOM_TO_TOP
            270 -> ShapeGradientOrientation.TOP_TO_BOTTOM
            315 -> ShapeGradientOrientation.TOP_LEFT_TO_BOTTOM_RIGHT
            3150 -> ShapeGradientOrientation.TOP_START_TO_BOTTOM_END
            45 -> ShapeGradientOrientation.BOTTOM_LEFT_TO_TOP_RIGHT
            450 -> ShapeGradientOrientation.BOTTOM_START_TO_TOP_END
            225 -> ShapeGradientOrientation.TOP_RIGHT_TO_BOTTOM_LEFT
            2250 -> ShapeGradientOrientation.TOP_END_TO_BOTTOM_START
            135 -> ShapeGradientOrientation.BOTTOM_RIGHT_TO_TOP_LEFT
            1350 -> ShapeGradientOrientation.BOTTOM_END_TO_TOP_START
            0 -> ShapeGradientOrientation.LEFT_TO_RIGHT
            else -> ShapeGradientOrientation.LEFT_TO_RIGHT
        }
    }

    companion object {
        private const val NO_COLOR = Color.TRANSPARENT
    }
}
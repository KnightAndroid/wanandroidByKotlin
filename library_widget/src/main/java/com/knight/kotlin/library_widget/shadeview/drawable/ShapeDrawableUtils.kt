package com.knight.kotlin.library_widget.shadeview.drawable

import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.RectF
import android.os.Build
import android.view.View
import androidx.annotation.ColorInt
import androidx.annotation.IntRange


/**
 * @author created by luguian
 * @organize
 * @Date 2024/9/5 15:53
 * @descript:ShapeDrawable 工具类（仅供内部使用）
 */
internal object ShapeDrawableUtils {
    fun saveCanvasLayer(canvas: Canvas, left: Float, top: Float, right: Float, bottom: Float, paint: Paint) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            canvas.saveLayer(left, top, right, bottom, paint)
        } else {
            canvas.saveLayer(left, top, right, bottom, paint, Canvas.ALL_SAVE_FLAG)
        }
    }

    fun computeLinearGradientCoordinate(layoutDirection: Int, r: RectF, level: Float, orientation: ShapeGradientOrientation?): FloatArray {
        val x0: Float
        val x1: Float
        val y0: Float
        val y1: Float
        when (orientation) {
            ShapeGradientOrientation.START_TO_END -> return computeLinearGradientCoordinate(
                layoutDirection, r, level,
                if (layoutDirection == View.LAYOUT_DIRECTION_RTL) ShapeGradientOrientation.RIGHT_TO_LEFT else ShapeGradientOrientation.LEFT_TO_RIGHT
            )

            ShapeGradientOrientation.END_TO_START -> return computeLinearGradientCoordinate(
                layoutDirection, r, level,
                if (layoutDirection == View.LAYOUT_DIRECTION_RTL) ShapeGradientOrientation.LEFT_TO_RIGHT else ShapeGradientOrientation.RIGHT_TO_LEFT
            )

            ShapeGradientOrientation.TOP_START_TO_BOTTOM_END -> return computeLinearGradientCoordinate(
                layoutDirection, r, level,
                if (layoutDirection == View.LAYOUT_DIRECTION_RTL) ShapeGradientOrientation.TOP_RIGHT_TO_BOTTOM_LEFT else ShapeGradientOrientation.TOP_LEFT_TO_BOTTOM_RIGHT
            )

            ShapeGradientOrientation.TOP_END_TO_BOTTOM_START -> return computeLinearGradientCoordinate(
                layoutDirection, r, level,
                if (layoutDirection == View.LAYOUT_DIRECTION_RTL) ShapeGradientOrientation.TOP_LEFT_TO_BOTTOM_RIGHT else ShapeGradientOrientation.TOP_RIGHT_TO_BOTTOM_LEFT
            )

            ShapeGradientOrientation.BOTTOM_START_TO_TOP_END -> return computeLinearGradientCoordinate(
                layoutDirection, r, level,
                if (layoutDirection == View.LAYOUT_DIRECTION_RTL) ShapeGradientOrientation.BOTTOM_RIGHT_TO_TOP_LEFT else ShapeGradientOrientation.BOTTOM_LEFT_TO_TOP_RIGHT
            )

            ShapeGradientOrientation.BOTTOM_END_TO_TOP_START -> return computeLinearGradientCoordinate(
                layoutDirection, r, level,
                if (layoutDirection == View.LAYOUT_DIRECTION_RTL) ShapeGradientOrientation.BOTTOM_LEFT_TO_TOP_RIGHT else ShapeGradientOrientation.BOTTOM_RIGHT_TO_TOP_LEFT
            )

            ShapeGradientOrientation.TOP_TO_BOTTOM -> {
                x0 = r.left
                y0 = r.top
                x1 = x0
                y1 = level * r.bottom
            }

            ShapeGradientOrientation.TOP_RIGHT_TO_BOTTOM_LEFT -> {
                x0 = r.right
                y0 = r.top
                x1 = level * r.left
                y1 = level * r.bottom
            }

            ShapeGradientOrientation.RIGHT_TO_LEFT -> {
                x0 = r.right
                y0 = r.top
                x1 = level * r.left
                y1 = y0
            }

            ShapeGradientOrientation.BOTTOM_RIGHT_TO_TOP_LEFT -> {
                x0 = r.right
                y0 = r.bottom
                x1 = level * r.left
                y1 = level * r.top
            }

            ShapeGradientOrientation.BOTTOM_TO_TOP -> {
                x0 = r.left
                y0 = r.bottom
                x1 = x0
                y1 = level * r.top
            }

            ShapeGradientOrientation.BOTTOM_LEFT_TO_TOP_RIGHT -> {
                x0 = r.left
                y0 = r.bottom
                x1 = level * r.right
                y1 = level * r.top
            }

            ShapeGradientOrientation.LEFT_TO_RIGHT -> {
                x0 = r.left
                y0 = r.top
                x1 = level * r.right
                y1 = y0
            }

            ShapeGradientOrientation.TOP_LEFT_TO_BOTTOM_RIGHT -> {
                x0 = r.left
                y0 = r.top
                x1 = level * r.right
                y1 = level * r.bottom
            }

            else -> {
                x0 = r.left
                y0 = r.top
                x1 = level * r.right
                y1 = level * r.bottom
            }
        }
        return floatArrayOf(x0, y0, x1, y1)
    }

    /**
     * 设置颜色的透明度，参考 Support 包中的 ColorUtils.setAlphaComponent 方法
     */
    @ColorInt
    fun setColorAlphaComponent(
        @ColorInt color: Int,
        @IntRange(from = 0x0, to = 0xFF) alpha: Int
    ): Int {
        require(!(alpha < 0 || alpha > 255)) { "alpha must be between 0 and 255." }
        return (color and 0x00ffffff) or (alpha shl 24)
    }
}
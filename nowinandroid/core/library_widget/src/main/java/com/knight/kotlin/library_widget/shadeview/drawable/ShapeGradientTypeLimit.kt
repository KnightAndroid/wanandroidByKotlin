package com.knight.kotlin.library_widget.shadeview.drawable

import androidx.annotation.IntDef
import java.lang.annotation.Retention
import java.lang.annotation.RetentionPolicy

/**
 * @author created by luguian
 * @organize
 * @Date 2024/9/5 15:55
 * @descript:
 */
@IntDef(
    ShapeGradientType.LINEAR_GRADIENT,
    ShapeGradientType.RADIAL_GRADIENT,
    ShapeGradientType.SWEEP_GRADIENT
)
@Retention(RetentionPolicy.SOURCE)
annotation class ShapeGradientTypeLimit
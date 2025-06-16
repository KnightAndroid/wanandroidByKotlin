package com.knight.kotlin.library_widget.shadeview.drawable

import androidx.annotation.IntDef
import java.lang.annotation.RetentionPolicy
import java.lang.annotation.Retention

/**
 * @author created by luguian
 * @organize
 * @Date 2024/9/5 15:59
 * @descript:
 */
@IntDef(
    ShapeType.RECTANGLE, ShapeType.OVAL, ShapeType.LINE, ShapeType.RING
)
@Retention(RetentionPolicy.SOURCE)
annotation class ShapeTypeLimit
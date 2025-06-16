package com.knight.kotlin.library_widget.shadeview.layout

import android.content.Context
import android.util.AttributeSet
import android.widget.LinearLayout
import com.knight.kotlin.library_widget.R
import com.knight.kotlin.library_widget.shadeview.builder.ShapeDrawableBuilder
import com.knight.kotlin.library_widget.shadeview.config.IGetShapeDrawableBuilder
import com.knight.kotlin.library_widget.shadeview.styleable.ShapeLinearLayoutStyleable


/**
 * @author created by luguian
 * @organize
 * @Date 2024/9/5 9:37
 * @descript:支持直接定义 Shape 背景的 LinearLayout
 */
class ShapeLinearLayout @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) :
    LinearLayout(context, attrs, defStyleAttr), IGetShapeDrawableBuilder {
    private val mShapeDrawableBuilder: ShapeDrawableBuilder

    init {
        val typedArray = context.obtainStyledAttributes(attrs, R.styleable.ShapeLinearLayout)
        mShapeDrawableBuilder = ShapeDrawableBuilder(this, typedArray, STYLEABLE)
        typedArray.recycle()

        mShapeDrawableBuilder.intoBackground()
    }

    override fun getShapeDrawableBuilder(): ShapeDrawableBuilder {
        return mShapeDrawableBuilder
    }

    companion object {
        private val STYLEABLE: ShapeLinearLayoutStyleable = ShapeLinearLayoutStyleable()
    }
}
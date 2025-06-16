package com.knight.kotlin.library_widget.shadeview.view

import android.content.Context
import android.util.AttributeSet
import android.view.View
import com.knight.kotlin.library_widget.R
import com.knight.kotlin.library_widget.shadeview.builder.ShapeDrawableBuilder
import com.knight.kotlin.library_widget.shadeview.config.IGetShapeDrawableBuilder
import com.knight.kotlin.library_widget.shadeview.styleable.ShapeViewStyleable


/**
 * @author created by luguian
 * @organize
 * @Date 2024/9/5 14:44
 * @descript:支持直接定义 Shape 背景的 View
 */
class ShapeView @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) :
    View(context, attrs, defStyleAttr), IGetShapeDrawableBuilder {
    private val mShapeDrawableBuilder: ShapeDrawableBuilder

    init {
        val typedArray = context.obtainStyledAttributes(attrs, R.styleable.ShapeView)
        mShapeDrawableBuilder = ShapeDrawableBuilder(this, typedArray, STYLEABLE)
        typedArray.recycle()

        mShapeDrawableBuilder.intoBackground()
    }

    override fun getShapeDrawableBuilder(): ShapeDrawableBuilder {
        return mShapeDrawableBuilder
    }

    companion object {
        private val STYLEABLE = ShapeViewStyleable()
    }
}
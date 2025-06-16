package com.knight.kotlin.library_widget.shadeview.layout


import android.content.Context
import android.util.AttributeSet
import androidx.recyclerview.widget.RecyclerView
import com.knight.kotlin.library_widget.R
import com.knight.kotlin.library_widget.shadeview.builder.ShapeDrawableBuilder
import com.knight.kotlin.library_widget.shadeview.config.IGetShapeDrawableBuilder
import com.knight.kotlin.library_widget.shadeview.styleable.ShapeRecyclerViewStyleable


/**
 * @author created by luguian
 * @organize
 * @Date 2024/9/5 9:39
 * @descript:支持直接定义 Shape 背景的 RecyclerView
 */
class ShapeRecyclerView @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) :
    RecyclerView(context, attrs, defStyleAttr), IGetShapeDrawableBuilder {
    private val mShapeDrawableBuilder: ShapeDrawableBuilder

    init {
        val typedArray = context.obtainStyledAttributes(attrs, R.styleable.ShapeRecyclerView)
        mShapeDrawableBuilder = ShapeDrawableBuilder(this, typedArray, STYLEABLE)
        typedArray.recycle()

        mShapeDrawableBuilder.intoBackground()
    }

    override fun getShapeDrawableBuilder(): ShapeDrawableBuilder {
        return mShapeDrawableBuilder
    }

    companion object {
        private val STYLEABLE: ShapeRecyclerViewStyleable = ShapeRecyclerViewStyleable()
    }
}
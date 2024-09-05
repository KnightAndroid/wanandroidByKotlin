package com.knight.kotlin.library_widget.shadeview.view


import android.content.Context
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatTextView
import com.knight.kotlin.library_widget.R
import com.knight.kotlin.library_widget.shadeview.builder.ShapeDrawableBuilder
import com.knight.kotlin.library_widget.shadeview.builder.TextColorBuilder
import com.knight.kotlin.library_widget.shadeview.config.IGetShapeDrawableBuilder
import com.knight.kotlin.library_widget.shadeview.config.IGetTextColorBuilder
import com.knight.kotlin.library_widget.shadeview.styleable.ShapeTextViewStyleable


/**
 * @author created by luguian
 * @organize
 * @Date 2024/9/5 14:23
 * @descript:支持直接定义 Shape 背景的 TextView
 */
class ShapeTextView @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = android.R.attr.textViewStyle) :
    AppCompatTextView(context, attrs, defStyleAttr), IGetShapeDrawableBuilder, IGetTextColorBuilder {
    private val mShapeDrawableBuilder: ShapeDrawableBuilder
    private val mTextColorBuilder: TextColorBuilder?

    init {
        val typedArray = context.obtainStyledAttributes(attrs, R.styleable.ShapeTextView)
        mShapeDrawableBuilder = ShapeDrawableBuilder(this, typedArray, STYLEABLE)
        mTextColorBuilder = TextColorBuilder(this, typedArray, STYLEABLE)
        typedArray.recycle()

        mShapeDrawableBuilder.intoBackground()

        if (mTextColorBuilder.isTextGradientColorsEnable() || mTextColorBuilder.isTextStrokeColorEnable()) {
            text = text
        } else {
            mTextColorBuilder.intoTextColor()
        }
    }

    override fun setTextColor(color: Int) {
        super.setTextColor(color)
        if (mTextColorBuilder == null) {
            return
        }
        mTextColorBuilder.setTextColor(color)
    }

    override fun setText(text: CharSequence?, type: BufferType?) {
        if (mTextColorBuilder != null &&
            (mTextColorBuilder.isTextGradientColorsEnable() || mTextColorBuilder.isTextStrokeColorEnable())
        ) {
            super.setText(mTextColorBuilder.buildTextSpannable(text), type)
        } else {
            super.setText(text, type)
        }
    }

    override fun getShapeDrawableBuilder(): ShapeDrawableBuilder {
        return mShapeDrawableBuilder
    }

    override fun getTextColorBuilder(): TextColorBuilder? {
        return mTextColorBuilder
    }

    companion object {
        private val STYLEABLE = ShapeTextViewStyleable()
    }
}
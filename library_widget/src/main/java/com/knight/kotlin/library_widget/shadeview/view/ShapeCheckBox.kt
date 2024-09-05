package com.knight.kotlin.library_widget.shadeview.view


import android.content.Context
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatCheckBox
import com.knight.kotlin.library_widget.R
import com.knight.kotlin.library_widget.shadeview.builder.ButtonDrawableBuilder
import com.knight.kotlin.library_widget.shadeview.builder.ShapeDrawableBuilder
import com.knight.kotlin.library_widget.shadeview.builder.TextColorBuilder
import com.knight.kotlin.library_widget.shadeview.config.IGetButtonDrawableBuilder
import com.knight.kotlin.library_widget.shadeview.config.IGetShapeDrawableBuilder
import com.knight.kotlin.library_widget.shadeview.config.IGetTextColorBuilder
import com.knight.kotlin.library_widget.shadeview.styleable.ShapeCheckBoxStyleable


/**
 * @author created by luguian
 * @organize
 * @Date 2024/9/5 11:22
 * @descript:支持直接定义 Shape 背景的 CheckBox
 */
class ShapeCheckBox @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = android.R.attr.checkboxStyle) :
    AppCompatCheckBox(context, attrs, defStyleAttr), IGetShapeDrawableBuilder, IGetTextColorBuilder, IGetButtonDrawableBuilder {
    private val mShapeDrawableBuilder: ShapeDrawableBuilder
    private val mTextColorBuilder: TextColorBuilder?
    private val mButtonDrawableBuilder: ButtonDrawableBuilder?

    init {
        val typedArray = context.obtainStyledAttributes(attrs, R.styleable.ShapeCheckBox, 0, R.style.ShapeCheckBoxStyle)
        mShapeDrawableBuilder = ShapeDrawableBuilder(this, typedArray, STYLEABLE)
        mTextColorBuilder = TextColorBuilder(this, typedArray, STYLEABLE)
        mButtonDrawableBuilder = ButtonDrawableBuilder(this, typedArray, STYLEABLE)
        typedArray.recycle()

        mShapeDrawableBuilder.intoBackground()

        if (mTextColorBuilder.isTextGradientColorsEnable() || mTextColorBuilder.isTextStrokeColorEnable()) {
            text = text
        } else {
            mTextColorBuilder.intoTextColor()
        }

        mButtonDrawableBuilder.intoButtonDrawable()
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

    override fun setButtonDrawable(drawable: Drawable?) {
        super.setButtonDrawable(drawable)
        if (mButtonDrawableBuilder == null) {
            return
        }
        mButtonDrawableBuilder.setButtonDrawable(drawable)
    }

    override fun getShapeDrawableBuilder(): ShapeDrawableBuilder {
        return mShapeDrawableBuilder
    }

    override fun getTextColorBuilder(): TextColorBuilder? {
        return mTextColorBuilder
    }

    override fun getButtonDrawableBuilder(): ButtonDrawableBuilder? {
        return mButtonDrawableBuilder
    }

    companion object {
        private val STYLEABLE = ShapeCheckBoxStyleable()
    }
}
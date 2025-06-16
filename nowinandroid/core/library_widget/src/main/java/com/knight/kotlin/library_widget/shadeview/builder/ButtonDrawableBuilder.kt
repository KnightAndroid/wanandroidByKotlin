package com.knight.kotlin.library_widget.shadeview.builder


import android.content.res.TypedArray
import android.graphics.drawable.Drawable
import android.graphics.drawable.StateListDrawable
import android.widget.CompoundButton
import androidx.core.widget.CompoundButtonCompat
import com.knight.kotlin.library_widget.R
import com.knight.kotlin.library_widget.shadeview.config.ICompoundButtonStyleable


/**
 * @author created by luguian
 * @organize
 * @Date 2024/9/5 9:07
 * @descript:ButtonDrawable 构建类
 */
class ButtonDrawableBuilder(compoundButton: CompoundButton, typedArray: TypedArray, styleable: ICompoundButtonStyleable) {
    private val mCompoundButton = compoundButton

    private var mButtonDrawable: Drawable? = null
    private var mButtonPressedDrawable: Drawable? = null
    private var mButtonCheckedDrawable: Drawable? = null
    private var mButtonDisabledDrawable: Drawable? = null
    private var mButtonFocusedDrawable: Drawable? = null
    private var mButtonSelectedDrawable: Drawable? = null

    init {
        if (typedArray.hasValue(styleable.getButtonDrawableStyleable())) {
            mButtonDrawable = if (typedArray.getResourceId(styleable.getButtonDrawableStyleable(), 0) != R.drawable.widget_shape_view_placeholder) {
                typedArray.getDrawable(styleable.getButtonDrawableStyleable())
            } else {
                CompoundButtonCompat.getButtonDrawable(mCompoundButton)
            }
        } else {
            mButtonDrawable = null
            mCompoundButton.buttonDrawable = null
        }

        if (typedArray.hasValue(styleable.getButtonPressedDrawableStyleable())) {
            mButtonPressedDrawable = typedArray.getDrawable(styleable.getButtonPressedDrawableStyleable())
        }

        if (typedArray.hasValue(styleable.getButtonCheckedDrawableStyleable())) {
            mButtonCheckedDrawable = typedArray.getDrawable(styleable.getButtonCheckedDrawableStyleable())
        }

        if (typedArray.hasValue(styleable.getButtonDisabledDrawableStyleable())) {
            mButtonDisabledDrawable = typedArray.getDrawable(styleable.getButtonDisabledDrawableStyleable())
        }

        if (typedArray.hasValue(styleable.getButtonFocusedDrawableStyleable())) {
            mButtonFocusedDrawable = typedArray.getDrawable(styleable.getButtonFocusedDrawableStyleable())
        }

        if (typedArray.hasValue(styleable.getButtonSelectedDrawableStyleable())) {
            mButtonSelectedDrawable = typedArray.getDrawable(styleable.getButtonSelectedDrawableStyleable())
        }
    }

    fun setButtonDrawable(drawable: Drawable?): ButtonDrawableBuilder {
        if (mButtonPressedDrawable === mButtonDrawable) {
            mButtonPressedDrawable = drawable
        }
        if (mButtonCheckedDrawable === mButtonDrawable) {
            mButtonCheckedDrawable = drawable
        }
        if (mButtonDisabledDrawable === mButtonDrawable) {
            mButtonDisabledDrawable = drawable
        }
        if (mButtonFocusedDrawable === mButtonDrawable) {
            mButtonFocusedDrawable = drawable
        }
        if (mButtonSelectedDrawable === mButtonDrawable) {
            mButtonSelectedDrawable = drawable
        }
        mButtonDrawable = drawable
        return this
    }

    fun getButtonDrawable(): Drawable? {
        return mButtonDrawable
    }

    fun setButtonPressedDrawable(drawable: Drawable?): ButtonDrawableBuilder {
        mButtonPressedDrawable = drawable
        return this
    }

    fun getButtonPressedDrawable(): Drawable? {
        return mButtonPressedDrawable
    }

    fun setButtonCheckedDrawable(drawable: Drawable?): ButtonDrawableBuilder {
        mButtonCheckedDrawable = drawable
        return this
    }

    fun getButtonCheckedDrawable(): Drawable? {
        return mButtonCheckedDrawable
    }

    fun setButtonDisabledDrawable(drawable: Drawable?): ButtonDrawableBuilder {
        mButtonDisabledDrawable = drawable
        return this
    }

    fun getButtonDisabledDrawable(): Drawable? {
        return mButtonDisabledDrawable
    }

    fun setButtonFocusedDrawable(drawable: Drawable?): ButtonDrawableBuilder {
        mButtonFocusedDrawable = drawable
        return this
    }

    fun getButtonFocusedDrawable(): Drawable? {
        return mButtonFocusedDrawable
    }

    fun setButtonSelectedDrawable(drawable: Drawable?): ButtonDrawableBuilder {
        mButtonSelectedDrawable = drawable
        return this
    }

    fun getButtonSelectedDrawable(): Drawable? {
        return mButtonSelectedDrawable
    }

    fun intoButtonDrawable() {
        if (mButtonDrawable == null) {
            return
        }

        if (mButtonPressedDrawable == null && mButtonCheckedDrawable == null && mButtonDisabledDrawable == null && mButtonFocusedDrawable == null && mButtonSelectedDrawable == null) {
            mCompoundButton.buttonDrawable = mButtonDrawable
            return
        }

        val drawable = StateListDrawable()
        if (mButtonPressedDrawable != null) {
            drawable.addState(intArrayOf(android.R.attr.state_pressed), mButtonPressedDrawable)
        }
        if (mButtonCheckedDrawable != null) {
            drawable.addState(intArrayOf(android.R.attr.state_checked), mButtonCheckedDrawable)
        }
        if (mButtonDisabledDrawable != null) {
            drawable.addState(intArrayOf(android.R.attr.state_enabled), mButtonDisabledDrawable)
        }
        if (mButtonFocusedDrawable != null) {
            drawable.addState(intArrayOf(android.R.attr.state_focused), mButtonFocusedDrawable)
        }
        if (mButtonSelectedDrawable != null) {
            drawable.addState(intArrayOf(android.R.attr.state_selected), mButtonSelectedDrawable)
        }
        drawable.addState(intArrayOf(), mButtonDrawable)
        mCompoundButton.buttonDrawable = drawable
    }
}
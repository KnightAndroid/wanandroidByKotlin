package com.knight.kotlin.library_widget.shadeview.builder


import android.content.res.ColorStateList
import android.content.res.TypedArray
import android.graphics.Color
import android.text.SpannableString
import android.text.Spanned
import android.widget.TextView
import androidx.annotation.Nullable
import com.knight.kotlin.library_widget.shadeview.config.ITextColorStyleable
import com.knight.kotlin.library_widget.shadeview.span.LinearGradientFontSpan
import com.knight.kotlin.library_widget.shadeview.span.MultiFontSpan
import com.knight.kotlin.library_widget.shadeview.span.StrokeFontSpan


/**
 * @author created by luguian
 * @organize
 * @Date 2024/9/5 9:10
 * @descript:TextColor 构建类
 */
class TextColorBuilder(textView: TextView, typedArray: TypedArray, styleable: ITextColorStyleable) {
    private val mTextView = textView

    private var mTextColor: Int
    private var mTextPressedColor: Int? = null
    private var mTextCheckedColor: Int? = null
    private var mTextDisabledColor: Int? = null
    private var mTextFocusedColor: Int? = null
    private var mTextSelectedColor: Int? = null

    private var mTextGradientColors: IntArray? = null
    private var mTextGradientOrientation: Int

    private var mTextStrokeColor = 0
    private var mTextStrokeSize = 0

    init {
        mTextColor = typedArray.getColor(styleable.getTextColorStyleable(), textView.textColors.defaultColor)
        if (typedArray.hasValue(styleable.getTextPressedColorStyleable())) {
            mTextPressedColor = typedArray.getColor(styleable.getTextPressedColorStyleable(), mTextColor)
        }
        if (styleable.getTextCheckedColorStyleable() > 0 && typedArray.hasValue(styleable.getTextCheckedColorStyleable())) {
            mTextCheckedColor = typedArray.getColor(styleable.getTextCheckedColorStyleable(), mTextColor)
        }
        if (typedArray.hasValue(styleable.getTextDisabledColorStyleable())) {
            mTextDisabledColor = typedArray.getColor(styleable.getTextDisabledColorStyleable(), mTextColor)
        }
        if (typedArray.hasValue(styleable.getTextFocusedColorStyleable())) {
            mTextFocusedColor = typedArray.getColor(styleable.getTextFocusedColorStyleable(), mTextColor)
        }
        if (typedArray.hasValue(styleable.getTextSelectedColorStyleable())) {
            mTextSelectedColor = typedArray.getColor(styleable.getTextSelectedColorStyleable(), mTextColor)
        }

        if (typedArray.hasValue(styleable.getTextStartColorStyleable()) && typedArray.hasValue(styleable.getTextEndColorStyleable())) {
            mTextGradientColors = if (typedArray.hasValue(styleable.getTextCenterColorStyleable())) {
                intArrayOf(
                    typedArray.getColor(styleable.getTextStartColorStyleable(), mTextColor),
                    typedArray.getColor(styleable.getTextCenterColorStyleable(), mTextColor),
                    typedArray.getColor(styleable.getTextEndColorStyleable(), mTextColor)
                )
            } else {
                intArrayOf(
                    typedArray.getColor(styleable.getTextStartColorStyleable(), mTextColor),
                    typedArray.getColor(styleable.getTextEndColorStyleable(), mTextColor)
                )
            }
        }

        mTextGradientOrientation = typedArray.getColor(
            styleable.getTextGradientOrientationStyleable(),
            LinearGradientFontSpan.GRADIENT_ORIENTATION_HORIZONTAL
        )

        if (typedArray.hasValue(styleable.getTextStrokeColorStyleable())) {
            mTextStrokeColor = typedArray.getColor(styleable.getTextStrokeColorStyleable(), 0)
        }

        if (typedArray.hasValue(styleable.getTextStrokeSizeStyleable())) {
            mTextStrokeSize = typedArray.getDimensionPixelSize(styleable.getTextStrokeSizeStyleable(), 0)
        }
    }

    fun setTextColor(color: Int): TextColorBuilder {
        mTextColor = color
        return this
    }

    fun getTextColor(): Int {
        return mTextColor
    }

    fun setTextPressedColor(color: Int?): TextColorBuilder {
        mTextPressedColor = color
        return this
    }

    @Nullable
    fun getTextPressedColor(): Int? {
        return mTextPressedColor
    }

    fun setTextCheckedColor(color: Int?): TextColorBuilder {
        mTextCheckedColor = color
        return this
    }

    @Nullable
    fun getTextCheckedColor(): Int? {
        return mTextCheckedColor
    }

    fun setTextDisabledColor(color: Int?): TextColorBuilder {
        mTextDisabledColor = color
        return this
    }

    @Nullable
    fun getTextDisabledColor(): Int? {
        return mTextDisabledColor
    }

    fun setTextFocusedColor(color: Int?): TextColorBuilder {
        mTextFocusedColor = color
        return this
    }

    @Nullable
    fun getTextFocusedColor(): Int? {
        return mTextFocusedColor
    }

    fun setTextSelectedColor(color: Int?): TextColorBuilder {
        mTextSelectedColor = color
        return this
    }

    @Nullable
    fun getTextSelectedColor(): Int? {
        return mTextSelectedColor
    }

    fun setTextGradientColors(startColor: Int, endColor: Int): TextColorBuilder {
        return setTextGradientColors(intArrayOf(startColor, endColor))
    }

    fun setTextGradientColors(startColor: Int, centerColor: Int, endColor: Int): TextColorBuilder {
        return setTextGradientColors(intArrayOf(startColor, centerColor, endColor))
    }

    fun setTextGradientColors(colors: IntArray?): TextColorBuilder {
        mTextGradientColors = colors
        return this
    }

    @Nullable
    fun getTextGradientColors(): IntArray? {
        return mTextGradientColors
    }

    fun isTextGradientColorsEnable(): Boolean {
        return mTextGradientColors != null && mTextGradientColors!!.size > 0
    }

    fun setTextGradientOrientation(orientation: Int): TextColorBuilder {
        mTextGradientOrientation = orientation
        return this
    }

    fun getTextGradientOrientation(): Int {
        return mTextGradientOrientation
    }

    fun setTextStrokeColor(color: Int): TextColorBuilder {
        mTextStrokeColor = color
        return this
    }

    fun setTextStrokeSize(size: Int): TextColorBuilder {
        mTextStrokeSize = size
        return this
    }

    fun getTextStrokeColor(): Int {
        return mTextStrokeColor
    }

    fun getTextStrokeSize(): Int {
        return mTextStrokeSize
    }

    fun isTextStrokeColorEnable(): Boolean {
        return mTextStrokeColor != Color.TRANSPARENT && mTextStrokeSize > 0
    }

    fun clearTextSpannable() {
        mTextStrokeColor = Color.TRANSPARENT
        mTextStrokeSize = 0
        if (!isTextGradientColorsEnable()) {
            mTextView.setTextColor(mTextColor)
        }
        mTextView.text = mTextView.text.toString()
    }

    fun buildTextSpannable(text: CharSequence?): SpannableString {
        val builder = SpannableString(text)

        var linearGradientFontSpan: LinearGradientFontSpan? = null
        var strokeFontSpan: StrokeFontSpan? = null

        if (isTextGradientColorsEnable()) {
            linearGradientFontSpan = mTextGradientColors?.let {
                LinearGradientFontSpan()
                    .setTextGradientColor(it)
                    .setTextGradientOrientation(mTextGradientOrientation)
                    .setTextGradientPositions(FloatArray(0))
            }
        }
        if (isTextStrokeColorEnable()) {
            strokeFontSpan = StrokeFontSpan()
                .setTextStrokeColor(mTextStrokeColor)
                .setTextStrokeSize(mTextStrokeSize)
        }

        if (linearGradientFontSpan != null && strokeFontSpan != null) {
            val multiFontSpan: MultiFontSpan = MultiFontSpan(strokeFontSpan, linearGradientFontSpan)
            builder.setSpan(multiFontSpan, 0, builder.length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        } else if (linearGradientFontSpan != null) {
            builder.setSpan(linearGradientFontSpan, 0, builder.length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        } else if (strokeFontSpan != null) {
            builder.setSpan(strokeFontSpan, 0, builder.length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        }
        return builder
    }

    fun buildColorState(): ColorStateList {
        if (mTextPressedColor == null && mTextCheckedColor == null && mTextDisabledColor == null && mTextFocusedColor == null && mTextSelectedColor == null) {
            return ColorStateList.valueOf(mTextColor)
        }

        val maxSize = 6
        var arraySize = 0
        val statesTemp = arrayOfNulls<IntArray>(maxSize)
        val colorsTemp = IntArray(maxSize)

        mTextPressedColor?.let {
            statesTemp[arraySize] = intArrayOf(android.R.attr.state_pressed)
            colorsTemp[arraySize] = it
            arraySize++
        }

        mTextCheckedColor?.let {
            statesTemp[arraySize] = intArrayOf(android.R.attr.state_checked)
            colorsTemp[arraySize] = it
            arraySize++
        }

        mTextDisabledColor?.let {
            statesTemp[arraySize] = intArrayOf(android.R.attr.state_enabled)
            colorsTemp[arraySize] = it
            arraySize++
        }

        mTextFocusedColor?.let {
            statesTemp[arraySize] = intArrayOf(android.R.attr.state_focused)
            colorsTemp[arraySize] = it
            arraySize++
        }

        mTextSelectedColor?.let {
            statesTemp[arraySize] = intArrayOf(android.R.attr.state_selected)
            colorsTemp[arraySize] = it
            arraySize++
        }

        statesTemp[arraySize] = intArrayOf()
        colorsTemp[arraySize] = mTextColor
        arraySize++

        val states: Array<IntArray?>
        val colors: IntArray
        if (arraySize == maxSize) {
            states = statesTemp
            colors = colorsTemp
        } else {
            states = arrayOfNulls(arraySize)
            colors = IntArray(arraySize)
            // 对数组进行拷贝
            System.arraycopy(statesTemp, 0, states, 0, arraySize)
            System.arraycopy(colorsTemp, 0, colors, 0, arraySize)
        }
        return ColorStateList(states, colors)
    }

    fun intoTextColor() {
        mTextView.setTextColor(buildColorState())
        if (isTextGradientColorsEnable() || isTextStrokeColorEnable()) {
            mTextView.text = buildTextSpannable(mTextView.text)
        }
    }
}
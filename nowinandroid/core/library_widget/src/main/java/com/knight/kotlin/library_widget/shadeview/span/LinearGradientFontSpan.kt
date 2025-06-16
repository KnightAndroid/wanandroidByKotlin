package com.knight.kotlin.library_widget.shadeview.span

import android.graphics.Canvas
import android.graphics.LinearGradient
import android.graphics.Paint
import android.graphics.Shader
import android.text.SpannableStringBuilder
import android.text.Spanned
import android.widget.LinearLayout
import androidx.annotation.NonNull


/**
 * @author created by luguian
 * @organize
 * @Date 2024/9/5 10:01
 * @descript:支持直接定义文本渐变色的 Span
 */
class LinearGradientFontSpan : CommonFontSpan() {
    /** 文字渐变方向  */
    private var mTextGradientOrientation = 0

    /** 文字渐变颜色组  */
    private lateinit var mTextGradientColor: IntArray

    /** 文字渐变位置组  */
    private lateinit var mTextGradientPositions: FloatArray

    override fun onDraw(@NonNull canvas: Canvas, @NonNull paint: Paint, text: CharSequence?, start: Int, end: Int, x: Float, top: Int, y: Int, bottom: Int) {
        val linearGradient = if (mTextGradientOrientation == GRADIENT_ORIENTATION_VERTICAL) {
            LinearGradient(
                0f, 0f, 0f, paint.descent() - paint.ascent(),
                mTextGradientColor, mTextGradientPositions, Shader.TileMode.REPEAT
            )
        } else {
            LinearGradient(
                x, 0f, x + getMeasureTextWidth(), 0f,
                mTextGradientColor, mTextGradientPositions, Shader.TileMode.REPEAT
            )
        }
        paint.setShader(linearGradient)
        canvas.drawText(text!!, start, end, x, y.toFloat(), paint)
    }

    fun setTextGradientOrientation(orientation: Int): LinearGradientFontSpan {
        mTextGradientOrientation = orientation
        return this
    }

    fun setTextGradientColor(colors: IntArray): LinearGradientFontSpan {
        mTextGradientColor = colors
        return this
    }

    fun setTextGradientPositions(positions: FloatArray): LinearGradientFontSpan {
        mTextGradientPositions = positions
        return this
    }

    companion object {
        /** 水平渐变方向  */
        const val GRADIENT_ORIENTATION_HORIZONTAL: Int = LinearLayout.HORIZONTAL

        /** 垂直渐变方向  */
        const val GRADIENT_ORIENTATION_VERTICAL: Int = LinearLayout.VERTICAL

        /**
         * 构建一个文字渐变色的 Spannable 对象
         */
        fun buildLinearGradientSpannable(text: CharSequence?, colors: IntArray, positions: FloatArray, orientation: Int): SpannableStringBuilder {
            val builder = SpannableStringBuilder(text)
            val span = LinearGradientFontSpan()
                .setTextGradientColor(colors)
                .setTextGradientOrientation(orientation)
                .setTextGradientPositions(positions)
            builder.setSpan(span, 0, builder.length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
            return builder
        }
    }
}
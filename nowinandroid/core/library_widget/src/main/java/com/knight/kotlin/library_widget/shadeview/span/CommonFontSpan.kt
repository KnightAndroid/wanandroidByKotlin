package com.knight.kotlin.library_widget.shadeview.span

import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Paint.FontMetricsInt
import android.text.style.ReplacementSpan
import androidx.annotation.IntRange
import androidx.annotation.NonNull
import androidx.annotation.Nullable


/**
 * @author created by luguian
 * @organize
 * @Date 2024/9/5 9:57
 * @descript:通用 Span 类
 */
abstract class CommonFontSpan : ReplacementSpan() {
    /** 测量的文本宽度  */
    private var mMeasureTextWidth = 0f

    override fun getSize(@NonNull paint: Paint, text: CharSequence?, start: Int, end: Int, @Nullable fontMetricsInt: FontMetricsInt?): Int {
        mMeasureTextWidth = onMeasure(paint, fontMetricsInt, text, start, end)
        // 这段不可以去掉，字体高度没设置，会出现 draw 方法没有被调用的问题
        // 详情请见：https://stackoverflow.com/questions/20069537/replacementspans-draw-method-isnt-called
        val metrics = paint.fontMetricsInt
        if (fontMetricsInt != null) {
            fontMetricsInt.top = metrics.top
            fontMetricsInt.ascent = metrics.ascent
            fontMetricsInt.descent = metrics.descent
            fontMetricsInt.bottom = metrics.bottom
        }
        return mMeasureTextWidth.toInt()
    }

    override fun draw(@NonNull canvas: Canvas, text: CharSequence?, start: Int, end: Int, x: Float, top: Int, y: Int, bottom: Int, @NonNull paint:Paint) {
        val alpha = paint.alpha
        // 判断是否给画笔设置了透明度
        if (alpha != 255) {
            // 如果是则设置不透明
            paint.alpha = 255
        }
        onDraw(canvas, paint, text, start, end, x, top, y, bottom)
        // 绘制完成之后将画笔的透明度还原回去
        paint.alpha = alpha
    }

    fun onMeasure(
        @NonNull paint: Paint,
        @Nullable fontMetricsInt: FontMetricsInt?,
        text: CharSequence?,
        @IntRange(from = 0) start: Int,
        @IntRange(from = 0) end: Int
    ): Float {
        return paint.measureText(text, start, end)
    }

    abstract fun onDraw(
        @NonNull canvas: Canvas,
        @NonNull paint: Paint,
        text: CharSequence?,
        @IntRange(from = 0) start: Int,
        @IntRange(from = 0) end: Int,
        x: Float,
        top: Int,
        y: Int,
        bottom: Int
    )

    fun getMeasureTextWidth(): Float {
        return mMeasureTextWidth
    }
}
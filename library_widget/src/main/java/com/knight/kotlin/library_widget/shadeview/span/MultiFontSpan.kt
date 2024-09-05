package com.knight.kotlin.library_widget.shadeview.span

import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Paint.FontMetricsInt
import android.text.TextPaint
import android.text.style.ReplacementSpan
import androidx.annotation.NonNull
import java.util.Arrays
import kotlin.math.max


/**
 * @author created by luguian
 * @organize
 * @Date 2024/9/5 10:04
 * @descript:
 */
class MultiFontSpan(vararg replacementSpans: ReplacementSpan) : ReplacementSpan() {
    /** 测量的文本宽度  */
    private var mMeasureTextWidth = 0f

    private val mReplacementSpans: MutableList<ReplacementSpan> = Arrays.asList(*replacementSpans)

    override fun getSize(@NonNull paint: Paint, text: CharSequence?, start: Int, end: Int, fm: FontMetricsInt?): Int {
        for (replacementSpan in mReplacementSpans) {
            val size = replacementSpan.getSize(paint, text, start, end, fm)
            mMeasureTextWidth = max(mMeasureTextWidth.toDouble(), size.toDouble()).toFloat()
        }
        return mMeasureTextWidth.toInt()
    }

    override fun draw(@NonNull canvas: Canvas, text: CharSequence?, start: Int, end: Int, x: Float, top: Int, y: Int, bottom: Int, @NonNull paint: Paint) {
        for (replacementSpan in mReplacementSpans) {
            replacementSpan.draw(canvas, text, start, end, x, top, y, bottom, paint!!)
        }
    }

    override fun updateMeasureState(p: TextPaint) {
        super.updateMeasureState(p!!)
        for (replacementSpan in mReplacementSpans) {
            replacementSpan.updateMeasureState(p)
        }
    }

    override fun updateDrawState(ds: TextPaint) {
        super.updateDrawState(ds)
        for (replacementSpan in mReplacementSpans) {
            replacementSpan.updateDrawState(ds)
        }
    }
}
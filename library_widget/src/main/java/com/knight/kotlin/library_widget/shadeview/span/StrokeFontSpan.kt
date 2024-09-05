package com.knight.kotlin.library_widget.shadeview.span

import android.graphics.Canvas
import android.graphics.Paint
import androidx.annotation.NonNull


/**
 * @author created by luguian
 * @organize
 * @Date 2024/9/5 10:06
 * @descript:
 */
class StrokeFontSpan : CommonFontSpan() {
    /** 描边画笔  */
    private val mStrokePaint = Paint()

    private var mTextStrokeColor = 0
    private var mTextStrokeSize = 0

    override fun onDraw(@NonNull canvas: Canvas, @NonNull paint: Paint, text: CharSequence?, start: Int, end: Int, x: Float, top: Int, y: Int, bottom: Int) {
        mStrokePaint.set(paint)
        // 设置抗锯齿
        mStrokePaint.isAntiAlias = true
        // 设置防抖动
        mStrokePaint.isDither = true
        mStrokePaint.textSize = paint.textSize
        // 描边宽度
        mStrokePaint.strokeWidth = mTextStrokeSize.toFloat()
        mStrokePaint.style = Paint.Style.STROKE
        // 设置粗体
        //mStrokePaint.setFakeBoldText(true);
        mStrokePaint.color = mTextStrokeColor
        canvas.drawText(text!!, start, end, x, y.toFloat(), mStrokePaint)
    }

    fun setTextStrokeColor(color: Int): StrokeFontSpan {
        mTextStrokeColor = color
        return this
    }

    fun setTextStrokeSize(size: Int): StrokeFontSpan {
        mTextStrokeSize = size
        return this
    }

}
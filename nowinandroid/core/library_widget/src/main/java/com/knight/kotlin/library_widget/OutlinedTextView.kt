package com.knight.kotlin.library_widget


import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.util.AttributeSet
import android.view.ViewGroup
import android.widget.TextView


/**
 * Author:Knight
 * Time:2024/5/9 10:48
 * Description:OutlinedTextView 给TextView添加描边
 */
class OutlinedTextView@JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) :  androidx.appcompat.widget.AppCompatTextView(context, attrs, defStyleAttr)  {
     val borderText = TextView(context, attrs, defStyleAttr)
     var strokeWidth = 0
     var strokeColor : Int = 0X000000
     init {

         if (attrs != null) {
             val array = context.obtainStyledAttributes(attrs, R.styleable.OutlinedTextView)
             strokeColor = array.getColor(R.styleable.OutlinedTextView_stroke_color,strokeColor)
             strokeWidth = array.getDimensionPixelOffset(R.styleable.OutlinedTextView_stroke_width, strokeWidth)
             val tp1 = borderText.paint
             tp1.color = strokeColor
             tp1.strokeWidth = strokeWidth.toFloat() //设置描边宽度
             tp1.style = Paint.Style.STROKE
             borderText.setTextColor(strokeColor) //设置描边颜色
             borderText.setGravity(gravity)
             array.recycle()
         }
     }

    override fun setLayoutParams(params: ViewGroup.LayoutParams?) {
        super.setLayoutParams(params)
        borderText.setLayoutParams(params)
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val tt = borderText.getText()

        //两个TextView上的文字必须一致
        if (tt == null || tt != getText()) {
            borderText.text = getText()
            this.postInvalidate()
        }
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        borderText.measure(widthMeasureSpec, heightMeasureSpec)
    }

    override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
        super.onLayout(changed, left, top, right, bottom)
        borderText.layout(left, top, right, bottom)
    }

    override fun onDraw(canvas: Canvas) {
        borderText.draw(canvas)
        super.onDraw(canvas)
    }



}
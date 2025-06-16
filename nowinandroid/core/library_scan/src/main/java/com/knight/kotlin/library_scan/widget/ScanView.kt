package com.knight.kotlin.library_scan.widget

import android.animation.ValueAnimator
import android.animation.ValueAnimator.AnimatorUpdateListener
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Rect
import android.util.AttributeSet
import android.view.animation.LinearInterpolator
import com.knight.kotlin.library_scan.R

/**
 * Author:Knight
 * Time:2022/2/11 17:20
 * Description:ScanView
 */
class ScanView: BaseScanView {

    private var scanMaginWith = 0
    private var scanMaginheight = 0

    private var paint: Paint? = null
    private lateinit var scanLine: Bitmap
    private var scanRect: Rect = Rect()
    private var lineRect: Rect = Rect()

    //扫描线位置
    private var scanLineTop = 0

    //透明度
    private var alpha = 100

    private var bitmapHigh:Int = 0
    @JvmOverloads
    constructor(context: Context, attributeSet: AttributeSet? = null, defAttrStyle: Int = 0)
            : super(context, attributeSet, defAttrStyle) {
         init()
    }

    private fun init() {
        paint = Paint(Paint.ANTI_ALIAS_FLAG)
        scanLine = BitmapFactory.decodeResource(
            resources,
            R.drawable.scan_line_icon
        )
        bitmapHigh = scanLine.height
        scanRect = Rect()
        lineRect = Rect()
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        scanMaginWith = measuredWidth / 10
        scanMaginheight = measuredHeight shr 2
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        scanRect[scanMaginWith, scanMaginheight, width - scanMaginWith] = height - scanMaginheight
        startAnim()
        paint?.alpha = alpha
        lineRect[scanMaginWith, scanLineTop, width - scanMaginWith] = scanLineTop + bitmapHigh
        canvas.drawBitmap(scanLine, null, lineRect, paint)
    }

    override fun startAnim() {
        if (valueAnimator == null) {
            valueAnimator = ValueAnimator.ofInt(scanRect.top, scanRect.bottom)
            valueAnimator?.repeatCount = ValueAnimator.INFINITE
            valueAnimator?.repeatMode = ValueAnimator.RESTART
            valueAnimator?.duration = 4000
            valueAnimator?.interpolator = LinearInterpolator()
            valueAnimator?.addUpdateListener(AnimatorUpdateListener { animation ->
                scanLineTop = animation.animatedValue as Int
                val startHideHeight = (scanRect.bottom - scanRect.top) / 6
                alpha =
                    if (scanRect.bottom - scanLineTop <= startHideHeight) ((scanRect.bottom - scanLineTop).toDouble() / startHideHeight * 100).toInt() else 100
                postInvalidate()
            })
            valueAnimator?.start()
        }
    }

    override fun cancelAnim() {
        if (valueAnimator != null) {
            valueAnimator?.cancel()
        }
    }
}
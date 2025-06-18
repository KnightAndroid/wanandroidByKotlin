package com.knight.kotlin.library_widget

import android.content.Context
import android.util.AttributeSet
import android.widget.FrameLayout
import androidx.annotation.DrawableRes


/**
 * @author created by luguian
 * @organize
 * @Date 2025/6/18 16:25
 * @descript:循环移动
 */
class DoubleCloudView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null
) : FrameLayout(context, attrs) {

    private val backgroundLayer = InfiniteScrollImageView(context)
    private val foregroundLayer = InfiniteScrollImageView(context)

    init {
        // 先添加背景，再添加前景，确保前景在上层
        addView(backgroundLayer, LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT))
        addView(foregroundLayer, LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT))
    }

    fun setCloudImages(
        @DrawableRes backgroundResId: Int,
        @DrawableRes foregroundResId: Int,
        backgroundSpeed: Float = 0.5f,
        foregroundSpeed: Float = 1.2f
    ) {
        backgroundLayer.setBitmapResource(backgroundResId)
        backgroundLayer.scrollSpeed = backgroundSpeed

        foregroundLayer.setBitmapResource(foregroundResId)
        foregroundLayer.scrollSpeed = foregroundSpeed
    }

    fun start() {
        backgroundLayer.startScroll()
        foregroundLayer.startScroll()
    }

    fun stop() {
        backgroundLayer.stopScroll()
        foregroundLayer.stopScroll()
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        stop()
    }
}
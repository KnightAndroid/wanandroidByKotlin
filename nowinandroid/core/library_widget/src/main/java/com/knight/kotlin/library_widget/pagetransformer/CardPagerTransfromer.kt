package com.knight.kotlin.library_widget.pagetransformer

import android.view.View
import androidx.viewpager2.widget.ViewPager2

/**
 * Author:Knight
 * Time:2022/3/8 10:35
 * Description:CardPagerTransfromer
 */
class CardPagerTransfromer constructor(minScale:Float) : ViewPager2.PageTransformer {
    private val DEFAULT_CENTER = 0.5f

    private val mMinScale = minScale

    override fun transformPage(page: View, position: Float) {
        val pageWidth: Int = page.getWidth()
        val pageHeight: Int = page.getHeight()
        page.setPivotY((pageHeight shr 1).toFloat())
        page.setPivotX((pageWidth shr 1).toFloat())
        if (position < -1) { // [-Infinity,-1)
            // This page is way off-screen to the left.
            page.setScaleX(mMinScale)
            page.setScaleY(mMinScale)
            page.setPivotX(pageWidth.toFloat())
        } else if (position <= 1) {
            // [-1,1]
            // Modify the default slide transition to shrink the page as well
            if (position < 0) {
                //1-2:1[0,-1] ;2-1:1[-1,0]
                val scaleFactor = (1 + position) * (1 - mMinScale) + mMinScale
                page.setScaleX(scaleFactor)
                page.setScaleY(scaleFactor)
                page.setPivotX(pageWidth * (DEFAULT_CENTER + DEFAULT_CENTER * -position))
            } else {
                //1-2:2[1,0] ;2-1:2[0,1]
                val scaleFactor = (1 - position) * (1 - mMinScale) + mMinScale
                page.setScaleX(scaleFactor)
                page.setScaleY(scaleFactor)
                page.setPivotX(pageWidth * ((1 - position) * DEFAULT_CENTER))
            }
        } else {
            // (1,+Infinity]
            page.setPivotX(0f)
            page.setScaleX(mMinScale)
            page.setScaleY(mMinScale)
        }

    }
}
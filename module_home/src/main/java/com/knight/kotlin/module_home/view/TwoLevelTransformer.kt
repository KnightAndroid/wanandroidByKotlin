package com.knight.kotlin.module_home.view

import android.view.View
import android.view.ViewGroup
import androidx.core.view.ViewCompat
import androidx.viewpager2.widget.ViewPager2
import com.knight.kotlin.library_widget.CompatToolBar
import com.knight.kotlin.module_home.R
import kotlin.math.abs
import kotlin.math.min

/**
 * Author:Knight
 * Time:2024/4/18 10:48
 * Description:TwoLevelTransformer
 */
class TwoLevelTransformer(val mViewPager:ViewPager2,val expendPointView:ExpendPointView) : ViewPager2.PageTransformer {
    private lateinit var mToolbar: CompatToolBar
    private lateinit var rl_home: ViewGroup
    private var fromFloorPage = true

    /** 以下为可调参数，请根据实际情况进行调整  */ //floorPageVisibleOffset：顶页可见部分百分比（其余是底页可见部分）
    private val floorPageVisibleOffset = 1.0f


    override fun transformPage(page: View, position: Float) {
        when (page.id) {
            4 -> doInFloorPage(page, position)
            6 -> doInCeilPage(page, position)
        }
        ViewCompat.setElevation(page, mViewPager.offscreenPageLimit - position)
    }


    /**
     * 实现对底部页面的操作
     * @param page 页面对象
     * @param position 位移量
     */
    private fun doInFloorPage(page: View, position: Float) {
        val drawableID: Int
        mToolbar = page.findViewById<CompatToolBar>(R.id.home_include_toolbar)
        rl_home = page.findViewById<ViewGroup>(R.id.rl_home)
        val offset: Float //设置底部偏移（在大于某值时不移动，使底页部分露出）
        if (position > -floorPageVisibleOffset) {
            offset = 0f
        } else {
            offset =
                page.height * (abs(position) - floorPageVisibleOffset)
        }
        page.translationY = offset
        val alpha: Float //设置底页的透明度（三个点完全展开后，开始渐变）
        alpha = if (abs(position.toDouble()) < 0.21f) {
            1f
        } else {
            Math.max(expendPointView!!.getBackgroundAlpha() / 255f, 0.5f)
        }
        mToolbar.setAlpha(alpha)
        rl_home.setAlpha(alpha)
    }


    /**
     * 实现对顶部页面的操作
     * @param page 页面对象
     * @param position 位移量
     */
    private fun doInCeilPage(page: View, position: Float) {
        if (getFromFloorPage()) {
            //设置顶页缩放动画，以及透明度渐变
            val translationY = (page.height - 300) * position
            page.translationY = -translationY
            val scaleFactor = min((1f - position * 0.3f).toDouble(), 1.0).toFloat()
            page.scaleX = scaleFactor
            page.scaleY = scaleFactor
        }
        page.setAlpha((1f - abs(position.toDouble())).toFloat())
    }

    /**
     * 设置起始滑动页面的标志
     * @param fromFloorPage 起始页面标志
     * true：从底页开始滑动
     * false：从顶页开始滑动
     */
    fun setFromFloorPage(fromFloorPage: Boolean) {
        this.fromFloorPage = fromFloorPage
    }

    /**
     * 设置起始滑动页面的标志
     * @return 起始页面标志
     */
    fun getFromFloorPage(): Boolean {
        return fromFloorPage
    }
}
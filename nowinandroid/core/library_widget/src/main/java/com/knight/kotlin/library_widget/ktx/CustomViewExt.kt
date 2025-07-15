package com.knight.kotlin.library_util

import android.content.Context
import android.graphics.Color
import android.view.animation.AccelerateInterpolator
import android.view.animation.DecelerateInterpolator
import androidx.viewpager2.widget.ViewPager2
import com.core.library_common.util.dp2px
import com.knight.kotlin.library_base.BaseApp
import com.knight.kotlin.library_common.util.CacheUtils
import com.knight.kotlin.library_widget.ScaleTransitionPagerTitleView
import net.lucode.hackware.magicindicator.MagicIndicator
import net.lucode.hackware.magicindicator.buildins.commonnavigator.CommonNavigator
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.CommonNavigatorAdapter
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerIndicator
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerTitleView
import net.lucode.hackware.magicindicator.buildins.commonnavigator.indicators.LinePagerIndicator
import net.lucode.hackware.magicindicator.buildins.commonnavigator.indicators.WrapPagerIndicator
import net.lucode.hackware.magicindicator.buildins.commonnavigator.titles.SimplePagerTitleView

/**
 * Author:Knight
 * Time:2021/12/29 17:07
 * Description:CustomViewExt
 */

fun MagicIndicator.bindViewPager2(
    viewPager: ViewPager2,
    mDataList: MutableList<String> = arrayListOf(),
    mStringList: MutableList<String> = arrayListOf(),
    action: (index: Int) -> Unit = {}
){
    val commonNavigator = CommonNavigator(BaseApp.application)
    commonNavigator.leftPadding = 16.dp2px()
    commonNavigator.scrollPivotX = 0.35f
    commonNavigator.adapter = object : CommonNavigatorAdapter(){
        override fun getCount():Int {
            return if (mDataList.size != 0) {
                mDataList.size
            } else {
                mStringList.size
            }
        }

        override fun getTitleView(context: Context, index: Int): IPagerTitleView {
            return SimplePagerTitleView(context).apply {
                text = if (mDataList.size != 0) {
                    mDataList[index]
                } else {
                    mStringList[index]
                }
                textScaleX = CacheUtils.getSystemFontSize()
                normalColor = Color.parseColor("#999999")
                selectedColor = Color.parseColor("#ffffff")
                setOnClickListener {
                    viewPager.currentItem = index
                    action.invoke(index)
                }
            }
        }

        override fun getIndicator(context: Context): IPagerIndicator {
            return WrapPagerIndicator(context).apply {
                roundRadius = 6.dp2px().toFloat()
                fillColor = CacheUtils.getThemeColor()
            }
        }
    }
    this.navigator = commonNavigator
    viewPager.registerOnPageChangeCallback(object:ViewPager2.OnPageChangeCallback(){

        override fun onPageSelected(position:Int) {
            super.onPageSelected(position)
            this@bindViewPager2.onPageSelected(position)

        }
        override fun onPageScrolled(
            position: Int,
            positionOffset: Float,
            positionOffsetPixels: Int
        ) {
            super.onPageScrolled(position, positionOffset, positionOffsetPixels)
            this@bindViewPager2.onPageScrolled(position,positionOffset,positionOffsetPixels)
        }
        override fun onPageScrollStateChanged(state:Int) {
            super.onPageScrollStateChanged(state)
            this@bindViewPager2.onPageScrollStateChanged(state)
        }


    })
}


fun MagicIndicator.bindWechatViewPager2(
    viewPager: ViewPager2,
    mDataList: MutableList<String> = arrayListOf(),
    mStringList: MutableList<String> = arrayListOf(),
    action: (index: Int) -> Unit = {}
){
    val commonNavigator = CommonNavigator(BaseApp.application)
    commonNavigator.adapter = object : CommonNavigatorAdapter(){
        override fun getCount():Int {
            return if (mDataList.size != 0) {
                mDataList.size
            } else {
                mStringList.size
            }
        }

        override fun getTitleView(context: Context, index: Int): IPagerTitleView {
            return ScaleTransitionPagerTitleView(context).apply {
                text = if (mDataList.size != 0) {
                    mDataList[index]
                } else {
                    mStringList[index]
                }
                textSize = 18f
                textScaleX = CacheUtils.getSystemFontSize()
                if (CacheUtils.getNormalDark()) {
                    normalColor = Color.parseColor("#D3D3D3")
                    selectedColor = Color.parseColor("#D3D3D3")
                } else {
                    normalColor = CacheUtils.getThemeColor()
                    selectedColor = CacheUtils.getThemeColor()
                }

                setOnClickListener {
                    viewPager.currentItem = index
                    action.invoke(index)
                }
            }
        }

        override fun getIndicator(context: Context): IPagerIndicator {
            return LinePagerIndicator(context).apply {
                mode = LinePagerIndicator.MODE_MATCH_EDGE
                lineHeight = 3.dp2px().toFloat()
                lineWidth = 30.dp2px().toFloat()
                roundRadius = 6.dp2px().toFloat()
                startInterpolator = AccelerateInterpolator()
                endInterpolator = DecelerateInterpolator(2.0f)
                setColors(CacheUtils.getThemeColor())

            }
        }
    }
    this.navigator = commonNavigator
    viewPager.registerOnPageChangeCallback(object:ViewPager2.OnPageChangeCallback(){

        override fun onPageSelected(position:Int) {
            super.onPageSelected(position)
            this@bindWechatViewPager2.onPageSelected(position)

        }
        override fun onPageScrolled(
            position: Int,
            positionOffset: Float,
            positionOffsetPixels: Int
        ) {
            super.onPageScrolled(position, positionOffset, positionOffsetPixels)
            this@bindWechatViewPager2.onPageScrolled(position,positionOffset,positionOffsetPixels)
        }
        override fun onPageScrollStateChanged(state:Int) {
            super.onPageScrollStateChanged(state)
            this@bindWechatViewPager2.onPageScrollStateChanged(state)
        }


    })








}





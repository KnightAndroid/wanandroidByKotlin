package com.knight.kotlin.library_util

import android.graphics.Color
import android.graphics.Rect
import android.os.Build
import android.view.View
import android.widget.TextView
import androidx.annotation.NonNull
import androidx.annotation.RequiresApi
import androidx.core.widget.NestedScrollView
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2


/**
 * Author:Knight
 * Time:2021/12/29 17:02
 * Description:ViewInitUtils
 * 一些View的初始化
 */
object ViewInitUtils {

    fun setViewPager2Init(activity: FragmentActivity, viewPager2: ViewPager2, fragments:List<Fragment>, isOffscreenPageLimit:Boolean,isUserInputEnabled:Boolean) {
        //是否左右滑动
        viewPager2.isUserInputEnabled = isUserInputEnabled
        if (isOffscreenPageLimit) {
            //下面方法能解决fragment切换白屏问题
            viewPager2.offscreenPageLimit = fragments.size
        }

        viewPager2.adapter = object : FragmentStateAdapter(activity) {
            @NonNull
            override fun createFragment(position: Int): Fragment {
                return fragments[position]
            }

            override fun getItemCount(): Int {
                return fragments.size
            }

        }
    }



    @RequiresApi(api = Build.VERSION_CODES.M)
    fun avoidHintColor(view: View) {
        if (view is TextView) {
            (view).highlightColor = Color.parseColor("#00000000")
        }
    }


    /**
     * 判断view是否进入屏幕内
     */
    fun isViewVisibleOnScreen(view: View): Boolean {
        val rect = Rect()
        return view.isShown && view.getGlobalVisibleRect(rect)
    }


    /**
     *
     *  普通判断 View 是否在 NestedScrollView 的“可视区域”
     */
    fun isViewNormalVisibleInScroll(scrollView: NestedScrollView, view: View): Boolean {
        val scrollBounds = Rect()
        scrollView.getHitRect(scrollBounds)
        return view.getLocalVisibleRect(scrollBounds)
    }
    /**
     *
     * 判断 View 是否在 NestedScrollView 的“可视区域” recycleview
     */
    fun isViewVisibleInScroll(scrollView: NestedScrollView, view: View): Boolean {
        val scrollBounds = Rect()
        scrollView.getHitRect(scrollBounds) // ScrollView 可视区域，相对父布局
        val top = view.top
        val bottom = view.bottom

        // View 是否在 ScrollView 可视区域
        return bottom >= scrollBounds.top && top <= scrollBounds.bottom
    }


}
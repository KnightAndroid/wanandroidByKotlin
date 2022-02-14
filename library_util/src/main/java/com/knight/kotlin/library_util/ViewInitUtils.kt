package com.knight.kotlin.library_util

import android.graphics.Color
import android.os.Build
import android.view.View
import android.widget.TextView
import androidx.annotation.NonNull
import androidx.annotation.RequiresApi
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


}
package com.knight.kotlin.module_main.utils

import androidx.annotation.NonNull
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2


/**
 * Author:Knight
 * Time:2021/12/23 18:30
 * Description:ViewSetUtils
 */
object ViewSetUtils {

    fun setIsUserInputEnable(activity:FragmentActivity,viewPager2:ViewPager2,fragments:List<Fragment>,isUserInputEnabled:Boolean) {
        viewPager2.isUserInputEnabled = isUserInputEnabled
        viewPager2.offscreenPageLimit = fragments.size
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
}
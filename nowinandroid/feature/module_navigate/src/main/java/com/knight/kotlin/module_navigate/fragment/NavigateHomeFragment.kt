package com.knight.kotlin.module_navigate.fragment

import androidx.fragment.app.Fragment
import com.knight.kotlin.library_base.fragment.BaseFragment
import com.core.library_base.route.RouteFragment
import com.core.library_base.vm.EmptyViewModel
import com.knight.kotlin.library_util.ViewInitUtils
import com.knight.kotlin.library_util.bindWechatViewPager2
import com.knight.kotlin.module_navigate.R
import com.knight.kotlin.module_navigate.databinding.NavigateHomeFragmentBinding
import com.wyjson.router.annotation.Route
import dagger.hilt.android.AndroidEntryPoint

/**
 * Author:Knight
 * Time:2021/12/23 17:28
 * Description:NavigateHomeFragment
 */
@AndroidEntryPoint
@Route(path = RouteFragment.Navigate.NavigateHomeFragment)
class NavigateHomeFragment : BaseFragment<NavigateHomeFragmentBinding, EmptyViewModel>() {


    //存放体系和导航两个Fragment
    val mFragments: MutableList<Fragment> = mutableListOf()

    override fun initObserver() {

    }

    override fun initRequestData() {
        val mTitleList: MutableList<String> = ArrayList()
        mTitleList.add(getString(R.string.navigate_hierachy_tab_navigate))
        mTitleList.add(getString(R.string.navigate_hierachy_tab_hierachy))
        mFragments.clear()
        mFragments.add(NavigateFragment())
        mFragments.add(HierachyFragment())
        ViewInitUtils.setViewPager2Init(requireActivity(),mBinding.hierachyNavigateViewpager,mFragments,
            isOffscreenPageLimit = true,
            isUserInputEnabled = false
        )
        mBinding.hierachyNavigateIndicator.bindWechatViewPager2(mBinding.hierachyNavigateViewpager,mTitleList)
    }

    override fun setThemeColor(isDarkMode: Boolean) {
    }

    override fun reLoadData() {

    }

    override fun NavigateHomeFragmentBinding.initView() {

    }


}
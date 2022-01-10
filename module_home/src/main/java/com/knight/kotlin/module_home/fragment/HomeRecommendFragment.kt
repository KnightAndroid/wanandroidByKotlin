package com.knight.kotlin.module_home.fragment

import androidx.fragment.app.viewModels
import com.alibaba.android.arouter.facade.annotation.Route
import com.knight.kotlin.library_base.fragment.BaseFragment
import com.knight.kotlin.library_base.route.RouteFragment
import com.knight.kotlin.library_base.vm.EmptyViewModel
import com.knight.kotlin.module_home.databinding.HomeRecommendFragmentBinding
import dagger.hilt.android.AndroidEntryPoint

/**
 * Author:Knight
 * Time:2021/12/29 15:46
 * Description:HomeRecommendFragment
 */
@AndroidEntryPoint
@Route(path = RouteFragment.Home.RecommendFragment)
class HomeRecommendFragment:BaseFragment<HomeRecommendFragmentBinding,EmptyViewModel>() {

    override val mViewModel: EmptyViewModel by viewModels()
    override fun setThemeColor(isDarkMode: Boolean) {
    }

    override fun HomeRecommendFragmentBinding.initView() {
    }

    override fun initObserver() {
    }

    override fun initRequestData() {

    }
}
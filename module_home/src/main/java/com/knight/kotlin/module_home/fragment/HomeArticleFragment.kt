package com.knight.kotlin.module_home.fragment

import androidx.fragment.app.viewModels
import com.alibaba.android.arouter.facade.annotation.Route
import com.knight.kotlin.library_base.fragment.BaseFragment
import com.knight.kotlin.library_base.route.RouteFragment
import com.knight.kotlin.library_base.vm.EmptyViewModel
import com.knight.kotlin.module_home.databinding.HomeArticleFragmentBinding
import dagger.hilt.android.AndroidEntryPoint

/**
 * Author:Knight
 * Time:2021/12/29 16:36
 * Description:HomeArticleFragment
 */
@AndroidEntryPoint
@Route(path = RouteFragment.Home.HomeArticleFragment)
class HomeArticleFragment:BaseFragment<HomeArticleFragmentBinding,EmptyViewModel>() {
    override val mViewModel: EmptyViewModel by viewModels()
    override fun setThemeColor(isDarkMode: Boolean) {

    }

    override fun HomeArticleFragmentBinding.initView() {
    }

    override fun initObserver() {
    }

    override fun initRequestData() {
    }
}
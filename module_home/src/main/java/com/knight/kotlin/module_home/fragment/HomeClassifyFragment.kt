package com.knight.kotlin.module_home.fragment

import com.knight.kotlin.library_base.fragment.BaseFragment
import com.knight.kotlin.library_base.route.RouteFragment
import com.knight.kotlin.library_base.vm.EmptyViewModel
import com.knight.kotlin.module_home.databinding.HomeClassifyFragmentBinding
import com.wyjson.router.annotation.Route
import dagger.hilt.android.AndroidEntryPoint

/**
 * Author:Knight
 * Time:2024/4/18 10:42
 * Description:HomeClassifyFragment
 */
@AndroidEntryPoint
@Route(path = RouteFragment.Home.HomeClassifyFragment)
class HomeClassifyFragment : BaseFragment<HomeClassifyFragmentBinding,EmptyViewModel>() {
    override fun setThemeColor(isDarkMode: Boolean) {

    }

    override fun initObserver() {

    }

    override fun initRequestData() {

    }

    override fun reLoadData() {

    }

    override fun HomeClassifyFragmentBinding.initView() {
        mBinding.root.rotation = 180f

    }

}
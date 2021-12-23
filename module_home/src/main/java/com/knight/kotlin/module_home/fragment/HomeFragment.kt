package com.knight.kotlin.module_home.fragment

import androidx.fragment.app.viewModels
import com.alibaba.android.arouter.facade.annotation.Route
import com.knight.kotlin.library_base.fragment.BaseFragment
import com.knight.kotlin.library_base.route.RouteFragment
import com.knight.kotlin.library_base.vm.EmptyViewModel
import com.knight.kotlin.module_home.databinding.HomeFragmentBinding
import dagger.hilt.android.AndroidEntryPoint

/**
 * Author:Knight
 * Time:2021/12/22 19:33
 * Description:HomeFragment
 */
@AndroidEntryPoint
@Route(path = RouteFragment.Home.HomeFragment)
class HomeFragment :BaseFragment<HomeFragmentBinding,EmptyViewModel>(){

    override val mViewModel: EmptyViewModel by viewModels()
    override fun HomeFragmentBinding.initView() {

    }

    override fun initObserver() {

    }

    override fun initRequestData() {

    }
}
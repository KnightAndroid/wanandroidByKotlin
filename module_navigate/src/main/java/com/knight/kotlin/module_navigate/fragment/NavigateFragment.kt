package com.knight.kotlin.module_navigate.fragment

import androidx.fragment.app.viewModels
import com.alibaba.android.arouter.facade.annotation.Route
import com.knight.kotlin.library_base.fragment.BaseFragment
import com.knight.kotlin.library_base.route.RouteFragment
import com.knight.kotlin.library_base.vm.EmptyViewModel
import com.knight.kotlin.module_navigate.databinding.NavigateFragmentBinding
import dagger.hilt.android.AndroidEntryPoint

/**
 * Author:Knight
 * Time:2021/12/23 17:28
 * Description:NavigateFragment
 */
@AndroidEntryPoint
@Route(path = RouteFragment.Navigate.NavigateFragment)
class NavigateFragment :BaseFragment<NavigateFragmentBinding, EmptyViewModel>() {
    override val mViewModel: EmptyViewModel by viewModels()
    override fun NavigateFragmentBinding.initView() {

    }

    override fun initObserver() {

    }

    override fun initRequestData() {

    }
}
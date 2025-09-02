package com.knight.kotlin.module_constellate.fragment

import com.core.library_base.route.RouteFragment
import com.core.library_base.vm.EmptyViewModel
import com.knight.kotlin.library_base.fragment.BaseFragment
import com.knight.kotlin.module_constellate.databinding.ConstellateMonthFortuneFragmentBinding
import com.wyjson.router.annotation.Route
import dagger.hilt.android.AndroidEntryPoint

/**
 * @Description
 * @Author knight
 * @Time 2025/9/2 21:19
 *
 */
@AndroidEntryPoint
@Route(path = RouteFragment.Constellate.ConstellateFortuneMonthFragment)
class ConstellateMonthFortuneFragment:BaseFragment<ConstellateMonthFortuneFragmentBinding,EmptyViewModel>() {
    override fun ConstellateMonthFortuneFragmentBinding.initView() {

    }

    override fun setThemeColor(isDarkMode: Boolean) {

    }

    override fun initObserver() {

    }

    override fun initRequestData() {

    }

    override fun reLoadData() {

    }
}
package com.knight.kotlin.module_constellate.fragment

import com.core.library_base.route.RouteFragment
import com.core.library_base.vm.EmptyViewModel
import com.knight.kotlin.library_base.fragment.BaseFragment
import com.knight.kotlin.module_constellate.databinding.ConstellateWeekFortuneFragmentBinding
import com.wyjson.router.annotation.Route
import dagger.hilt.android.AndroidEntryPoint

/**
 * @Description
 * @Author knight
 * @Time 2025/9/2 20:33
 *
 */
@AndroidEntryPoint
@Route(path = RouteFragment.Constellate.ConstellateFortuneWeekFragment)
class ConstellateWeekFortuneFragment:BaseFragment<ConstellateWeekFortuneFragmentBinding,EmptyViewModel>() {
    override fun ConstellateWeekFortuneFragmentBinding.initView() {

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
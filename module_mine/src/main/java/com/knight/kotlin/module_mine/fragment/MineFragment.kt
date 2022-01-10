package com.knight.kotlin.module_mine.fragment

import androidx.fragment.app.viewModels
import com.alibaba.android.arouter.facade.annotation.Route
import com.knight.kotlin.library_base.fragment.BaseFragment
import com.knight.kotlin.library_base.route.RouteFragment
import com.knight.kotlin.library_base.vm.EmptyViewModel
import com.knight.kotlin.module_mine.databinding.MineFragmentBinding
import dagger.hilt.android.AndroidEntryPoint

/**
 * Author:Knight
 * Time:2021/12/23 18:12
 * Description:MineFragment
 */
@AndroidEntryPoint
@Route(path = RouteFragment.Mine.MineFragment)
class MineFragment: BaseFragment<MineFragmentBinding, EmptyViewModel>() {
    override val mViewModel: EmptyViewModel by viewModels()
    override fun MineFragmentBinding.initView() {
    }

    override fun initObserver() {
    }

    override fun initRequestData() {
    }

    override fun setThemeColor(isDarkMode: Boolean) {

    }
}
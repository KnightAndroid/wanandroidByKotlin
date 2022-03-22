package com.knight.kotlin.module_square.fragment

import androidx.fragment.app.viewModels
import com.alibaba.android.arouter.facade.annotation.Route
import com.knight.kotlin.library_base.fragment.BaseFragment
import com.knight.kotlin.library_base.route.RouteFragment
import com.knight.kotlin.library_base.vm.EmptyViewModel
import com.knight.kotlin.library_util.toast
import com.knight.kotlin.module_square.databinding.SquareFragmentBinding
import dagger.hilt.android.AndroidEntryPoint

/**
 * Author:Knight
 * Time:2021/12/23 15:59
 * Description:SquareFragment
 */
@AndroidEntryPoint
@Route(path = RouteFragment.Square.SquareFragment)
class SquareFragment:BaseFragment<SquareFragmentBinding, EmptyViewModel>() {
    override val mViewModel: EmptyViewModel by viewModels()
    override fun SquareFragmentBinding.initView() {
    }

    override fun initObserver() {

    }

    override fun initRequestData() {
        toast("这是广场页")
    }

    override fun setThemeColor(isDarkMode: Boolean) {
    }

    override fun reLoadData() {

    }
}
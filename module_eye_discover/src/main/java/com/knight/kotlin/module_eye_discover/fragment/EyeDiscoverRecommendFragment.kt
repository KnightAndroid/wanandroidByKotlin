package com.knight.kotlin.module_eye_discover.fragment

import com.knight.kotlin.library_base.fragment.BaseFragment
import com.knight.kotlin.library_base.route.RouteFragment
import com.knight.kotlin.library_base.vm.EmptyViewModel
import com.knight.kotlin.module_eye_discover.databinding.EyeDiscoverRecommendFragmentBinding
import com.wyjson.router.annotation.Route
import dagger.hilt.android.AndroidEntryPoint


/**
 * @author created by luguian
 * @organize
 * @Date 2024/12/10 17:20
 * @descript:
 */
@AndroidEntryPoint
@Route(path = RouteFragment.Discover.DiscoverRecommendFragment)
class EyeDiscoverRecommendFragment : BaseFragment<EyeDiscoverRecommendFragmentBinding,EmptyViewModel>() {
    override fun setThemeColor(isDarkMode: Boolean) {

    }

    override fun initObserver() {

    }

    override fun initRequestData() {

    }

    override fun reLoadData() {

    }

    override fun EyeDiscoverRecommendFragmentBinding.initView() {

    }
}
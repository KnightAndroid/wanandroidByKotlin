package com.knight.kotlin.module_eye_discover.fragment

import com.knight.kotlin.library_base.fragment.BaseFragment
import com.knight.kotlin.library_base.route.RouteFragment
import com.knight.kotlin.module_eye_discover.databinding.EyeDiscoverSearchItemFragmentBinding
import com.knight.kotlin.module_eye_discover.vm.EyeDiscoverSearchResultItemVm
import com.scwang.smart.refresh.layout.api.RefreshLayout
import com.scwang.smart.refresh.layout.listener.OnLoadMoreListener

import com.wyjson.router.annotation.Route
import dagger.hilt.android.AndroidEntryPoint

/**
 * @Description 搜索结果item项
 * @Author knight
 * @Time 2025/1/5 16:59
 *
 */
@AndroidEntryPoint
@Route(path = RouteFragment.Discover.DiscoverSearchResultItemFragment)
class EyeDiscoverSearchResultItemFragment : BaseFragment<EyeDiscoverSearchItemFragmentBinding,EyeDiscoverSearchResultItemVm>(),
    OnLoadMoreListener {




    private lateinit var type:String

    override fun setThemeColor(isDarkMode: Boolean) {

    }

    override fun initObserver() {

    }

    override fun initRequestData() {

    }

    override fun reLoadData() {

    }

    override fun EyeDiscoverSearchItemFragmentBinding.initView() {
        type = arguments?.getString("type","") ?:""
        mBinding.searchResultRefreshLayout.setOnLoadMoreListener(this@EyeDiscoverSearchResultItemFragment)
    }

    override fun onLoadMore(refreshLayout: RefreshLayout) {

    }
}
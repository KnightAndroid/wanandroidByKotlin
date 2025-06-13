package com.knight.kotlin.module_eye_discover.fragment

import android.view.LayoutInflater
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.knight.kotlin.library_base.fragment.BaseFragment
import com.knight.kotlin.library_base.ktx.init
import com.knight.kotlin.library_base.route.RouteFragment
import com.knight.kotlin.module_eye_discover.adapter.EyeDiscoverAdapter
import com.knight.kotlin.module_eye_discover.databinding.EyeDiscoverListFooterItemBinding
import com.knight.kotlin.module_eye_discover.databinding.EyeDiscoverScollListFragmentBinding
import com.knight.kotlin.module_eye_discover.vm.EyeDiscoverScrollListVm
import com.scwang.smart.refresh.layout.api.RefreshLayout
import com.scwang.smart.refresh.layout.listener.OnRefreshListener
import com.wyjson.router.annotation.Route
import dagger.hilt.android.AndroidEntryPoint


/**
 * @author created by luguian
 * @organize
 * @Date 2024/12/10 15:56
 * @descript:发现滚动列表页面
 */
@AndroidEntryPoint
@Route(path = RouteFragment.Discover.DiscoverScollListFragment)
class EyeDiscoverScollListFragment : BaseFragment<EyeDiscoverScollListFragmentBinding,EyeDiscoverScrollListVm>(), OnRefreshListener {


    //发现适配器
    private val mEyeDiscoverAdapter: EyeDiscoverAdapter by lazy { EyeDiscoverAdapter(
        requireActivity(),mutableListOf()) }





    override fun setThemeColor(isDarkMode: Boolean) {

    }

    override fun initObserver() {

    }

    override fun initRequestData() {
        mViewModel.getDiscoverData().observerKt {
            mBinding.discoverListRefreshLayout.finishRefresh()

            mEyeDiscoverAdapter.submitList(it)
            if (mBinding.rvDiscoverList.footerCount == 0) {
                mBinding.rvDiscoverList.addFooterView(getFootView())
            }
        }

        mViewModel.getNav("discovery").observerKt {

        }
    }

    override fun reLoadData() {

    }

    override fun EyeDiscoverScollListFragmentBinding.initView() {

        discoverListRefreshLayout.setOnRefreshListener(this@EyeDiscoverScollListFragment)
        discoverListRefreshLayout.setEnableLoadMore(false)

        rvDiscoverList.init(
            LinearLayoutManager(activity),
            mEyeDiscoverAdapter,
            true
        )

    }


    override fun onRefresh(refreshLayout: RefreshLayout) {
        initRequestData()
    }

    /**
     * 脚步View
     */
    private fun getFootView(): View {
        return EyeDiscoverListFooterItemBinding.inflate(LayoutInflater.from(activity)).root

    }
}
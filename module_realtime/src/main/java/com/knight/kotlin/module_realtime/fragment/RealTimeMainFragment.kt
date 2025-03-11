package com.knight.kotlin.module_realtime.fragment

import androidx.recyclerview.widget.LinearLayoutManager
import com.knight.kotlin.library_base.fragment.BaseFragment
import com.knight.kotlin.library_base.route.RouteFragment
import com.knight.kotlin.library_widget.ktx.init
import com.knight.kotlin.module_realtime.adapter.RealTimeHotMainAdapter
import com.knight.kotlin.module_realtime.databinding.RealtimeMainFragmentBinding
import com.knight.kotlin.module_realtime.vm.RealTimeHomeVm
import com.wyjson.router.annotation.Route
import dagger.hilt.android.AndroidEntryPoint


/**
 * @author created by luguian
 * @organize
 * @Date 2025/3/11 16:43
 * @descript:
 */

@AndroidEntryPoint
@Route(path = RouteFragment.RealTime.RealTimeMainFragment)
class RealTimeMainFragment : BaseFragment<RealtimeMainFragmentBinding, RealTimeHomeVm>() {

    //推荐文章适配器
    private val mRealTimeHotMainAdapter: RealTimeHotMainAdapter by lazy { RealTimeHotMainAdapter() }

    override fun setThemeColor(isDarkMode: Boolean) {

    }

    override fun initObserver() {

    }

    override fun initRequestData() {
        mViewModel.getMainBaiduRealTime().observerKt {
            mRealTimeHotMainAdapter.submitList(it.cards.get(0).content)
        }
    }

    override fun reLoadData() {

    }

    override fun RealtimeMainFragmentBinding.initView() {
        rvHotList.init(LinearLayoutManager(requireActivity()), mRealTimeHotMainAdapter)
    }
}
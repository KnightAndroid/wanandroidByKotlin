package com.knight.kotlin.module_realtime.fragment

import androidx.recyclerview.widget.LinearLayoutManager
import com.knight.kotlin.library_base.fragment.BaseFragment
import com.core.library_base.route.RouteFragment
import com.knight.kotlin.library_widget.ktx.init
import com.knight.kotlin.module_realtime.adapter.HotRankMainAdapter
import com.knight.kotlin.module_realtime.databinding.RealtimeTextFragmentBinding
import com.knight.kotlin.module_realtime.enum.HotListEnum
import com.knight.kotlin.module_realtime.ktx.handleAdapterClick
import com.knight.kotlin.module_realtime.vm.RealTimeTextVm
import com.wyjson.router.annotation.Route
import dagger.hilt.android.AndroidEntryPoint


/**
 * @author created by luguian
 * @organize
 * @Date 2025/3/11 15:31
 * @descript:热搜 惹梗 财经 民生 界面
 */
@AndroidEntryPoint
@Route(path = RouteFragment.RealTime.RealTimeTextFragment)
class RealTimeTextFragment: BaseFragment<RealtimeTextFragmentBinding, RealTimeTextVm>() {


    private lateinit var typeName:String

    private val mRealTimeHotMainAdapter: HotRankMainAdapter by lazy {
        HotRankMainAdapter(HotListEnum.valueOf(typeName))
    }
    override fun setThemeColor(isDarkMode: Boolean) {

    }

    override fun initObserver() {

    }

    override fun initRequestData() {
        mViewModel.getDataByTab("wise",typeName.lowercase()).observerKt {
            if (typeName == HotListEnum.REALTIME.name) {
                it.cards.get(0).content.addAll(0,it.cards.get(0).topContent)
            }
            mRealTimeHotMainAdapter.submitList(it.cards.get(0).content)
        }
    }

    override fun reLoadData() {

    }

    override fun RealtimeTextFragmentBinding.initView() {
        typeName = arguments?.getString("typeName") ?:""
        rvHotRank.init(LinearLayoutManager(requireActivity()),mRealTimeHotMainAdapter,true)
        handleAdapterClick(mRealTimeHotMainAdapter)
    }
}
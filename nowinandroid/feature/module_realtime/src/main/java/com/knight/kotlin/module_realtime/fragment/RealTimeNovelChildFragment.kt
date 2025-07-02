package com.knight.kotlin.module_realtime.fragment

import androidx.recyclerview.widget.LinearLayoutManager
import com.knight.kotlin.library_base.fragment.BaseFragment
import com.core.library_base.route.RouteFragment
import com.knight.kotlin.library_widget.ktx.init
import com.knight.kotlin.module_realtime.adapter.HotRankNovelMovieAdapter
import com.knight.kotlin.module_realtime.databinding.RealtimeNovelChildFragmentBinding
import com.knight.kotlin.module_realtime.ktx.handleAdapterClick
import com.knight.kotlin.module_realtime.vm.RealTimeNovelVm
import com.wyjson.router.annotation.Route
import dagger.hilt.android.AndroidEntryPoint


/**
 * @author created by luguian
 * @organize
 * @Date 2025/3/20 9:39
 * @descript:
 */
@AndroidEntryPoint
@Route(path = RouteFragment.RealTime.RealTimeNovelChildFragment)
class RealTimeNovelChildFragment : BaseFragment<RealtimeNovelChildFragmentBinding, RealTimeNovelVm>(){

    private lateinit var category:String
    private val mNovelChildAdapter: HotRankNovelMovieAdapter by lazy { HotRankNovelMovieAdapter() }


    override fun setThemeColor(isDarkMode: Boolean) {

    }

    override fun initObserver() {

    }

    override fun initRequestData() {

       mViewModel.getChildDataByTab("wise","novel","{\"category\":\""+category+"\"}").observerKt {
           mNovelChildAdapter.submitList(it.cards.get(0).content)
       }
    }

    override fun reLoadData() {

    }

    override fun RealtimeNovelChildFragmentBinding.initView() {
        category = arguments?.getString("category") ?:""
        rvNovelHotRank.init(LinearLayoutManager(requireActivity()),mNovelChildAdapter,true)
        handleAdapterClick(mNovelChildAdapter)
    }


}
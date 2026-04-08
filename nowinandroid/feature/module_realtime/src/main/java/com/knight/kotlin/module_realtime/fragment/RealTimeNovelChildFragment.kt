package com.knight.kotlin.module_realtime.fragment

import androidx.recyclerview.widget.LinearLayoutManager
import com.core.library_base.route.RouteFragment
import com.knight.kotlin.library_base.fragment.BaseMviFragment
import com.knight.kotlin.library_widget.ktx.init
import com.knight.kotlin.module_realtime.adapter.HotRankNovelMovieAdapter
import com.knight.kotlin.module_realtime.contract.RealtimeChildNovelContract
import com.knight.kotlin.module_realtime.databinding.RealtimeNovelChildFragmentBinding
import com.knight.kotlin.module_realtime.ktx.handleAdapterClick
import com.knight.kotlin.module_realtime.vm.RealTimeChildNovelVm
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
class RealTimeNovelChildFragment :
    BaseMviFragment<
            RealtimeNovelChildFragmentBinding,
            RealTimeChildNovelVm,
            RealtimeChildNovelContract.Event,
            RealtimeChildNovelContract.State,
            RealtimeChildNovelContract.Effect>() {

    // =========================
    // Adapter
    // =========================
    private val novelAdapter by lazy {
        HotRankNovelMovieAdapter()
    }

    // =========================
    // 参数
    // =========================
    private val category: String by lazy {
        arguments?.getString("category").orEmpty()
    }

    // =========================
    // 初始化
    // =========================
    override fun RealtimeNovelChildFragmentBinding.initView() {
        rvNovelHotRank.init(
            LinearLayoutManager(requireActivity()),
            novelAdapter,
            true
        )

        handleAdapterClick(novelAdapter)

        requestLoading(rvNovelHotRank)
    }

    override fun initObserver() {

    }

    override fun setThemeColor(isDarkMode: Boolean) {}

    // =========================
    // 懒加载
    // =========================
    override fun initRequestData() {
        sendEvent(RealtimeChildNovelContract.Event.Init(category))
    }

    override fun reLoadData() {
        sendEvent(RealtimeChildNovelContract.Event.Init(category))
    }

    // =========================
    // 渲染
    // =========================
    override fun renderState(state: RealtimeChildNovelContract.State) {

        if (state.isLoading) {
            requestLoading(mBinding.rvNovelHotRank)
            return
        }

        if (state.isEmpty) {
            requestEmptyData()
        } else {
            requestSuccess()
            novelAdapter.submitList(state.list)
        }
    }

    override fun handleEffect(effect: RealtimeChildNovelContract.Effect) {
        // 暂无
    }
}
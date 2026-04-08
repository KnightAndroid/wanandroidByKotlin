package com.knight.kotlin.module_realtime.fragment

import androidx.recyclerview.widget.LinearLayoutManager
import com.core.library_base.route.RouteFragment
import com.knight.kotlin.library_base.fragment.BaseMviFragment
import com.knight.kotlin.library_widget.ktx.init
import com.knight.kotlin.module_realtime.adapter.HotRankMainAdapter
import com.knight.kotlin.module_realtime.contract.RealtimeTextContract
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
class RealTimeTextFragment :
    BaseMviFragment<
            RealtimeTextFragmentBinding,
            RealTimeTextVm,
            RealtimeTextContract.Event,
            RealtimeTextContract.State,
            RealtimeTextContract.Effect>() {

    // =========================
    // 参数（安全写法）
    // =========================
    private val typeName: String by lazy {
        arguments?.getString("typeName").orEmpty()
    }

    // =========================
    // Adapter（安全初始化）
    // =========================
    private val adapter by lazy {
        HotRankMainAdapter(
            HotListEnum.valueOf(typeName.ifEmpty { HotListEnum.REALTIME.name })
        )
    }

    // =========================
    // 初始化
    // =========================
    override fun RealtimeTextFragmentBinding.initView() {

        rvHotRank.init(
            LinearLayoutManager(requireActivity()),
            adapter,
            true
        )

        handleAdapterClick(adapter)

        requestLoading(rvHotRank)
    }

    override fun initObserver() {

    }

    override fun setThemeColor(isDarkMode: Boolean) {}

    // =========================
    // 懒加载
    // =========================
    override fun initRequestData() {
        sendEvent(RealtimeTextContract.Event.Init(typeName))
    }

    override fun reLoadData() {
        sendEvent(RealtimeTextContract.Event.Init(typeName))
    }

    // =========================
    // 渲染
    // =========================
    override fun renderState(state: RealtimeTextContract.State) {

        if (state.isLoading) {
            requestLoading(mBinding.rvHotRank)
            return
        }

        if (state.isEmpty) {
            requestEmptyData()
        } else {
            requestSuccess()
            adapter.submitList(state.list)
        }
    }

    override fun handleEffect(effect: RealtimeTextContract.Effect) {}
}
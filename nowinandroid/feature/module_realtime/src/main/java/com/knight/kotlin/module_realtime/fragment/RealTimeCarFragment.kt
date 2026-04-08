package com.knight.kotlin.module_realtime.fragment

import android.graphics.Color
import androidx.recyclerview.widget.LinearLayoutManager
import com.core.library_base.route.RouteFragment
import com.knight.kotlin.library_base.fragment.BaseMviFragment
import com.knight.kotlin.library_widget.ktx.init
import com.knight.kotlin.module_realtime.adapter.HotRankCarAdapter
import com.knight.kotlin.module_realtime.adapter.HotRankCategoryAdapter
import com.knight.kotlin.module_realtime.contract.RealTimeCarContract
import com.knight.kotlin.module_realtime.databinding.RealtimeCarFragmentBinding
import com.knight.kotlin.module_realtime.enum.LevelEnum
import com.knight.kotlin.module_realtime.ktx.handleAdapterClick
import com.knight.kotlin.module_realtime.vm.RealTimeCarVm
import com.wyjson.router.annotation.Route
import dagger.hilt.android.AndroidEntryPoint


/**
 * @author created by luguian
 * @organize
 * @Date 2025/3/25 14:40
 * @descript:汽车榜页面
 */
@AndroidEntryPoint
@Route(path = RouteFragment.RealTime.RealTimeCarFragment)
class RealTimeCarFragment : BaseMviFragment<
        RealtimeCarFragmentBinding,
        RealTimeCarVm,
        RealTimeCarContract.Event,
        RealTimeCarContract.State,
        RealTimeCarContract.Effect>(),
    HotRankCategoryAdapter.OnChipClickListener {

    private var category: String = "全部"

    private val mCategoryAdapter by lazy {
        HotRankCategoryAdapter(this, LevelEnum.PARENT)
    }

    private val mHotRankCarAdapter by lazy {
        HotRankCarAdapter()
    }

    override fun setThemeColor(isDarkMode: Boolean) {}

    override fun RealtimeCarFragmentBinding.initView() {
        requestLoading(rvCar, Color.parseColor("#F84C48"))

        rvCar.init(
            LinearLayoutManager(requireActivity()),
            mHotRankCarAdapter,
            true
        )

        rvCarCategory.init(
            LinearLayoutManager(requireActivity(), LinearLayoutManager.HORIZONTAL, false),
            mCategoryAdapter,
            false
        )

        handleAdapterClick(mHotRankCarAdapter)
    }

    override fun initObserver() {

    }

    // ✅ 初始化加载
    override fun initRequestData() {
        sendEvent(RealTimeCarContract.Event.LoadData(category))
    }

    override fun reLoadData() {
        sendEvent(RealTimeCarContract.Event.LoadData(category))
    }

    // ✅ 唯一UI更新入口
    override fun renderState(state: RealTimeCarContract.State) {

        // loading
        if (state.isLoading) {
            requestLoading(mBinding.rvCar, Color.parseColor("#F84C48"))
        }

        // 分类
        if (state.categoryList.isNotEmpty()) {
            mCategoryAdapter.submitList(state.categoryList)
        }

        // 列表
        if (state.carList.isNotEmpty()) {
            requestSuccess()
            mHotRankCarAdapter.submitList(state.carList)
        } else if (state.isEmpty) {
            requestEmptyData()
        }
    }

    override fun handleEffect(effect: RealTimeCarContract.Effect) {
        // 以后可以放 Toast / 跳转
    }

    // ✅ 分类点击 → 发事件
    override fun onChipClick(enum: LevelEnum, chipText: String) {
        category = chipText
        sendEvent(RealTimeCarContract.Event.ChangeCategory(category))
    }
}
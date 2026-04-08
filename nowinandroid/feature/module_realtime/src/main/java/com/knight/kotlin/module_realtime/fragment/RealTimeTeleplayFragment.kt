package com.knight.kotlin.module_realtime.fragment

import androidx.recyclerview.widget.LinearLayoutManager
import com.core.library_base.route.RouteFragment
import com.knight.kotlin.library_base.fragment.BaseMviFragment
import com.knight.kotlin.library_widget.ktx.init
import com.knight.kotlin.module_realtime.adapter.HotRankCategoryAdapter
import com.knight.kotlin.module_realtime.adapter.HotRankNovelMovieAdapter
import com.knight.kotlin.module_realtime.contract.RealtimeTeleplayContract
import com.knight.kotlin.module_realtime.databinding.RealtimeTeleplayFragmentBinding
import com.knight.kotlin.module_realtime.enum.LevelEnum
import com.knight.kotlin.module_realtime.ktx.handleAdapterClick
import com.knight.kotlin.module_realtime.vm.RealTimeTeleplayVm
import com.wyjson.router.annotation.Route
import dagger.hilt.android.AndroidEntryPoint


/**
 * @author created by luguian
 * @organize
 * @Date 2025/3/25 9:46
 * @descript:电视剧榜
 */
@AndroidEntryPoint
@Route(path = RouteFragment.RealTime.RealTimeTeleplayFragment)
class RealTimeTeleplayFragment :
    BaseMviFragment<
            RealtimeTeleplayFragmentBinding,
            RealTimeTeleplayVm,
            RealtimeTeleplayContract.Event,
            RealtimeTeleplayContract.State,
            RealtimeTeleplayContract.Effect>(),
    HotRankCategoryAdapter.OnChipClickListener {

    // =========================
    // Adapter
    // =========================
    private val categoryAdapter by lazy {
        HotRankCategoryAdapter(this, LevelEnum.PARENT)
    }

    private val countryAdapter by lazy {
        HotRankCategoryAdapter(this, LevelEnum.CHILD)
    }

    private val teleplayAdapter by lazy {
        HotRankNovelMovieAdapter()
    }

    // =========================
    // 筛选条件
    // =========================
    private var category: String = "全部类型"
    private var country: String = "中国大陆"

    // =========================
    // 初始化
    // =========================
    override fun RealtimeTeleplayFragmentBinding.initView() {

        rvTeleplayCategory.init(
            LinearLayoutManager(requireActivity(), LinearLayoutManager.HORIZONTAL, false),
            categoryAdapter,
            false
        )

        rvTeleplayCountry.init(
            LinearLayoutManager(requireActivity(), LinearLayoutManager.HORIZONTAL, false),
            countryAdapter,
            false
        )

        rvTeleplay.init(
            LinearLayoutManager(requireActivity()),
            teleplayAdapter,
            false
        )

        handleAdapterClick(teleplayAdapter)

        requestLoading(rvTeleplay)
    }

    override fun initObserver() {

    }

    override fun setThemeColor(isDarkMode: Boolean) {}

    // =========================
    // 懒加载
    // =========================
    override fun initRequestData() {
        sendEvent(RealtimeTeleplayContract.Event.Init)
    }

    override fun reLoadData() {
        sendEvent(
            RealtimeTeleplayContract.Event.FilterChanged(category, country)
        )
    }

    // =========================
    // 渲染
    // =========================
    override fun renderState(state: RealtimeTeleplayContract.State) {

        if (state.isLoading) {
            requestLoading(mBinding.rvTeleplay)
            return
        }

        if (state.categoryList.isNotEmpty()) {
            categoryAdapter.submitList(state.categoryList)
        }

        if (state.countryList.isNotEmpty()) {
            countryAdapter.submitList(state.countryList)
        }

        if (state.isEmpty) {
            requestEmptyData()
        } else {
            requestSuccess()
            teleplayAdapter.submitList(state.list)
        }
    }

    override fun handleEffect(effect: RealtimeTeleplayContract.Effect) {}

    // =========================
    // 点击筛选
    // =========================
    override fun onChipClick(enum: LevelEnum, chipText: String) {
        when (enum) {
            LevelEnum.PARENT -> category = chipText
            LevelEnum.CHILD -> country = chipText
        }

        sendEvent(
            RealtimeTeleplayContract.Event.FilterChanged(category, country)
        )
    }
}
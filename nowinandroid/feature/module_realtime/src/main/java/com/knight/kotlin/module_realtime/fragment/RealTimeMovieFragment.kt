package com.knight.kotlin.module_realtime.fragment

import androidx.recyclerview.widget.LinearLayoutManager
import com.core.library_base.route.RouteFragment
import com.knight.kotlin.library_base.fragment.BaseMviFragment
import com.knight.kotlin.library_widget.ktx.init
import com.knight.kotlin.module_realtime.adapter.HotRankCategoryAdapter
import com.knight.kotlin.module_realtime.adapter.HotRankNovelMovieAdapter
import com.knight.kotlin.module_realtime.contract.RealtimeMovieContract
import com.knight.kotlin.module_realtime.databinding.RealtimeMovieFragmentBinding
import com.knight.kotlin.module_realtime.enum.LevelEnum
import com.knight.kotlin.module_realtime.ktx.handleAdapterClick
import com.knight.kotlin.module_realtime.vm.RealtimeMovieVm
import com.wyjson.router.annotation.Route
import dagger.hilt.android.AndroidEntryPoint


/**
 * @author created by luguian
 * @organize
 * @Date 2025/3/24 10:28
 * @descript:电影
 */
@AndroidEntryPoint
@Route(path = RouteFragment.RealTime.RealTimeMovieFragment)
class RealTimeMovieFragment :
    BaseMviFragment<
            RealtimeMovieFragmentBinding,
            RealtimeMovieVm,
            RealtimeMovieContract.Event,
            RealtimeMovieContract.State,
            RealtimeMovieContract.Effect>(),
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

    private val movieAdapter by lazy {
        HotRankNovelMovieAdapter()
    }

    // =========================
    // 筛选条件（UI层持有）
    // =========================
    private var category: String = "全部类型"
    private var country: String = "全部地区"

    // =========================
    // View 初始化
    // =========================
    override fun RealtimeMovieFragmentBinding.initView() {

        rvMovieCategory.init(
            LinearLayoutManager(requireActivity(), LinearLayoutManager.HORIZONTAL, false),
            categoryAdapter,
            false
        )

        rvMovieCountry.init(
            LinearLayoutManager(requireActivity(), LinearLayoutManager.HORIZONTAL, false),
            countryAdapter,
            false
        )

        rvMovie.init(
            LinearLayoutManager(requireActivity()),
            movieAdapter,
            false
        )

        handleAdapterClick(movieAdapter)

        requestLoading(rvMovie)
    }

    override fun initObserver() {

    }

    override fun setThemeColor(isDarkMode: Boolean) {}

    // =========================
    // 懒加载触发
    // =========================
    override fun initRequestData() {
        sendEvent(RealtimeMovieContract.Event.Init)
    }

    override fun reLoadData() {
        sendEvent(
            RealtimeMovieContract.Event.FilterChanged(category, country)
        )
    }

    // =========================
    // 渲染 UI（🔥核心）
    // =========================
    override fun renderState(state: RealtimeMovieContract.State) {

        // loading
        if (state.isLoading) {
            requestLoading(mBinding.rvMovie)
            return
        }

        // 分类（只初始化一次）
        if (state.categoryList.isNotEmpty()) {
            categoryAdapter.submitList(state.categoryList)
        }

        if (state.countryList.isNotEmpty()) {
            countryAdapter.submitList(state.countryList)
        }

        // 列表
        if (state.isEmpty) {
            requestEmptyData()
        } else {
            requestSuccess()
            movieAdapter.submitList(state.movieList)
        }
    }

    override fun handleEffect(effect: RealtimeMovieContract.Effect) {
        // 暂无
    }

    // =========================
    // 点击筛选
    // =========================
    override fun onChipClick(enum: LevelEnum, chipText: String) {
        when (enum) {
            LevelEnum.PARENT -> category = chipText
            LevelEnum.CHILD -> country = chipText
        }

        sendEvent(
            RealtimeMovieContract.Event.FilterChanged(category, country)
        )
    }
}
package com.knight.kotlin.module_realtime.fragment

import android.graphics.Color
import androidx.recyclerview.widget.LinearLayoutManager
import com.core.library_base.route.RouteFragment
import com.knight.kotlin.library_base.fragment.BaseMviFragment
import com.knight.kotlin.library_widget.ktx.init
import com.knight.kotlin.module_realtime.adapter.HotRankCategoryAdapter
import com.knight.kotlin.module_realtime.adapter.HotRankGameAdapter
import com.knight.kotlin.module_realtime.contract.RealTimeGameContract
import com.knight.kotlin.module_realtime.databinding.RealtimeGameFragmentBinding
import com.knight.kotlin.module_realtime.enum.LevelEnum
import com.knight.kotlin.module_realtime.ktx.handleAdapterClick
import com.knight.kotlin.module_realtime.vm.RealTimeGameVm
import com.wyjson.router.annotation.Route
import dagger.hilt.android.AndroidEntryPoint


/**
 * @author created by luguian
 * @organize
 * @Date 2025/3/25 16:42
 * @descript:游戏界面
 */
@AndroidEntryPoint
@Route(path = RouteFragment.RealTime.RealTimeGameFragment)
class RealTimeGameFragment :
    BaseMviFragment<
            RealtimeGameFragmentBinding,
            RealTimeGameVm,
            RealTimeGameContract.Event,
            RealTimeGameContract.State,
            RealTimeGameContract.Effect>(),
    HotRankCategoryAdapter.OnChipClickListener {

    private val mCategoryAdapter by lazy {
        HotRankCategoryAdapter(this, LevelEnum.PARENT)
    }

    private val mGameAdapter by lazy {
        HotRankGameAdapter()
    }

    // =========================
    // UI 初始化
    // =========================
    override fun RealtimeGameFragmentBinding.initView() {

        requestLoading(rvGame, Color.parseColor("#F84C48"))

        rvGame.init(
            LinearLayoutManager(requireActivity()),
            mGameAdapter,
            true
        )

        rvGameCategory.init(
            LinearLayoutManager(requireActivity(), LinearLayoutManager.HORIZONTAL, false),
            mCategoryAdapter,
            false
        )

        handleAdapterClick(mGameAdapter)

        // ✅ 触发初始化
        sendEvent(RealTimeGameContract.Event.Init)
    }

    override fun initObserver() {

    }

    override fun initRequestData() {

    }

    override fun setThemeColor(isDarkMode: Boolean) {}

    // =========================
    // MVI 核心
    // =========================
    override fun renderState(state: RealTimeGameContract.State) {

        when {
            state.isLoading -> {
                requestLoading(mBinding.rvGame)
            }

            state.isError -> {
                requestFailure()
            }

            state.isEmpty -> {
                requestEmptyData()
            }

            else -> {
                requestSuccess()

                mCategoryAdapter.submitList(state.categoryList)
                mGameAdapter.submitList(state.gameList)
            }
        }
    }

    override fun handleEffect(effect: RealTimeGameContract.Effect) {
        when (effect) {
            is RealTimeGameContract.Effect.ShowToast -> {
                // showToast(effect.msg)
            }
        }
    }

    // =========================
    // 交互
    // =========================
    override fun onChipClick(enum: LevelEnum, chipText: String) {
        sendEvent(
            RealTimeGameContract.Event.SelectCategory(chipText)
        )
    }

    override fun reLoadData() {
        sendEvent(RealTimeGameContract.Event.Retry)
    }
}
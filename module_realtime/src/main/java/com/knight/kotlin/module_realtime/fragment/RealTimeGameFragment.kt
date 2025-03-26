package com.knight.kotlin.module_realtime.fragment

import android.graphics.Color
import androidx.recyclerview.widget.LinearLayoutManager
import com.knight.kotlin.library_base.fragment.BaseFragment
import com.knight.kotlin.library_base.route.RouteFragment
import com.knight.kotlin.library_widget.ktx.init
import com.knight.kotlin.module_realtime.adapter.HotRankCategoryAdapter
import com.knight.kotlin.module_realtime.adapter.HotRankGameAdapter
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
class RealTimeGameFragment: BaseFragment<RealtimeGameFragmentBinding, RealTimeGameVm>(), HotRankCategoryAdapter.OnChipClickListener {

    private var category:String = "全部类型"
    private val mCategoryAdapter: HotRankCategoryAdapter by lazy { HotRankCategoryAdapter(this, LevelEnum.PARENT) }
    private val mGameAdapter:HotRankGameAdapter by lazy{HotRankGameAdapter()}
    override fun setThemeColor(isDarkMode: Boolean) {

    }

    override fun initObserver() {

    }

    override fun initRequestData() {
        mViewModel.getChildDataByTab("wise","game","{\"category\":\""+category+"\"}").observerKt {
            requestSuccess()
            mCategoryAdapter.submitList(it.tag.get(0).content)
            mGameAdapter.submitList(it.cards.get(0).content)
        }
    }

    override fun reLoadData() {
        getGameDataByCategory()
    }

    override fun RealtimeGameFragmentBinding.initView() {
        requestLoading(rvGame, Color.parseColor("#F84C48"))
        rvGame.init(LinearLayoutManager(requireActivity()),mGameAdapter,true)
        rvGameCategory.init(LinearLayoutManager(requireActivity(), LinearLayoutManager.HORIZONTAL, false),mCategoryAdapter,false)
        handleAdapterClick(mGameAdapter)
    }

    override fun onChipClick(enum: LevelEnum, chipText: String) {
        category = chipText
        getGameDataByCategory()
    }

    fun getGameDataByCategory() {
        mViewModel.getChildDataByTab("wise","car","{\"category\":\""+category+"\"}").observerKt {
            if (it.cards.get(0).content.size > 0) {
                requestSuccess()
                mGameAdapter.submitList(it.cards.get(0).content)
            } else {
                requestEmptyData()
            }

        }
    }
}
package com.knight.kotlin.module_realtime.fragment

import android.graphics.Color
import androidx.recyclerview.widget.LinearLayoutManager
import com.knight.kotlin.library_base.fragment.BaseFragment
import com.core.library_base.route.RouteFragment
import com.knight.kotlin.library_widget.ktx.init
import com.knight.kotlin.module_realtime.adapter.HotRankCarAdapter
import com.knight.kotlin.module_realtime.adapter.HotRankCategoryAdapter
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
class RealTimeCarFragment : BaseFragment<RealtimeCarFragmentBinding, RealTimeCarVm>(),HotRankCategoryAdapter.OnChipClickListener {


    private var category:String = "全部"
    private val mCategoryAdapter: HotRankCategoryAdapter by lazy { HotRankCategoryAdapter(this, LevelEnum.PARENT) }
    private val mHotRankCarAdapter: HotRankCarAdapter by lazy { HotRankCarAdapter() }



    override fun setThemeColor(isDarkMode: Boolean) {

    }

    override fun initObserver() {

    }

    override fun initRequestData() {
        mViewModel.getChildDataByTab("wise","car","{\"category\":\""+category+"\"}").observerKt {
            requestSuccess()
            mCategoryAdapter.submitList(it.tag.get(0).content)
            mHotRankCarAdapter.submitList(it.cards.get(0).content)
        }
    }

    override fun reLoadData() {
        getCarDataByCategory()
    }

    override fun RealtimeCarFragmentBinding.initView() {
        requestLoading(rvCar, Color.parseColor("#F84C48"))
        rvCar.init(LinearLayoutManager(requireActivity()),mHotRankCarAdapter,true)
        rvCarCategory.init(LinearLayoutManager(requireActivity(), LinearLayoutManager.HORIZONTAL, false),mCategoryAdapter,false)
        handleAdapterClick(mHotRankCarAdapter)
    }

    override fun onChipClick(enum: LevelEnum, chipText: String) {
        category = chipText
        getCarDataByCategory()
    }


    fun getCarDataByCategory() {
        mViewModel.getChildDataByTab("wise","car","{\"category\":\""+category+"\"}").observerKt {
            if (it.cards.get(0).content.size > 0) {
                requestSuccess()
                mHotRankCarAdapter.submitList(it.cards.get(0).content)
            } else {
                requestEmptyData()
            }

        }
    }
}
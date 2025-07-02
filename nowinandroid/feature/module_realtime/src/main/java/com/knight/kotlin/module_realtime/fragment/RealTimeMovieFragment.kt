package com.knight.kotlin.module_realtime.fragment

import android.graphics.Color
import androidx.recyclerview.widget.LinearLayoutManager
import com.knight.kotlin.library_base.fragment.BaseFragment
import com.core.library_base.route.RouteFragment
import com.knight.kotlin.library_widget.ktx.init
import com.knight.kotlin.module_realtime.adapter.HotRankCategoryAdapter
import com.knight.kotlin.module_realtime.adapter.HotRankNovelMovieAdapter
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
class RealTimeMovieFragment : BaseFragment<RealtimeMovieFragmentBinding, RealtimeMovieVm>(),HotRankCategoryAdapter.OnChipClickListener {


    private var category:String = "全部类型"
    private var country:String = "中国大陆"

    private val mCategoryAdapter:HotRankCategoryAdapter by lazy { HotRankCategoryAdapter(this,LevelEnum.PARENT) }
    private val mCountryAdapter:HotRankCategoryAdapter by lazy { HotRankCategoryAdapter(this,LevelEnum.CHILD) }
    private val mMovieAdapter: HotRankNovelMovieAdapter by lazy { HotRankNovelMovieAdapter() }

    override fun setThemeColor(isDarkMode: Boolean) {

    }

    override fun initObserver() {

    }

    override fun initRequestData() {
        mViewModel.getChildDataByTab("wise","movie","{\"category\":\""+category+"\",\"country\":\""+country+"\"}").observerKt {
           requestSuccess()
           mCategoryAdapter.submitList(it.tag.get(0).content)
           mCountryAdapter.submitList(it.tag.get(1).content)
           mMovieAdapter.submitList(it.cards.get(0).content)
        }
    }

    override fun reLoadData() {
        getMovieDataByCategoryWithCountry()
    }

    override fun RealtimeMovieFragmentBinding.initView() {
        requestLoading(rvMovie, Color.parseColor("#F84C48"))
        rvMovieCategory.init(LinearLayoutManager(requireActivity(),LinearLayoutManager.HORIZONTAL, false),mCategoryAdapter,false)
        rvMovieCountry.init(LinearLayoutManager(requireActivity(),LinearLayoutManager.HORIZONTAL, false),mCountryAdapter,false)
        rvMovie.init(LinearLayoutManager(requireActivity()),mMovieAdapter,false)
        handleAdapterClick(mMovieAdapter)
    }

    override fun onChipClick(enum: LevelEnum,chipText: String) {
        if (enum == LevelEnum.PARENT) {
            category = chipText
        } else if (enum == LevelEnum.CHILD) {
            country = chipText
        }
        getMovieDataByCategoryWithCountry()


    }

    fun getMovieDataByCategoryWithCountry() {
        mViewModel.getChildDataByTab("wise","movie","{\"category\":\""+category+"\",\"country\":\""+country+"\"}").observerKt {
            if (it.cards.get(0).content.size > 0) {
                requestSuccess()
                mMovieAdapter.submitList(it.cards.get(0).content)
            } else {
                requestEmptyData()
            }

        }
    }


}
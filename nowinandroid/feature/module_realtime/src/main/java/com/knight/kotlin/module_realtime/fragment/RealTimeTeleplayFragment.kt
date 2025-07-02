package com.knight.kotlin.module_realtime.fragment

import android.graphics.Color
import androidx.recyclerview.widget.LinearLayoutManager
import com.knight.kotlin.library_base.fragment.BaseFragment
import com.core.library_base.route.RouteFragment
import com.knight.kotlin.library_widget.ktx.init
import com.knight.kotlin.module_realtime.adapter.HotRankCategoryAdapter
import com.knight.kotlin.module_realtime.adapter.HotRankNovelMovieAdapter
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
class RealTimeTeleplayFragment : BaseFragment<RealtimeTeleplayFragmentBinding, RealTimeTeleplayVm>(),HotRankCategoryAdapter.OnChipClickListener {

    private var category:String = "全部类型"
    private var country:String = "中国大陆"

    private val mCategoryAdapter: HotRankCategoryAdapter by lazy { HotRankCategoryAdapter(this, LevelEnum.PARENT) }
    private val mCountryAdapter: HotRankCategoryAdapter by lazy { HotRankCategoryAdapter(this, LevelEnum.CHILD) }
    private val mTeleplayAdapter: HotRankNovelMovieAdapter by lazy {HotRankNovelMovieAdapter()}

    override fun setThemeColor(isDarkMode: Boolean) {

    }

    override fun RealtimeTeleplayFragmentBinding.initView() {
        requestLoading(rvTeleplay, Color.parseColor("#F84C48"))

        rvTeleplayCategory.init(LinearLayoutManager(requireActivity(), LinearLayoutManager.HORIZONTAL, false),mCategoryAdapter,false)
        rvTeleplayCountry.init(LinearLayoutManager(requireActivity(), LinearLayoutManager.HORIZONTAL, false),mCountryAdapter,false)
        rvTeleplay.init(LinearLayoutManager(requireActivity()),mTeleplayAdapter,false)
        handleAdapterClick(mTeleplayAdapter)
    }

    override fun initObserver() {

    }

    override fun initRequestData() {
        mViewModel.getChildDataByTab("wise","teleplay","{\"category\":\""+category+"\",\"country\":\""+country+"\"}").observerKt {
            requestSuccess()
            mCategoryAdapter.submitList(it.tag.get(0).content)
            mCountryAdapter.submitList(it.tag.get(1).content)
            mTeleplayAdapter.submitList(it.cards.get(0).content)
        }
    }

    override fun reLoadData() {
        getTeleplayDataByCategoryWithCountry()
    }

    override fun onChipClick(enum: LevelEnum, chipText: String) {
        if (enum == LevelEnum.PARENT) {
            category = chipText
        } else if (enum == LevelEnum.CHILD) {
            country = chipText
        }
        getTeleplayDataByCategoryWithCountry()

    }



    fun getTeleplayDataByCategoryWithCountry() {
        mViewModel.getChildDataByTab("wise","teleplay","{\"category\":\""+category+"\",\"country\":\""+country+"\"}").observerKt {
            if (it.cards.get(0).content.size > 0) {
                requestSuccess()
                mTeleplayAdapter.submitList(it.cards.get(0).content)
            } else {
                requestEmptyData()
            }

        }
    }
}
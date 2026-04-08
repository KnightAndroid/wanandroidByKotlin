package com.knight.kotlin.module_realtime.fragment

import android.view.LayoutInflater
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.knight.kotlin.library_base.fragment.BaseMviFragment
import com.knight.kotlin.library_widget.ktx.init
import com.knight.kotlin.module_realtime.R
import com.knight.kotlin.module_realtime.adapter.HotRankMainAdapter
import com.knight.kotlin.module_realtime.adapter.HotRankNovelMovieAdapter
import com.knight.kotlin.module_realtime.contract.RealTimeMainContract
import com.knight.kotlin.module_realtime.databinding.RealtimeMainFragmentBinding
import com.knight.kotlin.module_realtime.databinding.RealtimeTabBoardFootItemBinding
import com.knight.kotlin.module_realtime.databinding.RealtimeTabBoardHeadItemBinding
import com.knight.kotlin.module_realtime.enum.HotListEnum
import com.knight.kotlin.module_realtime.ktx.handleAdapterClick
import com.knight.kotlin.module_realtime.vm.RealTimeMainVm


/**
 * @author created by luguian
 * @organize
 * @Date 2025/3/11 16:43
 * @descript:
 */

class RealTimeMainFragment :
    BaseMviFragment<
            RealtimeMainFragmentBinding,
            RealTimeMainVm,
            RealTimeMainContract.Event,
            RealTimeMainContract.State,
            RealTimeMainContract.Effect>() {

    private val hotAdapter by lazy { HotRankMainAdapter(HotListEnum.REALTIME) }
    private val novelAdapter by lazy { HotRankNovelMovieAdapter() }
    private val movieAdapter by lazy { HotRankNovelMovieAdapter() }

    private var selectRankListener: OnSelectRankListener? = null

    override fun RealtimeMainFragmentBinding.initView() {

        rvHotList.init(LinearLayoutManager(requireActivity()), hotAdapter, false)
        rvNovelList.init(LinearLayoutManager(requireActivity()), novelAdapter, false)
        rvMovieList.init(LinearLayoutManager(requireActivity()), movieAdapter, false)

        setOnClickListener(rlMainTeleplay, rlMainCar)

        handleAdapterClick(hotAdapter, novelAdapter, movieAdapter)

        sendEvent(RealTimeMainContract.Event.Init)
    }

    override fun initObserver() {

    }

    override fun initRequestData() {

    }

    override fun reLoadData() {

    }

    override fun setThemeColor(isDarkMode: Boolean) {

    }

    override fun renderState(state: RealTimeMainContract.State) {

        when {
            state.isLoading -> {
                // loading
            }

            state.isError -> {
                // error
            }

            else -> {
                hotAdapter.submitList(state.hotList)
                novelAdapter.submitList(state.novelList)
                movieAdapter.submitList(state.movieList)

                initHeadFootOnce()
            }
        }
    }

    override fun handleEffect(effect: RealTimeMainContract.Effect) {}

    // =========================
    // 🔥 关键优化：头尾统一封装
    // =========================
    private fun initHeadFootOnce() {

        if (mBinding.rvHotList.headerCount == 0) {
            addHeaderFooter(
                mBinding.rvHotList,
                R.drawable.realtime_hot_icon,
                "热搜榜",
                HotListEnum.REALTIME
            )
        }

        if (mBinding.rvNovelList.headerCount == 0) {
            addHeaderFooter(
                mBinding.rvNovelList,
                R.drawable.realtime_novel_icon,
                "小说榜",
                HotListEnum.NOVEL
            )
        }

        if (mBinding.rvMovieList.headerCount == 0) {
            addHeaderFooter(
                mBinding.rvMovieList,
                R.drawable.realtime_movie_icon,
                "电影榜",
                HotListEnum.MOVIE
            )
        }
    }

    private fun addHeaderFooter(
        recyclerView: Any, // 你这里是自定义RecyclerView类型
        icon: Int,
        title: String,
        type: HotListEnum
    ) {
        val inflater = LayoutInflater.from(requireContext())

        val head = RealtimeTabBoardHeadItemBinding.inflate(inflater)
        head.ivRealtimeIcon.setBackgroundResource(icon)
        head.tvTealtimeTitle.text = title

        val foot = RealtimeTabBoardFootItemBinding.inflate(inflater)
        foot.root.setOnClickListener {
            selectRankListener?.onChipClick(type)
        }

        (recyclerView as com.yanzhenjie.recyclerview.SwipeRecyclerView).apply {
            addHeaderView(head.root)
            addFooterView(foot.root)
        }
    }

    override fun onClick(v: View) {
        when (v) {
            mBinding.rlMainTeleplay -> {
                selectRankListener?.onChipClick(HotListEnum.TELEPLAY)
            }

            mBinding.rlMainCar -> {
                selectRankListener?.onChipClick(HotListEnum.CAR)
            }
        }
    }

    fun setSelectRankListener(listener: OnSelectRankListener): RealTimeMainFragment {
        this.selectRankListener = listener
        return this
    }

    interface OnSelectRankListener {
        fun onChipClick(enum: HotListEnum)
    }
}
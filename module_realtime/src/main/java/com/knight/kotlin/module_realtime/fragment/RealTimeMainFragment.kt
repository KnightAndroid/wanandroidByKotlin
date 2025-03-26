package com.knight.kotlin.module_realtime.fragment

import android.view.LayoutInflater
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.flyjingfish.android_aop_core.annotations.SingleClick
import com.knight.kotlin.library_base.fragment.BaseFragment
import com.knight.kotlin.library_base.route.RouteFragment
import com.knight.kotlin.library_widget.ktx.init
import com.knight.kotlin.module_realtime.R
import com.knight.kotlin.module_realtime.adapter.HotRankMainAdapter
import com.knight.kotlin.module_realtime.adapter.HotRankNovelMovieAdapter
import com.knight.kotlin.module_realtime.databinding.RealtimeMainFragmentBinding
import com.knight.kotlin.module_realtime.databinding.RealtimeTabBoardFootItemBinding
import com.knight.kotlin.module_realtime.databinding.RealtimeTabBoardHeadItemBinding
import com.knight.kotlin.module_realtime.enum.HotListEnum
import com.knight.kotlin.module_realtime.ktx.handleAdapterClick
import com.knight.kotlin.module_realtime.vm.RealTimeHomeVm
import com.wyjson.router.annotation.Route
import dagger.hilt.android.AndroidEntryPoint


/**
 * @author created by luguian
 * @organize
 * @Date 2025/3/11 16:43
 * @descript:
 */

@AndroidEntryPoint
@Route(path = RouteFragment.RealTime.RealTimeMainFragment)
class RealTimeMainFragment : BaseFragment<RealtimeMainFragmentBinding, RealTimeHomeVm>() {

    //热搜适配器
    private val mRealTimeHotMainAdapter: HotRankMainAdapter by lazy { HotRankMainAdapter(HotListEnum.REALTIME) }
    //热梗适配器
    private val mPhraseMainAdapter: HotRankMainAdapter by lazy { HotRankMainAdapter(HotListEnum.PHRASE) }
    //小说适配器
    private val mNovelAdapter:HotRankNovelMovieAdapter by lazy {HotRankNovelMovieAdapter()}
    //电影适配器
    private val mMovieAdapter:HotRankNovelMovieAdapter by lazy {HotRankNovelMovieAdapter()}
    //热搜头部 尾部
    private lateinit var mRealTimeHeaderBinding: RealtimeTabBoardHeadItemBinding
    private lateinit var mRealTimeFootBinding: RealtimeTabBoardFootItemBinding
    private lateinit var mNovelHeadBinding: RealtimeTabBoardHeadItemBinding
    private lateinit var mNovelFootBinding: RealtimeTabBoardFootItemBinding
    //热梗榜
    private lateinit var mPhraseHeaderBinding: RealtimeTabBoardHeadItemBinding
    private lateinit var mPhraseFootBinding: RealtimeTabBoardFootItemBinding
    //小说榜
    private lateinit var mNovelHeaderBinding: RealtimeTabBoardHeadItemBinding
    private lateinit var mNoveleFootBinding: RealtimeTabBoardFootItemBinding
    //电影榜
    private lateinit var mMovieHeaderBinding: RealtimeTabBoardHeadItemBinding
    private lateinit var mMovieFootBinding: RealtimeTabBoardFootItemBinding

    private var selectRankListener:OnSelectRankListener ? = null
    override fun setThemeColor(isDarkMode: Boolean) {

    }

    override fun initObserver() {

    }

    override fun initRequestData() {
        mViewModel.getDataByTab("wise","homepage").observerKt {

            it.cards.get(0).content.addAll(0,it.cards.get(0).topContent)
            mRealTimeHotMainAdapter.submitList(it.cards.get(0).content)

            mPhraseMainAdapter.submitList(it.cards.get(1).content)

            mNovelAdapter.submitList(it.cards.get(2).content)

            mMovieAdapter.submitList(it.cards.get(3).content)
            initHeadFootTabBoard()

        }
    }

    /**
     *
     * 初始化个榜单头部
     */
    private fun initHeadFootTabBoard() {
        if (mBinding.rvHotList.headerCount == 0) {
            if (!::mRealTimeHeaderBinding.isInitialized) {
                mRealTimeHeaderBinding =
                    RealtimeTabBoardHeadItemBinding.inflate(LayoutInflater.from(requireActivity()))
               mBinding.rvHotList.addHeaderView(mRealTimeHeaderBinding.root)
                mRealTimeHeaderBinding.ivRealtimeIcon.setBackgroundResource(R.drawable.realtime_hot_icon)
               mRealTimeHeaderBinding.tvTealtimeTitle.text = "热搜榜"
            }


        }

        if (mBinding.rvHotList.footerCount == 0) {
            if (!::mRealTimeFootBinding.isInitialized) {
                mRealTimeFootBinding = RealtimeTabBoardFootItemBinding.inflate(LayoutInflater.from(requireActivity()))
                mBinding.rvHotList.addFooterView(mRealTimeFootBinding.root)
                mRealTimeFootBinding.root.setOnClickListener {
                    selectRankListener?.onChipClick(HotListEnum.REALTIME)
                }
            }

        }

        if (mBinding.rvPhraseList.headerCount == 0) {
            if (!::mPhraseHeaderBinding.isInitialized) {
                mPhraseHeaderBinding =
                    RealtimeTabBoardHeadItemBinding.inflate(LayoutInflater.from(requireActivity()))
                mBinding.rvPhraseList.addHeaderView(mPhraseHeaderBinding.root)
                mPhraseHeaderBinding.ivRealtimeIcon.setBackgroundResource(R.drawable.realtime_phrase_icon)
                mPhraseHeaderBinding.tvTealtimeTitle.text = "热梗榜"
            }
        }

        if (mBinding.rvPhraseList.footerCount == 0) {
            if (!::mPhraseFootBinding.isInitialized) {
                mPhraseFootBinding = RealtimeTabBoardFootItemBinding.inflate(LayoutInflater.from(requireActivity()))
                mBinding.rvPhraseList.addFooterView(mPhraseFootBinding.root)
                mPhraseFootBinding.root.setOnClickListener {
                    selectRankListener?.onChipClick(HotListEnum.PHRASE)
                }
            }

        }


        if (mBinding.rvNovelList.headerCount == 0) {
            if (!::mNovelHeadBinding.isInitialized) {
                mNovelHeadBinding =
                    RealtimeTabBoardHeadItemBinding.inflate(LayoutInflater.from(requireActivity()))
                mBinding.rvNovelList.addHeaderView(mNovelHeadBinding.root)
                mNovelHeadBinding.ivRealtimeIcon.setBackgroundResource(R.drawable.realtime_novel_icon)
                mNovelHeadBinding.tvTealtimeTitle.text = "小说榜"
            }
        }

        if (mBinding.rvNovelList.footerCount == 0) {
            if (!::mNovelFootBinding.isInitialized) {
                mNovelFootBinding= RealtimeTabBoardFootItemBinding.inflate(LayoutInflater.from(requireActivity()))
                mBinding.rvNovelList.addFooterView(mNovelFootBinding.root)

                mNovelFootBinding.root.setOnClickListener {
                    selectRankListener?.onChipClick(HotListEnum.NOVEL)
                }
            }

        }


        if (mBinding.rvMovieList.headerCount == 0) {
            if (!::mMovieHeaderBinding.isInitialized) {
                mMovieHeaderBinding =
                    RealtimeTabBoardHeadItemBinding.inflate(LayoutInflater.from(requireActivity()))
                mBinding.rvMovieList.addHeaderView(mMovieHeaderBinding.root)
                mMovieHeaderBinding.ivRealtimeIcon.setBackgroundResource(R.drawable.realtime_movie_icon)
                mMovieHeaderBinding.tvTealtimeTitle.text = "电影榜"
            }
        }

        if (mBinding.rvMovieList.footerCount == 0) {
            if (!::mMovieFootBinding.isInitialized) {
                mMovieFootBinding= RealtimeTabBoardFootItemBinding.inflate(LayoutInflater.from(requireActivity()))
                mBinding.rvMovieList.addFooterView(mMovieFootBinding.root)

                mMovieFootBinding.root.setOnClickListener {
                    selectRankListener?.onChipClick(HotListEnum.MOVIE)
                }
            }

        }
    }

    override fun reLoadData() {

    }

    fun setSelectRankListener(selectRankListener:OnSelectRankListener):RealTimeMainFragment {
        this.selectRankListener = selectRankListener
        return this
    }

    override fun RealtimeMainFragmentBinding.initView() {
        rvHotList.init(LinearLayoutManager(requireActivity()), mRealTimeHotMainAdapter,false)
        rvPhraseList.init(LinearLayoutManager(requireActivity()),mPhraseMainAdapter,false)
        rvNovelList.init(LinearLayoutManager(requireActivity()),mNovelAdapter,false)
        rvMovieList.init(LinearLayoutManager(requireActivity()),mMovieAdapter,false)
        setOnClickListener(rlMainTeleplay,rlMainCar)
        initAdapterClickListener()
    }


    private fun initAdapterClickListener() {

        handleAdapterClick(mMovieAdapter,mRealTimeHotMainAdapter,mPhraseMainAdapter,mNovelAdapter)
    }



    @SingleClick
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


    interface OnSelectRankListener {
        fun onChipClick(enum: HotListEnum)
    }
}
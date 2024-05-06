package com.knight.kotlin.module_mine.activity

import androidx.recyclerview.widget.LinearLayoutManager
import com.knight.kotlin.library_base.activity.BaseActivity
import com.knight.kotlin.library_base.ktx.init
import com.knight.kotlin.library_base.ktx.setOnClick
import com.knight.kotlin.library_base.route.RouteActivity
import com.knight.kotlin.library_util.startPageWithParams
import com.knight.kotlin.library_widget.ktx.setItemClickListener
import com.knight.kotlin.module_mine.R
import com.knight.kotlin.module_mine.adapter.RankCoinAdapter
import com.knight.kotlin.module_mine.databinding.MineCoinRankActivityBinding
import com.knight.kotlin.module_mine.entity.CoinRankListEntity
import com.knight.kotlin.module_mine.vm.CoinRankViewModel
import com.scwang.smart.refresh.layout.api.RefreshLayout
import com.scwang.smart.refresh.layout.listener.OnLoadMoreListener
import com.scwang.smart.refresh.layout.listener.OnRefreshListener
import com.wyjson.router.annotation.Route
import dagger.hilt.android.AndroidEntryPoint

/**
 * Author:Knight
 * Time:2022/5/7 13:43
 * Description:CoinRankActivity
 */
@AndroidEntryPoint
@Route(path = RouteActivity.Mine.UserCoinRankActivity)
class CoinRankActivity : BaseActivity<MineCoinRankActivityBinding,CoinRankViewModel>(),OnRefreshListener,OnLoadMoreListener {



    private val mRankCoinAdapter : RankCoinAdapter by lazy {RankCoinAdapter(mutableListOf())}
    private var page = 1

    override fun setThemeColor(isDarkMode: Boolean) {

    }

    override fun MineCoinRankActivityBinding.initView() {
        includeCoinrankToolbar.baseTvTitle.setText(getString(R.string.mine_coin_rank))
        includeCoinrankToolbar.baseIvBack.setOnClick { finish() }
        includeMineCoinrank.baseBodyRv.init(LinearLayoutManager(this@CoinRankActivity),mRankCoinAdapter,false)
        includeMineCoinrank.baseFreshlayout.setOnRefreshListener(this@CoinRankActivity)
        includeMineCoinrank.baseFreshlayout.setOnLoadMoreListener(this@CoinRankActivity)
        requestLoading(includeMineCoinrank.baseFreshlayout)
        initListener()

    }

    override fun initObserver() {

    }

    override fun initRequestData() {
        mViewModel.getRankCoin(page).observerKt {
            getRankData(it)
        }
    }

    override fun reLoadData() {
        mBinding.includeMineCoinrank.baseFreshlayout.setEnableLoadMore(false)
        mViewModel.getRankCoin(page).observerKt {
            getRankData(it)
        }
    }

    override fun onRefresh(refreshLayout: RefreshLayout) {
        page = 1
        mViewModel.getRankCoin(page).observerKt {
            getRankData(it)
        }
        mBinding.includeMineCoinrank.baseFreshlayout.setEnableLoadMore(false)
    }

    override fun onLoadMore(refreshLayout: RefreshLayout) {
        mViewModel.getRankCoin(page).observerKt {
            getRankData(it)
        }
    }


    /**
     *
     * 获取积分排行榜数据
     */
    fun getRankData(data:CoinRankListEntity) {
        requestSuccess()
        mBinding.includeMineCoinrank.baseFreshlayout.finishRefresh()
        mBinding.includeMineCoinrank.baseFreshlayout.finishLoadMore()
        if (data.datas.size > 0) {
            if (page == 1) {
                mRankCoinAdapter.setNewInstance(data.datas)
            } else {
                mRankCoinAdapter.addData(data.datas)
            }
            page++
        } else {
            mBinding.includeMineCoinrank.baseFreshlayout.setEnableLoadMore(true)
        }
    }


    fun initListener() {
        mRankCoinAdapter.run {
            setItemClickListener { adapter, view, position ->
              startPageWithParams(RouteActivity.Mine.OtherShareArticleActivity,"uid" to data[position].userId)
            }
        }
    }
}
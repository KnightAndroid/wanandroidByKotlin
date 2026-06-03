package com.knight.kotlin.module_mine.activity

import android.view.LayoutInflater
import android.view.View
import android.widget.LinearLayout
import androidx.recyclerview.widget.LinearLayoutManager
import com.core.library_base.ktx.init
import com.core.library_base.ktx.setOnClick
import com.core.library_base.route.RouteActivity
import com.knight.kotlin.library_base.activity.BaseMviActivity
import com.knight.kotlin.library_util.toast
import com.knight.kotlin.library_widget.CountNumberView
import com.knight.kotlin.module_mine.R
import com.knight.kotlin.module_mine.adapter.MyCointsAdapter
import com.knight.kotlin.module_mine.contract.MyDetailCoinsContract
import com.knight.kotlin.module_mine.databinding.MineCoinsActivityBinding
import com.knight.kotlin.module_mine.dialog.CoinsRuleDialog
import com.knight.kotlin.module_mine.vm.MyDetailCoinsViewModel
import com.scwang.smart.refresh.layout.api.RefreshLayout
import com.scwang.smart.refresh.layout.listener.OnLoadMoreListener
import com.scwang.smart.refresh.layout.listener.OnRefreshListener
import com.wyjson.router.annotation.Param
import com.wyjson.router.annotation.Route
import dagger.hilt.android.AndroidEntryPoint

/**
 * Author:Knight
 * Time:2022/5/11 9:40
 * Description:MyCoinsActivity
 */
/**
 * 我的积分明细
 */
@AndroidEntryPoint
@Route(path = RouteActivity.Mine.MyPointsActivity)
class MyCoinsActivity :
    BaseMviActivity<
            MineCoinsActivityBinding,
            MyDetailCoinsViewModel,
            MyDetailCoinsContract.Event,
            MyDetailCoinsContract.State,
            MyDetailCoinsContract.Effect>(),
    OnRefreshListener,
    OnLoadMoreListener {

    @JvmField
    @Param(name = "userCoin")
    var userCoin = ""

    /**
     * 头部View
     */
    private val userInfoCoinHeadView: View by lazy {
        LayoutInflater.from(this)
            .inflate(
                R.layout.mine_coints_detail,
                null
            )
    }

    /**
     * Adapter
     */
    private val mUserDetailCoinAdapter by lazy {
        MyCointsAdapter()
    }

    override fun setThemeColor(isDarkMode: Boolean) {

    }

    override fun MineCoinsActivityBinding.initView() {
        title = getString(R.string.mine_coin_detail)
        includePointToolbar.baseIvBack.setOnClick {
            finish()
        }

        includePointToolbar.baseTvTitle.setText(
            R.string.mine_coin_detail
        )

        val tvHeadDetailPoint =
            userInfoCoinHeadView.findViewById<CountNumberView>(
                R.id.tv_head_detailpoint
            )

        val pointRuleLayout =
            userInfoCoinHeadView.findViewById<LinearLayout>(
                R.id.mine_ll_point_rule
            )

        includeMineDetailpoint.baseBodyRv.init(
            LinearLayoutManager(this@MyCoinsActivity),
            mUserDetailCoinAdapter,
            true
        )

        pointRuleLayout.setOnClick {

            CoinsRuleDialog.newInstance()
                .showAllowingStateLoss(
                    supportFragmentManager,
                    "coinRuleDialog"
                )
        }

        tvHeadDetailPoint.setTextColor(themeColor)

        tvHeadDetailPoint.showNumberWithAnimation(
            userCoin.toFloatOrNull() ?: 0f,
            CountNumberView.INTREGEX
        )

        includeMineDetailpoint.baseFreshlayout
            .setOnRefreshListener(this@MyCoinsActivity)

        includeMineDetailpoint.baseFreshlayout
            .setOnLoadMoreListener(this@MyCoinsActivity)

        requestLoading(
            includeMineDetailpoint.baseFreshlayout
        )
    }

    override fun initObserver() {

    }

    /**
     * 首次请求
     */
    override fun initRequestData() {

        mViewModel.setEvent(
            MyDetailCoinsContract.Event.LoadData
        )
    }

    /**
     * LoadSir重试
     */
    override fun reLoadData() {

        mViewModel.setEvent(
            MyDetailCoinsContract.Event.LoadData
        )
    }

    /**
     * State渲染
     */
    override fun renderState(
        state: MyDetailCoinsContract.State
    ) {

        if (state.isLoading) {
            return
        }

        requestSuccess()

        mBinding.includeMineDetailpoint
            .baseFreshlayout
            .finishRefresh()

        mBinding.includeMineDetailpoint
            .baseFreshlayout
            .finishLoadMore()

        state.coinData?.let { data ->

            if (data.datas.isNotEmpty()) {

                /**
                 * 第一页
                 */
                if (state.page == 2) {

                    mUserDetailCoinAdapter.submitList(
                        data.datas
                    )

                    if (mBinding.includeMineDetailpoint
                            .baseBodyRv.headerCount == 0
                    ) {

                        mBinding.includeMineDetailpoint
                            .baseBodyRv
                            .addHeaderView(
                                userInfoCoinHeadView
                            )
                    }

                } else {

                    /**
                     * 加载更多
                     */
                    mUserDetailCoinAdapter.addAll(
                        data.datas
                    )
                }
            }

            mBinding.includeMineDetailpoint
                .baseFreshlayout
                .setEnableLoadMore(
                    !state.noMoreData
                )
        }
    }

    /**
     * Effect处理
     */
    override fun handleEffect(
        effect: MyDetailCoinsContract.Effect
    ) {

        when (effect) {

            is MyDetailCoinsContract.Effect.ShowToast -> {

                toast(effect.msg)
            }
        }
    }

    /**
     * 下拉刷新
     */
    override fun onRefresh(
        refreshLayout: RefreshLayout
    ) {

        mBinding.includeMineDetailpoint
            .baseFreshlayout
            .setEnableLoadMore(true)

        mViewModel.setEvent(
            MyDetailCoinsContract.Event.Refresh
        )
    }

    /**
     * 上拉加载更多
     */
    override fun onLoadMore(
        refreshLayout: RefreshLayout
    ) {

        mViewModel.setEvent(
            MyDetailCoinsContract.Event.LoadMore
        )
    }
}
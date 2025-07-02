package com.knight.kotlin.module_mine.activity

import android.view.LayoutInflater
import android.view.View
import android.widget.LinearLayout
import androidx.recyclerview.widget.LinearLayoutManager
import com.knight.kotlin.library_base.activity.BaseActivity
import com.core.library_base.ktx.init
import com.core.library_base.ktx.setOnClick
import com.core.library_base.route.RouteActivity
import com.knight.kotlin.library_widget.CountNumberView
import com.knight.kotlin.module_mine.R
import com.knight.kotlin.module_mine.adapter.MyCointsAdapter
import com.knight.kotlin.module_mine.databinding.MineCoinsActivityBinding
import com.knight.kotlin.module_mine.dialog.CoinsRuleDialog
import com.knight.kotlin.module_mine.entity.MyDetailCoinListEntity
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
@AndroidEntryPoint
@Route(path = RouteActivity.Mine.MyPointsActivity)
class MyCoinsActivity : BaseActivity<MineCoinsActivityBinding, MyDetailCoinsViewModel>(),OnRefreshListener,OnLoadMoreListener{

    @JvmField
    @Param(name = "userCoin")
    var userCoin = ""
    private var page = 1


    private val userInfoCoinHeadView: View by lazy{ LayoutInflater.from(this@MyCoinsActivity).inflate(R.layout.mine_coints_detail, null) }
    private val mUserDetailCoinAdapter:MyCointsAdapter by lazy{MyCointsAdapter()}
    override fun setThemeColor(isDarkMode: Boolean) {

    }

    override fun MineCoinsActivityBinding.initView() {
        mBinding.includePointToolbar.baseIvBack.setOnClick { finish() }
        mBinding.includePointToolbar.baseTvTitle.setText(getString(R.string.mine_coin_detail))
        val tv_head_detailpoint = userInfoCoinHeadView.findViewById<CountNumberView>(R.id.tv_head_detailpoint)
        val mine_ll_point_rule = userInfoCoinHeadView.findViewById<LinearLayout>(R.id.mine_ll_point_rule)
        includeMineDetailpoint.baseBodyRv.init(LinearLayoutManager(this@MyCoinsActivity),mUserDetailCoinAdapter,true)
        mine_ll_point_rule.setOnClick {
            CoinsRuleDialog.newInstance().showAllowingStateLoss(supportFragmentManager,"coinRuleDialog")
        }
        tv_head_detailpoint.setTextColor(themeColor)
        tv_head_detailpoint.showNumberWithAnimation(userCoin.toFloat(),CountNumberView.INTREGEX)
        includeMineDetailpoint.baseFreshlayout.setOnRefreshListener(this@MyCoinsActivity)
        includeMineDetailpoint.baseFreshlayout.setOnLoadMoreListener(this@MyCoinsActivity)
        requestLoading(includeMineDetailpoint.baseFreshlayout)

    }

    override fun initObserver() {
    }

    override fun initRequestData() {
        mViewModel.getMyDetailCoin(page).observerKt {
            setUserDetailCoin(it)
        }
    }

    override fun reLoadData() {
        mViewModel.getMyDetailCoin(page).observerKt {
            setUserDetailCoin(it)
        }
    }

    override fun onRefresh(refreshLayout: RefreshLayout) {
        page = 1
        mViewModel.getMyDetailCoin(page).observerKt {
            setUserDetailCoin(it)
        }
        mBinding.includeMineDetailpoint.baseFreshlayout.setEnableLoadMore(true)
    }

    override fun onLoadMore(refreshLayout: RefreshLayout) {
        mViewModel.getMyDetailCoin(page).observerKt {
            setUserDetailCoin(it)
        }
    }


    /***
     *
     * 设置我的积分明细
     */
    private fun setUserDetailCoin(data:MyDetailCoinListEntity) {
        requestSuccess()
        mBinding.includeMineDetailpoint.baseFreshlayout.finishLoadMore()
        mBinding.includeMineDetailpoint.baseFreshlayout.finishRefresh()
        if (data.datas.size > 0) {
            if (page == 1) {
                mUserDetailCoinAdapter.submitList(data.datas)
                if (mBinding.includeMineDetailpoint.baseBodyRv.headerCount == 0) {
                    mBinding.includeMineDetailpoint.baseBodyRv.addHeaderView(userInfoCoinHeadView)
                }
            } else {
                mUserDetailCoinAdapter.addAll(data.datas)
            }
            page ++

        } else {
            mBinding.includeMineDetailpoint.baseFreshlayout.setEnableLoadMore(false)
        }
    }
}
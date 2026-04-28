package com.knight.kotlin.module_navigate.fragment

import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.core.library_base.route.RouteFragment
import com.knight.kotlin.library_aop.loginintercept.LoginCheck
import com.knight.kotlin.library_base.fragment.BaseMviFragment
import com.knight.kotlin.library_base.utils.ArouteUtils
import com.knight.kotlin.library_util.toast
import com.knight.kotlin.library_widget.ktx.init
import com.knight.kotlin.library_widget.ktx.setSafeOnItemChildClickListener
import com.knight.kotlin.library_widget.ktx.setSafeOnItemClickListener
import com.knight.kotlin.module_navigate.adapter.HierachyArticleAdapter
import com.knight.kotlin.module_navigate.contract.HierachyArticleContract
import com.knight.kotlin.module_navigate.databinding.NavigateHierachyArticleFragmentBinding
import com.knight.kotlin.module_navigate.vm.HierachyArticleVm
import com.scwang.smart.refresh.layout.api.RefreshLayout
import com.scwang.smart.refresh.layout.listener.OnLoadMoreListener
import com.scwang.smart.refresh.layout.listener.OnRefreshListener
import com.wyjson.router.annotation.Route
import dagger.hilt.android.AndroidEntryPoint

/**
 * Author:Knight
 * Time:2022/5/6 15:38
 * Description:HierachyTabArticleFragment
 */

@AndroidEntryPoint
@Route(path = RouteFragment.Navigate.HierachyTabArticleFragment)
class HierachyTabArticleFragment :
    BaseMviFragment<
            NavigateHierachyArticleFragmentBinding,
            HierachyArticleVm,
            HierachyArticleContract.Event,
            HierachyArticleContract.State,
            HierachyArticleContract.Effect>(),
    OnLoadMoreListener,
    OnRefreshListener {

    private var cid: Int = 0

    // 保留（业务逻辑需要）
    private var selectItem = -1

    private val mHierachyArticleAdapter: HierachyArticleAdapter by lazy {
        HierachyArticleAdapter(arrayListOf())
    }

    companion object {
        fun newInstance(cid: Int): HierachyTabArticleFragment {
            val fragment = HierachyTabArticleFragment()
            val args = Bundle()
            args.putInt("cid", cid)
            fragment.arguments = args
            return fragment
        }
    }

    override fun setThemeColor(isDarkMode: Boolean) {}

    override fun NavigateHierachyArticleFragmentBinding.initView() {
        cid = arguments?.getInt("cid") ?: 0

        mViewModel.setCid(cid)

        includeTabarticle.baseFreshlayout.setOnRefreshListener(this@HierachyTabArticleFragment)
        includeTabarticle.baseFreshlayout.setOnLoadMoreListener(this@HierachyTabArticleFragment)

        includeTabarticle.baseBodyRv.init(
            LinearLayoutManager(requireActivity()),
            mHierachyArticleAdapter,
            false
        )

        initListener()

        // 👉 保留原来的 loading 行为
        requestLoading(rlTabarticle)
    }

    override fun initObserver() {

    }

    override fun initRequestData() {
        sendEvent(HierachyArticleContract.Event.LoadData(true))
    }

    override fun reLoadData() {

    }

    private fun initListener() {
        mHierachyArticleAdapter.run {

            setSafeOnItemClickListener { _, _, position ->
                val item = items[position]
                ArouteUtils.startWebArticle(
                    item.link,
                    item.title,
                    item.id,
                    item.collect,
                    item.envelopePic,
                    item.desc,
                    item.chapterName,
                    item.author,
                    item.shareUser
                )
            }

            setSafeOnItemChildClickListener(com.core.library_base.R.id.base_icon_collect) { _, _, position ->
                handleCollectClick(position)
            }

            setSafeOnItemChildClickListener(com.core.library_base.R.id.base_article_collect) { _, _, position ->
                handleCollectClick(position)
            }
        }
    }

    @LoginCheck
    private fun handleCollectClick(position: Int) {
        val item = mHierachyArticleAdapter.items[position]
        selectItem = position

        if (item.collect) {
            sendEvent(HierachyArticleContract.Event.UnCollect(item.id, position))
        } else {
            sendEvent(HierachyArticleContract.Event.Collect(item.id, position))
        }
    }

    override fun renderState(state: HierachyArticleContract.State) {

        // 👉 保留你原来的分页逻辑
        if (state.page == 1) {
            mHierachyArticleAdapter.submitList(state.list)
        } else {
            mHierachyArticleAdapter.submitList(state.list.toMutableList())
        }

        mBinding.includeTabarticle.baseFreshlayout.setEnableLoadMore(state.hasMore)
    }

    override fun handleEffect(effect: HierachyArticleContract.Effect) {
        when (effect) {

            is HierachyArticleContract.Effect.ShowToast -> {
                toast(effect.msg)
            }

            is HierachyArticleContract.Effect.FinishRefresh -> {
                mBinding.includeTabarticle.baseFreshlayout.finishRefresh()
                requestSuccess()
            }

            is HierachyArticleContract.Effect.FinishLoadMore -> {
                mBinding.includeTabarticle.baseFreshlayout.finishLoadMore()
            }

            is HierachyArticleContract.Effect.UpdateItem -> {
                mHierachyArticleAdapter.items[effect.position].collect = effect.collect
                mHierachyArticleAdapter.notifyItemChanged(effect.position)
            }
        }
    }

    // ================= 刷新 / 加载更多 =================

    override fun onRefresh(refreshLayout: RefreshLayout) {
        mBinding.includeTabarticle.baseFreshlayout.setEnableLoadMore(true)
        sendEvent(HierachyArticleContract.Event.LoadData(true))
    }

    override fun onLoadMore(refreshLayout: RefreshLayout) {
        sendEvent(HierachyArticleContract.Event.LoadData(false))
    }
}
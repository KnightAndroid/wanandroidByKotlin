package com.knight.kotlin.module_wechat.fragment

import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.core.library_base.ktx.setOnClick
import com.core.library_base.route.RouteFragment
import com.core.library_common.util.ColorUtils
import com.knight.kotlin.library_aop.loginintercept.LoginCheck
import com.knight.kotlin.library_base.fragment.BaseMviFragment
import com.knight.kotlin.library_base.utils.ArouteUtils
import com.knight.kotlin.library_common.util.CacheUtils
import com.knight.kotlin.library_util.toast
import com.knight.kotlin.library_widget.ktx.init
import com.knight.kotlin.library_widget.ktx.setSafeOnItemChildClickListener
import com.knight.kotlin.library_widget.ktx.setSafeOnItemClickListener
import com.knight.kotlin.module_wechat.adapter.WechatArticleAdapter
import com.knight.kotlin.module_wechat.contact.WechatContract
import com.knight.kotlin.module_wechat.databinding.WechatOfficialaccountFragmentBinding
import com.knight.kotlin.module_wechat.vm.WechatVm
import com.scwang.smart.refresh.layout.api.RefreshLayout
import com.scwang.smart.refresh.layout.listener.OnLoadMoreListener
import com.scwang.smart.refresh.layout.listener.OnRefreshListener
import com.wyjson.router.annotation.Route
import dagger.hilt.android.AndroidEntryPoint

/**
 *
 * @ProjectName:    wanandroid
 * @Package:        com.knight.kotlin.module_wechat.fragment
 * @ClassName:      WechatArticleFragment
 * @Description:    java类作用描述
 * @Author:         knight
 * @CreateDate:     2022/3/18 5:15 下午
 * @UpdateUser:     更新者
 * @UpdateDate:     2022/3/18 5:15 下午
 * @UpdateRemark:   更新说明
 * @Version:        1.0
 */

@AndroidEntryPoint
@Route(path = RouteFragment.Wechat.WechatOfficialAccountFragment)
class WechatOfficialAccountFragment :
    BaseMviFragment<
            WechatOfficialaccountFragmentBinding,
            WechatVm,
            WechatContract.Event,
            WechatContract.State,
            WechatContract.Effect>(),
    OnRefreshListener,
    OnLoadMoreListener {

    private var cid: Int = 0
    private var keyword: String = ""
    private var selectItemPosition = 0

    private val mWechatArticleAdapter by lazy { WechatArticleAdapter() }

    companion object {
        fun newInstance(cid: Int): WechatOfficialAccountFragment {
            return WechatOfficialAccountFragment().apply {
                arguments = Bundle().apply {
                    putInt("cid", cid)
                }
            }
        }
    }

    // =========================
    // Theme
    // =========================
    override fun setThemeColor(isDarkMode: Boolean) {}

    // =========================
    // View 初始化
    // =========================
    override fun WechatOfficialaccountFragmentBinding.initView() {
        cid = arguments?.getInt("cid") ?: 0
        wechatFloatBtn.imageTintList = null
        wechatFloatBtn.backgroundTintList = ColorUtils.createColorStateList(themeColor, themeColor)
        includeWechatArticles.baseFreshlayout.setOnRefreshListener(this@WechatOfficialAccountFragment)
        includeWechatArticles.baseFreshlayout.setOnLoadMoreListener(this@WechatOfficialAccountFragment)

        includeWechatArticles.baseBodyRv.init(
            LinearLayoutManager(activity),
            mWechatArticleAdapter,
            false
        )

        wechatFloatBtn.backgroundTintList =
            ColorUtils.createColorStateList(
                CacheUtils.getThemeColor(),
                CacheUtils.getThemeColor()
            )

        wechatFloatBtn.setOnClick {
            includeWechatArticles.baseBodyRv.smoothScrollToPosition(0)
        }

        initListener()
    }

    override fun initObserver() {

    }

    // 首次请求
    // =========================
    override fun initRequestData() {
        sendEvent(
            WechatContract.Event.LoadArticles(
                cid = cid,
                page = 1
            )
        )
    }

    // =========================
    // Adapter 事件 → Event
    // =========================
    private fun initListener() {
        mWechatArticleAdapter.run {

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

            setSafeOnItemChildClickListener(
                com.core.library_base.R.id.base_icon_collect
            ) { _, _, position ->
                val item = items[position]
                selectItemPosition = position

                collectOrunCollect(item.collect,item.id,selectItemPosition)
            }
        }
    }


    @LoginCheck
    private fun collectOrunCollect(collect: Boolean, articleId: Int,position:Int){
        sendEvent(
            if (collect) {
                WechatContract.Event.UnCollectArticle(
                    articleId = articleId,
                    position = position
                )
            } else {
                WechatContract.Event.CollectArticle(
                    articleId = articleId,
                    position = position
                )
            }
        )
    }



    // =========================
    // 下拉刷新
    // =========================
    override fun onRefresh(refreshLayout: RefreshLayout) {
        keyword = ""

        sendEvent(
            WechatContract.Event.LoadArticles(
                cid = cid,
                page = 1,
                isRefresh = true
            )
        )
    }


    // 加载更多
    // =========================
    override fun onLoadMore(refreshLayout: RefreshLayout) {
        val nextPage =
            (currentState.articlePage?.curpage ?: 1)

        sendEvent(
            WechatContract.Event.LoadArticles(
                cid = cid,
                page = nextPage,
                keyword = keyword
            )
        )
    }

    // =========================
    // 搜索
    // =========================
    fun searchArticlesByKeyWords(keywords: String) {
        keyword = keywords

        sendEvent(
            WechatContract.Event.LoadArticles(
                cid = cid,
                page = 1,
                keyword = keywords,
                isRefresh = true
            )
        )

        mBinding.includeWechatArticles.baseFreshlayout.autoRefresh()
    }

    // =========================
    // State 渲染
    // =========================
    override fun renderState(state: WechatContract.State) {
        // ⭐ 只在“首次进入 + 列表无数据”时显示 Loading
        if (
            state.isLoading &&
            (state.articlePage == null || state.articlePage.datas.isEmpty())
        ) {
            requestLoading(mBinding.includeWechatArticles.baseFreshlayout)
            return
        }

        requestSuccess()

        mBinding.includeWechatArticles.baseFreshlayout.finishRefresh(state.isRefreshing)
        mBinding.includeWechatArticles.baseFreshlayout.finishLoadMore()

        state.articlePage?.let { pageData ->

            if (pageData.datas.isEmpty()) {
                requestEmptyData()
            } else {
                // ⭐ 核心：只做这一句
                mWechatArticleAdapter.submitList(
                    pageData.datas.toMutableList()
                )
            }

            mBinding.includeWechatArticles.baseFreshlayout
                .setEnableLoadMore(!pageData.over)
        }
    }

    // =========================
    // Effect 处理（一次性）
    // =========================
    override fun handleEffect(effect: WechatContract.Effect) {
        when (effect) {

            is WechatContract.Effect.ShowToast -> {
                toast(effect.msg)
            }

            is WechatContract.Effect.UpdateCollect -> {
                mWechatArticleAdapter.items[effect.position].collect =
                    effect.collect
                mWechatArticleAdapter.notifyItemChanged(effect.position)
            }
        }
    }

    // =========================
    // LoadSir 重试
    // =========================
    override fun reLoadData() {
        sendEvent(
            WechatContract.Event.LoadArticles(
                cid = cid,
                page = 1
            )
        )
    }

}
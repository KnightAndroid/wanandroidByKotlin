package com.knight.kotlin.module_mine.activity

import androidx.recyclerview.widget.LinearLayoutManager
import com.core.library_base.ktx.init
import com.core.library_base.ktx.setOnClick
import com.core.library_base.route.RouteActivity
import com.knight.kotlin.library_base.activity.BaseMviActivity
import com.knight.kotlin.library_base.utils.ArouteUtils
import com.knight.kotlin.library_util.DialogUtils
import com.knight.kotlin.library_util.toast
import com.knight.kotlin.library_widget.ktx.setSafeOnItemChildClickListener
import com.knight.kotlin.library_widget.ktx.setSafeOnItemClickListener
import com.knight.kotlin.module_mine.R
import com.knight.kotlin.module_mine.adapter.MyShareArticleAdapter
import com.knight.kotlin.module_mine.contract.MyShareArticlesContract
import com.knight.kotlin.module_mine.databinding.MineMySharearticlesActivityBinding
import com.knight.kotlin.module_mine.vm.MyShareArticlesViewModel
import com.scwang.smart.refresh.layout.api.RefreshLayout
import com.scwang.smart.refresh.layout.listener.OnLoadMoreListener
import com.scwang.smart.refresh.layout.listener.OnRefreshListener
import com.wyjson.router.annotation.Route
import dagger.hilt.android.AndroidEntryPoint

/**
 * Author:Knight
 * Time:2022/5/17 10:37
 * Description:MyShareArticlesActivity
 */
@AndroidEntryPoint
@Route(path = RouteActivity.Mine.MyShareArticlesActivity)
class MyShareArticlesActivity :
    BaseMviActivity<
            MineMySharearticlesActivityBinding,
            MyShareArticlesViewModel,
            MyShareArticlesContract.Event,
            MyShareArticlesContract.State,
            MyShareArticlesContract.Effect>(),
    OnRefreshListener,
    OnLoadMoreListener {

    private val mAdapter by lazy {
        MyShareArticleAdapter()
    }

    override fun setThemeColor(
        isDarkMode: Boolean
    ) {
    }

    override fun MineMySharearticlesActivityBinding.initView() {
        title = getString(R.string.mine_me_shareArticles)
        includeSharearticlesToolbar.baseTvTitle.setText(
            getString(
                R.string.mine_me_shareArticles
            )
        )

        includeSharearticlesToolbar.baseIvBack.setOnClick {
            finish()
        }

        requestLoading(
            includeMineSharearticlesRv.baseFreshlayout
        )

        includeMineSharearticlesRv.baseBodyRv.init(
            LinearLayoutManager(
                this@MyShareArticlesActivity
            ),
            mAdapter,
            false
        )

        includeMineSharearticlesRv.baseFreshlayout
            .setOnRefreshListener(
                this@MyShareArticlesActivity
            )

        includeMineSharearticlesRv.baseFreshlayout
            .setOnLoadMoreListener(
                this@MyShareArticlesActivity
            )

        initListener()
    }

    override fun initObserver() {

    }

    override fun initRequestData() {
        mViewModel.setEvent(
            MyShareArticlesContract.Event
                .GetMyShareArticles
        )
    }

    /**
     * 初始化事件
     */
    override fun initEvent() {


    }

    /**
     * LoadSir重新加载
     */
    override fun reLoadData() {

        mViewModel.setEvent(
            MyShareArticlesContract.Event
                .GetMyShareArticles
        )
    }

    /**
     * 渲染状态
     */
    override fun renderState(
        state: MyShareArticlesContract.State
    ) {

        if (state.requestSuccess) {

            requestSuccess()

            mBinding.includeMineSharearticlesRv
                .baseFreshlayout
                .finishRefresh()

            mBinding.includeMineSharearticlesRv
                .baseFreshlayout
                .finishLoadMore()

            if (state.isEmpty) {

                requestEmptyData()

            } else {

                mAdapter.submitList(
                    state.articleList.toMutableList()
                )
            }

            mBinding.includeMineSharearticlesRv
                .baseFreshlayout
                .setEnableLoadMore(
                    state.hasMore
                )
        }
    }

    /**
     * Effect
     */
    override fun handleEffect(
        effect: MyShareArticlesContract.Effect
    ) {

        when (effect) {

            is MyShareArticlesContract.Effect.DeleteSuccess -> {

                mAdapter.removeAt(
                    effect.position
                )

                mAdapter.notifyItemRemoved(
                    effect.position
                )
            }

            is MyShareArticlesContract.Effect.ShowToast -> {

                toast(
                    effect.msg
                )
            }

            is MyShareArticlesContract.Effect.OpenArticle -> {

                val item = effect.article

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
        }
    }

    /**
     * 下拉刷新
     */
    override fun onRefresh(
        refreshLayout: RefreshLayout
    ) {

        mBinding.includeMineSharearticlesRv
            .baseFreshlayout
            .setEnableLoadMore(true)

        mViewModel.setEvent(

            MyShareArticlesContract.Event
                .Refresh
        )
    }

    /**
     * 加载更多
     */
    override fun onLoadMore(
        refreshLayout: RefreshLayout
    ) {

        mViewModel.setEvent(

            MyShareArticlesContract.Event
                .LoadMore
        )
    }

    /**
     * 点击事件
     */
    private fun initListener() {

        mAdapter.run {

            /**
             * 点击文章
             */
            setSafeOnItemClickListener {

                    adapter,
                    view,
                    position ->

                ArouteUtils.startWebArticle(

                    items[position].link,

                    items[position].title,

                    items[position].id,

                    items[position].collect,

                    items[position].envelopePic,

                    items[position].desc,

                    items[position].chapterName,

                    items[position].author,

                    items[position].shareUser
                )
            }

            /**
             * 删除分享
             */
            setSafeOnItemChildClickListener(

                R.id.mine_iv_share_articles_delete

            ) {

                    adapter,
                    view,
                    position ->

                DialogUtils.getConfirmDialog(

                    this@MyShareArticlesActivity,

                    getString(
                        R.string.mine_confirm_cancel_share_article
                    ),

                    { dialog, which ->

                        mViewModel.setEvent(

                            MyShareArticlesContract.Event
                                .DeleteMyShareArticle(

                                    items[position].id,

                                    position
                                )
                        )

                    }

                ) { dialog, which ->

                }
            }
        }
    }
}
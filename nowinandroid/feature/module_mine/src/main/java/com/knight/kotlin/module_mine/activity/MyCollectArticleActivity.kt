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
import com.knight.kotlin.module_mine.adapter.MyCollectArticleAdapter
import com.knight.kotlin.module_mine.contract.MyCollectArticleContract
import com.knight.kotlin.module_mine.databinding.MineCollectarticlesActivityBinding
import com.knight.kotlin.module_mine.vm.MyCollectArticleViewModel
import com.scwang.smart.refresh.layout.api.RefreshLayout
import com.scwang.smart.refresh.layout.listener.OnLoadMoreListener
import com.scwang.smart.refresh.layout.listener.OnRefreshListener
import com.wyjson.router.annotation.Route
import dagger.hilt.android.AndroidEntryPoint

/**
 * Author:Knight
 * Time:2022/5/13 10:05
 * Description:MyCollectArticleActivity
 */
/**
 * 我的收藏文章
 */
@AndroidEntryPoint
@Route(path = RouteActivity.Mine.MyCollectArticleActivity)
class MyCollectArticleActivity :
    BaseMviActivity<
            MineCollectarticlesActivityBinding,
            MyCollectArticleViewModel,
            MyCollectArticleContract.Event,
            MyCollectArticleContract.State,
            MyCollectArticleContract.Effect>(),
    OnRefreshListener,
    OnLoadMoreListener {

    private val mMyCollectArticleAdapter by lazy {
        MyCollectArticleAdapter(arrayListOf())
    }

    override fun setThemeColor(isDarkMode: Boolean) {

    }

    override fun MineCollectarticlesActivityBinding.initView() {

        title = getString(R.string.mine_me_collect)
        includeMycollectToolbar.baseIvBack.setOnClick {
            finish()
        }

        includeMineCollectfreshalayout.baseBodyRv.init(
            LinearLayoutManager(this@MyCollectArticleActivity),
            mMyCollectArticleAdapter,
            false
        )

        includeMineCollectfreshalayout.baseFreshlayout
            .setOnRefreshListener(this@MyCollectArticleActivity)

        includeMineCollectfreshalayout.baseFreshlayout
            .setOnLoadMoreListener(this@MyCollectArticleActivity)

        requestLoading(
            includeMineCollectfreshalayout.baseFreshlayout
        )

        initListener()
    }

    override fun initObserver() {

    }

    /**
     * 首次加载
     */
    override fun initRequestData() {

        mBinding.includeMineCollectfreshalayout
            .baseFreshlayout
            .setEnableLoadMore(true)

        mViewModel.setEvent(
            MyCollectArticleContract.Event.LoadData
        )
    }

    /**
     * LoadSir重试
     */
    override fun reLoadData() {

        mViewModel.setEvent(
            MyCollectArticleContract.Event.LoadData
        )
    }

    /**
     * State渲染
     */
    override fun renderState(
        state: MyCollectArticleContract.State
    ) {

        requestSuccess()

        mBinding.includeMineCollectfreshalayout
            .baseFreshlayout
            .finishRefresh()

        mBinding.includeMineCollectfreshalayout
            .baseFreshlayout
            .finishLoadMore()

        state.articleData?.let { articleData ->

            if (articleData.datas.isNotEmpty()) {

                /**
                 * page=0请求后
                 * ViewModel会更新成page=1
                 */
                if (state.page == 1) {

                    mMyCollectArticleAdapter.submitList(
                        articleData.datas
                    )

                } else {

                    mMyCollectArticleAdapter.addAll(
                        articleData.datas
                    )
                }

            } else {

                if (state.page == 0) {

                    requestEmptyData()
                }
            }

            mBinding.includeMineCollectfreshalayout
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
        effect: MyCollectArticleContract.Effect
    ) {

        when (effect) {

            is MyCollectArticleContract.Effect.ShowToast -> {

                toast(effect.msg)
            }

            is MyCollectArticleContract.Effect.RemoveItem -> {

                mMyCollectArticleAdapter.removeAt(
                    effect.position
                )

                mMyCollectArticleAdapter.notifyItemRemoved(
                    effect.position
                )

                if (mMyCollectArticleAdapter.items.isEmpty()) {

                    requestEmptyData()
                }
            }
        }
    }

    /**
     * 下拉刷新
     */
    override fun onRefresh(
        refreshLayout: RefreshLayout
    ) {

        mBinding.includeMineCollectfreshalayout
            .baseFreshlayout
            .setEnableLoadMore(true)

        mViewModel.setEvent(
            MyCollectArticleContract.Event.Refresh
        )
    }

    /**
     * 加载更多
     */
    override fun onLoadMore(
        refreshLayout: RefreshLayout
    ) {

        mViewModel.setEvent(
            MyCollectArticleContract.Event.LoadMore
        )
    }

    /**
     * RecyclerView事件
     */
    private fun initListener() {

        mMyCollectArticleAdapter.run {

            /**
             * 点击文章
             */
            setSafeOnItemClickListener { _, _, position ->

                items.getOrNull(position)?.let { article ->

                    ArouteUtils.startWebArticle(
                        article.link,
                        article.title,
                        article.id,
                        true,
                        article.envelopePic,
                        article.desc,
                        article.chapterName,
                        article.author,
                        article.shareUser
                    )
                }
            }

            /**
             * 收藏按钮
             */
            val cancelCollectAction: (Int) -> Unit = { position ->

                DialogUtils.getConfirmDialog(
                    this@MyCollectArticleActivity,
                    getString(R.string.mine_confirm_cancelarticle),

                    { _, _ ->

                        items.getOrNull(position)?.let {

                            mViewModel.setEvent(
                                MyCollectArticleContract.Event.UnCollectArticle(
                                    articleId = it.originId,
                                    position = position
                                )
                            )
                        }
                    }

                ) { _, _ ->

                }
            }

            setSafeOnItemChildClickListener(
                com.core.library_base.R.id.base_icon_collect
            ) { _, _, position ->

                cancelCollectAction(position)
            }

            setSafeOnItemChildClickListener(
                com.core.library_base.R.id.base_article_collect
            ) { _, _, position ->

                cancelCollectAction(position)
            }
        }
    }
}
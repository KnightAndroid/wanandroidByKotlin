package com.knight.kotlin.module_square.fragment

import androidx.recyclerview.widget.LinearLayoutManager
import com.core.library_base.annotation.EventBusRegister
import com.core.library_base.event.MessageEvent
import com.core.library_base.route.RouteFragment
import com.knight.kotlin.library_aop.loginintercept.LoginCheck
import com.knight.kotlin.library_base.fragment.BaseMviFragment
import com.knight.kotlin.library_base.utils.ArouteUtils
import com.knight.kotlin.library_util.toast
import com.knight.kotlin.library_widget.ktx.init
import com.knight.kotlin.library_widget.ktx.setSafeOnItemChildClickListener
import com.knight.kotlin.library_widget.ktx.setSafeOnItemClickListener
import com.knight.kotlin.module_square.R
import com.knight.kotlin.module_square.adapter.SquareArticleAdapter
import com.knight.kotlin.module_square.constants.SquareConstants
import com.knight.kotlin.module_square.contract.SquareArticleContract
import com.knight.kotlin.module_square.databinding.SquareArticleFragmentBinding
import com.knight.kotlin.module_square.vm.SquareArticleVm
import com.scwang.smart.refresh.layout.api.RefreshLayout
import com.scwang.smart.refresh.layout.listener.OnLoadMoreListener
import com.scwang.smart.refresh.layout.listener.OnRefreshListener
import com.wyjson.router.annotation.Route
import dagger.hilt.android.AndroidEntryPoint
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode

/**
 * Author:Knight
 * Time:2021/12/29 16:36
 * Description:SquareArticleFragment 各标题Fragment
 */

@EventBusRegister
@AndroidEntryPoint
@Route(path = RouteFragment.Square.SquareArticleFragment)
class SquareArticleFragment :
    BaseMviFragment<
            SquareArticleFragmentBinding,
            SquareArticleVm,
            SquareArticleContract.Event,
            SquareArticleContract.State,
            SquareArticleContract.Effect>(),
    OnLoadMoreListener,
    OnRefreshListener {

    // 推荐文章适配器
    private val mSquareArticleAdapter by lazy { SquareArticleAdapter(arrayListOf()) }

    private var currentPage = 1
    private var selectItem = -1

    override fun setThemeColor(isDarkMode: Boolean) {}

    // =========================
    // View 初始化
    // =========================
    override fun SquareArticleFragmentBinding.initView() {

        includeArticleRecycleview.baseBodyRv.init(
            LinearLayoutManager(requireActivity()),
            mSquareArticleAdapter,
            true
        )

        includeArticleRecycleview.baseFreshlayout.setOnLoadMoreListener(this@SquareArticleFragment)
        includeArticleRecycleview.baseFreshlayout.setOnRefreshListener(this@SquareArticleFragment)

        initListener()
    }

    override fun initObserver() {}

    // =========================
    // 首次请求
    // =========================
    override fun initRequestData() {

        sendEvent(
            SquareArticleContract.Event.GetArticleByTag(
                page = 1,
                keyword = SquareConstants.ARTICLE_TYPE
            )
        )
    }

    // =========================
    // Adapter 事件 → Event
    // =========================
    private fun initListener() {

        mSquareArticleAdapter.run {

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

            setSafeOnItemChildClickListener(R.id.square_icon_collect) { _, _, position ->

                val item = items[position]

                selectItem = position

                collectOrunCollect(item.collect, item.id, position)
            }
        }
    }

    // =========================
    // 收藏 / 取消收藏
    // =========================
    @LoginCheck
    private fun collectOrunCollect(
        collect: Boolean,
        articleId: Int,
        position: Int
    ) {

        sendEvent(
            if (collect) {
                SquareArticleContract.Event.UnCollectArticle(
                    id = articleId
                )
            } else {
                SquareArticleContract.Event.CollectArticle(
                    id = articleId
                )
            }
        )
    }

    // =========================
    // 下拉刷新
    // =========================
    override fun onRefresh(refreshLayout: RefreshLayout) {

        currentPage = 1

        sendEvent(
            SquareArticleContract.Event.GetArticleByTag(
                page = 1,
                keyword = SquareConstants.ARTICLE_TYPE
            )
        )
    }

    // =========================
    // 加载更多
    // =========================
    override fun onLoadMore(refreshLayout: RefreshLayout) {

        val nextPage =
            (currentState.articleList?.curPage ?: 1)

        sendEvent(
            SquareArticleContract.Event.GetArticleByTag(
                page = nextPage,
                keyword = SquareConstants.ARTICLE_TYPE
            )
        )
    }

    // =========================
    // State 渲染
    // =========================
    override fun renderState(state: SquareArticleContract.State) {

        if (
            state.loading &&
            (state.articleList == null || state.articleList.datas.isEmpty())
        ) {
            requestLoading(mBinding.includeArticleRecycleview.baseFreshlayout)
            return
        }

        requestSuccess()

        mBinding.includeArticleRecycleview.baseFreshlayout.finishRefresh()
        mBinding.includeArticleRecycleview.baseFreshlayout.finishLoadMore()

        state.articleList?.let { data ->

            if (data.datas.isEmpty()) {

                requestEmptyData()

            } else {

                mSquareArticleAdapter.submitList(
                    data.datas.toMutableList()
                )
            }

            mBinding.includeArticleRecycleview.baseFreshlayout
                .setEnableLoadMore(!data.over)
        }
    }

    // =========================
    // Effect
    // =========================
    override fun handleEffect(effect: SquareArticleContract.Effect) {

        when (effect) {

            is SquareArticleContract.Effect.ShowError -> {

                toast(effect.msg)
            }

            SquareArticleContract.Effect.CollectSuccess -> {

                mSquareArticleAdapter.items[selectItem].collect = true
                mSquareArticleAdapter.notifyItemChanged(selectItem)
            }

            SquareArticleContract.Effect.UnCollectSuccess -> {

                mSquareArticleAdapter.items[selectItem].collect = false
                mSquareArticleAdapter.notifyItemChanged(selectItem)
            }
        }
    }

    // =========================
    // LoadSir 重试
    // =========================
    override fun reLoadData() {

        sendEvent(
            SquareArticleContract.Event.GetArticleByTag(
                page = 1,
                keyword = SquareConstants.ARTICLE_TYPE
            )
        )
    }

    // =========================
    // EventBus
    // =========================
    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onMessageEvent(event: MessageEvent) {

        when (event.type) {

            MessageEvent.MessageType.CollectSuccess -> {

                sendEvent(
                    SquareArticleContract.Event.GetArticleByTag(
                        page = 1,
                        keyword = SquareConstants.ARTICLE_TYPE
                    )
                )
            }

            else -> {}
        }
    }
}
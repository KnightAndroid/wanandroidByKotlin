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
import com.knight.kotlin.module_square.adapter.SquareShareArticleAdapter
import com.knight.kotlin.module_square.contract.SquareListContract
import com.knight.kotlin.module_square.databinding.SquareListFragmentBinding
import com.knight.kotlin.module_square.entity.SquareShareArticleListBean
import com.knight.kotlin.module_square.vm.SquareListVm
import com.scwang.smart.refresh.layout.api.RefreshLayout
import com.scwang.smart.refresh.layout.listener.OnLoadMoreListener
import com.scwang.smart.refresh.layout.listener.OnRefreshListener
import com.wyjson.router.annotation.Route
import dagger.hilt.android.AndroidEntryPoint
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode

/**
 * Author:Knight
 * Time:2021/12/23 15:59
 * Description:SquareShareListFragment 最新分享文章
 */
@EventBusRegister
@AndroidEntryPoint
@Route(path = RouteFragment.Square.SquareShareListFragment)
class SquareShareListFragment :
    BaseMviFragment<
            SquareListFragmentBinding,
            SquareListVm,
            SquareListContract.Event,
            SquareListContract.State,
            SquareListContract.Effect>(),
    OnLoadMoreListener,
    OnRefreshListener {

    //页码
    private var page = 0

    //点击位置
    private var selectItem = -1

    //适配器
    private val mSquareShareArticleAdapter by lazy {
        SquareShareArticleAdapter()
    }

    override fun SquareListFragmentBinding.initView() {

        requestLoading(squareSharearticleFreshlayout)

        squareArticleRv.init(
            LinearLayoutManager(requireActivity()),
            mSquareShareArticleAdapter,
            true
        )

        squareSharearticleFreshlayout.setOnRefreshListener(this@SquareShareListFragment)
        squareSharearticleFreshlayout.setOnLoadMoreListener(this@SquareShareListFragment)

        initAdapterClickListener()
    }

    override fun initObserver() {

    }

    /**
     * 渲染 State
     */
    override fun renderState(state: SquareListContract.State) {

        when {
            state.loading -> {
                requestLoading(mBinding.squareSharearticleFreshlayout)
            }

            state.articleList != null -> {
                setSquareList(state.articleList)
            }
        }
    }

    /**
     * Effect
     */
    override fun handleEffect(effect: SquareListContract.Effect) {

        when (effect) {

            is SquareListContract.Effect.ShowError -> {

                toast(effect.msg)
                requestFailure()
            }

            SquareListContract.Effect.CollectSuccess -> {

                collectArticleSuccess()
            }

            SquareListContract.Effect.UnCollectSuccess -> {

                unCollectArticleSuccess()
            }
        }
    }

    override fun initRequestData() {

        sendEvent(SquareListContract.Event.GetSquareArticles(page))
    }

    override fun reLoadData() {

        sendEvent(SquareListContract.Event.GetSquareArticles(page))
    }

    override fun setThemeColor(isDarkMode: Boolean) {

    }

    /**
     * 设置列表数据
     */
    private fun setSquareList(data: SquareShareArticleListBean) {

        requestSuccess()

        mBinding.squareSharearticleFreshlayout.finishLoadMore()
        mBinding.squareSharearticleFreshlayout.finishRefresh()

        if (data.datas.isNotEmpty()) {

            if (page == 0) {

                mSquareShareArticleAdapter.submitList(data.datas)

            } else {

                mSquareShareArticleAdapter.addAll(data.datas)

            }

            page++

        } else {

            mBinding.squareSharearticleFreshlayout.setEnableLoadMore(false)
        }
    }

    /**
     * Adapter 点击
     */
    private fun initAdapterClickListener() {

        mSquareShareArticleAdapter.run {

            setSafeOnItemClickListener { adapter, view, position ->

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

            setSafeOnItemChildClickListener(R.id.square_icon_collect) { adapter, view, position ->

                selectItem = position

                collectOrunCollect(
                    items[position].collect,
                    items[position].id
                )
            }
        }
    }

    /**
     * 收藏成功
     */
    private fun collectArticleSuccess() {

        mSquareShareArticleAdapter.items[selectItem].collect = true

        mSquareShareArticleAdapter.notifyItemChanged(selectItem)
    }

    /**
     * 取消收藏成功
     */
    private fun unCollectArticleSuccess() {

        mSquareShareArticleAdapter.items[selectItem].collect = false

        mSquareShareArticleAdapter.notifyItemChanged(selectItem)
    }

    /**
     * 收藏 / 取消收藏
     */
    @LoginCheck
    private fun collectOrunCollect(collect: Boolean, articleId: Int) {

        if (collect) {

            sendEvent(
                SquareListContract.Event.UnCollectArticle(articleId)
            )

        } else {

            sendEvent(
                SquareListContract.Event.CollectArticle(articleId)
            )
        }
    }

    override fun onLoadMore(refreshLayout: RefreshLayout) {

        sendEvent(SquareListContract.Event.GetSquareArticles(page))
    }

    override fun onRefresh(refreshLayout: RefreshLayout) {

        page = 0

        mBinding.squareSharearticleFreshlayout.setEnableLoadMore(true)

        sendEvent(SquareListContract.Event.GetSquareArticles(page))
    }

    /**
     * EventBus
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onMessageEvent(event: MessageEvent) {

        when (event.type) {

            MessageEvent.MessageType.CollectSuccess,
            MessageEvent.MessageType.ShareArticleSuccess -> {

                onRefresh(mBinding.squareSharearticleFreshlayout)
            }

            MessageEvent.MessageType.LoginSuccess,
            MessageEvent.MessageType.LogoutSuccess -> {

                page = 0

                sendEvent(SquareListContract.Event.GetSquareArticles(page))
            }

            else -> {}
        }
    }
}
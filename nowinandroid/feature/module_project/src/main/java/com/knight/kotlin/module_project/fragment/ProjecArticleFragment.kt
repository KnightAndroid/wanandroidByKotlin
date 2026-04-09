package com.knight.kotlin.module_project.fragment

import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.core.library_base.annotation.EventBusRegister
import com.core.library_base.event.MessageEvent
import com.core.library_base.ktx.setOnClick
import com.core.library_base.route.RouteFragment
import com.core.library_common.util.ColorUtils
import com.knight.kotlin.library_aop.loginintercept.LoginCheck
import com.knight.kotlin.library_base.fragment.BaseMviFragment
import com.knight.kotlin.library_base.utils.ArouteUtils
import com.knight.kotlin.library_common.util.CacheUtils
import com.knight.kotlin.library_widget.ktx.init
import com.knight.kotlin.library_widget.ktx.setSafeOnItemChildClickListener
import com.knight.kotlin.library_widget.ktx.setSafeOnItemClickListener
import com.knight.kotlin.module_project.adapter.ProjectArticleAdapter
import com.knight.kotlin.module_project.contract.ProjectArticleContract
import com.knight.kotlin.module_project.databinding.ProjectArticleFragmentBinding
import com.knight.kotlin.module_project.vm.ProjectArticleVm
import com.scwang.smart.refresh.layout.api.RefreshLayout
import com.scwang.smart.refresh.layout.listener.OnLoadMoreListener
import com.scwang.smart.refresh.layout.listener.OnRefreshListener
import com.wyjson.router.annotation.Route
import dagger.hilt.android.AndroidEntryPoint
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode

/**
 * Author:Knight
 * Time:2022/4/28 18:21
 * Description:ProjecArticleFragment
 */
@EventBusRegister
@AndroidEntryPoint
@Route(path = RouteFragment.Project.ProjectArticleFragment)
class ProjecArticleFragment : BaseMviFragment<
        ProjectArticleFragmentBinding,
        ProjectArticleVm,
        ProjectArticleContract.Event,
        ProjectArticleContract.State,
        ProjectArticleContract.Effect>(),
    OnRefreshListener,
    OnLoadMoreListener {

    private val adapter by lazy { ProjectArticleAdapter() }

    private var cid = 0
    private var isNewProject = false

    companion object {
        fun newInstance(cid: Int, isNewProject: Boolean): ProjecArticleFragment {
            return ProjecArticleFragment().apply {
                arguments = Bundle().apply {
                    putInt("cid", cid)
                    putBoolean("isNewProject", isNewProject)
                }
            }
        }
    }

    override fun setThemeColor(isDarkMode: Boolean) {}

    override fun ProjectArticleFragmentBinding.initView() {
        requestLoading(includeProject.baseFreshlayout)

        cid = arguments?.getInt("cid") ?: 0
        isNewProject = arguments?.getBoolean("isNewProject") ?: false

        mViewModel.init(cid, isNewProject)

        includeProject.baseFreshlayout.setOnRefreshListener(this@ProjecArticleFragment)
        includeProject.baseFreshlayout.setOnLoadMoreListener(this@ProjecArticleFragment)

        includeProject.baseBodyRv.init(
            LinearLayoutManager(requireActivity()),
            adapter,
            true
        )

        projectFloatBtn.backgroundTintList =
            ColorUtils.createColorStateList(
                CacheUtils.getThemeColor(),
                CacheUtils.getThemeColor()
            )

        projectFloatBtn.setOnClick {
            includeProject.baseBodyRv.smoothScrollToPosition(0)
        }

        initListener()
    }

    override fun initObserver() {

    }

    override fun initRequestData() {
        sendEvent(ProjectArticleContract.Event.Refresh)
    }

    override fun reLoadData() {
        sendEvent(ProjectArticleContract.Event.Refresh)
    }

    override fun renderState(state: ProjectArticleContract.State) {

        if (state.isRefresh) {
            mBinding.includeProject.baseFreshlayout.finishRefresh()
        } else {
            mBinding.includeProject.baseFreshlayout.finishLoadMore()
        }

        if (state.list.isNotEmpty()) {
            requestSuccess()
            adapter.submitList(state.list.toMutableList())
        }

        mBinding.includeProject.baseFreshlayout.setEnableLoadMore(state.hasMore)
    }

    override fun handleEffect(effect: ProjectArticleContract.Effect) {
        when (effect) {
            is ProjectArticleContract.Effect.Toast -> {
                // toast
            }
        }
    }

    override fun onRefresh(refreshLayout: RefreshLayout) {
        sendEvent(ProjectArticleContract.Event.Refresh)
    }

    override fun onLoadMore(refreshLayout: RefreshLayout) {
        sendEvent(ProjectArticleContract.Event.LoadMore)
    }

    private fun initListener() {
        adapter.setSafeOnItemChildClickListener(
            com.core.library_base.R.id.base_article_collect
        ) { _, _, position ->
            val item = adapter.items[position]
            handleCollectClick(item.collect, item.id, position)
        }

        adapter.setSafeOnItemClickListener { _, _, position ->
            val item = adapter.items[position]
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

    @LoginCheck
    private fun handleCollectClick(collect: Boolean, id: Int, position: Int) {
        if (collect) {
            sendEvent(ProjectArticleContract.Event.UnCollect(id, position))
        } else {
            sendEvent(ProjectArticleContract.Event.Collect(id, position))
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onMessageEvent(event: MessageEvent) {
        when (event.type) {
            MessageEvent.MessageType.CollectSuccess,
            MessageEvent.MessageType.ShareArticleSuccess,
            MessageEvent.MessageType.LoginSuccess,
            MessageEvent.MessageType.LogoutSuccess -> {
                sendEvent(ProjectArticleContract.Event.Refresh)
            }
            else -> {}
        }
    }
}
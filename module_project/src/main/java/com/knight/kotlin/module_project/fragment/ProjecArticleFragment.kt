package com.knight.kotlin.module_project.fragment

import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.knight.kotlin.library_aop.loginintercept.LoginCheck
import com.knight.kotlin.library_base.annotation.EventBusRegister
import com.knight.kotlin.library_base.event.MessageEvent
import com.knight.kotlin.library_base.fragment.BaseFragment
import com.knight.kotlin.library_base.ktx.observeLiveData
import com.knight.kotlin.library_base.ktx.setOnClick
import com.knight.kotlin.library_base.route.RouteFragment
import com.knight.kotlin.library_base.util.ArouteUtils
import com.knight.kotlin.library_base.util.CacheUtils
import com.knight.kotlin.library_base.util.ColorUtils
import com.knight.kotlin.library_widget.ktx.init
import com.knight.kotlin.module_project.adapter.ProjectArticleAdapter
import com.knight.kotlin.module_project.databinding.ProjectArticleFragmentBinding
import com.knight.kotlin.module_project.entity.ProjectArticleListBean
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
class ProjecArticleFragment:BaseFragment<ProjectArticleFragmentBinding, ProjectArticleVm>(),OnRefreshListener,OnLoadMoreListener {


    //文章适配器列表
    private val mProjectArticleAdapter:ProjectArticleAdapter by lazy { ProjectArticleAdapter(mutableListOf())}

    /**
     * 页码
     */
    private var page:Int = 1

    /**
     * 项目类别
     *
     */
    private var cid:Int = 0

    /**
     * 是否是新文章
     *
     */
    private var isNewProject = false

    /**
     *
     * 选择点赞文章
     */
    private var selectItem = -1

    companion object {
        fun newInstance(cid: Int, isNewProject: Boolean): ProjecArticleFragment {
            val projectViewpagerFragment: ProjecArticleFragment = ProjecArticleFragment()
            val args = Bundle()
            args.putInt("cid", cid)
            args.putBoolean("isNewProject", isNewProject)
            projectViewpagerFragment.setArguments(args)
            return projectViewpagerFragment
        }
    }

    override fun setThemeColor(isDarkMode: Boolean) {

    }

    override fun ProjectArticleFragmentBinding.initView() {
        requestLoading(includeProject.baseFreshlayout)
        cid = arguments?.getInt("cid") ?: 0
        isNewProject = arguments?.getBoolean("isNewProject") ?:false
        if (isNewProject) page = 0 else page =1
        includeProject.baseFreshlayout.setOnRefreshListener(this@ProjecArticleFragment)
        includeProject.baseFreshlayout.setOnLoadMoreListener(this@ProjecArticleFragment)
        includeProject.baseBodyRv.init(LinearLayoutManager(requireActivity()),mProjectArticleAdapter,true)
        projectFloatBtn.backgroundTintList = ColorUtils.createColorStateList(CacheUtils.getThemeColor(), CacheUtils.getThemeColor())
        initListener()
        projectFloatBtn.setOnClick {
            includeProject.baseBodyRv.smoothScrollToPosition(0)
        }
    }

    override fun initObserver() {
        observeLiveData(mViewModel.collectArticle,::collectArticleSuccess)
        observeLiveData(mViewModel.unCollectArticle,::unCollectArticleSuccess)
        observeLiveData(mViewModel.projectArticle,::setProjectArticle)
    }

    override fun initRequestData() {
        if (isNewProject) {
            mViewModel.getNewProjectArticle(page)
        } else {
            mViewModel.getProjectArticle(page, cid)
        }
    }

    override fun reLoadData() {
        if (isNewProject) {
            mViewModel.getNewProjectArticle(page)
        } else {
            mViewModel.getProjectArticle(page, cid)
        }
    }

    override fun onRefresh(refreshLayout: RefreshLayout) {
        mBinding.includeProject.baseFreshlayout.setEnableLoadMore(true)
        if (isNewProject) {
            page = 0
            mViewModel.getNewProjectArticle(page)
        } else {
            page = 1
            mViewModel.getProjectArticle(page, cid)
        }


    }

    override fun onLoadMore(refreshLayout: RefreshLayout) {
        if (isNewProject) {
            mViewModel.getNewProjectArticle(page)
        } else {
            mViewModel.getProjectArticle(page,cid)
        }
    }

    /**
     *
     * 设置项目文章列表数据
     */
    private fun setProjectArticle(data:ProjectArticleListBean) {
        requestSuccess()
        mBinding.includeProject.baseFreshlayout.finishLoadMore()
        mBinding.includeProject.baseFreshlayout.finishRefresh()
        if (data.datas.size > 0) {
            if (data.curPage == 1) {
                mProjectArticleAdapter.setNewInstance(data.datas)
            } else {
                mProjectArticleAdapter.addData(data.datas)
            }

            if (data.datas.size == 0) {
                mBinding.includeProject.baseFreshlayout.setEnableLoadMore(false)
            } else {
                page++
            }
        } else {
            mBinding.includeProject.baseFreshlayout.setEnableLoadMore(false)
        }
    }


    private fun initListener() {
        mProjectArticleAdapter.run {
            addChildClickViewIds(com.knight.kotlin.library_base.R.id.base_article_collect)
            setOnItemChildClickListener { adapter, view, position ->
                selectItem = position
                collectOrunCollect(data[position].collect,data[position].id)
            }

            setOnItemClickListener { adapter, view, position ->
                ArouteUtils.startWebArticle(data.get(position).link,data.get(position).title,
                    data.get(position).id,data.get(position).collect,data.get(position).envelopePic,
                    data.get(position).desc,data.get(position).chapterName,data[position].author,data[position].shareUser)
            }


        }
    }

    /**
     *
     * 收藏文章成功
     */
    private fun collectArticleSuccess(boolean: Boolean) {
        mProjectArticleAdapter.data[selectItem].collect = true
        mProjectArticleAdapter.notifyItemChanged(selectItem)
    }

    /**
     *
     * 取消收藏文章成功
     */
    private fun unCollectArticleSuccess(boolean: Boolean) {
        mProjectArticleAdapter.data[selectItem].collect = false
        mProjectArticleAdapter.notifyItemChanged(selectItem)
    }

    @LoginCheck
    private fun collectOrunCollect(collect: Boolean, articleId: Int) {
        if (collect) {
            mViewModel.unCollectArticle(articleId)
        } else {
            mViewModel.collectArticle(articleId)
        }
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onMessageEvent(event: MessageEvent) {
        when (event.type) {
            //收藏成功 分享文章成功 登录成功,登出成功
            MessageEvent.MessageType.CollectSuccess, MessageEvent.MessageType.ShareArticleSuccess,
            MessageEvent.MessageType.LoginSuccess, MessageEvent.MessageType.LogoutSuccess-> {
                onRefresh(mBinding.includeProject.baseFreshlayout)
            }

            else -> {}
        }

    }
}
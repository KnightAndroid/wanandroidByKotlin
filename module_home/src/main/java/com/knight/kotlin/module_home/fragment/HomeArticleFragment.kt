package com.knight.kotlin.module_home.fragment

import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.alibaba.android.arouter.facade.annotation.Route
import com.knight.kotlin.library_aop.loginintercept.LoginCheck
import com.knight.kotlin.library_base.annotation.EventBusRegister
import com.knight.kotlin.library_base.event.MessageEvent
import com.knight.kotlin.library_base.fragment.BaseFragment
import com.knight.kotlin.library_base.ktx.observeLiveData
import com.knight.kotlin.library_base.ktx.observeLiveDataWithError
import com.knight.kotlin.library_base.route.RouteFragment
import com.knight.kotlin.library_base.util.ArouteUtils
import com.knight.kotlin.library_widget.ktx.init
import com.knight.kotlin.library_widget.ktx.setItemChildClickListener
import com.knight.kotlin.library_widget.ktx.setItemClickListener
import com.knight.kotlin.module_home.R
import com.knight.kotlin.module_home.adapter.HomeArticleAdapter
import com.knight.kotlin.module_home.constants.HomeConstants
import com.knight.kotlin.module_home.databinding.HomeArticleFragmentBinding
import com.knight.kotlin.module_home.entity.HomeArticleListBean
import com.knight.kotlin.module_home.vm.HomeArticleVm
import com.scwang.smart.refresh.layout.api.RefreshLayout
import com.scwang.smart.refresh.layout.listener.OnLoadMoreListener
import com.scwang.smart.refresh.layout.listener.OnRefreshListener
import dagger.hilt.android.AndroidEntryPoint
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode

/**
 * Author:Knight
 * Time:2021/12/29 16:36
 * Description:HomeArticleFragment
 */
@EventBusRegister
@AndroidEntryPoint
@Route(path = RouteFragment.Home.HomeArticleFragment)
class HomeArticleFragment:BaseFragment<HomeArticleFragmentBinding,HomeArticleVm>(),OnLoadMoreListener,OnRefreshListener {
    override val mViewModel: HomeArticleVm by viewModels()
    //推荐文章适配器
    private val mHomeArticleAdapter: HomeArticleAdapter by lazy { HomeArticleAdapter(arrayListOf()) }
    private var currentPage = 0
    //选择收藏/取消收藏的Item项
    private var selectItem = -1


    override fun setThemeColor(isDarkMode: Boolean) {

    }

    override fun HomeArticleFragmentBinding.initView() {
        includeArticleRecycleview.baseBodyRv.init(LinearLayoutManager(requireActivity()),mHomeArticleAdapter,true)
        includeArticleRecycleview.baseFreshlayout.setOnLoadMoreListener(this@HomeArticleFragment)
        includeArticleRecycleview.baseFreshlayout.setOnRefreshListener(this@HomeArticleFragment)
        mHomeArticleAdapter.run {
            setItemClickListener { adapter, view, position ->
                ArouteUtils.startWebArticle(
                    mHomeArticleAdapter.data[position].link,
                    mHomeArticleAdapter.data[position].title,
                    mHomeArticleAdapter.data[position].id,
                    mHomeArticleAdapter.data[position].collect,
                    mHomeArticleAdapter.data[position].envelopePic,
                    mHomeArticleAdapter.data[position].desc,
                    mHomeArticleAdapter.data[position].chapterName,
                    mHomeArticleAdapter.data[position].author,
                    mHomeArticleAdapter.data[position].shareUser
                )
            }

            addChildClickViewIds(R.id.home_icon_collect)
            setItemChildClickListener { adapter, view, position ->
                when (view.id) {
                    R.id.home_icon_collect -> {
                        selectItem = position
                        collectOrunCollect(
                            mHomeArticleAdapter.data[position].collect,
                            mHomeArticleAdapter.data[position].id
                        )
                    }
                }
            }


        }
    }

    override fun initObserver() {
        observeLiveData(mViewModel.collectArticle, ::collectSucess)
        observeLiveData(mViewModel.unCollectArticle, ::unCollectSuccess)
        observeLiveDataWithError(mViewModel.articleList,mViewModel.articleListSuccess,::setArticleDatas,::setArticleDatasFailure)

    }

    override fun initRequestData() {
        requestLoading(mBinding.llHome)
        mViewModel.getArticleByTag(currentPage, HomeConstants.ARTICLE_TYPE)
    }

    override fun reLoadData() {
        currentPage = 0
        mViewModel.getArticleByTag(currentPage, HomeConstants.ARTICLE_TYPE)
    }

    override fun onLoadMore(refreshLayout: RefreshLayout) {
        mViewModel.getArticleByTag(currentPage, HomeConstants.ARTICLE_TYPE)
    }

    override fun onRefresh(refreshLayout: RefreshLayout) {
        currentPage = 0
        mViewModel.getArticleByTag(currentPage, HomeConstants.ARTICLE_TYPE)
    }


    @LoginCheck
    private fun collectOrunCollect(collect: Boolean, articleId: Int) {
        if (collect) {
            mViewModel.unCollectArticle(articleId)
        } else {
            mViewModel.collectArticle(articleId)
        }
    }


    /**
     *
     * 获取首页文章列表数据
     */
    private fun setArticleDatas(data: HomeArticleListBean) {
        requestSuccess()
        //这里获取currentPage自动会加1
        currentPage = data.curPage
        mBinding.includeArticleRecycleview.baseFreshlayout.finishRefresh()
        mBinding.includeArticleRecycleview.baseFreshlayout.finishLoadMore()
        if (currentPage > 1) {
            mHomeArticleAdapter.addData(data.datas)
        } else {
            mHomeArticleAdapter.setNewInstance(data.datas)
        }

        if (data.datas.size == 0) {
            mBinding.includeArticleRecycleview.baseFreshlayout.setEnableLoadMore(false)
        }

    }

    private fun setArticleDatasFailure(){
        if (currentPage == 0) {
            requestFailure()
        }
    }

    /**
     *
     * 收藏成功
     */
    private fun collectSucess(data: Boolean) {
        mHomeArticleAdapter.data[selectItem].collect = true
        mHomeArticleAdapter.notifyItemChanged(selectItem)

    }

    /**
     *
     * 取消收藏
     */
    private fun unCollectSuccess(data: Boolean) {
        mHomeArticleAdapter.data[selectItem].collect = false
        mHomeArticleAdapter.notifyItemChanged(selectItem)

    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onMessageEvent(event: MessageEvent) {
        when (event.type) {
            MessageEvent.MessageType.CollectSuccess ->{
                currentPage = 0
                mViewModel.getArticleByTag(currentPage,HomeConstants.ARTICLE_TYPE)
            }

            else -> {}
        }
    }


}
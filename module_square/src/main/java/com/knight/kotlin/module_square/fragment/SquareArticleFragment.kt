package com.knight.kotlin.module_square.fragment

import androidx.recyclerview.widget.LinearLayoutManager
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
import com.knight.kotlin.module_square.R
import com.knight.kotlin.module_square.adapter.SquareArticleAdapter
import com.knight.kotlin.module_square.constants.SquareConstants
import com.knight.kotlin.module_square.databinding.SquareArticleFragmentBinding
import com.knight.kotlin.module_square.entity.SquareArticleListBean
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
 * Description:SquareArticleFragment
 */

@EventBusRegister
@AndroidEntryPoint
@Route(path = RouteFragment.Square.SquareArticleFragment)
class SquareArticleFragment:BaseFragment<SquareArticleFragmentBinding, SquareArticleVm>(),OnLoadMoreListener,OnRefreshListener {

    //推荐文章适配器
    private val mSquareArticleAdapter: SquareArticleAdapter by lazy { SquareArticleAdapter(arrayListOf()) }
    private var currentPage = 0
    //选择收藏/取消收藏的Item项
    private var selectItem = -1


    override fun setThemeColor(isDarkMode: Boolean) {

    }

    override fun SquareArticleFragmentBinding.initView() {
        includeArticleRecycleview.baseBodyRv.init(LinearLayoutManager(requireActivity()),mSquareArticleAdapter,true)
        includeArticleRecycleview.baseFreshlayout.setOnLoadMoreListener(this@SquareArticleFragment)
        includeArticleRecycleview.baseFreshlayout.setOnRefreshListener(this@SquareArticleFragment)
        mSquareArticleAdapter.run {
            setItemClickListener { adapter, view, position ->
                ArouteUtils.startWebArticle(
                    mSquareArticleAdapter.data[position].link,
                    mSquareArticleAdapter.data[position].title,
                    mSquareArticleAdapter.data[position].id,
                    mSquareArticleAdapter.data[position].collect,
                    mSquareArticleAdapter.data[position].envelopePic,
                    mSquareArticleAdapter.data[position].desc,
                    mSquareArticleAdapter.data[position].chapterName,
                    mSquareArticleAdapter.data[position].author,
                    mSquareArticleAdapter.data[position].shareUser
                )
            }

            addChildClickViewIds(R.id.square_icon_collect)
            setItemChildClickListener { adapter, view, position ->
                when (view.id) {
                    R.id.square_icon_collect -> {
                        selectItem = position
                        collectOrunCollect(
                            mSquareArticleAdapter.data[position].collect,
                            mSquareArticleAdapter.data[position].id
                        )
                    }
                }
            }


        }
    }

    override fun initObserver() {
        observeLiveData(mViewModel.collectArticleStatus, ::collect)
        observeLiveData(mViewModel.unCollectArticleStatus, ::unCollect)
        observeLiveDataWithError(mViewModel.articleList,mViewModel.articleListSuccess,::setArticleDatas,::setArticleDatasFailure)

    }

    override fun initRequestData() {
        requestLoading(mBinding.llHome)
        mViewModel.getArticleByTag(currentPage, SquareConstants.ARTICLE_TYPE)
    }

    override fun reLoadData() {
        currentPage = 0
        mViewModel.getArticleByTag(currentPage, SquareConstants.ARTICLE_TYPE)
    }

    override fun onLoadMore(refreshLayout: RefreshLayout) {
        mViewModel.getArticleByTag(currentPage, SquareConstants.ARTICLE_TYPE)
    }

    override fun onRefresh(refreshLayout: RefreshLayout) {
        currentPage = 0
        mViewModel.getArticleByTag(currentPage, SquareConstants.ARTICLE_TYPE)
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
    private fun setArticleDatas(data: SquareArticleListBean) {
        requestSuccess()
        //这里获取currentPage自动会加1
        currentPage = data.curPage
        mBinding.includeArticleRecycleview.baseFreshlayout.finishRefresh()
        mBinding.includeArticleRecycleview.baseFreshlayout.finishLoadMore()
        if (currentPage > 1) {
            mSquareArticleAdapter.addData(data.datas)
        } else {
            mSquareArticleAdapter.setNewInstance(data.datas)
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
    private fun collect(status: Boolean) {
        if (status) {
            mSquareArticleAdapter.data[selectItem].collect = true
            mSquareArticleAdapter.notifyItemChanged(selectItem)
        }


    }

    /**
     *
     * 取消收藏
     */
    private fun unCollect(status: Boolean) {
        if (status) {
            mSquareArticleAdapter.data[selectItem].collect = false
            mSquareArticleAdapter.notifyItemChanged(selectItem)
        }



    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onMessageEvent(event: MessageEvent) {
        when (event.type) {
            MessageEvent.MessageType.CollectSuccess ->{
                currentPage = 0
                mViewModel.getArticleByTag(currentPage,SquareConstants.ARTICLE_TYPE)
            }

            else -> {}
        }
    }


}
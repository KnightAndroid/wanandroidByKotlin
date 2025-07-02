package com.knight.kotlin.module_navigate.fragment

import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.knight.kotlin.library_base.fragment.BaseFragment
import com.core.library_base.route.RouteFragment
import com.knight.kotlin.library_base.utils.ArouteUtils
import com.knight.kotlin.library_aop.loginintercept.LoginCheck
import com.knight.kotlin.library_widget.ktx.init
import com.knight.kotlin.library_widget.ktx.setSafeOnItemChildClickListener
import com.knight.kotlin.library_widget.ktx.setSafeOnItemClickListener
import com.knight.kotlin.module_navigate.adapter.HierachyArticleAdapter
import com.knight.kotlin.module_navigate.databinding.NavigateHierachyArticleFragmentBinding
import com.knight.kotlin.module_navigate.entity.HierachyTabArticleListEntity
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
class HierachyTabArticleFragment: BaseFragment<NavigateHierachyArticleFragmentBinding, HierachyArticleVm>(),OnLoadMoreListener,OnRefreshListener {



    private var cid:Int = 0
    private var page:Int = 0
    //选择收藏/取消收藏的Item项
    private var selectItem = -1

    private val mHierachyArticleAdapter:HierachyArticleAdapter by lazy{HierachyArticleAdapter(arrayListOf())}

    companion object {
        fun newInstance(cid: Int): HierachyTabArticleFragment {
            val hierachyTabArticleFragment = HierachyTabArticleFragment()
            val args = Bundle()
            args.putInt("cid", cid)
            hierachyTabArticleFragment.arguments = args
            return hierachyTabArticleFragment
        }
    }


    override fun setThemeColor(isDarkMode: Boolean) {

    }

    override fun NavigateHierachyArticleFragmentBinding.initView() {
        cid = arguments?.getInt("cid") ?: 0
        includeTabarticle.baseFreshlayout.setOnRefreshListener(this@HierachyTabArticleFragment)
        includeTabarticle.baseFreshlayout.setOnLoadMoreListener(this@HierachyTabArticleFragment)
        includeTabarticle.baseBodyRv.init(LinearLayoutManager(requireActivity()),mHierachyArticleAdapter,false)
        initListener()
        requestLoading(rlTabarticle)
    }


    fun initListener() {
        mHierachyArticleAdapter.run {
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

            setSafeOnItemChildClickListener(com.core.library_base.R.id.base_icon_collect) { adapter, view, position ->

                        selectItem = position
                        collectOrunCollect(
                            items[position].collect,
                            items[position].id
                        )


            }
            setSafeOnItemChildClickListener(com.core.library_base.R.id.base_article_collect) { adapter, view, position ->

                        selectItem = position
                        collectOrunCollect(
                            items[position].collect,
                            items[position].id
                        )


            }

        }
    }


    @LoginCheck
    private fun collectOrunCollect(collect: Boolean, articleId: Int) {
        if (collect) {
            mViewModel.unCollectArticle(articleId).observerKt {
                unCollectSuccess()
            }
        } else {
            mViewModel.collectArticle(articleId).observerKt {
                collectSucess()
            }
        }
    }

    /**
     *
     * 收藏成功
     */
    private fun collectSucess() {
        mHierachyArticleAdapter.items[selectItem].collect = true
        mHierachyArticleAdapter.notifyItemChanged(selectItem)

    }

    /**
     *
     * 取消收藏
     */
    private fun unCollectSuccess() {
        mHierachyArticleAdapter.items[selectItem].collect = false
        mHierachyArticleAdapter.notifyItemChanged(selectItem)

    }





    override fun initObserver() {

    }


    fun setHierachyArticles(data: HierachyTabArticleListEntity) {
        requestSuccess()
        mBinding.includeTabarticle.baseFreshlayout.finishRefresh()
        mBinding.includeTabarticle.baseFreshlayout.finishLoadMore()
        if (data.datas.size > 0) {
            if (page == 0) {
                mHierachyArticleAdapter.submitList(data.datas)
            } else {
                mHierachyArticleAdapter.addAll(data.datas)
            }

            if (data.datas.size == 0) {
                mBinding.includeTabarticle.baseFreshlayout.setEnableLoadMore(false)
            } else {
                page ++
            }
        } else {
            mBinding.includeTabarticle.baseFreshlayout.setEnableLoadMore(false)
        }

    }


    override fun initRequestData() {
        page = 0
        mViewModel.getHierachyArticle(page, cid).observerKt {
            setHierachyArticles(it)
        }
    }

    override fun reLoadData() {
        page = 0
        mViewModel.getHierachyArticle(page, cid).observerKt {
            setHierachyArticles(it)
        }
    }

    override fun onLoadMore(refreshLayout: RefreshLayout) {
        mViewModel.getHierachyArticle(page, cid).observerKt {
            setHierachyArticles(it)
        }
    }

    override fun onRefresh(refreshLayout: RefreshLayout) {
        page = 0
        mBinding.includeTabarticle.baseFreshlayout.setEnableLoadMore(true)
        mViewModel.getHierachyArticle(page, cid).observerKt {
            setHierachyArticles(it)
        }
    }
}
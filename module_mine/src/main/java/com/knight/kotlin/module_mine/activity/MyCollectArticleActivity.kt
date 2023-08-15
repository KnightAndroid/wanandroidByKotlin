package com.knight.kotlin.module_mine.activity

import androidx.activity.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.alibaba.android.arouter.facade.annotation.Route
import com.knight.kotlin.library_base.activity.BaseActivity
import com.knight.kotlin.library_base.ktx.init
import com.knight.kotlin.library_base.ktx.observeLiveData
import com.knight.kotlin.library_base.ktx.setOnClick
import com.knight.kotlin.library_base.route.RouteActivity
import com.knight.kotlin.library_base.util.ArouteUtils
import com.knight.kotlin.library_util.DialogUtils
import com.knight.kotlin.library_widget.ktx.setItemChildClickListener
import com.knight.kotlin.library_widget.ktx.setItemClickListener
import com.knight.kotlin.module_mine.R
import com.knight.kotlin.module_mine.adapter.MyCollectArticleAdapter
import com.knight.kotlin.module_mine.databinding.MineCollectarticlesActivityBinding
import com.knight.kotlin.module_mine.entity.MyCollectArticleListEntity
import com.knight.kotlin.module_mine.vm.MyCollectArticleViewModel
import com.scwang.smart.refresh.layout.api.RefreshLayout
import com.scwang.smart.refresh.layout.listener.OnLoadMoreListener
import com.scwang.smart.refresh.layout.listener.OnRefreshListener
import dagger.hilt.android.AndroidEntryPoint

/**
 * Author:Knight
 * Time:2022/5/13 10:05
 * Description:MyCollectArticleActivity
 */
@AndroidEntryPoint
@Route(path = RouteActivity.Mine.MyCollectArticleActivity)
class MyCollectArticleActivity : BaseActivity<MineCollectarticlesActivityBinding, MyCollectArticleViewModel>(),OnRefreshListener,OnLoadMoreListener {

    override val mViewModel: MyCollectArticleViewModel by viewModels()

    private var page:Int = 0
    private val mMyCollectArticleAdapter:MyCollectArticleAdapter by lazy { MyCollectArticleAdapter(
        arrayListOf()) }

    //选择收藏/取消收藏的Item项
    private var selectItem = -1
    override fun setThemeColor(isDarkMode: Boolean) {

    }

    override fun MineCollectarticlesActivityBinding.initView() {
        includeMycollectToolbar.baseTvTitle.setText(getString(R.string.mine_me_collect))
        includeMycollectToolbar.baseIvBack.setOnClick { finish() }
        includeMineCollectfreshalayout.baseBodyRv.init(LinearLayoutManager(this@MyCollectArticleActivity),mMyCollectArticleAdapter,false)
        includeMineCollectfreshalayout.baseFreshlayout.setOnRefreshListener(this@MyCollectArticleActivity)
        includeMineCollectfreshalayout.baseFreshlayout.setOnLoadMoreListener(this@MyCollectArticleActivity)
        requestLoading(includeMineCollectfreshalayout.baseFreshlayout)
        initListener()

    }

    override fun initObserver() {
        observeLiveData(mViewModel.myCollectArticleLists,::setCollectArticles)
        observeLiveData(mViewModel.unCollectArticle,::unCollectArticle)
    }

    override fun initRequestData() {
        mBinding.includeMineCollectfreshalayout.baseFreshlayout.setEnableLoadMore(true)
        mViewModel.getMyCollectArticles(page)
    }

    override fun reLoadData() {
        mViewModel.getMyCollectArticles(page)
    }

    override fun onRefresh(refreshLayout: RefreshLayout) {
        page = 0
        mBinding.includeMineCollectfreshalayout.baseFreshlayout.setEnableLoadMore(true)
        mViewModel.getMyCollectArticles(page)
    }

    override fun onLoadMore(refreshLayout: RefreshLayout) {
        mViewModel.getMyCollectArticles(page)
    }


    private fun setCollectArticles(myCollectArticles:MyCollectArticleListEntity) {
        requestSuccess()
        mBinding.includeMineCollectfreshalayout.baseFreshlayout.finishRefresh()
        mBinding.includeMineCollectfreshalayout.baseFreshlayout.finishLoadMore()
        if (myCollectArticles.datas.size > 0) {
            if (page == 0) {
                mMyCollectArticleAdapter.setNewInstance(myCollectArticles.datas)
            } else {
                mMyCollectArticleAdapter.addData(myCollectArticles.datas)
            }
            page ++
        } else {
            if (page == 0) {
                requestEmptyData()
            }
            mBinding.includeMineCollectfreshalayout.baseFreshlayout.setEnableLoadMore(false)
        }

    }


    private fun unCollectArticle(data:Boolean) {
        mMyCollectArticleAdapter.data.removeAt(selectItem)
        mMyCollectArticleAdapter.notifyItemRemoved(selectItem)

    }
    private fun initListener() {
        mMyCollectArticleAdapter.run {
            setItemClickListener { adapter, view, position ->
                ArouteUtils.startWebArticle(
                    data[position].link,
                    data[position].title,
                    data[position].id,
                    true,
                    data[position].envelopePic,
                    data[position].desc,
                    data[position].chapterName,
                    data[position].author,
                    data[position].shareUser
                )
            }

            addChildClickViewIds(com.knight.kotlin.library_base.R.id.base_icon_collect,com.knight.kotlin.library_base.R.id.base_article_collect)
            setItemChildClickListener { adapter, view, position ->
                when (view.id) {
                    com.knight.kotlin.library_base.R.id.base_icon_collect,com.knight.kotlin.library_base.R.id.base_article_collect -> {
                        selectItem = position
                        DialogUtils.getConfirmDialog(this@MyCollectArticleActivity,getString(R.string.mine_confirm_cancelarticle),
                            { dialog, which ->
                                selectItem = position
                            mViewModel.unCollectArticle(mMyCollectArticleAdapter.data[position].originId)
                        }) {
                                dialog, which ->
                        }

                    }
                }
            }
        }
    }
}
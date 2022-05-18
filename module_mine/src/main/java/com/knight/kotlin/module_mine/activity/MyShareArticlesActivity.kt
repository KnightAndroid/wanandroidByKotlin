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
import com.knight.kotlin.module_mine.adapter.MyShareArticleAdapter
import com.knight.kotlin.module_mine.databinding.MineMySharearticlesActivityBinding
import com.knight.kotlin.module_mine.entity.MyShareArticleEntity
import com.knight.kotlin.module_mine.vm.MyShareArticlesViewModel
import com.scwang.smart.refresh.layout.api.RefreshLayout
import com.scwang.smart.refresh.layout.listener.OnLoadMoreListener
import com.scwang.smart.refresh.layout.listener.OnRefreshListener
import dagger.hilt.android.AndroidEntryPoint

/**
 * Author:Knight
 * Time:2022/5/17 10:37
 * Description:MyShareArticlesActivity
 */
@AndroidEntryPoint
@Route(path = RouteActivity.Mine.MyShareArticlesActivity)
class MyShareArticlesActivity : BaseActivity<MineMySharearticlesActivityBinding,MyShareArticlesViewModel>(),OnRefreshListener,OnLoadMoreListener {

    override val mViewModel: MyShareArticlesViewModel by viewModels()

    private var page:Int = 1
    private val mMyshareArticleAdater:MyShareArticleAdapter by lazy {MyShareArticleAdapter(arrayListOf())}
    //删除我的分享的Item项
    private var selectItem = -1
    override fun setThemeColor(isDarkMode: Boolean) {

    }

    override fun MineMySharearticlesActivityBinding.initView() {
        includeSharearticlesToolbar.baseTvTitle.setText(getString(R.string.mine_me_shareArticles))
        includeSharearticlesToolbar.baseIvBack.setOnClick { finish() }
        requestLoading(includeMineSharearticlesRv.baseFreshlayout)
        includeMineSharearticlesRv.baseBodyRv.init(LinearLayoutManager(this@MyShareArticlesActivity),mMyshareArticleAdater,false)
        includeMineSharearticlesRv.baseFreshlayout.setOnRefreshListener(this@MyShareArticlesActivity)
        includeMineSharearticlesRv.baseFreshlayout.setOnLoadMoreListener(this@MyShareArticlesActivity)
        initListener()
    }

    override fun initObserver() {
        observeLiveData(mViewModel.myShareArticlesList,::getMyShareArticles)
        observeLiveData(mViewModel.deleteMyArticleSuccess,::deleteArticle)
    }

    override fun initRequestData() {
        mViewModel.getMyShareArticles(page)
    }

    override fun reLoadData() {
        mViewModel.getMyShareArticles(page)
    }

    override fun onRefresh(refreshLayout: RefreshLayout) {
        page = 1
        mViewModel.getMyShareArticles(page)
        mBinding.includeMineSharearticlesRv.baseFreshlayout.setEnableLoadMore(true)
    }

    private fun getMyShareArticles(data: MyShareArticleEntity) {
        requestSuccess()
        mBinding.includeMineSharearticlesRv.baseFreshlayout.finishLoadMore()
        mBinding.includeMineSharearticlesRv.baseFreshlayout.finishRefresh()
        if (data.shareArticles.datas.size > 0) {
            if (page == 1) {
                mMyshareArticleAdater.setNewInstance(data.shareArticles.datas)
            } else {
                mMyshareArticleAdater.addData(data.shareArticles.datas)
            }
            page ++
        } else {
            if (page == 1) {
                requestEmptyData()
            }
            mBinding.includeMineSharearticlesRv.baseFreshlayout.setEnableLoadMore(false)
        }
    }


    private fun deleteArticle(data:Boolean) {
        mMyshareArticleAdater.data.removeAt(selectItem)
        mMyshareArticleAdater.notifyItemRemoved(selectItem)

    }

    override fun onLoadMore(refreshLayout: RefreshLayout) {
        mViewModel.getMyShareArticles(page)
    }


    private fun initListener() {
        mMyshareArticleAdater.run {
            setItemClickListener { adapter, view, position ->
                ArouteUtils.startWebArticle(
                    data[position].link,
                    data[position].title,
                    data[position].id,
                    data[position].collect,
                    data[position].envelopePic,
                    data[position].desc,
                    data[position].chapterName,
                    data[position].author,
                    data[position].shareUser
                )
            }

            addChildClickViewIds(R.id.mine_iv_share_articles_delete)
            setItemChildClickListener { adapter, view, position ->
                selectItem = position
                DialogUtils.getConfirmDialog(this@MyShareArticlesActivity,getString(R.string.mine_confirm_cancel_share_article),
                    { dialog, which ->
                        selectItem = position
                        mViewModel.deleteMyShareArticles(data[position].id)
                    }) {
                        dialog, which ->
                }
            }
        }
    }
}
package com.knight.kotlin.module_mine.activity

import androidx.recyclerview.widget.LinearLayoutManager
import com.knight.kotlin.library_base.activity.BaseActivity
import com.core.library_base.ktx.init
import com.core.library_base.ktx.setOnClick
import com.core.library_base.route.RouteActivity
import com.knight.kotlin.library_base.utils.ArouteUtils
import com.knight.kotlin.library_util.DialogUtils
import com.knight.kotlin.library_widget.ktx.setSafeOnItemChildClickListener
import com.knight.kotlin.library_widget.ktx.setSafeOnItemClickListener
import com.knight.kotlin.module_mine.R
import com.knight.kotlin.module_mine.adapter.MyShareArticleAdapter
import com.knight.kotlin.module_mine.databinding.MineMySharearticlesActivityBinding
import com.knight.kotlin.module_mine.entity.MyShareArticleEntity
import com.knight.kotlin.module_mine.vm.MyShareArticlesViewModel
import com.scwang.smart.refresh.layout.api.RefreshLayout
import com.scwang.smart.refresh.layout.listener.OnLoadMoreListener
import com.scwang.smart.refresh.layout.listener.OnRefreshListener
import com.wyjson.router.annotation.Route
import dagger.hilt.android.AndroidEntryPoint

/**
 * Author:Knight
 * Time:2022/5/17 10:37
 * Description:MyShareArticlesActivity
 */
@AndroidEntryPoint
@Route(path = RouteActivity.Mine.MyShareArticlesActivity)
class MyShareArticlesActivity : BaseActivity<MineMySharearticlesActivityBinding, MyShareArticlesViewModel>(),OnRefreshListener,OnLoadMoreListener {



    private var page:Int = 1
    private val mMyshareArticleAdater:MyShareArticleAdapter by lazy {MyShareArticleAdapter()}
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

    }

    override fun initRequestData() {
        mViewModel.getMyShareArticles(page).observerKt {
            getMyShareArticles(it)
        }
    }

    override fun reLoadData() {
        mViewModel.getMyShareArticles(page).observerKt {
            getMyShareArticles(it)
        }
    }

    override fun onRefresh(refreshLayout: RefreshLayout) {
        page = 1
        mViewModel.getMyShareArticles(page).observerKt {
            getMyShareArticles(it)
        }
        mBinding.includeMineSharearticlesRv.baseFreshlayout.setEnableLoadMore(true)
    }

    private fun getMyShareArticles(data: MyShareArticleEntity) {
        requestSuccess()
        mBinding.includeMineSharearticlesRv.baseFreshlayout.finishLoadMore()
        mBinding.includeMineSharearticlesRv.baseFreshlayout.finishRefresh()
        if (data.shareArticles.datas.size > 0) {
            if (page == 1) {
                mMyshareArticleAdater.submitList(data.shareArticles.datas)
            } else {
                mMyshareArticleAdater.addAll(data.shareArticles.datas)
            }
            page ++
        } else {
            if (page == 1) {
                requestEmptyData()
            }
            mBinding.includeMineSharearticlesRv.baseFreshlayout.setEnableLoadMore(false)
        }
    }


    private fun deleteArticle() {
        mMyshareArticleAdater.removeAt(selectItem)
        mMyshareArticleAdater.notifyItemRemoved(selectItem)

    }

    override fun onLoadMore(refreshLayout: RefreshLayout) {
        mViewModel.getMyShareArticles(page).observerKt {
            getMyShareArticles(it)
        }
    }


    private fun initListener() {
        mMyshareArticleAdater.run {
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


            setSafeOnItemChildClickListener(R.id.mine_iv_share_articles_delete) { adapter, view, position ->
                selectItem = position
                DialogUtils.getConfirmDialog(this@MyShareArticlesActivity,getString(R.string.mine_confirm_cancel_share_article),
                    { dialog, which ->
                        selectItem = position
                        mViewModel.deleteMyShareArticles(items[position].id).observerKt {
                            deleteArticle()
                        }
                    }) {
                        dialog, which ->
                }
            }
        }
    }
}
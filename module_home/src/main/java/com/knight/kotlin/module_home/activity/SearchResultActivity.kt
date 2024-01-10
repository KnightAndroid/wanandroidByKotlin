package com.knight.kotlin.module_home.activity

import android.text.TextUtils
import android.view.inputmethod.EditorInfo
import android.widget.TextView.OnEditorActionListener
import androidx.activity.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.knight.kotlin.library_aop.loginintercept.LoginCheck
import com.knight.kotlin.library_base.activity.BaseActivity
import com.knight.kotlin.library_base.config.Appconfig
import com.knight.kotlin.library_base.ktx.observeLiveData
import com.knight.kotlin.library_base.ktx.setOnClick
import com.knight.kotlin.library_base.route.RouteActivity
import com.knight.kotlin.library_base.util.ArouteUtils
import com.knight.kotlin.library_util.DataBaseUtils
import com.knight.kotlin.library_util.SystemUtils
import com.knight.kotlin.library_util.toast.ToastUtils
import com.knight.kotlin.library_widget.ktx.init
import com.knight.kotlin.library_widget.ktx.setItemChildClickListener
import com.knight.kotlin.library_widget.ktx.setItemClickListener
import com.knight.kotlin.module_home.R
import com.knight.kotlin.module_home.adapter.SearchResultAdapter
import com.knight.kotlin.module_home.databinding.HomeSearchresultActivityBinding
import com.knight.kotlin.module_home.entity.HomeArticleListBean
import com.knight.kotlin.module_home.vm.HomeSearchResultVm
import com.scwang.smart.refresh.layout.api.RefreshLayout
import com.scwang.smart.refresh.layout.listener.OnLoadMoreListener
import com.scwang.smart.refresh.layout.listener.OnRefreshListener
import com.wyjson.router.annotation.Param
import com.wyjson.router.annotation.Route
import dagger.hilt.android.AndroidEntryPoint

/**
 * Author:Knight
 * Time:2022/4/20 10:46
 * Description:SearchResultActivity
 */

@AndroidEntryPoint
@Route(path = RouteActivity.Home.HomeSearchResultActivity)
class SearchResultActivity : BaseActivity<HomeSearchresultActivityBinding, HomeSearchResultVm>(),
    OnRefreshListener, OnLoadMoreListener {


    /**
     * 搜索关键字
     */
    @JvmField
    @Param(name = "keyword")
    var keyword: String = ""

    //选择收藏/取消收藏的Item项
    private var selectItem = -1

    /**
     * 页码
     */
    private var page = 0

    //搜索结果适配器
    private val mSearchResultAdapter: SearchResultAdapter by lazy { SearchResultAdapter(arrayListOf()) }

    override val mViewModel: HomeSearchResultVm by viewModels()
    override fun setThemeColor(isDarkMode: Boolean) {

    }

    override fun HomeSearchresultActivityBinding.initView() {
        requestLoading(includeSearchresult.baseFreshlayout)
        searchresultEt.setText(keyword)
        includeSearchresult.baseBodyRv.init(
            LinearLayoutManager(this@SearchResultActivity),
            mSearchResultAdapter,
            true
        )
        includeSearchresult.baseFreshlayout.setOnLoadMoreListener(this@SearchResultActivity)
        includeSearchresult.baseFreshlayout.setOnRefreshListener(this@SearchResultActivity)
        initListener()
        searchresultIvBack.setOnClick { finish() }
        SystemUtils.editTextChangeListener(searchresultEt, searchresultTvCancel)
        searchresultEt.setOnEditorActionListener(OnEditorActionListener { v, actionId, event ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                keyword = searchresultEt.getText().toString().trim()
                if (TextUtils.isEmpty(keyword)) {
                    ToastUtils.show(R.string.home_input_content_search)
                } else {
                    searchNewKeywords()
                }
                return@OnEditorActionListener true
            }
            false
        })

        searchresultTvCancel.setOnClick {
            if (searchresultTvCancel.text.toString().equals(getString(R.string.home_cancel))) {
                finish()
            } else {
                searchNewKeywords()
            }
        }
    }

    override fun initObserver() {
        observeLiveData(mViewModel.articleResultList, ::setSearchResultData)
        observeLiveData(mViewModel.collectArticle, ::collectSucess)
        observeLiveData(mViewModel.unCollectArticle, ::unCollectSuccess)
    }

    override fun initRequestData() {
        mViewModel.searchArticleByKeyword(page, keyword)
    }

    override fun reLoadData() {
        mBinding.includeSearchresult.baseFreshlayout.setEnableLoadMore(true)
        mViewModel.searchArticleByKeyword(page, keyword)
    }

    override fun onRefresh(refreshLayout: RefreshLayout) {
        page = 0
        mBinding.includeSearchresult.baseFreshlayout.setEnableLoadMore(true)
        mViewModel.searchArticleByKeyword(page, keyword)
    }

    override fun onLoadMore(refreshLayout: RefreshLayout) {
        mViewModel.searchArticleByKeyword(page, keyword)
    }

    private fun setSearchResultData(data: HomeArticleListBean) {
        requestSuccess()
        mBinding.includeSearchresult.baseFreshlayout.finishLoadMore()
        mBinding.includeSearchresult.baseFreshlayout.finishRefresh()
        if (page == 0) {
            mSearchResultAdapter.setNewInstance(data.datas)
            if (data.datas.size === 0) {
                requestEmptyData()
            }
        } else {
            mSearchResultAdapter.addData(data.datas)
        }
        if (data.datas.size === 0) {
            mBinding.includeSearchresult.baseFreshlayout.setEnableLoadMore(false)
        } else {
            page++
        }

    }


    private fun initListener() {
        mSearchResultAdapter.run {
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

            addChildClickViewIds(com.knight.kotlin.library_base.R.id.base_icon_collect, com.knight.kotlin.library_base.R.id.base_article_collect)
            setItemChildClickListener { adapter, view, position ->
                if (view.id == com.knight.kotlin.library_base.R.id.base_icon_collect || view.id == com.knight.kotlin.library_base.R.id.base_article_collect) {
                    selectItem = position
                    collectOrunCollect(
                        data[position].collect,
                        data[position].id
                    )
                }

            }
        }
    }

    private fun searchNewKeywords() {
        mBinding.includeSearchresult.baseFreshlayout.autoRefresh()
        page = 0
        keyword = mBinding.searchresultEt.getText().toString()
        DataBaseUtils.saveSearchKeyword(keyword)
        Appconfig.search_keyword = keyword
        mBinding.includeSearchresult.baseFreshlayout.setEnableLoadMore(true)
        mViewModel.searchArticleByKeyword(page, keyword)
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
     * 收藏成功
     */
    private fun collectSucess(data: Boolean) {
        mSearchResultAdapter.data[selectItem].collect = true
        mSearchResultAdapter.notifyItemChanged(selectItem)

    }

    /**
     *
     * 取消收藏
     */
    private fun unCollectSuccess(data: Boolean) {
        mSearchResultAdapter.data[selectItem].collect = false
        mSearchResultAdapter.notifyItemChanged(selectItem)

    }
}
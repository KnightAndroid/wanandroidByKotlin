package com.knight.kotlin.module_home.activity

import android.graphics.drawable.GradientDrawable
import android.os.Build
import android.text.TextUtils
import android.view.View
import android.view.inputmethod.EditorInfo
import androidx.recyclerview.widget.LinearLayoutManager
import com.core.library_base.route.RouteActivity
import com.core.library_common.dp2px
import com.flyjingfish.android_aop_core.annotations.SingleClick
import com.google.android.flexbox.FlexDirection
import com.google.android.flexbox.FlexboxLayoutManager
import com.google.android.flexbox.JustifyContent
import com.knight.kotlin.library_base.activity.BaseActivity
import com.core.library_common.config.Appconfig
import com.knight.kotlin.library_base.entity.SearchHotKeyEntity
import com.knight.kotlin.library_database.entity.SearchHistroyKeywordEntity
import com.knight.kotlin.library_database.repository.HistroyKeywordsRepository
import com.knight.kotlin.library_util.DataBaseUtils
import com.knight.kotlin.library_util.DialogUtils
import com.knight.kotlin.library_util.SystemUtils
import com.knight.kotlin.library_util.startPage
import com.knight.kotlin.library_util.startPageWithParams
import com.knight.kotlin.library_util.toast
import com.knight.kotlin.library_widget.ktx.init
import com.knight.kotlin.library_widget.ktx.setSafeOnItemChildClickListener
import com.knight.kotlin.library_widget.ktx.setSafeOnItemClickListener
import com.knight.kotlin.module_home.R
import com.knight.kotlin.module_home.adapter.HomeHotKeyAdapter
import com.knight.kotlin.module_home.adapter.SearchRecordAdapter
import com.knight.kotlin.module_home.databinding.HomeSearchActivityBinding
import com.knight.kotlin.module_home.vm.HomeSearchVm
import com.wyjson.router.annotation.Route
import dagger.hilt.android.AndroidEntryPoint

/**
 * Author:Knight
 * Time:2022/4/12 15:14
 * Description:HomeSearchActivity 搜索界面
 */
@AndroidEntryPoint
@Route(path = RouteActivity.Home.HomeSearchActivity)
class HomeSearchActivity : BaseActivity<HomeSearchActivityBinding, HomeSearchVm>(){

    //热词适配器
    private val mHomeHotKeyAdapter: HomeHotKeyAdapter by lazy { HomeHotKeyAdapter() }
    //搜索记录
    private val mSearchRecordAdapter:SearchRecordAdapter by lazy {SearchRecordAdapter()}

    private var keyword: String = ""
    override fun setThemeColor(isDarkMode: Boolean) {
        val cursorDrawable = GradientDrawable()
        cursorDrawable.shape = GradientDrawable.RECTANGLE
        cursorDrawable.setColor(themeColor)
        cursorDrawable.setSize(2.dp2px(), 2.dp2px())
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            mBinding.homeSearchEt.setTextCursorDrawable(cursorDrawable)
        } else {
            SystemUtils.setCursorDrawableColor(mBinding.homeSearchEt, themeColor)
        }
    }

    override fun HomeSearchActivityBinding.initView() {
        requestLoading(mBinding.homeSearchhotRv)
        val flexboxLayoutManager = FlexboxLayoutManager(this@HomeSearchActivity)
        //方向 主轴为水平方向,起点在左端
        flexboxLayoutManager.flexDirection = FlexDirection.ROW
        //左对齐
        flexboxLayoutManager.justifyContent = JustifyContent.FLEX_START
        homeSearchhotRv.init(flexboxLayoutManager,mHomeHotKeyAdapter,false)
        homeSearchhistroyKeywordRv.init(LinearLayoutManager(this@HomeSearchActivity),mSearchRecordAdapter,false)
        initClickListener()
        setOnClickListener(homeSearchkeywordClearallTv,homeSearchEt,homeTvSearchCancel)
        SystemUtils.showDelaySoftKeyBoard(homeSearchEt)
        homeSearchEt.setOnEditorActionListener { v, actionId, event ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                keyword = homeSearchEt.text.toString().trim()
                if (TextUtils.isEmpty(keyword)) {
                    toast(R.string.home_input_content_search)
                } else {
                    Appconfig.search_keyword = keyword
                    startPage(RouteActivity.Home.HomeSearchResultActivity)
                }
                true
            }
            false
        }

       SystemUtils.editTextChangeListener(homeSearchEt,homeTvSearchCancel)


    }

    override fun initObserver() {

    }

    override fun initRequestData() {
        mViewModel.getHotKey(failureCallBack = {
            it?.run {
                toast(it)
            }
            requestSeatchHotKeyFailure()
        }).observerKt {
            setSearchHotKey(it)
        }

    }

    override fun reLoadData() {
        mViewModel.getHotKey(failureCallBack = {
            it?.run {
                toast(it)
            }
            requestSeatchHotKeyFailure()
        }).observerKt {
            setSearchHotKey(it)
        }
    }

    override fun onResume() {
        super.onResume()
        mViewModel.getLocalSearchwords().observerKt {
            setLocalSearchwords(it)
        }
    }


    /**
     *
     * 设置热词
     */
    private fun setSearchHotKey(data:MutableList<SearchHotKeyEntity>) {
        requestSuccess()
        mHomeHotKeyAdapter.submitList(data)
    }


    private fun setLocalSearchwords(data:MutableList<SearchHistroyKeywordEntity>){
        if (data.size > 0) {
            mBinding.homeSearchkeywordHeadRl.visibility = View.VISIBLE
            mBinding.homeSearchhistroyKeywordRv.visibility = View.VISIBLE
            mSearchRecordAdapter.submitList(data)
        } else {
            mBinding.homeSearchkeywordHeadRl.visibility = View.GONE
            mBinding.homeSearchhistroyKeywordRv.visibility = View.GONE
        }
    }


    private fun initClickListener() {
          mSearchRecordAdapter.run {
            setSafeOnItemClickListener { adapter, view, position ->
              keyword = items[position].name ?: ""
              Appconfig.search_keyword = keyword
              startPageWithParams(RouteActivity.Home.HomeSearchResultActivity,"keyword" to keyword)
            }
              //子view点击事件

              setSafeOnItemChildClickListener(R.id.iv_searchkeyword_delete) { adapter, view, position ->
                  HistroyKeywordsRepository.getInstance()?.deleteHistroyKeyword(items[position].id)
                  mSearchRecordAdapter.removeAt(position)
                  mSearchRecordAdapter.notifyItemRemoved(position)
                  if (items.size == 0) {
                      mBinding.homeSearchkeywordHeadRl.visibility = View.GONE
                  }

              }
        }

        mHomeHotKeyAdapter.run {
            setSafeOnItemClickListener { adapter, view, position ->
                keyword = items[position].name
                Appconfig.search_keyword = keyword
                DataBaseUtils.saveSearchKeyword(keyword)
                startPageWithParams(RouteActivity.Home.HomeSearchResultActivity,"keyword" to keyword)
            }
        }
    }

    /**
     *
     * 请求失败
     */
    private fun requestSeatchHotKeyFailure(){
        requestFailure()
    }

    @SingleClick
    override fun onClick(v: View) {
        when (v) {
            mBinding.homeSearchkeywordClearallTv -> {
                DialogUtils.getConfirmDialog(
                    this@HomeSearchActivity,
                    getString(R.string.home_clearall_searchrecord),
                    { dialog, which ->
                        HistroyKeywordsRepository.getInstance()?.deleteAllKeywords()
                        mViewModel.getLocalSearchwords().observerKt {
                            setLocalSearchwords(it)
                        }
                    }) { dialog, which -> }
            }

            mBinding.homeTvSearchCancel -> {
                if (mBinding.homeTvSearchCancel.text.toString().equals(getString(R.string.home_cancel))) {
                    finish()
                } else {
                    keyword = mBinding.homeSearchEt.text.toString().trim()
                    Appconfig.search_keyword = keyword
                    DataBaseUtils.saveSearchKeyword(keyword)
                    startPageWithParams(RouteActivity.Home.HomeSearchResultActivity,"keyword" to keyword)

                }
            }
        }
    }
}
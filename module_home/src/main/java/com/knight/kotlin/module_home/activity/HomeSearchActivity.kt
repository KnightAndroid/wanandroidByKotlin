package com.knight.kotlin.module_home.activity

import android.graphics.drawable.GradientDrawable
import android.os.Build
import android.text.TextUtils
import android.view.View
import android.view.inputmethod.EditorInfo
import androidx.activity.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.alibaba.android.arouter.facade.annotation.Route
import com.google.android.flexbox.FlexDirection
import com.google.android.flexbox.FlexboxLayoutManager
import com.google.android.flexbox.JustifyContent
import com.knight.kotlin.library_aop.clickintercept.SingleClick
import com.knight.kotlin.library_base.activity.BaseActivity
import com.knight.kotlin.library_base.config.Appconfig
import com.knight.kotlin.library_base.entity.SearchHotKeyEntity
import com.knight.kotlin.library_base.ktx.observeLiveData
import com.knight.kotlin.library_base.ktx.observeLiveDataWithError
import com.knight.kotlin.library_base.route.RouteActivity
import com.knight.kotlin.library_base.util.dp2px
import com.knight.kotlin.library_database.entity.SearchHistroyKeywordEntity
import com.knight.kotlin.library_database.repository.HistroyKeywordsRepository
import com.knight.kotlin.library_util.DataBaseUtils
import com.knight.kotlin.library_util.DialogUtils
import com.knight.kotlin.library_util.SystemUtils
import com.knight.kotlin.library_util.startPage
import com.knight.kotlin.library_util.startPageWithParams
import com.knight.kotlin.library_util.toast
import com.knight.kotlin.library_widget.ktx.init
import com.knight.kotlin.library_widget.ktx.setItemChildClickListener
import com.knight.kotlin.library_widget.ktx.setItemClickListener
import com.knight.kotlin.module_home.R
import com.knight.kotlin.module_home.adapter.HomeHotKeyAdapter
import com.knight.kotlin.module_home.adapter.SearchRecordAdapter
import com.knight.kotlin.module_home.databinding.HomeSearchActivityBinding
import com.knight.kotlin.module_home.vm.HomeSearchVm
import dagger.hilt.android.AndroidEntryPoint

/**
 * Author:Knight
 * Time:2022/4/12 15:14
 * Description:HomeSearchActivity
 */
@AndroidEntryPoint
@Route(path = RouteActivity.Home.HomeSearchActivity)
class HomeSearchActivity : BaseActivity<HomeSearchActivityBinding,HomeSearchVm>(){

    override val mViewModel: HomeSearchVm by viewModels()
    //???????????????
    private val mHomeHotKeyAdapter: HomeHotKeyAdapter by lazy { HomeHotKeyAdapter(arrayListOf()) }
    //????????????
    private val mSearchRecordAdapter:SearchRecordAdapter by lazy {SearchRecordAdapter(arrayListOf())}

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
        //?????? ?????????????????????,???????????????
        flexboxLayoutManager.flexDirection = FlexDirection.ROW
        //?????????
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
        observeLiveDataWithError(mViewModel.searchHotKeyList,mViewModel.requestSearchHotKeyFlag,::setSearchHotKey,::requestSeatchHotKeyFailure)
        observeLiveData(mViewModel.localSearchwords,::setLocalSearchwords)
    }

    override fun initRequestData() {
        mViewModel.getHotKey()

    }

    override fun reLoadData() {
        mViewModel.getHotKey()
    }

    override fun onResume() {
        super.onResume()
        mViewModel.getLocalSearchwords()
    }


    /**
     *
     * ????????????
     */
    private fun setSearchHotKey(data:MutableList<SearchHotKeyEntity>) {
        requestSuccess()
        mHomeHotKeyAdapter.setNewInstance(data)
    }


    private fun setLocalSearchwords(data:MutableList<SearchHistroyKeywordEntity>){
        if (data.size > 0) {
            mBinding.homeSearchkeywordHeadRl.visibility = View.VISIBLE
            mBinding.homeSearchhistroyKeywordRv.visibility = View.VISIBLE
            mSearchRecordAdapter.setNewInstance(data)
        } else {
            mBinding.homeSearchkeywordHeadRl.visibility = View.GONE
            mBinding.homeSearchhistroyKeywordRv.visibility = View.GONE
        }
    }


    private fun initClickListener() {
          mSearchRecordAdapter.run {
            setItemClickListener { adapter, view, position ->
              keyword = data[position].name ?: ""
              Appconfig.search_keyword = keyword
              startPageWithParams(RouteActivity.Home.HomeSearchResultActivity,"keyword" to keyword)
            }
              //???view????????????
              addChildClickViewIds(
                  R.id.iv_searchkeyword_delete
              )
              setItemChildClickListener { adapter, view, position ->
                  HistroyKeywordsRepository.getInstance()?.deleteHistroyKeyword(data[position].id)
                  mSearchRecordAdapter.data.removeAt(position)
                  mSearchRecordAdapter.notifyItemRemoved(position)
                  if (data.size == 0) {
                      mBinding.homeSearchkeywordHeadRl.visibility = View.GONE
                  }

              }
        }

        mHomeHotKeyAdapter.run {
            setItemClickListener { adapter, view, position ->
                keyword = data[position].name
                Appconfig.search_keyword = keyword
                DataBaseUtils.saveSearchKeyword(keyword)
                startPageWithParams(RouteActivity.Home.HomeSearchResultActivity,"keyword" to keyword)
            }
        }
    }

    /**
     *
     * ????????????
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
                        mViewModel.getLocalSearchwords()
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
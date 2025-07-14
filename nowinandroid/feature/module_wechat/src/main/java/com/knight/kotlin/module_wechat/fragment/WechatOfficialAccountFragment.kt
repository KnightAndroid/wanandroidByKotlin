package com.knight.kotlin.module_wechat.fragment

import android.os.Bundle
import android.text.TextUtils
import androidx.recyclerview.widget.LinearLayoutManager
import com.knight.kotlin.library_base.fragment.BaseFragment
import com.core.library_base.ktx.setOnClick
import com.core.library_base.route.RouteFragment
import com.knight.kotlin.library_base.utils.ArouteUtils
import com.core.library_common.util.CacheUtils
import com.core.library_common.util.ColorUtils
import com.knight.kotlin.library_aop.loginintercept.LoginCheck

import com.knight.kotlin.library_widget.ktx.init
import com.knight.kotlin.library_widget.ktx.setSafeOnItemChildClickListener
import com.knight.kotlin.library_widget.ktx.setSafeOnItemClickListener
import com.knight.kotlin.module_wechat.adapter.WechatArticleAdapter
import com.knight.kotlin.module_wechat.databinding.WechatOfficialaccountFragmentBinding
import com.knight.kotlin.module_wechat.entity.WechatArticleListEntity
import com.knight.kotlin.module_wechat.vm.WechatVm
import com.scwang.smart.refresh.layout.api.RefreshLayout
import com.scwang.smart.refresh.layout.listener.OnLoadMoreListener
import com.scwang.smart.refresh.layout.listener.OnRefreshListener
import com.wyjson.router.annotation.Route
import dagger.hilt.android.AndroidEntryPoint

/**
 *
 * @ProjectName:    wanandroid
 * @Package:        com.knight.kotlin.module_wechat.fragment
 * @ClassName:      WechatArticleFragment
 * @Description:    java类作用描述
 * @Author:         knight
 * @CreateDate:     2022/3/18 5:15 下午
 * @UpdateUser:     更新者
 * @UpdateDate:     2022/3/18 5:15 下午
 * @UpdateRemark:   更新说明
 * @Version:        1.0
 */

@AndroidEntryPoint
@Route(path = RouteFragment.Wechat.WechatOfficialAccountFragment)
class WechatOfficialAccountFragment: BaseFragment<WechatOfficialaccountFragmentBinding, WechatVm>(),OnRefreshListener,OnLoadMoreListener {



    //页码
    private var page:Int = 1

    //点击哪个哪个列表
    private var selectItemPosition = 0

    //关键词
    private var keyWords:String = ""

    //微信文章列表适配器
    private val mWechatArticleAdapter:WechatArticleAdapter by lazy {WechatArticleAdapter()}
    private var cid:Int = 0
    companion object {

        fun newInstance(cid:Int):WechatOfficialAccountFragment{
            val wechatOfficialAccountFragment = WechatOfficialAccountFragment()
            val args = Bundle()
            args.putInt("cid",cid)
            wechatOfficialAccountFragment.arguments = args
            return wechatOfficialAccountFragment
        }

    }

    override fun setThemeColor(isDarkMode: Boolean) {

    }

    override fun WechatOfficialaccountFragmentBinding.initView() {
        cid = arguments?.getInt("cid") ?: 0
        includeWechatArticles.baseFreshlayout.setOnRefreshListener(this@WechatOfficialAccountFragment)
        includeWechatArticles.baseFreshlayout.setOnLoadMoreListener(this@WechatOfficialAccountFragment)
        mBinding.includeWechatArticles.baseBodyRv.init(LinearLayoutManager(activity),mWechatArticleAdapter,false)
        wechatFloatBtn.backgroundTintList = ColorUtils.createColorStateList(CacheUtils.getThemeColor(), CacheUtils.getThemeColor())
        wechatFloatBtn.setOnClick { includeWechatArticles.baseBodyRv.smoothScrollToPosition(0) }
        initListener()
    }

    override fun initObserver() {

    }

    override fun initRequestData() {
        requestLoading(mBinding.includeWechatArticles.baseFreshlayout)
        mViewModel.getWechatArticle(cid,page, failureCallBack = {
            setWechatArticleFailure()
        }).observerKt {
            setWechatArticle(it)
        }
    }

    fun initListener() {
        mWechatArticleAdapter.run {
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
                selectItemPosition = position
                collectOrunCollect(items[position].collect,items[position].id)
            }

        }

        //长按反馈
    }





    override fun onRefresh(refreshLayout: RefreshLayout) {
        page = 1
        keyWords = ""
        mBinding.includeWechatArticles.baseFreshlayout.setEnableLoadMore(true)
        mViewModel.getWechatArticle(cid,page, failureCallBack = {
            setWechatArticleFailure()
        }).observerKt {
            setWechatArticle(it)
        }

    }

    //设置数据
    private fun setWechatArticle(data: WechatArticleListEntity) {
        requestSuccess()
        mBinding.includeWechatArticles.baseFreshlayout.finishRefresh()
        mBinding.includeWechatArticles.baseFreshlayout.finishLoadMore()
        if (page == 1) {
            if (data.datas.size > 0) {
                mWechatArticleAdapter.submitList(data.datas)
            } else {
                requestEmptyData()
            }

        } else {
            mWechatArticleAdapter.addAll(data.datas)
        }
        if (data.datas.size < 10) {
            mBinding.includeWechatArticles.baseFreshlayout.setEnableLoadMore(false)
        } else {
            page ++
        }


    }


    /**
     *
     * 搜索
     */
    fun searchArticlesByKeyWords(keywords:String) {
        page = 1
        this.keyWords = keywords
        mViewModel.getWechatArticleBykeywords(cid,page,keywords).observerKt {
            setWechatArticle(it)
        }
        mBinding.includeWechatArticles.baseFreshlayout.setEnableLoadMore(true)
        mBinding.includeWechatArticles.baseFreshlayout.autoRefresh()
    }

    /**
     *
     * 获取微信文章列表失败
     */
    private fun setWechatArticleFailure() {
        requestFailure()
    }


    /**
     *
     * 收藏成功
     */
    private fun collectSuccess() {
        mWechatArticleAdapter.items[selectItemPosition].collect = true
        mWechatArticleAdapter.notifyItemChanged(selectItemPosition)
    }

    /**
     *
     * 取消收藏成功
     */
    private fun unCollectSuccess() {
        mWechatArticleAdapter.items[selectItemPosition].collect = false
        mWechatArticleAdapter.notifyItemChanged(selectItemPosition)
    }



    override fun onLoadMore(refreshLayout: RefreshLayout) {
        if (!TextUtils.isEmpty(keyWords)) {
            mViewModel.getWechatArticleBykeywords(cid,page,keyWords).observerKt {
                setWechatArticle(it)
            }
        } else {
            mViewModel.getWechatArticle(cid,page, failureCallBack = {
                setWechatArticleFailure()
            }).observerKt {
                setWechatArticle(it)
            }
        }
    }

    override fun reLoadData() {
        page = 1
        mViewModel.getWechatArticle(cid,page, failureCallBack = {
            setWechatArticleFailure()
        }).observerKt {
            setWechatArticle(it)
        }
    }


    @LoginCheck
    private fun collectOrunCollect(collect: Boolean, articleId: Int){
        if(collect) {
            mViewModel.unCollectArticle(articleId).observerKt {
                unCollectSuccess()
            }
        } else {
            mViewModel.collectArticle(articleId).observerKt {
                collectSuccess()
            }
        }
    }
}
package com.knight.kotlin.module_mine.activity

import android.graphics.drawable.GradientDrawable
import androidx.recyclerview.widget.LinearLayoutManager
import com.kingja.loadsir.core.LoadService
import com.knight.kotlin.library_aop.loginintercept.LoginCheck
import com.knight.kotlin.library_base.activity.BaseActivity
import com.knight.kotlin.library_base.event.MessageEvent
import com.knight.kotlin.library_base.ktx.init
import com.knight.kotlin.library_base.ktx.loadServiceInit
import com.knight.kotlin.library_base.ktx.setOnClick
import com.knight.kotlin.library_base.loadsir.EmptyCallBack
import com.knight.kotlin.library_base.loadsir.LoadCallBack
import com.knight.kotlin.library_base.route.RouteActivity
import com.knight.kotlin.library_base.util.ArouteUtils
import com.knight.kotlin.library_base.util.ColorUtils
import com.knight.kotlin.library_widget.ktx.setItemChildClickListener
import com.knight.kotlin.library_widget.ktx.setItemClickListener
import com.knight.kotlin.library_widget.slidinglayout.SlidingUpPanelLayout
import com.knight.kotlin.module_mine.R
import com.knight.kotlin.module_mine.adapter.OtherShareArticleAdapter
import com.knight.kotlin.module_mine.databinding.MineOthershareActivityBinding
import com.knight.kotlin.module_mine.entity.OtherShareArticleListEntity
import com.knight.kotlin.module_mine.vm.OtherShareArticleViewModel
import com.scwang.smart.refresh.layout.api.RefreshLayout
import com.scwang.smart.refresh.layout.listener.OnLoadMoreListener
import com.scwang.smart.refresh.layout.listener.OnRefreshListener
import com.wyjson.router.annotation.Param
import com.wyjson.router.annotation.Route
import dagger.hilt.android.AndroidEntryPoint
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode

/**
 * Author:Knight
 * Time:2022/5/7 16:18
 * Description:OtherShareArticleActivity
 */
@AndroidEntryPoint
@Route(path = RouteActivity.Mine.OtherShareArticleActivity)
class OtherShareArticleActivity :BaseActivity<MineOthershareActivityBinding,OtherShareArticleViewModel>(),OnRefreshListener,OnLoadMoreListener {



    @JvmField
    @Param(name = "uid")
    var uid = 0
    //选择收藏/取消收藏的Item项
    private var selectItem = -1


    private lateinit var mViewLoadService: LoadService<Any>

    private var page = 1

    private val mOtherShareArticleAdapter:OtherShareArticleAdapter by lazy{OtherShareArticleAdapter(
        mutableListOf())}

    override fun setThemeColor(isDarkMode: Boolean) {
        mBinding.mineTvUsername.setTextColor(themeColor)
        mBinding.mineOtherCoincount.setTextColor(themeColor)
        mBinding.mineOtherTvLevel.setTextColor(themeColor)
        mBinding.mineOtherTvLevel.setTextColor(themeColor)

    }

    override fun MineOthershareActivityBinding.initView() {
        inculeOthermessageToolbar.baseIvBack.setOnClick { finish() }
        includeOtherSharearticle.baseFreshlayout.setOnRefreshListener(this@OtherShareArticleActivity)
        includeOtherSharearticle.baseFreshlayout.setOnLoadMoreListener(this@OtherShareArticleActivity)
        includeOtherSharearticle.baseBodyRv.init(LinearLayoutManager(this@OtherShareArticleActivity),mOtherShareArticleAdapter,true)
        mineSlidupPanellayout.setFadeOnClickListener {
            mineSlidupPanellayout.panelState = SlidingUpPanelLayout.PanelState.COLLAPSED
        }
        mineIvOtherrefresh.setOnClick {
            includeOtherSharearticle.baseFreshlayout.autoRefresh()
        }
        requestLoading(mineSlidupPanellayout)
        inculeOthermessageToolbar.baseTvTitle.setText(getString(R.string.mine_other_shareArticles))
        mViewLoadService = loadServiceInit(includeOtherSharearticle.baseFreshlayout,{
            mViewModel.getOtherShareArticle(uid, page).observerKt {
                setOtherShareArticle(it)
            }
        })
        mViewLoadService.showCallback(LoadCallBack::class.java)
        initListener()
    }

    override fun initObserver() {
    }



    fun setOtherShareArticle(data: OtherShareArticleListEntity) {
        requestSuccess()
        mViewLoadService.showSuccess()
        val gradientDrawable = GradientDrawable()
        gradientDrawable.shape = GradientDrawable.OVAL
        gradientDrawable.setColor(ColorUtils.getRandColorCode())
        mBinding.mineOtherIvHead.setBackground(gradientDrawable)
        mBinding.mineOtherTvUserabbr.setText(data.coinInfo.username.substring(0, 1))
        //用户名
        mBinding.mineTvUsername.setText(data.coinInfo.nickname)
        //积分
        mBinding.mineOtherCoincount.setText(
            getString(R.string.mine_integral) + data.coinInfo.coinCount
        )
        mBinding.mineOtherTvLevel.setText(
            getString(R.string.mine_gradle) + data.coinInfo.level
        )
        mBinding.mineOtherTvRank.setText(
            getString(R.string.mine_rank) + data.coinInfo.rank
        )
        mBinding.includeOtherSharearticle.baseFreshlayout.finishRefresh()
        mBinding.includeOtherSharearticle.baseFreshlayout.finishLoadMore()
        if (data.shareArticles.datas.size > 0) {
            if (page == 1) {
                mOtherShareArticleAdapter.setNewInstance(data.shareArticles.datas)
            } else {
                mOtherShareArticleAdapter.addData(data.shareArticles.datas)
            }
            page++
        } else {
            mViewLoadService.showCallback(EmptyCallBack::class.java)
            mBinding.includeOtherSharearticle.baseFreshlayout.setEnableLoadMore(false)
        }

    }

    private fun initListener() {
        mOtherShareArticleAdapter.run {
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
            addChildClickViewIds(com.knight.kotlin.library_base.R.id.base_icon_collect)
            setItemChildClickListener { adapter, view, position ->
                when (view.id) {
                    com.knight.kotlin.library_base.R.id.base_icon_collect -> {
                        selectItem = position
                        collectOrunCollect(
                            data[position].collect,
                            data[position].id
                        )
                    }
                }
            }
        }


    }
    override fun initRequestData() {
        mViewModel.getOtherShareArticle(uid, page).observerKt {
            setOtherShareArticle(it)
        }
    }

    override fun reLoadData() {
        mViewModel.getOtherShareArticle(uid, page).observerKt {
            setOtherShareArticle(it)
        }
    }

    override fun onRefresh(refreshLayout: RefreshLayout) {
        page = 1
        mViewModel.getOtherShareArticle(uid, page).observerKt {
            setOtherShareArticle(it)
        }
        mBinding.includeOtherSharearticle.baseFreshlayout.setEnableLoadMore(true)
    }

    override fun onLoadMore(refreshLayout: RefreshLayout) {
        mViewModel.getOtherShareArticle(uid, page).observerKt {
            setOtherShareArticle(it)
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
        mOtherShareArticleAdapter.data[selectItem].collect = true
        mOtherShareArticleAdapter.notifyItemChanged(selectItem)

    }

    /**
     *
     * 取消收藏
     */
    private fun unCollectSuccess() {
        mOtherShareArticleAdapter.data[selectItem].collect = false
        mOtherShareArticleAdapter.notifyItemChanged(selectItem)

    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onMessageEvent(event: MessageEvent) {
        when (event.type) {
            //收藏成功 分享文章成功 登录成功,登出成功
            MessageEvent.MessageType.CollectSuccess ->{
                onRefresh(mBinding.includeOtherSharearticle.baseFreshlayout)
            }

            else -> {}
        }

    }


}
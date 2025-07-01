package com.knight.kotlin.module_square.fragment

import androidx.recyclerview.widget.LinearLayoutManager
import com.core.library_base.annotation.EventBusRegister
import com.core.library_base.event.MessageEvent
import com.core.library_base.fragment.BaseFragment
import com.core.library_base.route.RouteFragment
import com.core.library_base.util.ArouteUtils
import com.knight.kotlin.library_aop.loginintercept.LoginCheck
import com.knight.kotlin.library_widget.ktx.init
import com.knight.kotlin.library_widget.ktx.setSafeOnItemChildClickListener
import com.knight.kotlin.library_widget.ktx.setSafeOnItemClickListener
import com.knight.kotlin.module_square.R
import com.knight.kotlin.module_square.adapter.SquareShareArticleAdapter
import com.knight.kotlin.module_square.databinding.SquareListFragmentBinding
import com.knight.kotlin.module_square.entity.SquareShareArticleListBean
import com.knight.kotlin.module_square.vm.SquareListVm
import com.scwang.smart.refresh.layout.api.RefreshLayout
import com.scwang.smart.refresh.layout.listener.OnLoadMoreListener
import com.scwang.smart.refresh.layout.listener.OnRefreshListener
import com.wyjson.router.annotation.Route
import dagger.hilt.android.AndroidEntryPoint
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode

/**
 * Author:Knight
 * Time:2021/12/23 15:59
 * Description:SquareShareListFragment
 */
@EventBusRegister
@AndroidEntryPoint
@Route(path = RouteFragment.Square.SquareShareListFragment)
class SquareShareListFragment: BaseFragment<SquareListFragmentBinding, SquareListVm>(),OnLoadMoreListener,OnRefreshListener {
    //广场页码
    private var page = 0

    //点击第几项的点赞
    private var selectItem = -1

    //广场文章适配器
    private val mSquareShareArticleAdapter: SquareShareArticleAdapter by lazy { SquareShareArticleAdapter() }

    override fun SquareListFragmentBinding.initView() {
        requestLoading(squareSharearticleFreshlayout)
        squareArticleRv.init(LinearLayoutManager(requireActivity()),mSquareShareArticleAdapter,true)
        squareSharearticleFreshlayout.setOnRefreshListener(this@SquareShareListFragment)
        squareSharearticleFreshlayout.setOnLoadMoreListener(this@SquareShareListFragment)





        initAdapterClickListener()


    }

    override fun initObserver() {

    }

    override fun initRequestData() {
        mViewModel.getSquareArticles(page).observerKt {
            setSquareList(it)
        }
    }

    override fun setThemeColor(isDarkMode: Boolean) {

//        mBinding.squareFabUp.setBackgroundTintList(
//            ColorUtils.createColorStateList(CacheUtils.getThemeColor(), CacheUtils.getThemeColor())
//        )
    }

    override fun reLoadData() {
        mViewModel.getSquareArticles(page).observerKt {
            setSquareList(it)
        }
    }

    /**
     * 获取广场文章列表数据
     *
     */
    fun setSquareList(data: SquareShareArticleListBean){
        requestSuccess()
        mBinding.squareSharearticleFreshlayout.finishLoadMore()
        mBinding.squareSharearticleFreshlayout.finishRefresh()
        if (data.datas.size > 0) {
            if (page == 0) {
                mSquareShareArticleAdapter.submitList(data.datas)
            } else {
                mSquareShareArticleAdapter.addAll(data.datas)
            }
            page++
        } else {
            mBinding.squareSharearticleFreshlayout.setEnableLoadMore(false)
        }
    }


    /**
     *
     * 适配器设置监听事件
     */
    private fun initAdapterClickListener() {
        mSquareShareArticleAdapter.run {
            setSafeOnItemClickListener { adapter, view, position ->
                ArouteUtils.startWebArticle(items.get(position).link,items.get(position).title,
                    items.get(position).id,items.get(position).collect,items.get(position).envelopePic,
                    items.get(position).desc,items.get(position).chapterName,items[position].author,items[position].shareUser)
            }

            setSafeOnItemChildClickListener(R.id.square_icon_collect) { adapter, view, position ->
                selectItem = position
                collectOrunCollect(items[position].collect,items[position].id)
            }


        }



    }


    /**
     *
     * 收藏文章成功
     */
    private fun collectArticleSuccess() {
        mSquareShareArticleAdapter.items[selectItem].collect = true
        mSquareShareArticleAdapter.notifyItemChanged(selectItem)


    }

    /**
     *
     * 取消收藏文章成功
     */
    private fun unCollectArticleSuccess() {
        mSquareShareArticleAdapter.items[selectItem].collect = false
        mSquareShareArticleAdapter.notifyItemChanged(selectItem)

    }




    @LoginCheck
    private fun collectOrunCollect(collect: Boolean, articleId: Int) {
        if (collect) {
            mViewModel.unCollectArticle(articleId).observerKt {
                unCollectArticleSuccess()
            }
        } else {
            mViewModel.collectArticle(articleId).observerKt {
                collectArticleSuccess()
            }
        }
    }

    override fun onLoadMore(refreshLayout: RefreshLayout) {
        mViewModel.getSquareArticles(page).observerKt {
            setSquareList(it)
        }
    }

    override fun onRefresh(refreshLayout: RefreshLayout) {
        page = 0
        mBinding.squareSharearticleFreshlayout.setEnableLoadMore(true)
        mViewModel.getSquareArticles(page).observerKt {
            setSquareList(it)
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onMessageEvent(event: MessageEvent) {
        when (event.type) {
            //收藏成功 分享文章成功
            MessageEvent.MessageType.CollectSuccess, MessageEvent.MessageType.ShareArticleSuccess -> {
                onRefresh(mBinding.squareSharearticleFreshlayout)
            }
            //登录成功,登出成功
            MessageEvent.MessageType.LoginSuccess,MessageEvent.MessageType.LogoutSuccess -> {
                page = 0
                mViewModel.getSquareArticles(page).observerKt {
                    setSquareList(it)
                }
            }

            else -> {}
        }

    }
}
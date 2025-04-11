package com.knight.kotlin.module_home.activity

import android.view.LayoutInflater
import androidx.recyclerview.widget.LinearLayoutManager
import com.knight.kotlin.library_base.activity.BaseActivity
import com.knight.kotlin.library_base.ktx.getScreenWidth
import com.knight.kotlin.library_base.route.RouteActivity
import com.knight.kotlin.library_util.image.ImageLoader
import com.knight.kotlin.library_widget.ktx.init
import com.knight.kotlin.module_home.adapter.HomeNewsAdapter
import com.knight.kotlin.module_home.databinding.HomeNewsActivityBinding
import com.knight.kotlin.module_home.databinding.HomeNewsHeadBinding
import com.knight.kotlin.module_home.vm.HomeNewsVm
import com.scwang.smart.refresh.layout.api.RefreshLayout
import com.scwang.smart.refresh.layout.listener.OnRefreshListener
import com.wyjson.router.annotation.Route
import dagger.hilt.android.AndroidEntryPoint


/**
 * @author created by luguian
 * @organize
 * @Date 2025/4/11 15:18
 * @descript:新闻界面
 */
@AndroidEntryPoint
@Route(path = RouteActivity.Home.HomeNewsActivty)
class HomeNewsActivity:BaseActivity<HomeNewsActivityBinding,HomeNewsVm>(), OnRefreshListener {


    private lateinit var mNewsHeaderBinding: HomeNewsHeadBinding

    private val mNewsAdapter: HomeNewsAdapter by lazy { HomeNewsAdapter() }
    override fun setThemeColor(isDarkMode: Boolean) {

    }

    override fun initObserver() {

    }

    override fun initRequestData() {
        mViewModel.getNews().observerKt {

            mBinding.includeNews.baseFreshlayout.finishRefresh()
            mNewsAdapter.submitList(it.news)
            initHeaderView(it.head_image)
        }
    }

    override fun reLoadData() {

    }

    override fun HomeNewsActivityBinding.initView() {
        includeNews.baseBodyRv.init(
            LinearLayoutManager(this@HomeNewsActivity),
            mNewsAdapter,
            true
        )
        includeNews.baseFreshlayout.setOnRefreshListener(this@HomeNewsActivity)

    }

    override fun onRefresh(refreshLayout: RefreshLayout) {
        initRequestData()
    }


    /**
     * 初始化头像
     */
    private fun initHeaderView(imageUrl:String) {
        if (mBinding.includeNews.baseBodyRv.headerCount == 0) {
            if (!::mNewsHeaderBinding.isInitialized) {
                mNewsHeaderBinding =
                HomeNewsHeadBinding.inflate(LayoutInflater.from(this@HomeNewsActivity))
                ImageLoader.loadImageWithAdaptiveSize(mNewsHeaderBinding.homeNewsHeadIv, getScreenWidth(), 0,imageUrl,{
                        width,height->

//                    val params = mNewsHeaderBinding.root.layoutParams
//                    params.width = width
//                    params.height = height
//                    mNewsHeaderBinding.root.layoutParams = params
                    mBinding.includeNews.baseBodyRv.addHeaderView(mNewsHeaderBinding.root)
                    mBinding.includeNews.baseBodyRv.post {
                        mBinding.includeNews.baseBodyRv.scrollToPosition(0)
                    }
                })

            }
        } else {

        }
    }

}
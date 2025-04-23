package com.knight.kotlin.module_home.activity


import android.view.LayoutInflater
import androidx.recyclerview.widget.LinearLayoutManager
import com.knight.kotlin.library_base.activity.BaseActivity
import com.knight.kotlin.library_base.ktx.getScreenWidth
import com.knight.kotlin.library_base.ktx.setOnClick
import com.knight.kotlin.library_base.route.RouteActivity
import com.knight.kotlin.library_util.DateUtils
import com.knight.kotlin.library_util.image.ImageLoader
import com.knight.kotlin.library_widget.ktx.init
import com.knight.kotlin.module_home.adapter.HomeNewsAdapter
import com.knight.kotlin.module_home.databinding.HomeNewsActivityBinding
import com.knight.kotlin.module_home.databinding.HomeNewsFootBinding
import com.knight.kotlin.module_home.databinding.HomeNewsHeadBinding
import com.knight.kotlin.module_home.entity.ZaoBaoBean
import com.knight.kotlin.module_home.utils.FloatMenuManager
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
    private lateinit var mNewsFootBinding:HomeNewsFootBinding
    private val mNewsAdapter: HomeNewsAdapter by lazy { HomeNewsAdapter() }
    override fun setThemeColor(isDarkMode: Boolean) {

    }

    override fun initObserver() {

    }

    override fun initRequestData() {
        mViewModel.getNews().observerKt {
            mBinding.includeNews.baseFreshlayout.finishRefresh()
            requestSuccess()
            initHeaderView(it)
        }
    }

    override fun reLoadData() {

    }

    override fun HomeNewsActivityBinding.initView() {
        FloatMenuManager.showNormal(this@HomeNewsActivity)
        mBinding.title = getString(com.knight.kotlin.module_home.R.string.home_tv_zaobao)
        requestLoading(mBinding.includeNews.baseFreshlayout)
        includeNews.baseFreshlayout.setEnableLoadMore(false)
        includeNews.baseBodyRv.init(
            LinearLayoutManager(this@HomeNewsActivity),
            mNewsAdapter,
            true
        )
        includeNews.baseFreshlayout.setOnRefreshListener(this@HomeNewsActivity)
        includeNewsToolbar.baseIvBack.setOnClick {
            finish()
        }

    }

    override fun onRefresh(refreshLayout: RefreshLayout) {
        initRequestData()
    }


    /**
     * 初始化头像
     */
    private fun initHeaderView(zaoBao: ZaoBaoBean) {
        if (mBinding.includeNews.baseBodyRv.headerCount == 0 || mBinding.includeNews.baseBodyRv.footerCount == 0) {
            if (!::mNewsHeaderBinding.isInitialized) {
                mNewsHeaderBinding =
                HomeNewsHeadBinding.inflate(LayoutInflater.from(this@HomeNewsActivity))
                ImageLoader.loadImageWithAdaptiveSize(mNewsHeaderBinding.homeNewsHeadIv, getScreenWidth(), 0,zaoBao.head_image,{
                        width,height->
                    mNewsAdapter.submitList(zaoBao.news)
                    mBinding.includeNews.baseBodyRv.addHeaderView(mNewsHeaderBinding.root)

                })
                mNewsHeaderBinding.tvNewsDate.text = DateUtils.formatDate(zaoBao.date,"yyyy-MM-dd", "yyyy年M月d日")
            }

            if (!::mNewsFootBinding.isInitialized) {
                mNewsFootBinding =
                    HomeNewsFootBinding.inflate(LayoutInflater.from(this@HomeNewsActivity))
                mBinding.includeNews.baseBodyRv.addFooterView(mNewsFootBinding.root)
                mNewsFootBinding.weiyu = zaoBao.weiyu
            }
        } else {
            mNewsAdapter.submitList(zaoBao.news)
        }
    }


}
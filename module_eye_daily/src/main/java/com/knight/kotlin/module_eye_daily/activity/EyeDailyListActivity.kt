package com.knight.kotlin.module_eye_daily.activity

import android.view.LayoutInflater
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.knight.kotlin.library_base.activity.BaseActivity
import com.knight.kotlin.library_base.ktx.setOnClick
import com.knight.kotlin.library_base.route.RouteActivity
import com.knight.kotlin.library_util.image.ImageLoader
import com.knight.kotlin.library_widget.ktx.init
import com.knight.kotlin.module_eye_daily.R
import com.knight.kotlin.module_eye_daily.adapter.EyeDailyAdapter
import com.knight.kotlin.module_eye_daily.constants.EyeDailyConstants
import com.knight.kotlin.module_eye_daily.databinding.EyeDailyListActivityBinding
import com.knight.kotlin.module_eye_daily.databinding.EyeDailyListHeadBinding
import com.knight.kotlin.module_eye_daily.entity.EyeDailyItemEntity
import com.knight.kotlin.module_eye_daily.view.MyCustomBannerIndicator
import com.knight.kotlin.module_eye_daily.vm.EyeDailyListVm
import com.scwang.smart.refresh.layout.api.RefreshLayout
import com.scwang.smart.refresh.layout.listener.OnLoadMoreListener
import com.scwang.smart.refresh.layout.listener.OnRefreshListener
import com.wyjson.router.annotation.Route
import com.youth.banner.adapter.BannerImageAdapter
import com.youth.banner.config.IndicatorConfig
import com.youth.banner.holder.BannerImageHolder
import dagger.hilt.android.AndroidEntryPoint

/**
 * Author:Knight
 * Time:2024/4/28 15:54
 * Description:EyeDailyListActivity
 */


@AndroidEntryPoint
@Route(path = RouteActivity.EyeDaily.DailyListActivity)
class EyeDailyListActivity:BaseActivity<EyeDailyListActivityBinding,EyeDailyListVm>(),
    OnRefreshListener, OnLoadMoreListener {

    /**
     *
     * 加载下一页的Url
     */
    private var mNextPageUrl: String? = null

    /**
     *
     * 用来判断是否加载还是刷新
     */
    private var loadDailyList = false

    //头部广告View
    private val bannerHeadView: EyeDailyListHeadBinding by lazy {
        EyeDailyListHeadBinding.inflate(LayoutInflater.from(this),mBinding.root,false)

    }

    //日报适配器
    private val mEyeDailyAdapter: EyeDailyAdapter by lazy { EyeDailyAdapter(arrayListOf()) }

    override fun setThemeColor(isDarkMode: Boolean) {

    }

    override fun initObserver() {

    }

    override fun initRequestData() {
          mViewModel.getDailyBanner().observerKt{
              mNextPageUrl = it.nextPageUrl
              //去除标识为文本卡片的
              it.itemList.removeAll{
                  it.type == EyeDailyConstants.TEXT_HEAD_TYPE
              }
              setDailyBanner(it.itemList)
          }
    }

    override fun reLoadData() {

    }

    override fun EyeDailyListActivityBinding.initView() {
        includeEyeDailyToolbar.baseTvTitle.text = getString(R.string.eye_daily_toolbar_title)
        includeEyeDailyToolbar.baseIvBack.setOnClick { finish() }
        rvDailyList.init(
            LinearLayoutManager(this@EyeDailyListActivity),
            mEyeDailyAdapter,
            true
        )
        dailyListRefreshLayout.setOnLoadMoreListener(this@EyeDailyListActivity)
        dailyListRefreshLayout.setOnRefreshListener(this@EyeDailyListActivity)
    }


    /**
     * 设置广告数据
     * @param data
     */
    private fun setDailyBanner(data:MutableList<EyeDailyItemEntity>) {
        bannerHeadView.eyeDailyBanner.apply {
             setAdapter(object: BannerImageAdapter<EyeDailyItemEntity>(data) {
                 override fun onBindView(
                     holder: BannerImageHolder,
                     data: EyeDailyItemEntity,
                     position: Int,
                     size: Int
                 ) {
                     data.data.content.data.cover?.let {
                         ImageLoader.loadStringPhoto(
                             this@EyeDailyListActivity,
                             it.feed,
                             holder.imageView
                         )
                     }
                 }

             })

             indicator = MyCustomBannerIndicator(this@EyeDailyListActivity)
             indicatorConfig.margins = IndicatorConfig.Margins(0,0,0,0)
             indicator.indicatorView.setBackgroundColor(ContextCompat.getColor(this@EyeDailyListActivity,R.color.eye_daily_banner_indicator_bg))
            if (mBinding.rvDailyList.headerCount == 0) {
                mBinding.rvDailyList.addHeaderView(bannerHeadView.root)
            }
            setDailyList()
         }.addBannerLifecycleObserver(this)

    }

    /**
     *
     * 拉取日报列表数据
     *
     */
    private fun setDailyList() {
        mViewModel.getDailyList(mNextPageUrl).observerKt {
            mNextPageUrl = it.nextPageUrl
            mBinding.dailyListRefreshLayout.finishLoadMore()
            mBinding.dailyListRefreshLayout.finishRefresh()
            if (loadDailyList) {
                mEyeDailyAdapter.addData(it.itemList)
            } else {
                mEyeDailyAdapter.setNewInstance(it.itemList)
            }

        }
    }

    override fun onRefresh(refreshLayout: RefreshLayout) {
        loadDailyList = false
        initRequestData()
    }

    override fun onLoadMore(refreshLayout: RefreshLayout) {
        loadDailyList = true
        setDailyList()
    }
}
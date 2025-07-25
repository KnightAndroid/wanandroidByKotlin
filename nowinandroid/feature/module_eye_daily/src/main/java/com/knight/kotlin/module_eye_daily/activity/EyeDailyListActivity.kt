package com.knight.kotlin.module_eye_daily.activity

import android.view.LayoutInflater
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.core.library_base.ktx.setOnClick
import com.core.library_base.ktx.toJson
import com.core.library_base.route.RouteActivity
import com.knight.kotlin.library_base.activity.BaseActivity
import com.knight.kotlin.library_common.config.Appconfig
import com.knight.kotlin.library_base.config.EyeTypeConstants
import com.knight.kotlin.library_base.entity.EyeDailyItemEntity
import com.knight.kotlin.library_base.entity.EyeVideoDetailEntity
import com.knight.kotlin.library_util.startPageWithAnimate
import com.knight.kotlin.library_widget.ktx.init
import com.knight.kotlin.module_eye_daily.R
import com.knight.kotlin.module_eye_daily.adapter.EyeBannerAdapter
import com.knight.kotlin.module_eye_daily.adapter.EyeDailyAdapter
import com.knight.kotlin.module_eye_daily.databinding.EyeDailyBannerItemBinding
import com.knight.kotlin.module_eye_daily.databinding.EyeDailyListActivityBinding
import com.knight.kotlin.module_eye_daily.databinding.EyeDailyListHeadBinding
import com.knight.kotlin.module_eye_daily.view.MyCustomBannerIndicator
import com.knight.kotlin.module_eye_daily.vm.EyeDailyListVm
import com.scwang.smart.refresh.layout.api.RefreshLayout
import com.scwang.smart.refresh.layout.listener.OnLoadMoreListener
import com.scwang.smart.refresh.layout.listener.OnRefreshListener
import com.wyjson.router.annotation.Param
import com.wyjson.router.annotation.Route
import com.youth.banner.config.IndicatorConfig
import dagger.hilt.android.AndroidEntryPoint

/**
 * Author:Knight
 * Time:2024/4/28 15:54
 * Description:EyeDailyListActivity
 */


@AndroidEntryPoint
@Route(path = RouteActivity.EyeDaily.DailyListActivity)
class EyeDailyListActivity : BaseActivity<EyeDailyListActivityBinding, EyeDailyListVm>(),
    OnRefreshListener, OnLoadMoreListener {


    @JvmField
    @Param(name = "tabTitle")
    var tabTitle:String = ""
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
       // EyeDailyListHeadBinding.inflate(LayoutInflater.from(this), mBinding.root, false)


        EyeDailyListHeadBinding.inflate(LayoutInflater.from(this))
    }

    //日报适配器
    private val mEyeDailyAdapter: EyeDailyAdapter by lazy { EyeDailyAdapter(mutableListOf(),this) }

    override fun setThemeColor(isDarkMode: Boolean) {

    }

    override fun initObserver() {

    }

    override fun initRequestData() {
        mViewModel.getDailyBanner().observerKt {
            mNextPageUrl = it.nextPageUrl
            val (textCardList, followCardList) = it.itemList.partition {
                it.type == EyeTypeConstants.TEXT_HEAD_TYPE
            }
            //去除标识为文本卡片的
            setDailyBanner(textCardList, followCardList)
        }
    }

    override fun reLoadData() {

    }

    override fun EyeDailyListActivityBinding.initView() {
       // includeEyeDailyToolbar.baseTvTitle.text = getString(R.string.eye_daily_toolbar_title)
        includeEyeDailyToolbar.baseIvBack.setOnClick { finish() }
        rvDailyList.init(
            LinearLayoutManager(this@EyeDailyListActivity),
            mEyeDailyAdapter,
            true
        )
        mBinding.title = tabTitle
        dailyListRefreshLayout.setOnLoadMoreListener(this@EyeDailyListActivity)
        dailyListRefreshLayout.setOnRefreshListener(this@EyeDailyListActivity)
    }


    /**
     * 设置广告数据
     * @param data
     */
    private fun setDailyBanner(
        textCardList: List<EyeDailyItemEntity>,
        followCardList: List<EyeDailyItemEntity>
    ) {
        if (textCardList.size > 0) {
            bannerHeadView.tvEyeDailyBannerHead.text = textCardList[0].data.text
        } else {
            bannerHeadView.tvEyeDailyBannerHead.text = getString(R.string.eye_daily_today_recommend)
        }
        bannerHeadView.eyeDailyBanner.apply {
            setAdapter(object : EyeBannerAdapter(this@EyeDailyListActivity, followCardList) {
                override fun onBindView(
                    holder: BannerViewHolder,
                    data: EyeDailyItemEntity,
                    position: Int,
                    size: Int
                ) {

                    holder.binding.eyeDailyIvBanner.setOnClick {
                        val videoDetailData = EyeVideoDetailEntity(
                            data.data.content.data.id,
                            data.data.content.data.title,
                            data.data.content.data.playUrl,
                            data.data.content.data.category,
                            data.data.content.data.author?.latestReleaseTime ?: System.currentTimeMillis(),
                            data.data.content.data.description,
                            data.data.content.data.consumption.collectionCount,
                            data.data.content.data.consumption.replyCount,
                            data.data.content.data.consumption.shareCount,
                            data.data.content.data.author?.icon ?: "",
                            data.data.content.data.author?.name ?: "",
                            data.data.content.data.author?.description ?: "",
                            data.data.content.data.cover?.blurred ?: ""
                        )



                        startPageWithAnimate(
                            this@EyeDailyListActivity,
                            RouteActivity.EyeVideo.EyeVideoDetail, holder.binding.eyeDailyIvBanner,
                            getString(com.core.library_base.R.string.base_daily_share_image),
                            Appconfig.EYE_VIDEO_PARAM_KEY to toJson(videoDetailData)
                        )

                    }

                    val binding =
                        DataBindingUtil.getBinding<EyeDailyBannerItemBinding>(holder.itemView)
                    binding?.model = data.data?.content
                }

            })

            indicator = MyCustomBannerIndicator(this@EyeDailyListActivity)
            indicatorConfig.margins = IndicatorConfig.Margins(0, 0, 0, 0)
            indicator.indicatorView.setBackgroundColor(
                ContextCompat.getColor(
                    this@EyeDailyListActivity,
                    R.color.eye_daily_banner_indicator_bg
                )
            )
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
                mEyeDailyAdapter.addAll(it.itemList)
            } else {
                mEyeDailyAdapter.submitList(it.itemList)
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
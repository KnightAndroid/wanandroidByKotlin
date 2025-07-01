package com.knight.kotlin.module_eye_discover.activity

import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.chad.library.adapter4.QuickAdapterHelper
import com.chad.library.adapter4.loadState.LoadState
import com.chad.library.adapter4.loadState.trailing.TrailingLoadStateAdapter
import com.core.library_base.activity.BaseActivity
import com.core.library_base.config.Appconfig
import com.core.library_base.entity.EyeVideoDetailEntity
import com.core.library_base.ktx.init
import com.core.library_base.ktx.toJson
import com.core.library_base.route.RouteActivity
import com.google.android.material.appbar.AppBarLayout
import com.knight.kotlin.library_util.startPageWithAnimate
import com.knight.kotlin.library_widget.ktx.setSafeOnItemClickListener
import com.knight.kotlin.library_widget.ktx.transformShareElementConfig
import com.knight.kotlin.library_widget.listener.AppBarStateChangeListener
import com.knight.kotlin.module_eye_discover.adapter.EyeDiscoverCategoryDetailAdapter
import com.knight.kotlin.module_eye_discover.databinding.EyeDiscoverCategoryDetailActivityBinding
import com.knight.kotlin.module_eye_discover.vm.EyeDiscoverCategoryDetailVm
import com.wyjson.router.annotation.Param
import com.wyjson.router.annotation.Route
import dagger.hilt.android.AndroidEntryPoint


/**
 * @author created by luguian
 * @organize
 * @Date 2024/8/13 17:28
 * @descript:分类详细界面
 */
@AndroidEntryPoint
@Route(path = RouteActivity.EyeDiscover.EyeCategoryDetailActivity)
class EyeCategoryDetailActivity : BaseActivity<EyeDiscoverCategoryDetailActivityBinding, EyeDiscoverCategoryDetailVm>(){

    @JvmField
    @Param(name = "id")
    var id:Long = 0

    @JvmField
    @Param(name = "name")
    var name:String = ""


    @JvmField
    @Param(name = "headImage")
    var headImage:String = ""

    /**
     * 下一页数据
     */
    private var mNextPageUrl: String = ""

    private val mEyeDiscoverCategoryDetailAdapter: EyeDiscoverCategoryDetailAdapter by lazy { EyeDiscoverCategoryDetailAdapter() }


    private lateinit var helper : QuickAdapterHelper
    override fun setThemeColor(isDarkMode: Boolean) {

    }

    override fun initObserver() {

    }

    override fun initRequestData() {
            mViewModel.getDiscoverCategoryDetailData(id).observerKt {
                helper.trailingLoadState = LoadState.NotLoading(false)
                mNextPageUrl = it.nextPageUrl + "&udid=${Appconfig.EYE_UUID}&deviceModel=Android"
                mEyeDiscoverCategoryDetailAdapter.submitList(it.itemList)
            }
    }

    override fun reLoadData() {

    }

    override fun EyeDiscoverCategoryDetailActivityBinding.initView() {
        title = name
        headerImage = headImage
        discoverDetailMToolbar.setNavigationOnClickListener {
            finishAfterTransition()
        }
        transformShareElementConfig(discoverDetailImg,getString(com.knight.kotlin.module_eye_discover.R.string.eye_discover_share_element_container))
        setLoadDataListener()
        addItemClickListener()
        discoverDetailAppBar.addOnOffsetChangedListener(object : AppBarStateChangeListener() {
            override fun onStateChanged(appBarLayout: AppBarLayout?, state: State?) {
                if (state == State.EXPANDED) {
                    discoverDetailMToolbar.navigationIcon = ContextCompat.getDrawable(this@EyeCategoryDetailActivity, com.knight.kotlin.library_base.R.drawable.base_iv_white_left_arrow)

                } else if (state == State.COLLAPSED) {
                    discoverDetailMToolbar.navigationIcon = ContextCompat.getDrawable(this@EyeCategoryDetailActivity, com.knight.kotlin.library_base.R.drawable.base_iv_left_arrow)
                } else {
                    discoverDetailMToolbar.navigationIcon = ContextCompat.getDrawable(this@EyeCategoryDetailActivity, com.knight.kotlin.library_base.R.drawable.base_iv_white_left_arrow)
                }
            }
        })
    }

    /**
     * 设置加载数据监听
     */
    private fun setLoadDataListener() {
        helper = QuickAdapterHelper.Builder(mEyeDiscoverCategoryDetailAdapter)
            // 使用默认样式的尾部"加载更多"
            .setTrailingLoadStateAdapter(object : TrailingLoadStateAdapter.OnTrailingListener {
                override fun onLoad() {
                    if (mNextPageUrl.isNotEmpty()) {
                        // 执行加载更多的操作，通常都是网络请求
                        mViewModel.getLoadMoreCategoryDetailData(mNextPageUrl).observerKt {
                            mNextPageUrl = it.nextPageUrl + "&udid=${Appconfig.EYE_UUID}&deviceModel=Android"
                            mEyeDiscoverCategoryDetailAdapter.addAll(it.itemList)

                            if(it.count < 10) {
                                // 没有分页数据了
                                /*
                                Set the status to not loaded, and there is no paging data.
                                设置状态为未加载，并且没有分页数据了
                                */
                                helper.trailingLoadState = LoadState.NotLoading(true)
                            } else {
                                // 后续还有分页数据
                                /*
                                Set the state to not loaded, and there is also paginated data
                                设置状态为未加载，并且还有分页数据
                                */
                                helper.trailingLoadState = LoadState.NotLoading(false)
                            }
                        }
                    }

                }

                override fun onFailRetry() {
                    if (mNextPageUrl.isNotEmpty()) {
                        // 加载失败后，点击重试的操作，通常都是网络请求
                        mViewModel.getLoadMoreCategoryDetailData(mNextPageUrl).observerKt {
                            mNextPageUrl = it.nextPageUrl + "&udid=${Appconfig.EYE_UUID}&deviceModel=Android"
                            mEyeDiscoverCategoryDetailAdapter.addAll(it.itemList)
                        }
                    }

                }

                override fun isAllowLoading(): Boolean {
                    // 是否允许触发“加载更多”，通常情况下，下拉刷新的时候不允许进行加载更多
                    return true
                }
            }).build()
        /**
         * 第三步，给 RecyclerView 设置 Adapter，
         * 注意：这个adapter不是前面创建的 mAdapter，而是 helper 所提供的 adapter（ConcatAdapter）
         */
        mBinding.discoverCategoryDetailRv.init(
            LinearLayoutManager(this@EyeCategoryDetailActivity),
            helper.adapter,
            true
        )

    }

    /**
     *
     *  设置item点击事件
     */
    private fun addItemClickListener() {
        mEyeDiscoverCategoryDetailAdapter.run {
            setSafeOnItemClickListener {adapter, view, position ->

                val videoDetailData = EyeVideoDetailEntity(adapter.items[position].data.id,
                    adapter.items[position].data.title,
                    adapter.items[position].data.playUrl,
                    adapter.items[position].data.category,
                    adapter.items[position].data.author?.latestReleaseTime?:System.currentTimeMillis(),
                    adapter.items[position].data.description,
                    adapter.items[position].data.consumption.collectionCount ,
                    adapter.items[position].data.consumption.replyCount ,
                    adapter.items[position].data.consumption.shareCount,
                    adapter.items[position].data.author?.icon ?: "",
                    adapter.items[position].data.author?.name?: "",
                    adapter.items[position].data.author?.description?: "",
                    adapter.items[position].data.cover?.blurred ?: ""
                )


                startPageWithAnimate(
                    this@EyeCategoryDetailActivity,
                    RouteActivity.EyeVideo.EyeVideoDetail,view,
                    getString(com.knight.kotlin.library_base.R.string.base_daily_share_image),
                    Appconfig.EYE_VIDEO_PARAM_KEY to toJson(videoDetailData)
                )

            }
        }
    }
}
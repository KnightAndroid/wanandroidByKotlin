package com.knight.kotlin.module_video.activity
import android.view.View
import androidx.recyclerview.widget.GridLayoutManager
import com.core.library_base.ktx.setOnClick
import com.core.library_base.route.RouteActivity
import com.core.library_common.util.ColorUtils
import com.core.library_common.util.dp2px
import com.flyjingfish.android_aop_core.annotations.SingleClick
import com.knight.kotlin.library_base.activity.BaseActivity
import com.knight.kotlin.library_base.activity.BaseMviActivity
import com.knight.kotlin.library_base.config.EyeTypeConstants
import com.knight.kotlin.library_base.entity.EyeDailyItemEntity
import com.knight.kotlin.library_common.util.CacheUtils
import com.knight.kotlin.library_util.startPageWithParams
import com.knight.kotlin.library_util.toast
import com.knight.kotlin.library_widget.SpacesItemDecoration
import com.knight.kotlin.library_widget.ktx.init
import com.knight.kotlin.library_widget.ktx.setSafeOnItemClickListener
import com.knight.kotlin.module_video.DataConstant
import com.knight.kotlin.module_video.adapter.VideoMainAdapter
import com.knight.kotlin.module_video.contract.VideoMainContract
import com.knight.kotlin.module_video.databinding.VideoMainActivityBinding
import com.knight.kotlin.module_video.entity.VideoPlayEntity
import com.knight.kotlin.module_video.vm.VideoVm
import com.scwang.smart.refresh.layout.api.RefreshLayout
import com.scwang.smart.refresh.layout.listener.OnLoadMoreListener
import com.scwang.smart.refresh.layout.listener.OnRefreshListener
import com.wyjson.router.annotation.Route
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
@Route(path = RouteActivity.Video.VideoMainActivity)
class VideoMainActivity :
    BaseMviActivity<
            VideoMainActivityBinding,
            VideoVm,
            VideoMainContract.Event,
            VideoMainContract.State,
            VideoMainContract.Effect>(),
    OnRefreshListener,
    OnLoadMoreListener {

    private val mVideoMainAdapter: VideoMainAdapter by lazy { VideoMainAdapter() }

    override fun setThemeColor(isDarkMode: Boolean) {}


    /**
     * ================
     * 初始化 UI
     * ================
     */
    override fun VideoMainActivityBinding.initView() {
        mBinding.title = getString(com.knight.kotlin.module_video.R.string.video_main_toolbar)

        includeVideoToolbar.baseIvBack.setOnClick {
            finish()
        }

        requestLoading(includeVideo.baseFreshlayout)

        includeVideo.baseFreshlayout.setOnRefreshListener(this@VideoMainActivity)
        includeVideo.baseFreshlayout.setEnableLoadMore(false)

        includeVideo.baseBodyRv.init(
            GridLayoutManager(this@VideoMainActivity, 2),
            mVideoMainAdapter,
            true
        )

        includeVideo.baseBodyRv.addItemDecoration(
            SpacesItemDecoration(10.dp2px())
        )

        videoFloatBtn.backgroundTintList =
            ColorUtils.createColorStateList(
                CacheUtils.getThemeColor(),
                CacheUtils.getThemeColor()
            )

        videoFloatBtn.imageTintList = null
        setOnClickListener(videoFloatBtn)

        initListener()
    }

    override fun initObserver() {

    }

    private fun initListener() {
        mVideoMainAdapter.run {
            setSafeOnItemClickListener { _, _, position ->
                startPageWithParams(
                    RouteActivity.Video.VideoPlayListActivity,
                    "curPos" to position
                )
            }
        }
    }

    /**
     * ================
     * 首次加载
     * ================
     */
    override fun initRequestData() {
        mViewModel.setEvent(VideoMainContract.Event.GetVideos)
    }

    override fun reLoadData() {
        mViewModel.setEvent(VideoMainContract.Event.GetVideos)
    }

    /**
     * ================
     * 处理 State
     * ================
     */
    override fun renderState(state: VideoMainContract.State) {

        // loading
        if (state.loading) {
            requestLoading(mBinding.includeVideo.baseFreshlayout)
        }

        // 刷新结束
        if (!state.refreshing) {
            mBinding.includeVideo.baseFreshlayout.finishRefresh()
        }

        // 数据来了
        if (state.videos.isNotEmpty()) {
            requestSuccess()
            DataConstant.videoDatas.addAll(state.videos)
            mVideoMainAdapter.submitList(state.videos)
        }

        // 错误
        state.errorMsg?.let {
            requestFailure()
        }
    }

    /**
     * ================
     * 处理 Effect（一次性事件）
     * ================
     */
    override fun handleEffect(effect: VideoMainContract.Effect) {
        when (effect) {
            is VideoMainContract.Effect.ShowToast -> {
                toast(effect.msg)
            }
        }
    }

    /**
     * ================
     * 下拉刷新
     * ================
     */
    override fun onRefresh(refreshLayout: RefreshLayout) {
        mViewModel.setEvent(VideoMainContract.Event.RefreshVideos)
    }

    override fun onLoadMore(refreshLayout: RefreshLayout) {

    }

    /**
     * ================
     * 点击事件
     * ================
     */
    @SingleClick
    override fun onClick(v: View) {
        when (v) {
            mBinding.videoFloatBtn -> {
                mBinding.includeVideo.baseBodyRv.smoothScrollToPosition(0)
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        DataConstant.videoDatas.clear()
    }
}
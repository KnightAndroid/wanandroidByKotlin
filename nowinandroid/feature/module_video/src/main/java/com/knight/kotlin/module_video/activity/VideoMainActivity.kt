package com.knight.kotlin.module_video.activity
import android.view.View
import androidx.recyclerview.widget.GridLayoutManager
import com.core.library_base.ktx.setOnClick
import com.core.library_base.route.RouteActivity
import com.core.library_common.util.CacheUtils
import com.core.library_common.util.ColorUtils
import com.core.library_common.dp2px
import com.flyjingfish.android_aop_core.annotations.SingleClick
import com.knight.kotlin.library_base.activity.BaseActivity
import com.knight.kotlin.library_base.config.EyeTypeConstants
import com.knight.kotlin.library_base.entity.EyeDailyItemEntity
import com.knight.kotlin.library_util.startPageWithParams
import com.knight.kotlin.library_widget.SpacesItemDecoration
import com.knight.kotlin.library_widget.ktx.init
import com.knight.kotlin.library_widget.ktx.setSafeOnItemClickListener
import com.knight.kotlin.module_video.DataConstant
import com.knight.kotlin.module_video.adapter.VideoMainAdapter
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
class VideoMainActivity : BaseActivity<VideoMainActivityBinding, VideoVm>(), OnRefreshListener,
    OnLoadMoreListener {

    //视频列表适配器
    private val mVideoMainAdapter:VideoMainAdapter by lazy { VideoMainAdapter()}



    override fun setThemeColor(isDarkMode: Boolean) {

    }

    override fun initObserver() {

    }

    override fun initRequestData() {
        mViewModel.getVideos().observerKt {
            val (textCardList, followCardList) = it.itemList.partition {
                it.type == EyeTypeConstants.TEXT_HEAD_TYPE
            }
            getVideos(followCardList)
            //去除标识为文本卡片的

        }


    }

    override fun reLoadData() {

    }

    override fun VideoMainActivityBinding.initView() {
        mBinding.title = getString(com.knight.kotlin.module_video.R.string.video_main_toolbar)
        includeVideoToolbar.baseIvBack.setOnClick {
            finish()
        }
        requestLoading(includeVideo.baseFreshlayout)
        includeVideo.baseFreshlayout.setOnRefreshListener(this@VideoMainActivity)
        //includeVideo.baseFreshlayout.setOnLoadMoreListener(this@VideoMainActivity)
        includeVideo.baseFreshlayout.setEnableLoadMore(false)
        includeVideo.baseBodyRv.init(
            GridLayoutManager(this@VideoMainActivity,
            2,
        ),mVideoMainAdapter,true)
        includeVideo.baseBodyRv.addItemDecoration(SpacesItemDecoration(10.dp2px()))
        videoFloatBtn.backgroundTintList = ColorUtils.createColorStateList(CacheUtils.getThemeColor(), CacheUtils.getThemeColor())
        videoFloatBtn.imageTintList = null
        setOnClickListener(videoFloatBtn)
        initListener()
    }


    private fun getVideos(data: List<EyeDailyItemEntity>) {
        for (i in data.size - 1 downTo 0) {

                val videoPlayEntity = VideoPlayEntity(data[i].data.content.data.id,
                    data[i].data.content.data.author!!.id,
                    data[i].data.content.data.playUrl,
                    data[i].data.content.data.cover!!.feed,
                    "1080,1920",
                    data[i].data.content.data.author!!.name,
                    data[i].data.content.data.author!!.icon,
                    data[i].data.content.data.description,
                    data[i].data.content.data.consumption.collectionCount,
                    data[i].data.content.data.consumption.shareCount,
                    data[i].data.content.data.consumption.replyCount,
                    0,
                    false,
                    false,
                    false
                    )
                DataConstant.videoDatas.add(videoPlayEntity)

        }

            requestSuccess()
            mBinding.includeVideo.baseFreshlayout.finishLoadMore()
            mBinding.includeVideo.baseFreshlayout.finishRefresh()
            mVideoMainAdapter.submitList(DataConstant.videoDatas)



    }

    fun initListener() {
        mVideoMainAdapter.run {
            setSafeOnItemClickListener { adapter, view, position ->
                startPageWithParams(RouteActivity.Video.VideoPlayListActivity,
                    "curPos" to position
                )
            }

        }

        //长按反馈
    }

    override fun onRefresh(refreshLayout: RefreshLayout) {
        DataConstant.videoDatas.clear()
        mViewModel.getVideos().observerKt {
            val (textCardList, followCardList) = it.itemList.partition {
                it.type == EyeTypeConstants.TEXT_HEAD_TYPE
            }
            getVideos(followCardList)
            //去除标识为文本卡片的

        }
    }

    override fun onLoadMore(refreshLayout: RefreshLayout) {

    }

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
        //清空视频缓存
        DataConstant.videoDatas.clear()
    }

}




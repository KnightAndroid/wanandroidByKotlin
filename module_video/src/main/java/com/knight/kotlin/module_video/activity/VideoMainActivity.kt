package com.knight.kotlin.module_video.activity
import androidx.recyclerview.widget.GridLayoutManager
import com.knight.kotlin.library_base.activity.BaseActivity
import com.knight.kotlin.library_base.ktx.observeLiveData
import com.knight.kotlin.library_base.ktx.setOnClick
import com.knight.kotlin.library_base.route.RouteActivity
import com.knight.kotlin.library_base.util.CacheUtils
import com.knight.kotlin.library_base.util.ColorUtils
import com.knight.kotlin.library_base.util.dp2px
import com.knight.kotlin.library_util.startPage
import com.knight.kotlin.library_widget.SpacesItemDecoration
import com.knight.kotlin.library_widget.ktx.init
import com.knight.kotlin.library_widget.ktx.setItemClickListener
import com.knight.kotlin.module_video.DataConstant
import com.knight.kotlin.module_video.adapter.VideoMainAdapter
import com.knight.kotlin.module_video.databinding.VideoMainActivityBinding
import com.knight.kotlin.module_video.entity.VideoListEntity
import com.knight.kotlin.module_video.entity.VideoPlayEntity
import com.knight.kotlin.module_video.utils.VideoHelpUtils
import com.knight.kotlin.module_video.vm.VideoVm
import com.scwang.smart.refresh.layout.api.RefreshLayout
import com.scwang.smart.refresh.layout.listener.OnLoadMoreListener
import com.scwang.smart.refresh.layout.listener.OnRefreshListener
import com.wyjson.router.annotation.Route
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
@Route(path = RouteActivity.Video.VideoMainActivity)
class VideoMainActivity : BaseActivity<VideoMainActivityBinding,VideoVm>(), OnRefreshListener,
    OnLoadMoreListener {

    //视频列表适配器
    private val mVideoMainAdapter:VideoMainAdapter by lazy { VideoMainAdapter(mutableListOf())}



    override fun setThemeColor(isDarkMode: Boolean) {

    }

    override fun initObserver() {
        observeLiveData(mViewModel.videos, ::getDouyinVideos)
    }

    override fun initRequestData() {
        mViewModel.getDouyinVideos()
    }

    override fun reLoadData() {

    }

    override fun VideoMainActivityBinding.initView() {
        includeVideoToolbar.baseTvTitle.text = getString(com.knight.kotlin.module_video.R.string.video_main_toolbar)
        includeVideoToolbar.baseIvBack.setOnClick {
            finish()
        }
        requestLoading(includeVideo.baseFreshlayout)
        includeVideo.baseFreshlayout.setOnRefreshListener(this@VideoMainActivity)
        includeVideo.baseFreshlayout.setOnLoadMoreListener(this@VideoMainActivity)
        includeVideo.baseBodyRv.init(
            GridLayoutManager(this@VideoMainActivity,
            2,
        ),mVideoMainAdapter,true)
        includeVideo.baseBodyRv.addItemDecoration(SpacesItemDecoration(10.dp2px()))
        videoFloatBtn.backgroundTintList = ColorUtils.createColorStateList(CacheUtils.getThemeColor(), CacheUtils.getThemeColor())
        initListener()
    }


    private fun getDouyinVideos(data: MutableList<VideoListEntity>) {
        for (i in data.size - 1 downTo 0) {
            val decryptUrl = VideoHelpUtils.removePrefixToDecry(data[i].joke.videoUrl)
            if (!decryptUrl.contains("flag=null")) {
                val videoPlayEntity = VideoPlayEntity(data[i].user.userId,
                    data[i].joke.jokesId,
                    decryptUrl,
                    VideoHelpUtils.removePrefixToDecry(data[i].joke.thumbUrl),
                    data[i].joke.videoSize,
                    data[i].user.nickName,
                    data[i].user.avatar,
                    data[i].joke.content,
                    data[i].info.LikeNum,
                    data[i].info.shareNum,
                    data[i].info.commentNum,
                    data[i].info.disLikeNum,
                    data[i].info.isLike,
                    data[i].info.isUnlike,
                    data[i].info.isAttention,
                    )
                DataConstant.videoDatas.add(videoPlayEntity)
            }
        }
        if (DataConstant.videoDatas.size < 10) {
            //这里如果少于10个视频就继续调用接口补充视频数
            mViewModel.getDouyinVideos()
        } else {
            requestSuccess()
            mBinding.includeVideo.baseFreshlayout.finishLoadMore()
            mBinding.includeVideo.baseFreshlayout.finishRefresh()
            mVideoMainAdapter.setNewInstance(DataConstant.videoDatas)
        }


    }

    fun initListener() {
        mVideoMainAdapter.run {
            setItemClickListener { adapter, view, position ->
                startPage(RouteActivity.Video.VideoPlayListActivity)
            }

        }

        //长按反馈
    }

    override fun onRefresh(refreshLayout: RefreshLayout) {
        DataConstant.videoDatas.clear()
        mViewModel.getDouyinVideos()
    }

    override fun onLoadMore(refreshLayout: RefreshLayout) {
        mViewModel.getDouyinVideos()
    }


    override fun onDestroy() {
        super.onDestroy()
        //清空视频缓存
        DataConstant.videoDatas.clear()
    }

}



package com.knight.kotlin.module_video.fragment

import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RelativeLayout.LayoutParams
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.google.android.exoplayer2.ExoPlaybackException
import com.google.android.exoplayer2.Player
import com.knight.kotlin.library_base.fragment.BaseFragment
import com.knight.kotlin.library_base.vm.EmptyViewModel
import com.knight.kotlin.module_video.DataConstant
import com.knight.kotlin.module_video.R
import com.knight.kotlin.module_video.activity.VideoPlayListActivity
import com.knight.kotlin.module_video.adapter.VideoPlayAdapter
import com.knight.kotlin.module_video.databinding.VideoPlayFragmentBinding
import com.knight.kotlin.module_video.dialog.VideoCommentDialog
import com.knight.kotlin.module_video.player.VideoPlayer
import com.knight.kotlin.module_video.utils.OnVideoControllerListener
import com.knight.kotlin.module_video.view.ControllerView

/**
 *
 *
 * Author:Knight
 * Time:2024/3/5 14:41
 * Description:VideoPlayFragment
 */
class VideoPlayFragment : BaseFragment<VideoPlayFragmentBinding,EmptyViewModel>(){

    private var adapter:VideoPlayAdapter?=null
    /** 当前播放视频位置  */
    private var curPlayPos = -1
    private lateinit var videoView: VideoPlayer

    override fun setThemeColor(isDarkMode: Boolean) {

    }

    override fun initObserver() {

    }

    override fun initRequestData() {

    }

    override fun reLoadData() {

    }

    override fun VideoPlayFragmentBinding.initView() {
        initRecyclerView()
        initVideoPlayer()
        setViewPagerLayoutManager()
    }


    private fun initRecyclerView() {
        adapter  = VideoPlayAdapter(requireContext(), mBinding.videoRecyclerView.getChildAt(0) as RecyclerView)
        mBinding.videoRecyclerView.adapter = adapter
        adapter?.appendList(DataConstant.videoDatas)
    }

    private fun initVideoPlayer() {
        var params = LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT)
        videoView = VideoPlayer(requireActivity())
        videoView.layoutParams = params
        lifecycle.addObserver(videoView)
    }

    private fun setViewPagerLayoutManager() {
        with(mBinding.videoRecyclerView) {
            orientation = ViewPager2.ORIENTATION_VERTICAL
            offscreenPageLimit = 1
            registerOnPageChangeCallback(pageChangeCallback)
            (mBinding.videoRecyclerView.getChildAt(0) as RecyclerView).scrollToPosition(VideoPlayListActivity.initPos)
        }
    }

    private val pageChangeCallback = object: ViewPager2.OnPageChangeCallback() {
        override fun onPageSelected(position: Int) {
            playCurVideo(position)
        }
    }

    private fun playCurVideo(position: Int) {
        if (position == curPlayPos) {
            return
        }
        val itemView = adapter!!.getRootViewAt(position)
        val rootView = itemView!!.findViewById<ViewGroup>(R.id.rl_video_root)
      //  val likeView: LikeView = rootView.findViewById(R.id.likeview)
        val controllerView: ControllerView = rootView.findViewById(R.id.controller)
        val ivPlay = rootView.findViewById<ImageView>(R.id.iv_play)
        val ivCover = rootView.findViewById<ImageView>(R.id.iv_cover)

//        //播放暂停事件
//        likeView.setOnPlayPauseListener(object: LikeView.OnPlayPauseListener {
//            override fun onPlayOrPause() {
//                if (videoView!!.isPlaying()) {
//                    videoView?.pause()
//                    ivPlay.visibility = View.VISIBLE
//                } else {
//                    videoView?.play()
//                    ivPlay.visibility = View.GONE
//                }
//            }
//
//        })

        //评论点赞事件
        clickEvent(controllerView)

        //切换播放视频的作者主页数据
       // RxBus.getDefault().post(CurUserBean(DataCreate.datas[position]?.userBean!!))
        curPlayPos = position

        //切换播放器位置
        dettachParentView(rootView)
        autoPlayVideo(curPlayPos, ivCover)
    }

    /**
     * 移除videoview父view
     */
    private fun dettachParentView(rootView: ViewGroup) {
        //1.添加videoView到当前需要播放的item中,添加进item之前，保证videoView没有父view
        videoView?.parent?.let {
            (it as ViewGroup).removeView(videoView)
        }

        rootView.addView(videoView, 0)
    }

    /**
     * 自动播放视频
     */
    private fun autoPlayVideo(position: Int, ivCover: ImageView) {
        videoView.playVideo(adapter!!.getDatas()[position].mediaSource!!)

        videoView.getplayer()?.addListener(object: Player.Listener {
            override fun onPlaybackStateChanged(state: Int) {
                // 播放状态发生变化时的回调
                // 播放状态包括：Player.STATE_IDLE、Player.STATE_BUFFERING、Player.STATE_READY、Player.STATE_ENDED
                if (state == Player.STATE_READY) {

                }
            }

            fun onPlayerError(error: ExoPlaybackException?) {
                // 播放发生错误时的回调
            }

            override fun onIsPlayingChanged(isPlaying: Boolean) {
                // 播放状态变为播放或暂停时的回调
            }

            override fun onRenderedFirstFrame() {
                //第一帧已渲染，隐藏封面
                ivCover.visibility = View.GONE
            }
        })
    }


    /**
     * 用户操作事件
     */
    private fun clickEvent(controllerView: ControllerView) {
        controllerView.setListener(object : OnVideoControllerListener {
            override fun onHeadClick() {

            }

            override fun onLikeClick() {}
            override fun onCommentClick(jokeId:Long) {
                val commentDialog = VideoCommentDialog(jokeId)
                commentDialog.show(childFragmentManager, "")
            }

            override fun onShareClick() {

            }
        })
    }

}
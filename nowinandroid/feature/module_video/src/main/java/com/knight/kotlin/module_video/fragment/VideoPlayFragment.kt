package com.knight.kotlin.module_video.fragment

import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver
import android.widget.ImageView
import android.widget.RelativeLayout.LayoutParams
import androidx.annotation.OptIn
import androidx.media3.common.Player
import androidx.media3.common.util.UnstableApi
import androidx.media3.exoplayer.ExoPlaybackException
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.core.library_base.contact.EmptyContract
import com.core.library_base.vm.EmptyMviViewModel
import com.core.library_base.vm.EmptyViewModel
import com.knight.kotlin.library_base.fragment.BaseFragment
import com.knight.kotlin.library_base.fragment.BaseMviFragment
import com.knight.kotlin.module_video.DataConstant
import com.knight.kotlin.module_video.R
import com.knight.kotlin.module_video.adapter.VideoPlayAdapter
import com.knight.kotlin.module_video.databinding.VideoPlayFragmentBinding
import com.knight.kotlin.module_video.dialog.VideoCommentDialog
import com.knight.kotlin.module_video.entity.VideoPlayEntity
import com.knight.kotlin.module_video.player.VideoPlayer
import com.knight.kotlin.module_video.utils.OnVideoControllerListener
import com.knight.kotlin.module_video.utils.precache.PreloadManager
import com.knight.kotlin.module_video.view.ControllerView
import com.knight.kotlin.module_video.view.LikeView


/**
 *
 *
 * Author:Knight
 * Time:2024/3/5 14:41
 * Description:VideoPlayFragment
 */
@UnstableApi
class VideoPlayFragment(curPlayPos: Int) :
    BaseMviFragment<VideoPlayFragmentBinding,
            EmptyMviViewModel,
            EmptyContract.Event,
            EmptyContract.State,
            EmptyContract.Effect>() {

    private var adapter: VideoPlayAdapter? = null
    private var curPlayPos = curPlayPos
    private lateinit var videoView: VideoPlayer

    override fun setThemeColor(isDarkMode: Boolean) {}

    override fun renderState(state: EmptyContract.State) {}

    override fun handleEffect(effect: EmptyContract.Effect) {}

    override fun initObserver() {}

    override fun initRequestData() {}

    override fun reLoadData() {}

    override fun VideoPlayFragmentBinding.initView() {
        initRecyclerView()
        initVideoPlayer()
        setupViewPager()
    }


    private fun initRecyclerView() {
        adapter = VideoPlayAdapter(
            requireContext(),
            mBinding.videoRecyclerView.getChildAt(0) as RecyclerView
        )
        mBinding.videoRecyclerView.adapter = adapter
        adapter?.appendList(DataConstant.videoDatas)
    }

    private fun initVideoPlayer() {
        val params = LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT)
        videoView = VideoPlayer(requireActivity())
        videoView.layoutParams = params
        lifecycle.addObserver(videoView)
    }

    private fun setupViewPager() = with(mBinding.videoRecyclerView) {
        orientation = ViewPager2.ORIENTATION_VERTICAL
        offscreenPageLimit = 1
        registerOnPageChangeCallback(pageChangeCallback)
        setCurrentItem(curPlayPos, false)
//        post {
//            // 首屏播放
//            bindPositionWhenReady(curPlayPos)
//        }
    }


    /**
     * ========= 关键回调 =========
     * 不能在 onPageSelected 播放
     * 必须等页面稳定 + View attach 完成
     */
    private val pageChangeCallback = object : ViewPager2.OnPageChangeCallback() {

        override fun onPageSelected(position: Int)  {
        //    super.onPageScrollStateChanged(state)

           // if (state == ViewPager2.SCROLL_STATE_IDLE) {
             //   val pos = mBinding.videoRecyclerView.currentItem
                bindPositionWhenReady(position)
         //   }
        }
    }


    /**
     * 绑定真正可播放的 View
     */
    private fun bindPositionWhenReady(position: Int) {
        val itemView = adapter!!.getRootViewAt(position)
        itemView?.let {
            playCurVideo(it, position)
        } ?: run {
            (mBinding.videoRecyclerView.getChildAt(0) as RecyclerView).viewTreeObserver.addOnGlobalLayoutListener(
                object : ViewTreeObserver.OnGlobalLayoutListener {
                    override fun onGlobalLayout() {
                        val itemView = adapter?.getRootViewAt(position)
                        itemView?.let {
                            playCurVideo(it, position)
                            (mBinding.videoRecyclerView.getChildAt(0) as RecyclerView).getViewTreeObserver()
                                .removeOnGlobalLayoutListener(this)
                        }

                    }
                })
        }
    }



    /**
     * 播放当前 Item
     */
    private fun playCurVideo(itemView: View, position: Int) {

        val rootView = itemView.findViewById<ViewGroup>(R.id.rl_video_root)
        val likeView: LikeView = rootView.findViewById(R.id.video_likeview)
        val controllerView: ControllerView = rootView.findViewById(R.id.controller)
        val ivPlay = rootView.findViewById<ImageView>(R.id.iv_play)
        val ivCover = rootView.findViewById<ImageView>(R.id.iv_cover)

        likeView.setOnPlayPauseListener(object : LikeView.OnPlayPauseListener {
            override fun onPlayOrPause() {
                if (videoView.isPlaying()) {
                    videoView.pause()
                    ivPlay.visibility = View.VISIBLE
                } else {
                    videoView.play()
                    ivPlay.visibility = View.GONE
                }
            }
        })

        clickEvent(controllerView)

        curPlayPos = position

        attachPlayerToRoot(rootView)

        autoPlayVideo(position, ivCover)
    }


    /**
     * 重新绑定 videoView
     */
    private fun attachPlayerToRoot(rootView: ViewGroup) {
        videoView.parent?.let {
            (it as ViewGroup).removeView(videoView)
        }
        rootView.addView(videoView, 0)
    }


    /**
     * 自动播放
     */
    @OptIn(UnstableApi::class)
    private fun autoPlayVideo(position: Int, ivCover: ImageView) {

        val url = PreloadManager
            .getInstance(requireContext())
            .getPlayUrl(adapter!!.getDatas()[position].videoUrl)

        url?.let { videoView.playVideo(it) }

        videoView.getplayer()?.addListener(object : Player.Listener {

            override fun onRenderedFirstFrame() {
                ivCover.visibility = View.GONE
            }

            override fun onPlaybackStateChanged(state: Int) {
                if (state == Player.STATE_READY) {}
            }

            fun onPlayerError(error: ExoPlaybackException?) {}
        })
    }


    /**
     * 点击事件
     */
    private fun clickEvent(controllerView: ControllerView) {
        controllerView.setListener(object : OnVideoControllerListener {
            override fun onHeadClick() {}

            override fun onLikeClick() {}

            override fun onCommentClick(videoData: VideoPlayEntity) {
                val dialog = VideoCommentDialog(videoData, videoView)
                dialog.show(childFragmentManager, "")
            }

            override fun onShareClick() {}
        })
    }
}


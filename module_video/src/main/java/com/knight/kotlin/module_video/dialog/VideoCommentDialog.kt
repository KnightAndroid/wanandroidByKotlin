package com.knight.kotlin.module_video.dialog

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.knight.kotlin.library_base.ktx.init
import com.knight.kotlin.library_base.ktx.screenHeight
import com.knight.kotlin.library_base.ktx.screenWidth
import com.knight.kotlin.library_util.toast
import com.knight.kotlin.library_widget.BaseBottomSheetDialog
import com.knight.kotlin.module_video.adapter.VideoCommentAdapter
import com.knight.kotlin.module_video.databinding.VideoDialogCommentBinding
import com.knight.kotlin.module_video.player.VideoPlayer
import com.knight.kotlin.module_video.vm.VideoVm
import dagger.hilt.android.AndroidEntryPoint


/**
 * Author:Knight
 * Time:2024/3/19 15:47
 * Description:VideoCommentDialog
 */
@AndroidEntryPoint
class VideoCommentDialog(val jokeId:Long,val videoView: VideoPlayer) :  BaseBottomSheetDialog() {

    private val videoVm by lazy{ ViewModelProvider(this)[VideoVm::class.java] }

    //视频列表适配器
    private val mVideoCommentAdapter: VideoCommentAdapter by lazy { VideoCommentAdapter(mutableListOf()) }
    private lateinit var binding: VideoDialogCommentBinding
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = VideoDialogCommentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init()
    }

    private fun init() {
        setBehavior()
        binding.recyclerView.init(LinearLayoutManager(activity),mVideoCommentAdapter,false)
        loadData()

    }

    private fun setBehavior() {
        setBehaviorChanged(object : IBehaviorChanged {
            override fun changedState(bottomSheet: View?, state: Int) {
                val width: Float = activity?.screenWidth?.toFloat() ?: 0f
                val height: Float = activity?.screenHeight?.toFloat() ?: 0f
                if (state == BottomSheetBehavior.STATE_EXPANDED) {
                    val x = width / 2f
                    //我在这里默认给了dialog的高度是500dp，也就是用屏幕高度-dialog高度就是视频的最小高度
                    //得到高度之后还要继续计算高度占比来进行等比例缩放：
                    //Dialog高度占比 = Dialog/屏幕高度
                    //VideoView 高度占比 = (1280-Dialog高度)/1280≈0.33
                    // 所以0.33这个系数就是setScaleX()和setScaleY()的缩放比
                    binding.videoLlParent.post {
                        val scale: Float = height - binding.videoLlParent.height
                        val scaleX = ObjectAnimator.ofFloat(videoView, "scaleX", 1.0f, scale / height)
                        val scaleY = ObjectAnimator.ofFloat(videoView, "scaleY", 1.0f, scale / height)
                        val set = AnimatorSet()
                        videoView.setPivotX(x)
                        videoView.setPivotY(0f)
                        set.play(scaleX).with(scaleY)
                        set.duration = 250
                        set.start()
                    }
                } else if (state == BottomSheetBehavior.STATE_COLLAPSED) {
                    videoView.setScaleX(1.0f)
                    videoView.setScaleY(1.0f)
                    videoView.setPivotX(0f)
                    videoView.setPivotY(0f)
                }
            }

            override fun changedOffset(bottomSheet: View, slideOffset: Float) {
                startAnimator(bottomSheet)
            }
        })
    }

    private fun loadData() {
        videoVm.getVideoCommentList(jokeId,1,successCallBack ={
            binding.tvTitle.setText(it.comments.size.toString().plus("条评论"))
            mVideoCommentAdapter.setNewInstance(it.comments)


        },failureCallBack = {
            toast(it ?: getString(com.knight.kotlin.library_base.R.string.base_request_failure))
        })
    }

    protected override val height: Int
        protected get() = resources.displayMetrics.heightPixels - 600


    /**
     * @param parent
     */
    private fun startAnimator(parent: View) {
        val width: Float = activity?.screenWidth?.toFloat() ?: 0f
        val height: Float = activity?.screenHeight?.toFloat() ?: 0f
        val x = width / 2f
        val py = parent.y / height
        videoView.setScaleX(py)
        videoView.setScaleY(py)
        videoView.setPivotX(x)
        videoView.setPivotY(0f)
    }

}
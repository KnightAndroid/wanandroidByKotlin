package com.knight.kotlin.module_video.view

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.view.animation.Animation
import android.view.animation.LinearInterpolator
import android.view.animation.RotateAnimation
import android.widget.RelativeLayout
import com.knight.kotlin.library_util.image.ImageLoader
import com.knight.kotlin.module_video.R
import com.knight.kotlin.module_video.databinding.VideoViewControllerBinding
import com.knight.kotlin.module_video.entity.VideoPlayEntity
import com.knight.kotlin.module_video.utils.LinkHerfUtils
import com.knight.kotlin.module_video.utils.NumberUtils
import com.knight.kotlin.module_video.utils.OnVideoControllerListener

/**
 * Author:Knight
 * Time:2024/3/18 16:56
 * Description:ControllerView
 */
class ControllerView @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null) : RelativeLayout(context, attrs), View.OnClickListener {
    private var listener: OnVideoControllerListener? = null
    private lateinit var videoData: VideoPlayEntity
    private var binding: VideoViewControllerBinding = VideoViewControllerBinding.inflate(LayoutInflater.from(context), this, true)

    init {
        init()
    }

    private fun init() {
        binding.ivHead.setOnClickListener(this)
        binding.ivComment.setOnClickListener(this)
        binding.ivShare.setOnClickListener(this)
        binding.rlLike.setOnClickListener(this)
        binding.ivFocus.setOnClickListener(this)
        setRotateAnim()
    }

    fun setVideoData(videoData: VideoPlayEntity) {
        this.videoData = videoData
        ImageLoader.loadStringPhoto(context,videoData.avatar,binding.ivHead)
        binding.tvNickname.text = "@" + videoData.nickName
        LinkHerfUtils.setContent(videoData.comment, binding.autoLinkTextView)

        ImageLoader.loadStringPhoto(context,videoData.avatar,binding.ivHeadAnim)
        binding.tvLikecount.text = NumberUtils.numberFilter(videoData.LikeNum)
        binding.tvCommentcount.text = NumberUtils.numberFilter(videoData.commentNum)
        binding.tvSharecount.text = NumberUtils.numberFilter(videoData.shareNum)
        binding.animationView.setAnimation("like.json")

        //点赞状态
        if (videoData.isLike) {
            binding.ivLike.setTextColor(resources.getColor(R.color.video_color_like))
        } else {
            binding.ivLike.setTextColor(resources.getColor(android.R.color.white))
        }

        //关注状态
        if (videoData.isAttention) {
            binding.ivFocus.visibility = GONE
        } else {
            binding.ivFocus.visibility = VISIBLE
        }
    }

    fun setListener(listener: OnVideoControllerListener?) {
        this.listener = listener
    }

    override fun onClick(v: View) {
        if (listener == null) {
            return
        }
        when (v.id) {
            R.id.ivHead -> listener?.onHeadClick()
            R.id.rlLike -> {
                listener?.onLikeClick()
                like()
            }
            R.id.ivComment -> listener?.onCommentClick(videoData)
            R.id.ivShare -> listener?.onShareClick()
            R.id.ivFocus -> if (!videoData.isAttention) {
                videoData.isLike = true
                binding.ivFocus.visibility = GONE
            }
        }
    }

    /**
     * 点赞动作
     */
    fun like() {
        if (!videoData.isLike) {
            //点赞
            binding.animationView.visibility = VISIBLE
            binding.animationView.playAnimation()
            binding.ivLike.setTextColor(resources.getColor(R.color.video_color_like))
        } else {
            //取消点赞
            binding.animationView.visibility = INVISIBLE
            binding.ivLike.setTextColor(resources.getColor(android.R.color.white))
        }
        videoData.isLike = !videoData.isLike
    }

    /**
     * 循环旋转动画
     */
    private fun setRotateAnim() {
        val rotateAnimation = RotateAnimation(0f, 359f,
            Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f)
        rotateAnimation.repeatCount = Animation.INFINITE
        rotateAnimation.duration = 8000
        rotateAnimation.interpolator = LinearInterpolator()
        binding.rlRecord.startAnimation(rotateAnimation)
    }
}
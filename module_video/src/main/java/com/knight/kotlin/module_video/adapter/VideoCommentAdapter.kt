package com.knight.kotlin.module_video.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.chad.library.adapter4.BaseQuickAdapter
import com.knight.kotlin.library_util.DateUtils
import com.knight.kotlin.library_util.TimeAgoUtils
import com.knight.kotlin.library_util.image.ImageLoader
import com.knight.kotlin.module_video.databinding.VideoCommentItemBinding
import com.knight.kotlin.module_video.entity.VideoCommentData
import com.knight.kotlin.module_video.entity.VideoCommentItem

/**
 * Author:Knight
 * Time:2024/3/19 15:28
 * Description:VideoCommentAdapter
 */
class VideoCommentAdapter :
    BaseQuickAdapter<VideoCommentItem, VideoCommentAdapter.VH>() {

    class VH(
        parent: ViewGroup,
        val binding: VideoCommentItemBinding = VideoCommentItemBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        ),
    ) : RecyclerView.ViewHolder(binding.root)


    override fun onBindViewHolder(holder: VH, position: Int, item: VideoCommentItem?) {
        item?.run {
            //本评论的用户昵称
            holder.binding.tvNickname.setText(data.user.nickname)
            //本评论的用户头像
            ImageLoader.loadStringPhoto(context,data.user.avatar,holder.binding.ivHead)
            //评论内容
            holder.binding.tvContent.setText(data.message)
            //点赞数
            holder.binding.tvLikecount.setText(data.likeCount.toString())
            //这里要补:00
            holder.binding.tvCommentTime.setText(TimeAgoUtils.format(DateUtils.formatDateMsByYMDHMS(data.createTime)))
        }
    }

    override fun onCreateViewHolder(context: Context, parent: ViewGroup, viewType: Int): VH {
        return VH(parent)
    }

}
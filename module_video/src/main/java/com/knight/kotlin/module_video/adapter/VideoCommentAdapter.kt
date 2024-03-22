package com.knight.kotlin.module_video.adapter

import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.knight.kotlin.library_util.TimeAgoUtils
import com.knight.kotlin.library_util.image.ImageLoader
import com.knight.kotlin.module_video.entity.VideoComment

/**
 * Author:Knight
 * Time:2024/3/19 15:28
 * Description:VideoCommentAdapter
 */
class VideoCommentAdapter(data : MutableList<VideoComment>) :
    BaseQuickAdapter<VideoComment,BaseViewHolder>(com.knight.kotlin.module_video.R.layout.video_comment_item,data) {
    override fun convert(holder: BaseViewHolder, item: VideoComment) {
        item.run {
            //本评论的用户昵称
            holder.setText(com.knight.kotlin.module_video.R.id.tv_nickname,commentUser.nickname)
            //本评论的用户头像
            ImageLoader.loadStringPhoto(context,commentUser.userAvatar,holder.getView(com.knight.kotlin.module_video.R.id.iv_head))
            //评论内容
            holder.setText(com.knight.kotlin.module_video.R.id.tv_content,content)
            //点赞数
            holder.setText(com.knight.kotlin.module_video.R.id.tv_likecount,likeNum.toString())
            //这里要补:00
            holder.setText(com.knight.kotlin.module_video.R.id.tv_comment_time, TimeAgoUtils.format(timeStr.plus(":00")))
        }
    }

}
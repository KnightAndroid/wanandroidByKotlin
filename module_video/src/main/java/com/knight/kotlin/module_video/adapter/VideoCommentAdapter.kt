package com.knight.kotlin.module_video.adapter

import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
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
            holder.setText(com.knight.kotlin.module_video.R.id.tv_nickname,commentUser.nickname)
        }
    }

}
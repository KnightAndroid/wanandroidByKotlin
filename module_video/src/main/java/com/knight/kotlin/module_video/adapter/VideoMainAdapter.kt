package com.knight.kotlin.module_video.adapter

import android.widget.ImageView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.knight.kotlin.library_util.image.ImageLoader
import com.knight.kotlin.module_video.entity.VideoPlayEntity

/**
 * Author:Knight
 * Time:2024/2/26 14:43
 * Description:VideoMainAdapter
 */
class VideoMainAdapter(data:MutableList<VideoPlayEntity>) : BaseQuickAdapter<VideoPlayEntity,BaseViewHolder>(
    com.knight.kotlin.module_video.R.layout.video_main_item,data) {
    override fun convert(holder: BaseViewHolder, item: VideoPlayEntity) {
        item.run {
            val videoImage :ImageView = holder.getView(com.knight.kotlin.module_video.R.id.iv_video)
           // VideoHelpUtils.getVideoFirstFrame(videoUrl,videoImage.width,videoImage.height, MediaStore.Images.Thumbnails.MICRO_KIND)
           // ImageLoader.loadVideoFirstFrameCorner(context,item.videoUrl,videoImage,10)
            ImageLoader.loadRoundedCornerPhoto(context,thumbUrl,videoImage,10)
        }


    }


}
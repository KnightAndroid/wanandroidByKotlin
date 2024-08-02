package com.knight.kotlin.module_video.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.chad.library.adapter4.BaseQuickAdapter
import com.knight.kotlin.library_util.image.ImageLoader
import com.knight.kotlin.module_video.databinding.VideoMainItemBinding
import com.knight.kotlin.module_video.entity.VideoPlayEntity

/**
 * Author:Knight
 * Time:2024/2/26 14:43
 * Description:VideoMainAdapter
 */
class VideoMainAdapter : BaseQuickAdapter<VideoPlayEntity, VideoMainAdapter.VH>() {
    class VH(
        parent: ViewGroup,
        val binding: VideoMainItemBinding = VideoMainItemBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        ),
    ) : RecyclerView.ViewHolder(binding.root)

    override fun onBindViewHolder(holder: VH, position: Int, item: VideoPlayEntity?) {
        item?.run {
            ImageLoader.loadRoundedCornerPhoto(context,thumbUrl,holder.binding.ivVideo,10)
        }

    }

    override fun onCreateViewHolder(context: Context, parent: ViewGroup, viewType: Int): VH {
        return VH(parent)
    }


}
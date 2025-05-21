package com.knight.kotlin.module_eye_video_detail.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.chad.library.adapter4.BaseQuickAdapter
import com.knight.kotlin.module_eye_video_detail.databinding.EyeVideoCommentItemBinding
import com.knight.kotlin.module_eye_video_detail.entity.EyeVideoCommentEntity


/**
 * @author created by luguian
 * @organize
 * @Date 2025/5/21 14:52
 * @descript:
 */
class EyeVideoReplyAdapter : BaseQuickAdapter<EyeVideoCommentEntity,EyeVideoReplyAdapter.VH>() {

    class VH(
        parent: ViewGroup,
        val binding: EyeVideoCommentItemBinding = EyeVideoCommentItemBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
    ) : RecyclerView.ViewHolder(binding.root)

    override fun onBindViewHolder(holder: VH, position: Int, item: EyeVideoCommentEntity?) {
        item?.let {
            holder.binding.videoComment = it
        }
    }

    override fun onCreateViewHolder(context: Context, parent: ViewGroup, viewType: Int): VH {
        return VH(parent)
    }
}





object CommentDiffCallback : DiffUtil.ItemCallback<EyeVideoCommentEntity>() {
    override fun areItemsTheSame(
        oldItem: EyeVideoCommentEntity,
        newItem: EyeVideoCommentEntity
    ): Boolean = oldItem.comment_id == newItem.comment_id

    override fun areContentsTheSame(
        oldItem: EyeVideoCommentEntity,
        newItem: EyeVideoCommentEntity
    ): Boolean = oldItem == newItem // 对于 data class 是结构比较，OK
}
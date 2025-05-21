package com.knight.kotlin.module_eye_video_detail.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.chad.library.adapter4.BaseQuickAdapter
import com.knight.kotlin.module_eye_video_detail.databinding.EyeVideoCommentItemBinding
import com.knight.kotlin.module_eye_video_detail.entity.EyeVideoCommentEntity


/**
 * @author created by luguian
 * @organize
 * @Date 2025/5/21 9:51
 * @descript:开眼视频评论
 */
class EyeVideoCommentAdapter:BaseQuickAdapter<EyeVideoCommentEntity,EyeVideoCommentAdapter.VH>() {

    class VH(
        parent: ViewGroup,
        val binding: EyeVideoCommentItemBinding = EyeVideoCommentItemBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        ),
    ) : RecyclerView.ViewHolder(binding.root) {
        val replyAdapter = EyeVideoCommentAdapter().apply {
            setHasStableIds(true) // 可选，提升性能
        }

        init {
            binding.rvReplyComment.apply {
                layoutManager = LinearLayoutManager(parent.context)
                adapter = replyAdapter
                isNestedScrollingEnabled = false
            }
        }
    }

    override fun onBindViewHolder(holder: VH, position: Int, item: EyeVideoCommentEntity?) {
        item?.run {
            holder.binding.videoComment = this

            if (reply_list.isNotEmpty()) {
                holder.binding.rvReplyComment.visibility = View.VISIBLE
                holder.replyAdapter.submitList(reply_list)
            } else {
                holder.binding.rvReplyComment.visibility = View.GONE
                holder.replyAdapter.submitList(emptyList()) // 防止数据残留
            }
        }
    }

    override fun onCreateViewHolder(context: Context, parent: ViewGroup, viewType: Int): VH {
        return VH(parent)
    }
}
package com.knight.kotlin.module_eye_recommend.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.chad.library.adapter4.BaseQuickAdapter
import com.knight.kotlin.module_eye_recommend.databinding.EyeRecommendSmallVideoItemBinding
import com.knight.kotlin.module_eye_recommend.entity.EyeRecommendVideoEntity


/**
 * @author created by luguian
 * @organize
 * @Date 2025/10/13 16:57
 * @descript:开眼推荐页面小视频
 */
class EyeRecommendSmallVideoAdapter: BaseQuickAdapter<EyeRecommendVideoEntity,EyeRecommendSmallVideoAdapter.VH>() {
    override fun onBindViewHolder(
        holder: VH,
        position: Int,
        item: EyeRecommendVideoEntity?
    ) {
        item?.let {
            holder.binding.viewModel = it
            holder.binding.tvEyeRecommendSmallVideoCategory.text =
                it.tags.takeIf { it.isNotEmpty() }?.joinToString(" ") { it.title } ?: ""
        }
    }

    override fun onCreateViewHolder(
        context: Context,
        parent: ViewGroup,
        viewType: Int
    ): VH {
        return VH(parent)
    }

    class VH(
        parent: ViewGroup,
        val binding: EyeRecommendSmallVideoItemBinding = EyeRecommendSmallVideoItemBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        ),
    ) : RecyclerView.ViewHolder(binding.root)
}
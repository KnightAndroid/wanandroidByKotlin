package com.knight.kotlin.module_eye_recommend.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.chad.library.adapter4.BaseQuickAdapter
import com.knight.kotlin.module_eye_recommend.databinding.EyeRecommendLargeVideoItemBinding
import com.knight.kotlin.module_eye_recommend.entity.EyeRecommendVideoEntity


/**
 * @author created by luguian
 * @organize
 * @Date 2025/10/13 16:56
 * @descript: 开眼大视频适配器
 */
class EyeRecommendLargeVideoAdapter: BaseQuickAdapter<EyeRecommendVideoEntity,EyeRecommendLargeVideoAdapter.VH>() {
    override fun onBindViewHolder(
        holder: VH,
        position: Int,
        item: EyeRecommendVideoEntity?
    ) {
        item?.let {
            holder.binding.viewModel = it

        }
    }

    override fun onCreateViewHolder(
        context: Context,
        parent: ViewGroup,
        viewType: Int
    ): VH {
        return VH(parent)
    }

    // --> eye_discover_category_detail_item
    class VH(
        parent: ViewGroup,
        val binding: EyeRecommendLargeVideoItemBinding = EyeRecommendLargeVideoItemBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        ),
    ) : RecyclerView.ViewHolder(binding.root)
}
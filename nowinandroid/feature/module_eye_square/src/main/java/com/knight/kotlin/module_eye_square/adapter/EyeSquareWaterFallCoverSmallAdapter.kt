package com.knight.kotlin.module_eye_square.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.ViewGroup.MarginLayoutParams
import androidx.recyclerview.widget.RecyclerView
import com.chad.library.adapter4.BaseQuickAdapter
import com.core.library_common.util.dp2px
import com.knight.kotlin.library_base.entity.eye_type.EyeWaterFallCoverVideoImage
import com.knight.kotlin.library_base.ktx.getScreenWidth
import com.knight.kotlin.module_eye_square.databinding.EyeSquareWaterfallCoverSmallBinding

/**
 * @Description
 * @Author knight
 * @Time 2025/2/11 23:19
 * 广场WaterCoverSmall适配器
 */

class EyeSquareWaterFallCoverSmallAdapter : BaseQuickAdapter<EyeWaterFallCoverVideoImage, EyeSquareWaterFallCoverSmallAdapter.VH>() {

    class VH(
        parent: ViewGroup,
        val binding: EyeSquareWaterfallCoverSmallBinding = EyeSquareWaterfallCoverSmallBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        ),
    ) : RecyclerView.ViewHolder(binding.root)

    override fun onBindViewHolder(holder: VH, position: Int, item: EyeWaterFallCoverVideoImage?) {
        holder.binding.viewModel = item

        holder.binding.ivWaterfallSamallCover.apply {
            layoutParams.width = getScreenWidth() / 2 - 12.dp2px()
            layoutParams.height = layoutParams.width
        }

        val params = holder.itemView.layoutParams as MarginLayoutParams
        when ((position + 1) % 2) {
            0 -> {
                params.marginStart = 4.dp2px()
                params.marginEnd = 8.dp2px()
            }

            1 -> {
                params.marginStart = 8.dp2px()
                params.marginEnd = 4.dp2px()
            }
        }

    }

    override fun onCreateViewHolder(context: Context, parent: ViewGroup, viewType: Int): VH {
        return VH(parent)
    }



}
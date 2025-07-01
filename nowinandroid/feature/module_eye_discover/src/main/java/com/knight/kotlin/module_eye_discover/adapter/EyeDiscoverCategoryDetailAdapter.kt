package com.knight.kotlin.module_eye_discover.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.chad.library.adapter4.BaseQuickAdapter
import com.core.library_base.entity.EyeItemEntity
import com.knight.kotlin.module_eye_discover.databinding.EyeDiscoverCategoryDetailItemBinding


/**
 * @author created by luguian
 * @organize
 * @Date 2024/8/14 18:06
 * @descript:详细分类适配器
 */
class EyeDiscoverCategoryDetailAdapter : BaseQuickAdapter<EyeItemEntity, EyeDiscoverCategoryDetailAdapter.VH>() {

    class VH(
        parent: ViewGroup,
        val binding: EyeDiscoverCategoryDetailItemBinding = EyeDiscoverCategoryDetailItemBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        ),
    ) : RecyclerView.ViewHolder(binding.root)

    override fun onBindViewHolder(holder: VH, position: Int, item: EyeItemEntity?) {
        holder.binding.viewModel = item
    }

    override fun onCreateViewHolder(context: Context, parent: ViewGroup, viewType: Int): VH {
        return VH(parent)
    }



}
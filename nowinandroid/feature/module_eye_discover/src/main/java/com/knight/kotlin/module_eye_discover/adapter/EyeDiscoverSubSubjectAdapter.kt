package com.knight.kotlin.module_eye_discover.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.chad.library.adapter4.BaseQuickAdapter
import com.knight.kotlin.module_eye_discover.databinding.EyeDiscoverSubjectSubItemBinding
import com.knight.kotlin.module_eye_discover.entity.SquareCard


/**
 * @author created by luguian
 * @organize
 * @Date 2024/8/9 16:29
 * @descript:专题策划布局适配器
 */
class EyeDiscoverSubSubjectAdapter : BaseQuickAdapter<SquareCard,EyeDiscoverSubSubjectAdapter.VH>(){

    class VH(
        parent: ViewGroup,
        val binding: EyeDiscoverSubjectSubItemBinding = EyeDiscoverSubjectSubItemBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        ),
    ) : RecyclerView.ViewHolder(binding.root)

    override fun onBindViewHolder(holder: VH, position: Int, item: SquareCard?) {
        holder.binding.viewModel = item
    }

    override fun onCreateViewHolder(context: Context, parent: ViewGroup, viewType: Int): VH {
        return VH(parent)
    }

}
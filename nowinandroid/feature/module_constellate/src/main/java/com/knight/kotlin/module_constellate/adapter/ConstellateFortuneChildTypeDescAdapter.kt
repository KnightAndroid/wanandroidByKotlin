package com.knight.kotlin.module_constellate.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.chad.library.adapter4.BaseQuickAdapter
import com.knight.kotlin.module_constellate.R
import com.knight.kotlin.module_constellate.databinding.ConstellateFortuneChildrenDescItemBinding
import com.knight.kotlin.module_constellate.entity.ConstellateFortuneChildrenEntity


/**
 * @author created by luguian
 * @organize
 * @Date 2025/9/12 17:00
 * @descript:运势子类适配器
 */
class ConstellateFortuneChildTypeDescAdapter:BaseQuickAdapter<ConstellateFortuneChildrenEntity,ConstellateFortuneChildTypeDescAdapter.VH>() {



    class VH(
        parent: ViewGroup,
        val binding: ConstellateFortuneChildrenDescItemBinding = ConstellateFortuneChildrenDescItemBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        ),
    ) : RecyclerView.ViewHolder(binding.root)

    override fun onBindViewHolder(holder: VH, position: Int, item: ConstellateFortuneChildrenEntity?) {
        item?.run {
            if (name == "工作") {
                holder.binding.ivConstellateChildFortune.setBackgroundResource(R.drawable.constellate_small_work_icon)
                holder.binding.tvFortuneChildName.text = "工作"
            } else {
                holder.binding.ivConstellateChildFortune.setBackgroundResource(R.drawable.constellate_small_study_icon)
                holder.binding.tvFortuneChildName.text = "学习"
            }
            holder.binding.tvFortuneChildDesc.text = desc
        }
    }

    override fun onCreateViewHolder(context: Context, parent: ViewGroup, viewType: Int): VH {
        return VH(parent)
    }

}
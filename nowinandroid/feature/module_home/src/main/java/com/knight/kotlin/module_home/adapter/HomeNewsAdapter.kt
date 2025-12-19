package com.knight.kotlin.module_home.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.chad.library.adapter4.BaseQuickAdapter
import com.knight.kotlin.module_home.databinding.HomeNewsItemBinding


/**
 * @author created by luguian
 * @organize
 * @Date 2025/4/11 15:28
 * @descript:新闻适配器
 */
class HomeNewsAdapter :
    BaseQuickAdapter<String, HomeNewsAdapter.VH>() {

    class VH(val binding: HomeNewsItemBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(
        context:Context,
        parent: ViewGroup,
        viewType: Int
    ): VH {
        val binding = HomeNewsItemBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return VH(binding)
    }

    override fun onBindViewHolder(holder: VH, position: Int, item: String?) {
        item ?: return
        holder.binding.title = item
    }
}
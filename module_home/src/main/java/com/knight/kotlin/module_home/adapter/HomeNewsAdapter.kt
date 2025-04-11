package com.knight.kotlin.module_home.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.chad.library.adapter4.BaseQuickAdapter
import com.knight.kotlin.module_home.adapter.BaiduHotSearchAdapter.VH
import com.knight.kotlin.module_home.databinding.HomeNewsItemBinding


/**
 * @author created by luguian
 * @organize
 * @Date 2025/4/11 15:28
 * @descript:新闻适配器
 */
class HomeNewsAdapter : BaseQuickAdapter<String, HomeNewsAdapter.VH>() {


    // 自定义ViewHolder类
    class VH(
        parent: ViewGroup,
        val binding: HomeNewsItemBinding = HomeNewsItemBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        ),
    ) : RecyclerView.ViewHolder(binding.root)

    override fun onBindViewHolder(holder: VH, position: Int, item: String?) {
        val binding = DataBindingUtil.getBinding<HomeNewsItemBinding>(holder.itemView)
        item?.run {
            binding?.title = this
        }
    }

    override fun onCreateViewHolder(context: Context, parent: ViewGroup, viewType: Int): VH {
        return VH(parent)
    }

}
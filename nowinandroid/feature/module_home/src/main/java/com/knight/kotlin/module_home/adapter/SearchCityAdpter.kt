package com.knight.kotlin.module_home.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.chad.library.adapter4.BaseQuickAdapter
import com.knight.kotlin.module_home.databinding.HomeSearchCityItemBinding


/**
 * @author created by luguian
 * @organize
 * @Date 2025/4/21 15:59
 * @descript:搜索城市适配器
 */
class SearchCityAdpter: BaseQuickAdapter<String, SearchCityAdpter.VH>() {

    // 自定义ViewHolder类
    class VH(
        parent: ViewGroup,
        val binding: HomeSearchCityItemBinding = HomeSearchCityItemBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        ),
    ) : RecyclerView.ViewHolder(binding.root)


    override fun onBindViewHolder(holder: VH, position: Int, item: String?) {
        item?.run {
            holder.binding.tvCityName.setText(this)
        }
    }

    override fun onCreateViewHolder(context: Context, parent: ViewGroup, viewType: Int): VH {
        return VH(parent)
    }


}
package com.knight.kotlin.module_home.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.chad.library.adapter4.BaseQuickAdapter
import com.knight.kotlin.module_home.R
import com.knight.kotlin.module_home.databinding.HomeBaiduRealtimeItemBinding
import com.knight.kotlin.library_base.entity.BaiduContent


/**
 * @author created by luguian
 * @organize
 * @Date 2024/9/4 16:13
 * @descript:百度热搜榜适配器
 */
class BaiduHotSearchAdapter : BaseQuickAdapter<BaiduContent, BaiduHotSearchAdapter.VH>(){


    // 自定义ViewHolder类
    class VH(
        parent: ViewGroup,
        val binding: HomeBaiduRealtimeItemBinding = HomeBaiduRealtimeItemBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        ),
    ) : RecyclerView.ViewHolder(binding.root)


    override fun onBindViewHolder(holder: VH, position: Int, item: BaiduContent?) {
        val binding = DataBindingUtil.getBinding<HomeBaiduRealtimeItemBinding>(holder.itemView)
        item?.run {
            binding?.viewModel = this
            if (position == 0) {
                holder.binding.ivRankHot.visibility = View.VISIBLE
                holder.binding.ivRankHot.setBackgroundResource(R.drawable.home_baidu_real_time_one)
            } else if (position == 1) {
                holder.binding.ivRankHot.visibility = View.VISIBLE
                holder.binding.ivRankHot.setBackgroundResource(R.drawable.home_baidu_real_time_two)
            } else if (position == 2) {
                holder.binding.ivRankHot.visibility = View.VISIBLE
                holder.binding.ivRankHot.setBackgroundResource(R.drawable.home_baidu_real_time_three)
            } else {
                holder.binding.ivRankHot.visibility = View.INVISIBLE
            }
        }
    }

    override fun onCreateViewHolder(context: Context, parent: ViewGroup, viewType: Int): VH {
        return VH(parent)
    }

}
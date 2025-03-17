package com.knight.kotlin.module_realtime.adapter

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.chad.library.adapter4.BaseQuickAdapter
import com.knight.kotlin.library_base.entity.BaiduContent
import com.knight.kotlin.module_realtime.databinding.RealtimeHotItemBinding


/**
 * @author created by luguian
 * @organize
 * @Date 2025/3/11 16:58
 * @descript: BaiduContent
 */
class RealTimeHotMainAdapter : BaseQuickAdapter<BaiduContent,RealTimeHotMainAdapter.VH>() {


    // 自定义ViewHolder类
    class VH(
        parent: ViewGroup,
        val binding: RealtimeHotItemBinding = RealtimeHotItemBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        ),
    ) : RecyclerView.ViewHolder(binding.root)

    override fun onBindViewHolder(holder: VH, position: Int, item: BaiduContent?) {
       if (position == 0) {
           holder.binding.tvRealTimeRank.setBackgroundResource(com.knight.kotlin.library_base.R.drawable.base_baidu_real_time_top)
       } else {
           holder.binding.tvRealTimeRank.background = null
           holder.binding.tvRealTimeRank.setText(position.toString())
       }

       if (position == 1) {
           holder.binding.tvRealTimeRank.setTextColor(Color.parseColor("#FE3951"))
       } else if (position == 2) {
           holder.binding.tvRealTimeRank.setTextColor(Color.parseColor("#FF7315"))
       } else if (position == 3) {
           holder.binding.tvRealTimeRank.setTextColor(Color.parseColor("#FAAC17"))
       } else {
           holder.binding.tvRealTimeRank.setTextColor(Color.parseColor("#858585"))
       }

        holder.binding.tvRealTimeTitle.setText(item?.query)

    }

    override fun onCreateViewHolder(context: Context, parent: ViewGroup, viewType: Int): VH {
        return VH(parent)
    }
}
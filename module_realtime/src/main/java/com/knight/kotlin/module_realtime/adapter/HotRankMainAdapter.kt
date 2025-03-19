package com.knight.kotlin.module_realtime.adapter

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.chad.library.adapter4.BaseQuickAdapter
import com.knight.kotlin.library_base.entity.BaiduContent
import com.knight.kotlin.library_util.image.ImageLoader
import com.knight.kotlin.module_realtime.databinding.RealtimeHotItemBinding
import com.knight.kotlin.module_realtime.enum.HotListEnum


/**
 * @author created by luguian
 * @organize
 * @Date 2025/3/11 16:58
 * @descript: BaiduContent
 */
class HotRankMainAdapter(val hotListEnum: HotListEnum) : BaseQuickAdapter<BaiduContent,HotRankMainAdapter.VH>() {


    // 自定义ViewHolder类
    class VH(
        parent: ViewGroup,
        val binding: RealtimeHotItemBinding = RealtimeHotItemBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        ),
    ) : RecyclerView.ViewHolder(binding.root)

    override fun onBindViewHolder(holder: VH, position: Int, item: BaiduContent?) {

       item?.run {
           if (hotListEnum == HotListEnum.REALTIME) {
               if (position == 0) {
                   holder.binding.tvRealTimeRank.setBackgroundResource(com.knight.kotlin.library_base.R.drawable.base_baidu_real_time_top)
                   holder.binding.tvRealTimeRank.setText("")
               } else {
                   holder.binding.tvRealTimeRank.background = null
                   holder.binding.tvRealTimeRank.setText(position.toString())
               }
               holder.binding.ivHotRankExpress.visibility = View.INVISIBLE
           } else {
               holder.binding.tvRealTimeRank.background = null
               holder.binding.tvRealTimeRank.setText((position+1).toString())
               expression?.let {
                   holder.binding.ivHotRankExpress.visibility = View.VISIBLE
                   ImageLoader.loadStringPhoto(context,it,holder.binding.ivHotRankExpress)
               } ?:run {
                   holder.binding.ivHotRankExpress.visibility = View.INVISIBLE
               }
           }

           if ((position == 1 && hotListEnum == HotListEnum.REALTIME) || (position == 0 && (hotListEnum == HotListEnum.PHRASE || hotListEnum == HotListEnum.LIVELIHOOD || hotListEnum == HotListEnum.FINANCE))) {
               holder.binding.tvRealTimeRank.setTextColor(Color.parseColor("#FE3951"))
           } else if ((position == 2 && hotListEnum == HotListEnum.REALTIME) || (position == 1 && (hotListEnum == HotListEnum.PHRASE || hotListEnum == HotListEnum.LIVELIHOOD || hotListEnum == HotListEnum.FINANCE))) {
               holder.binding.tvRealTimeRank.setTextColor(Color.parseColor("#FF7315"))
           } else if ((position == 3 && hotListEnum == HotListEnum.REALTIME) || (position == 2 && (hotListEnum == HotListEnum.PHRASE || hotListEnum == HotListEnum.LIVELIHOOD || hotListEnum == HotListEnum.FINANCE))) {
               holder.binding.tvRealTimeRank.setTextColor(Color.parseColor("#FAAC17"))
           } else {
               holder.binding.tvRealTimeRank.setTextColor(Color.parseColor("#858585"))
           }
           if (hotListEnum == HotListEnum.REALTIME) {
               holder.binding.tvRealTimeTitle.setText(item?.query)
           } else {
               holder.binding.tvRealTimeTitle.setText(item?.word)
           }


           hotTagImg?.let{
               holder.binding.ivHotRankTag.visibility = View.VISIBLE
               ImageLoader.loadStringPhoto(context,it,holder.binding.ivHotRankTag)
           } ?:run {
               holder.binding.ivHotRankTag.visibility = View.INVISIBLE
           }

           if (desc.isNotEmpty() && hotListEnum == HotListEnum.PHRASE) {
               holder.binding.tvRealTimeDesc.visibility = View.VISIBLE
               holder.binding.tvRealTimeDesc.setText(desc)
           } else {
               holder.binding.tvRealTimeDesc.visibility = View.GONE
           }
       }



    }

    override fun onCreateViewHolder(context: Context, parent: ViewGroup, viewType: Int): VH {
        return VH(parent)
    }
}
package com.knight.kotlin.module_realtime.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.chad.library.adapter4.BaseQuickAdapter
import com.knight.kotlin.module_realtime.databinding.RealtimeMovieCategoryBinding
import com.knight.kotlin.module_realtime.enum.LevelEnum


/**
 * @author created by luguian
 * @organize
 * @Date 2025/3/24 11:31
 * @descript:热搜电影类型选择
 */
class HotRankMovieCategoryAdapter(val listener:OnChipClickListener,val enum:LevelEnum):BaseQuickAdapter<String,HotRankMovieCategoryAdapter.VH>() {



    //默认第一个
    var selectedPosition = 0
    // 自定义ViewHolder类
    class VH(
        parent: ViewGroup,
        val binding: RealtimeMovieCategoryBinding = RealtimeMovieCategoryBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        ),
    ) : RecyclerView.ViewHolder(binding.root)

    override fun onBindViewHolder(holder: VH, position: Int, item: String?) {

        item?.run {
            holder.binding.realtimeMovieChip.isChecked = position == selectedPosition

            holder.binding.realtimeMovieChip.setOnClickListener {
                selectedPosition = holder.getAbsoluteAdapterPosition()
                notifyDataSetChanged()
                listener.onChipClick(enum,this)
            }
            holder.binding.realtimeMovieChip.text = this
        }

    }

    override fun onCreateViewHolder(context: Context, parent: ViewGroup, viewType: Int): VH {
        return VH(parent)
    }



    interface OnChipClickListener {
        fun onChipClick(enum: LevelEnum,chipText: String)
    }

}
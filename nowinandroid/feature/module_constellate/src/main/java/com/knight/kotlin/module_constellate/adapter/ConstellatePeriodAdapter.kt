package com.knight.kotlin.module_constellate.adapter

import android.content.Context
import android.graphics.Color
import android.graphics.Typeface
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.chad.library.adapter4.BaseQuickAdapter
import com.knight.kotlin.library_base.ktx.getScreenWidth
import com.knight.kotlin.library_common.util.CacheUtils
import com.knight.kotlin.module_constellate.R
import com.knight.kotlin.module_constellate.databinding.ConstellateWeekItemBinding


/**
 * @author created by luguian
 * @organize
 * @Date 2025/9/11 14:16
 * @descript:周运/月运/年运适配器
 */
class ConstellatePeriodAdapter:BaseQuickAdapter<String,ConstellatePeriodAdapter.VH>(){
    class VH(
        parent: ViewGroup,
        val binding: ConstellateWeekItemBinding = ConstellateWeekItemBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        ),
    ) : RecyclerView.ViewHolder(binding.root)

    override fun onBindViewHolder(holder: VH, position: Int, item: String?) {
        val itemWidth = getScreenWidth() / 5

        item?.run {
            if (position == 3) {
                holder.binding.tvWeekIndex.setTextColor(Color.WHITE)
                holder.binding.tvWeekIndex.setTypeface(null, Typeface.BOLD)   // 加粗
                holder.binding.tvWeekIndex.setBackgroundResource(R.drawable.constellate_week_select_bg)
            } else {
                holder.binding.tvWeekIndex.setTypeface(null, Typeface.NORMAL)   // 普通
                if (CacheUtils.getNormalDark()) {
                    holder.binding.tvWeekIndex.setTextColor(Color.parseColor("#555555"))
                } else {
                    holder.binding.tvWeekIndex.setTextColor(Color.parseColor("#303030"))
                }
                holder.binding.tvWeekIndex.setBackgroundResource(R.drawable.constellate_week_normal_bg)
            }

            holder.binding.week = item
            holder.binding.clWeekIndex.layoutParams.width = itemWidth
        }

    }

    override fun onCreateViewHolder(context: Context, parent: ViewGroup, viewType: Int): VH {
        return VH(parent)
    }
}
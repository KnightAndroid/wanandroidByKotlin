package com.knight.kotlin.module_constellate.adapter

import android.content.Context
import android.graphics.Color
import android.os.Build
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.chad.library.adapter4.BaseQuickAdapter
import com.knight.kotlin.library_base.ktx.getScreenWidth
import com.knight.kotlin.library_common.util.CacheUtils
import com.knight.kotlin.module_constellate.R
import com.knight.kotlin.module_constellate.databinding.ConstellateDateItemBinding
import com.knight.kotlin.module_constellate.entity.ConstellateDateEntity


/**
 * @author created by luguian
 * @organize
 * @Date 2025/8/6 16:52
 * @descript:日运 日期适配器
 */
class ConstellateDateAdapter : BaseQuickAdapter<ConstellateDateEntity,ConstellateDateAdapter.VH>(){

    class VH(
        parent: ViewGroup,
        val binding: ConstellateDateItemBinding = ConstellateDateItemBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        ),
    ) : RecyclerView.ViewHolder(binding.root)

    override fun onBindViewHolder(holder: VH, position: Int, item: ConstellateDateEntity?) {
        val itemWidth = getScreenWidth() / 7
        item?.run {
            if (display == "今") {
                if (CacheUtils.getNormalDark()) {
                    holder.binding.tvWeekNumber.setTextColor(Color.parseColor("#666666"))
                } else {
                    holder.binding.tvWeekNumber.setTextColor(Color.parseColor("#333333"))
                }

                holder.binding.tvDateNumber.setTextColor(Color.WHITE)
                holder.binding.tvDateNumber.setBackgroundResource(R.drawable.constellate_today_bg)
            } else {


                val weekDayColor = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    if (CacheUtils.getNormalDark()) {
                        Color.parseColor("#666666")

                    } else {
                        context.getColor(R.color.constellate_tv_week_number_text_color)
                    }

                } else {
                    if (CacheUtils.getNormalDark()) {
                        Color.parseColor("#666666")

                    } else {
                        @Suppress("DEPRECATION")
                        context.resources.getColor(R.color.constellate_tv_week_number_text_color)
                    }

                }





                holder.binding.tvWeekNumber.setTextColor(weekDayColor)

                val color = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    context.getColor(R.color.constellate_tv_week_number_text_color)
                } else {
                    @Suppress("DEPRECATION")
                    context.resources.getColor(R.color.constellate_tv_week_number_text_color)
                }

                if (display == "明") {
                    if (CacheUtils.getNormalDark()) {
                        holder.binding.tvDateNumber.setTextColor(Color.parseColor("#666666"))
                    } else {
                        holder.binding.tvDateNumber.setTextColor(Color.parseColor("#333333"))
                    }
                } else {
                    holder.binding.tvDateNumber.setTextColor(color)
                }


                holder.binding.tvDateNumber.setBackgroundResource(R.drawable.constellate_date_bg)
            }
            holder.binding.viewModel = item
            holder.binding.clRootConstellateDate.layoutParams.width = itemWidth
        }
    }

    override fun onCreateViewHolder(context: Context, parent: ViewGroup, viewType: Int): VH {
        return VH(parent)
    }
}
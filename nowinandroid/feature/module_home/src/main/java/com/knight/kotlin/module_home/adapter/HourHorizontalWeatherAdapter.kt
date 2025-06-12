package com.knight.kotlin.module_home.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.chad.library.adapter4.BaseQuickAdapter
import com.knight.kotlin.library_base.entity.WeatherEveryHour
import com.knight.kotlin.library_util.DateUtils
import com.knight.kotlin.library_widget.utils.WeatherPicUtil
import com.knight.kotlin.module_home.databinding.HomeWeatherTodayItemBinding


/**
 * @author created by luguian
 * @organize
 * @Date 2025/2/27 15:04
 * @descript:今天天气横向适配器
 */
class HourHorizontalWeatherAdapter : BaseQuickAdapter<WeatherEveryHour,HourHorizontalWeatherAdapter.VH>() {

    // 自定义ViewHolder类
    class VH(
        parent: ViewGroup,
        val binding: HomeWeatherTodayItemBinding = HomeWeatherTodayItemBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        ),
    ) : RecyclerView.ViewHolder(binding.root)

    override fun onBindViewHolder(holder: VH, position: Int, item: WeatherEveryHour?) {
        holder.binding.model = item
        item?.run {
            holder.binding.ivDayWeather.setImageResource(WeatherPicUtil.getDayWeatherPic(weather))
            holder.binding.tvTime.text = DateUtils.formatTimeToHourMinuter(updateTime)

        }

    }

    override fun onCreateViewHolder(context: Context, parent: ViewGroup, viewType: Int): VH {
        val vh = VH(parent)
        return vh
    }
}
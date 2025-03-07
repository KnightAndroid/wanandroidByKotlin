package com.knight.kotlin.module_home.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.chad.library.adapter4.BaseQuickAdapter
import com.knight.kotlin.library_base.entity.WeatherIndexItem
import com.knight.kotlin.library_util.image.ImageLoader
import com.knight.kotlin.module_home.R
import com.knight.kotlin.module_home.databinding.HomeWeatherIndexItemBinding


/**
 * @author created by luguian
 * @organize
 * @Date 2025/3/7 14:30
 * @descript:天气温馨提醒适配器
 */
class WeatherIndexAdapter: BaseQuickAdapter<WeatherIndexItem, WeatherIndexAdapter.VH>()  {


    // 自定义ViewHolder类
    class VH(
        parent: ViewGroup,
        val binding: HomeWeatherIndexItemBinding = HomeWeatherIndexItemBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        ),
    ) : RecyclerView.ViewHolder(binding.root)

    override fun onBindViewHolder(holder: VH, position: Int, item: WeatherIndexItem?) {
        item?.run {

            holder.binding.model = item
            if (item.name.equals("舒适度")) {
                holder.binding.ivWeatherIndexIcon.setImageResource(R.drawable.home_weather_comfort_icon)
            } else if (item.name.equals("路况")) {
                holder.binding.ivWeatherIndexIcon.setImageResource(R.drawable.home_weather_dry_icon)
            } else if (item.name.equals("心情")) {
                holder.binding.ivWeatherIndexIcon.setImageResource(R.drawable.home_weather_mood_icon)
            } else if (item.name.equals("晨练")) {
                holder.binding.ivWeatherIndexIcon.setImageResource(R.drawable.home_weather_morning_icon)
            } else if (item.name.equals("太阳镜")) {
                holder.binding.ivWeatherIndexIcon.setImageResource(R.drawable.home_weather_sunglasses_icon)
            } else if (item.name.equals("旅游")) {
                holder.binding.ivWeatherIndexIcon.setImageResource(R.drawable.home_weather_trourism_icon)
            } else {
                item.url?.let { ImageLoader.loadStringPhoto(context, it,holder.binding.ivWeatherIndexIcon) }
            }
        }
    }

    override fun onCreateViewHolder(context: Context, parent: ViewGroup, viewType: Int): VH {
        return VH(parent)
    }
}
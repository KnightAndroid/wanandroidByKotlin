package com.knight.kotlin.module_home.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.chad.library.adapter4.BaseQuickAdapter
import com.knight.kotlin.library_base.entity.WeatherIndexItem
import com.knight.kotlin.library_base.util.CacheUtils
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
            if (CacheUtils.getNormalDark()) {
                loadImageByName(holder.binding.ivWeatherIndexIcon,item.name)
            } else {
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
    }

    /**
     *
     * 根据名字加载对应名字
     */
    fun loadImageByName(iv: ImageView,name:String) {
         when(name) {
             "空调开启" -> iv.setImageResource(R.drawable.home_weather_night_airconditioner_icon)
             "过敏" ->  iv.setImageResource(R.drawable.home_weather_night_allergy_icon)
             "洗车" -> iv.setImageResource(R.drawable.home_weather_night_carwash_icon)
             "风寒" -> iv.setImageResource(R.drawable.home_weather_night_chill_icon)
             "穿衣" -> iv.setImageResource(R.drawable.home_weather_night_clothes_icon)
             "感冒" -> iv.setImageResource(R.drawable.home_weather_night_cold_icon)
             "舒适度" -> iv.setImageResource(R.drawable.home_weather_night_comfort_icon)
             "空气污染扩散条件" -> iv.setImageResource(R.drawable.home_weather_night_diffusion_icon)
             "路况" -> iv.setImageResource(R.drawable.home_weather_night_dry_icon)
             "晾晒" -> iv.setImageResource(R.drawable.home_weather_night_drying_icon)
             "钓鱼" -> iv.setImageResource(R.drawable.home_weather_night_fish_icon)
             "中暑" -> iv.setImageResource(R.drawable.home_weather_night_heatstroke_icon)
             "化妆" -> iv.setImageResource(R.drawable.home_weather_night_makeup_icon)
             "心情"  -> iv.setImageResource(R.drawable.home_weather_night_mood_icon)
             "晨练" -> iv.setImageResource(R.drawable.home_weather_night_morning_icon)
             "运动" -> iv.setImageResource(R.drawable.home_weather_night_sports_icon)
             "太阳镜"-> iv.setImageResource(R.drawable.home_weather_night_sunglasses_icon)
             "防晒"-> iv.setImageResource(R.drawable.home_weather_night_sunscreen_icon)
             "旅游"-> iv.setImageResource(R.drawable.home_weather_night_trourism_icon)
             "交通"-> iv.setImageResource(R.drawable.home_weather_night_traffic_icon)
             "紫外线强度"-> iv.setImageResource(R.drawable.home_weather_night_ultraviolet_icon)
             "雨伞" -> iv.setImageResource(R.drawable.home_weather_night_umbrella_icon)
             else -> iv.setImageResource(R.drawable.home_weather_night_airconditioner_icon)
         }

    }
    override fun onCreateViewHolder(context: Context, parent: ViewGroup, viewType: Int): VH {
        return VH(parent)
    }
}
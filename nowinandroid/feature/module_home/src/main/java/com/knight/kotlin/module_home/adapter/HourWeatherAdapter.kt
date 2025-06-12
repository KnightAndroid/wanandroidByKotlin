package com.knight.kotlin.module_home.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.knight.kotlin.library_base.entity.WeatherEveryHour
import com.knight.kotlin.module_home.R


/**
 * @author created by luguian
 * @organize
 * @Date 2025/2/27 17:22
 * @descript:
 */
class HourWeatherAdapter : RecyclerView.Adapter<HourWeatherAdapter.HorizontalViewHolder>() {


    private val listData:MutableList<WeatherEveryHour> = mutableListOf()

    fun setWeatherEveryHour(risks:List<WeatherEveryHour>) {
        listData.clear() // 清空旧数据，避免重复添加
        listData.addAll(risks)
        notifyItemChanged(0)
    }



    class HorizontalViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val horizontalRecyclerView: RecyclerView = itemView.findViewById(R.id.rv_horizontal_hour_weather)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HorizontalViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.home_weather_hour_item, parent, false)
        return HorizontalViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: HorizontalViewHolder, position: Int) {


        // 动态设置宽度和高度
//        val layoutParams: ViewGroup.LayoutParams = holder.itemView.getLayoutParams()
//        layoutParams.width = 100 // 设置为固定宽度
//        layoutParams.height = ViewGroup.LayoutParams.WRAP_CONTENT // 设置为 wrap_content
//        holder.itemView.setLayoutParams(layoutParams)
        if (listData.isNotEmpty()) {
            val horizontalAdapter = HourHorizontalWeatherAdapter()
            holder.horizontalRecyclerView.layoutManager = LinearLayoutManager(holder.horizontalRecyclerView.context, LinearLayoutManager.HORIZONTAL, false)
            holder.horizontalRecyclerView.adapter = horizontalAdapter
            horizontalAdapter.submitList(listData)
        }

    }

    override fun getItemCount(): Int {
        return 1
    }
}
package com.knight.kotlin.module_home.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.knight.kotlin.library_base.entity.Rise
import com.knight.kotlin.module_home.databinding.HomeHourWeatherHeadBinding


/**
 * @author created by luguian
 * @organize
 * @Date 2025/2/27 16:32
 * @descript:头部
 */
class HeadHourWeatherAdapter : RecyclerView.Adapter<HeadHourWeatherAdapter.VerticalHeaderViewHolder>() {


    private val mRisks:MutableList<Rise> = mutableListOf()

    fun setRisks(risks:List<Rise>) {
        mRisks.clear() // 清空旧数据，避免重复添加
        mRisks.addAll(risks)
        notifyItemChanged(0)
    }
    class VerticalHeaderViewHolder(val binding: HomeHourWeatherHeadBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VerticalHeaderViewHolder {
        val binding: HomeHourWeatherHeadBinding =  HomeHourWeatherHeadBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return VerticalHeaderViewHolder(binding)
    }

    override fun onBindViewHolder(holder: VerticalHeaderViewHolder, position: Int) {
        // 头部不需要绑定数据
        if (mRisks.isNotEmpty()) {
            holder.binding.model = mRisks.get(position)
        }

    }

    override fun getItemCount(): Int {
        return 1 // 头部只有一个
    }
}
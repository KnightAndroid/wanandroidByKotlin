package com.knight.kotlin.module_realtime.adapter

import android.content.Context
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

    }

    override fun onCreateViewHolder(context: Context, parent: ViewGroup, viewType: Int): VH {
        return VH(parent)
    }
}
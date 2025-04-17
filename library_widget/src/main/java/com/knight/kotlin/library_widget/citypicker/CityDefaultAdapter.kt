package com.knight.kotlin.library_widget.citypicker

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.chad.library.adapter4.BaseQuickAdapter
import com.knight.kotlin.library_widget.databinding.CityDefaultItemBinding


/**
 * @author created by luguian
 * @organize
 * @Date 2025/4/17 16:05
 * @descript:
 */
class CityDefaultAdapter(val mInnerListener:InnerListener): BaseQuickAdapter<CityBean, CityDefaultAdapter.VH>() {



    // 自定义ViewHolder类
    class VH(
        parent: ViewGroup,
        val binding: CityDefaultItemBinding = CityDefaultItemBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        ),
    ) : RecyclerView.ViewHolder(binding.root)

    override fun onBindViewHolder(holder: VH, position: Int, item: CityBean?) {
        item?.run {
            val binding = CityDefaultItemBinding.bind(holder.itemView)
            binding.cityListItemName.text = city

            binding.cityListItemName.setText(item.city)
            binding.cityListItemName.setOnClickListener(View.OnClickListener {
                if (mInnerListener != null) {
                    mInnerListener.dismiss(position, item)
                }
            })
        }
    }

    override fun onCreateViewHolder(context: Context, parent: ViewGroup, viewType: Int): VH {
        return VH(parent)
    }
}
package com.knight.kotlin.module_utils.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.chad.library.adapter4.BaseQuickAdapter
import com.core.library_base.util.CacheUtils
import com.knight.kotlin.module_utils.databinding.UtilsItemBinding
import com.knight.kotlin.module_utils.entity.UtilsEntity

/**
 * Author:Knight
 * Time:2022/6/2 15:21
 * Description:UtilItemAdapter
 */
class UtilItemAdapter:
    BaseQuickAdapter<UtilsEntity, UtilItemAdapter.VH>()  {
    class VH(
        parent: ViewGroup,
        val binding: UtilsItemBinding = UtilsItemBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        ),
    ) : RecyclerView.ViewHolder(binding.root)


    override fun onBindViewHolder(holder: VH, position: Int, item: UtilsEntity?) {
        item?.run {
            holder.binding.utilsTitle.setText(tabName)
            holder.binding.utilsDesc.setText(desc)
            holder.binding.utilsTitle.setTextColor(CacheUtils.getThemeColor())
        }
    }

    override fun onCreateViewHolder(context: Context, parent: ViewGroup, viewType: Int): VH {
        return VH(parent)
    }
}
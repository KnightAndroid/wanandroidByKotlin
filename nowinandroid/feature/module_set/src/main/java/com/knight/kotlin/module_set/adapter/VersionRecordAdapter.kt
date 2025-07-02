package com.knight.kotlin.module_set.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.chad.library.adapter4.BaseQuickAdapter
import com.knight.kotlin.library_base.utils.CacheUtils
import com.knight.kotlin.module_set.databinding.SetVersionRecordItemBinding
import com.knight.kotlin.module_set.entity.VersionRecordEntity

/**
 * Author:Knight
 * Time:2022/8/26 10:29
 * Description:VersionRecordAdapter
 */
class VersionRecordAdapter : BaseQuickAdapter<VersionRecordEntity, VersionRecordAdapter.VH>(){
    class VH(
        parent: ViewGroup,
        val binding: SetVersionRecordItemBinding = SetVersionRecordItemBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        ),
    ) : RecyclerView.ViewHolder(binding.root)

    override fun onBindViewHolder(holder: VH, position: Int, item: VersionRecordEntity?) {
        item?.run {
            holder.binding.tvAppupdateTitle.setText(title)
            holder.binding.tvAppupdateTitle.setTextColor(CacheUtils.getThemeColor())
            holder.binding.tvAppupdateTime.setText(item.publishTime)
            holder.binding.tvAppupdateDesc.setText(desc)
        }
    }

    override fun onCreateViewHolder(context: Context, parent: ViewGroup, viewType: Int): VH {
        return VH(parent)
    }

}
package com.knight.kotlin.module_mine.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.chad.library.adapter4.BaseQuickAdapter
import com.knight.kotlin.library_base.utils.CacheUtils
import com.knight.kotlin.module_mine.databinding.MineCoindetailItemBinding
import com.knight.kotlin.module_mine.entity.MyDetailCoinEntity

/**
 * Author:Knight
 * Time:2022/5/11 10:20
 * Description:MyCointsAdapter
 */
class MyCointsAdapter:
    BaseQuickAdapter<MyDetailCoinEntity, MyCointsAdapter.VH>()  {

    class VH(
        parent: ViewGroup,
        val binding: MineCoindetailItemBinding= MineCoindetailItemBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        ),
    ) : RecyclerView.ViewHolder(binding.root)

    override fun onBindViewHolder(holder: VH, position: Int, item: MyDetailCoinEntity?) {
        item?.run {
            holder.binding.mineTvDetailpointtitle.setText(reason)
            holder.binding.mineTvCoincount.setText( "+$coinCount")
            holder.binding.mineTvCoincount.setTextColor(CacheUtils.getThemeColor())
            holder.binding.mineTvTimereason.setText(desc)
        }
    }

    override fun onCreateViewHolder(context: Context, parent: ViewGroup, viewType: Int): VH {
        return VH(parent)
    }
}
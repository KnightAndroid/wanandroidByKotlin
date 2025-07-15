package com.knight.kotlin.module_set.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.chad.library.adapter4.BaseQuickAdapter
import com.knight.kotlin.library_common.util.CacheUtils
import com.knight.kotlin.library_common.util.LanguageFontSizeUtils
import com.knight.kotlin.module_set.databinding.SetSelectDarkItemBinding
import com.knight.kotlin.module_set.entity.DarkSelectEntity

/**
 * Author:Knight
 * Time:2022/5/20 17:29
 * Description:SelectDarkModeAdapter
 */
class SelectDarkModeAdapter :
    BaseQuickAdapter<DarkSelectEntity, SelectDarkModeAdapter.VH>()  {

    class VH(
        parent: ViewGroup,
        val binding: SetSelectDarkItemBinding = SetSelectDarkItemBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        ),
    ) : RecyclerView.ViewHolder(binding.root)


    override fun onBindViewHolder(holder: VH, position: Int, item: DarkSelectEntity?) {
        item?.run {
            if (LanguageFontSizeUtils.isChinese()) {
                holder.binding.setTvDarkName.setText(name)
            } else {
                holder.binding.setTvDarkName.setText(englishNamn)
            }

            if (select) {
                holder.binding.setIvSelectDarkmodel.visibility = View.VISIBLE
                holder.binding.setIvSelectDarkmodel.setColorFilter(CacheUtils.getThemeColor())
            } else {
                holder.binding.setIvSelectDarkmodel.visibility = View.INVISIBLE
            }

            if (showLine) {
                holder.binding.setTvDarkmodelLine.visibility = View.VISIBLE
            } else {
                holder.binding.setTvDarkmodelLine.visibility = View.INVISIBLE
            }
        }
    }

    override fun onCreateViewHolder(context: Context, parent: ViewGroup, viewType: Int): VH {
        return VH(parent)
    }
}
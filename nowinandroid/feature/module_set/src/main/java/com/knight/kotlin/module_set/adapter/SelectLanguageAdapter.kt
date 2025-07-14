package com.knight.kotlin.module_set.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.chad.library.adapter4.BaseQuickAdapter
import com.core.library_common.util.CacheUtils
import com.core.library_common.util.LanguageFontSizeUtils
import com.knight.kotlin.module_set.databinding.SetLanguageItemBinding
import com.knight.kotlin.module_set.entity.LanguageEntity

/**
 * Author:Knight
 * Time:2022/5/31 16:30
 * Description:SelectLanguageAdapter
 */
class SelectLanguageAdapter(data:MutableList<LanguageEntity>):
    BaseQuickAdapter<LanguageEntity,SelectLanguageAdapter.VH>() {

    class VH(
        parent: ViewGroup,
        val binding: SetLanguageItemBinding = SetLanguageItemBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        ),
    ) : RecyclerView.ViewHolder(binding.root)

    override fun onBindViewHolder(holder: VH, position: Int, item: LanguageEntity?) {
        item?.run {
            if (LanguageFontSizeUtils.isChinese()) {
                holder.binding.setTvLanguageName.setText(languageName)
            } else {
                holder.binding.setTvLanguageName.setText(englishName)
            }

            if (select) {
                holder.binding.setIvSelectLanguage.visibility = View.VISIBLE
                holder.binding.setIvSelectLanguage.setColorFilter(CacheUtils.getThemeColor())
            } else {
                holder.binding.setIvSelectLanguage.visibility = View.INVISIBLE
            }

            if (showLine) {
                holder.binding.setTvLanguageLine.visibility = View.VISIBLE
            } else {
                holder.binding.setTvLanguageLine.visibility = View.INVISIBLE
            }
        }
    }

    override fun onCreateViewHolder(context: Context, parent: ViewGroup, viewType: Int): VH {
        return VH(parent)
    }
}
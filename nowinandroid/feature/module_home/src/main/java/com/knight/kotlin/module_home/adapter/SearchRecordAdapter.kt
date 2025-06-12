package com.knight.kotlin.module_home.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.chad.library.adapter4.BaseQuickAdapter
import com.knight.kotlin.library_database.entity.SearchHistroyKeywordEntity
import com.knight.kotlin.module_home.databinding.HomeSearchkeywordItemBinding

/**
 * Author:Knight
 * Time:2022/4/19 14:27
 * Description:SearchRecordAdapter
 */
class SearchRecordAdapter:
    BaseQuickAdapter<SearchHistroyKeywordEntity, SearchRecordAdapter.VH>() {

    // 自定义ViewHolder类
    class VH(
        parent: ViewGroup,
        val binding: HomeSearchkeywordItemBinding = HomeSearchkeywordItemBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        ),
    ) : RecyclerView.ViewHolder(binding.root)


    override fun onBindViewHolder(holder: VH, position: Int, item: SearchHistroyKeywordEntity?) {
        item?.run {
            holder.binding.tvSearchKeyword.setText(name)
        }
    }

    override fun onCreateViewHolder(context: Context, parent: ViewGroup, viewType: Int): VH {
        return VH(parent)
    }


}
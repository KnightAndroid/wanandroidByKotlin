package com.knight.kotlin.module_home.adapter

import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.knight.kotlin.library_database.entity.SearchHistroyKeywordEntity
import com.knight.kotlin.module_home.R

/**
 * Author:Knight
 * Time:2022/4/19 14:27
 * Description:SearchRecordAdapter
 */
class SearchRecordAdapter(data:MutableList<SearchHistroyKeywordEntity>):
    BaseQuickAdapter<SearchHistroyKeywordEntity, BaseViewHolder>(R.layout.home_searchkeyword_item,data) {
    override fun convert(holder: BaseViewHolder, item: SearchHistroyKeywordEntity) {
        item.run {
            holder.setText(R.id.tv_search_keyword,name)
        }
    }
}
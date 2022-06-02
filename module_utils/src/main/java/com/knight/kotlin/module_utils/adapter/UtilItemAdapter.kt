package com.knight.kotlin.module_utils.adapter

import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.knight.kotlin.library_base.util.CacheUtils
import com.knight.kotlin.module_utils.R
import com.knight.kotlin.module_utils.entity.UtilsEntity

/**
 * Author:Knight
 * Time:2022/6/2 15:21
 * Description:UtilItemAdapter
 */
class UtilItemAdapter(data:MutableList<UtilsEntity>):
    BaseQuickAdapter<UtilsEntity, BaseViewHolder>( R.layout.utils_item,data)  {
    override fun convert(holder: BaseViewHolder, item: UtilsEntity) {
        item.run {
            holder.setText(R.id.utils_title,tabName)
            holder.setText(R.id.utils_desc,desc)
            holder.setTextColor(R.id.utils_title,CacheUtils.getThemeColor())
        }
    }
}
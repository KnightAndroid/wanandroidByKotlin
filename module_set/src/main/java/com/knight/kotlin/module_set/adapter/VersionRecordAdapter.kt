package com.knight.kotlin.module_set.adapter

import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.knight.kotlin.library_base.util.CacheUtils
import com.knight.kotlin.module_set.R
import com.knight.kotlin.module_set.entity.VersionRecordEntity

/**
 * Author:Knight
 * Time:2022/8/26 10:29
 * Description:VersionRecordAdapter
 */
class VersionRecordAdapter(data:MutableList<VersionRecordEntity>) : BaseQuickAdapter<VersionRecordEntity,BaseViewHolder>(
    R.layout.set_version_record_item,data){
    override fun convert(holder: BaseViewHolder, item: VersionRecordEntity) {
        item.run {
            holder.setText(R.id.tv_appupdate_title,title)
            holder.setTextColor(R.id.tv_appupdate_title,CacheUtils.getThemeColor())
            holder.setText(R.id.tv_appupdate_time,item.publishTime)
            holder.setText(R.id.tv_appupdate_desc,desc)
        }
    }

}
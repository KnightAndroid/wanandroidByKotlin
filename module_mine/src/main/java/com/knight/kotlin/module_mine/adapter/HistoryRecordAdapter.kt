package com.knight.kotlin.module_mine.adapter

import android.graphics.drawable.GradientDrawable
import android.os.Build
import android.text.TextUtils
import android.view.View
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.knight.kotlin.library_base.ktx.toHtml
import com.knight.kotlin.library_base.util.CacheUtils
import com.knight.kotlin.library_database.entity.HistoryReadRecordsEntity
import com.knight.kotlin.library_util.DateUtils

/**
 * Author:Knight
 * Time:2022/5/17 17:32
 * Description:HistoryRecordAdapter
 */
class HistoryRecordAdapter (data:MutableList<HistoryReadRecordsEntity>):
    BaseQuickAdapter<HistoryReadRecordsEntity, BaseViewHolder>(com.knight.kotlin.library_base.R.layout.base_text_item,data) {
    override fun convert(holder: BaseViewHolder, item: HistoryReadRecordsEntity) {
        item.run {
            //作者
            if (!TextUtils.isEmpty(author)) {
                holder.setText(com.knight.kotlin.library_base.R.id.base_item_article_author,author)
            } else {
                holder.setText(com.knight.kotlin.library_base.R.id.base_item_article_author,"不详")
            }
            //一级分类
            if (!TextUtils.isEmpty(chapterName)) {
                holder.setVisible(com.knight.kotlin.library_base.R.id.base_tv_article_superchaptername, true)
                holder.setText(com.knight.kotlin.library_base.R.id.base_tv_article_superchaptername, chapterName)
                holder.setTextColor(com.knight.kotlin.library_base.R.id.base_tv_article_superchaptername, CacheUtils.getThemeColor())
                val gradientDrawable = GradientDrawable()
                gradientDrawable.shape = GradientDrawable.RECTANGLE
                gradientDrawable.setStroke(2, CacheUtils.getThemeColor())
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                    holder.getView<View>(com.knight.kotlin.library_base.R.id.base_tv_article_superchaptername)
                        .setBackground(gradientDrawable)
                } else {
                    holder.getView<View>(com.knight.kotlin.library_base.R.id.base_tv_article_superchaptername)
                        .setBackgroundDrawable(gradientDrawable)
                }
            } else {
                holder.setGone(com.knight.kotlin.library_base.R.id.base_tv_article_superchaptername,true)
            }
            //时间赋值
            if (insertTime != null) {
                holder.setText(com.knight.kotlin.library_base.R.id.base_item_articledata,DateUtils.ConvertYearMonthDayTime(insertTime))
            } else {
                holder.setText(com.knight.kotlin.library_base.R.id.base_item_articledata,"")
            }
            //标题
            holder.setText(com.knight.kotlin.library_base.R.id.base_tv_articletitle,title.toHtml())
            holder.setGone(com.knight.kotlin.library_base.R.id.base_icon_collect,true)
        }

    }
}
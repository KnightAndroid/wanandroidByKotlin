package com.knight.kotlin.module_mine.adapter

import android.content.Context
import android.graphics.drawable.GradientDrawable
import android.os.Build
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.chad.library.adapter4.BaseQuickAdapter
import com.knight.kotlin.library_base.databinding.BaseTextItemBinding
import com.knight.kotlin.library_base.ktx.toHtml
import com.knight.kotlin.library_base.util.CacheUtils
import com.knight.kotlin.library_database.entity.HistoryReadRecordsEntity
import com.knight.kotlin.library_util.DateUtils

/**
 * Author:Knight
 * Time:2022/5/17 17:32
 * Description:HistoryRecordAdapter
 */
class HistoryRecordAdapter:
    BaseQuickAdapter<HistoryReadRecordsEntity, HistoryRecordAdapter.VH>() {

    class VH(
        parent: ViewGroup,
        val binding: BaseTextItemBinding = BaseTextItemBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        ),
    ) : RecyclerView.ViewHolder(binding.root)




    override fun onBindViewHolder(holder: VH, position: Int, item: HistoryReadRecordsEntity?) {
        item?.run {
            //作者
            if (!TextUtils.isEmpty(author)) {
                holder.binding.baseItemArticleAuthor.setText(author)
            } else {
                holder.binding.baseItemArticleAuthor.setText("不详")
            }
            //一级分类
            if (!TextUtils.isEmpty(chapterName)) {
                holder.binding.baseTvArticleSuperchaptername.visibility = View.VISIBLE
                holder.binding.baseTvArticleSuperchaptername.setText(chapterName)
                holder.binding.baseTvArticleSuperchaptername.setTextColor(CacheUtils.getThemeColor())
                val gradientDrawable = GradientDrawable()
                gradientDrawable.shape = GradientDrawable.RECTANGLE
                gradientDrawable.setStroke(2, CacheUtils.getThemeColor())
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                    holder.binding.baseTvArticleSuperchaptername.setBackground(gradientDrawable)
                } else {
                    holder.binding.baseTvArticleSuperchaptername.setBackgroundDrawable(gradientDrawable)
                }
            } else {
                holder.binding.baseTvArticleSuperchaptername.visibility = View.GONE
            }
            //时间赋值
            if (insertTime != null) {
                holder.binding.baseItemArticledate.setText(DateUtils.ConvertYearMonthDayTime(insertTime))
            } else {
                holder.binding.baseItemArticledate.setText("")
            }
            //标题
            holder.binding.baseTvArticletitle.setText(title.toHtml())
            holder.binding.baseIconCollect.visibility = View.GONE
        }
    }

    override fun onCreateViewHolder(context: Context, parent: ViewGroup, viewType: Int): VH {
        return VH(parent)
    }
}
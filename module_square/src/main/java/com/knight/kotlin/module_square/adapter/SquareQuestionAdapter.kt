package com.knight.kotlin.module_square.adapter

import android.graphics.drawable.GradientDrawable
import android.os.Build
import android.text.TextUtils
import android.view.View
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.knight.kotlin.library_base.config.Appconfig
import com.knight.kotlin.library_base.ktx.toHtml
import com.knight.kotlin.library_base.util.CacheUtils
import com.knight.kotlin.library_util.StringUtils
import com.knight.kotlin.module_square.R
import com.knight.kotlin.module_square.entity.SquareQuestionBean

/**
 * Author:Knight
 * Time:2022/4/27 14:02
 * Description:SquareQuestionAdapter
 */
class SquareQuestionAdapter(data:MutableList<SquareQuestionBean>): BaseQuickAdapter<SquareQuestionBean,BaseViewHolder>(
    R.layout.base_text_item,data) {
    override fun convert(holder: BaseViewHolder, item: SquareQuestionBean) {
        item.run {
            //作者
            if (!TextUtils.isEmpty(author)) {
                holder.setText(R.id.base_item_article_author,StringUtils.getStyle(context,author,Appconfig.search_keyword))
            }  else {
                holder.setText(R.id.base_item_article_author,StringUtils.getStyle(context,shareUser,Appconfig.search_keyword))
            }

            //一级分类
            if (!tags.isNullOrEmpty()) {
                holder.setVisible(R.id.base_tv_article_superchaptername, true)
                val gradientDrawable = GradientDrawable()
                gradientDrawable.shape = GradientDrawable.RECTANGLE
                gradientDrawable.setStroke(2, CacheUtils.getThemeColor())
                holder.setText(R.id.base_tv_article_superchaptername, tags.get(0).name)
                holder.setTextColor(R.id.base_tv_article_superchaptername, CacheUtils.getThemeColor())
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                    holder.getView<View>(R.id.base_tv_article_superchaptername).setBackground(gradientDrawable)
                } else {
                    holder.getView<View>(R.id.base_tv_article_superchaptername).setBackgroundDrawable(gradientDrawable)
                }
            } else {
                holder.setGone(R.id.base_tv_article_superchaptername,true)
            }

            //时间赋值
            if (!TextUtils.isEmpty(niceDate)) {
                holder.setText(R.id.base_item_articledata,niceDate)
            } else {
                holder.setText(R.id.base_item_articledata,niceShareDate)
            }
            //标题
            holder.setText(R.id.base_tv_articletitle,StringUtils.getStyle(context,title.toHtml().toString(),Appconfig.search_keyword))
            //是否收藏
            if (collect) {
                holder.setBackgroundResource(R.id.base_icon_collect,R.drawable.base_icon_collect)
            } else {
                holder.setBackgroundResource(R.id.base_icon_collect, R.drawable.base_icon_nocollect)
            }

        }

    }
}
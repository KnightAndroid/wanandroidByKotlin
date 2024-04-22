package com.knight.kotlin.module_square.adapter

import android.graphics.drawable.GradientDrawable
import android.os.Build
import android.text.Html
import android.widget.TextView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.knight.kotlin.library_base.ktx.toHtml
import com.knight.kotlin.library_base.util.CacheUtils
import com.knight.kotlin.module_square.R
import com.knight.kotlin.module_square.entity.SquareArticleEntity

/**
 * Author:Knight
 * Time:2022/2/18 15:35
 * Description:SquareArticleAdapter
 */
class SquareArticleAdapter(data:MutableList<SquareArticleEntity>): BaseQuickAdapter<SquareArticleEntity, BaseViewHolder>(
    R.layout.square_article_item,data) {
    override fun convert(holder: BaseViewHolder, item:SquareArticleEntity) {
        item.run {
            if (!author.isNullOrEmpty()) {
                holder.setText(R.id.square_item_article_author,author)
            } else {
                holder.setText(R.id.square_item_article_author,shareUser)
            }

            //是否是新文章
            if (fresh == true) {
                holder.setVisible(R.id.square_item_article_new_flag,true)
            } else {
                holder.setGone(R.id.square_item_article_new_flag,true)
            }

            //二级分类
            if (!chapterName.isNullOrEmpty()) {
                holder.setVisible(R.id.square_item_article_chaptername,true)
                holder.setText(R.id.square_item_article_chaptername,chapterName)
                val gradientDrawable = GradientDrawable()
                gradientDrawable.shape = GradientDrawable.RECTANGLE
                gradientDrawable.setStroke(2, CacheUtils.getThemeColor())

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                    (holder.getView(R.id.square_item_article_chaptername) as TextView).background = gradientDrawable
                } else {
                    (holder.getView(R.id.square_item_article_chaptername) as TextView).setBackgroundDrawable(gradientDrawable)
                }
                holder.setTextColor(R.id.square_item_article_chaptername,CacheUtils.getThemeColor())
            } else {
                holder.setGone(R.id.square_item_article_chaptername,true)
            }

            //时间赋值
            if (!niceDate.isNullOrEmpty()) {
                holder.setText(R.id.square_item_articledata,niceDate)
            } else {
                holder.setText(R.id.square_item_articledata,niceShareDate)
            }

            //标题
            holder.setText(R.id.square_tv_articletitle,title.toHtml())
            //描述
            if (!desc.isNullOrEmpty()) {
                holder.setVisible(R.id.square_tv_articledesc,true)
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    holder.setText(
                        R.id.square_tv_articledesc,Html.fromHtml(desc,
                        Html.FROM_HTML_MODE_LEGACY))
                } else {
                    holder.setText(R.id.square_tv_articledesc,Html.fromHtml(desc))
                }
            } else {
                holder.setGone(R.id.square_tv_articledesc,true)
            }

            //一级分类
            if (!superChapterName.isNullOrEmpty()) {
                holder.setVisible(R.id.square_tv_article_superchaptername,true)
                holder.setText(R.id.square_tv_article_superchaptername,superChapterName)
            } else {
                holder.setGone(R.id.square_tv_article_superchaptername,true)
            }

            //是否有收藏
            if (collect == true) {
                holder.setBackgroundResource(R.id.square_icon_collect,com.knight.kotlin.library_base.R.drawable.base_icon_collect)
            } else {
                holder.setBackgroundResource(R.id.square_icon_collect,com.knight.kotlin.library_base.R.drawable.base_icon_nocollect)
            }
        }
    }
}
package com.knight.kotlin.module_square.adapter

import android.graphics.drawable.GradientDrawable
import android.os.Build
import android.text.TextUtils
import android.view.View
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.knight.kotlin.library_base.ktx.toHtml
import com.knight.kotlin.library_base.util.CacheUtils
import com.knight.kotlin.module_square.R
import com.knight.kotlin.module_square.entity.SquareShareArticleBean

/**
 * Author:Knight
 * Time:2022/4/26 15:53
 * Description:SquareShareArticleAdapter
 */
class SquareShareArticleAdapter(data:MutableList<SquareShareArticleBean>):BaseQuickAdapter<SquareShareArticleBean,BaseViewHolder>(
    R.layout.square_articles,data) {
    override fun convert(holder: BaseViewHolder, item: SquareShareArticleBean) {
        item.run {
            //作者
            if (!TextUtils.isEmpty(item.author)) {
                holder.setText(R.id.square_item_articleauthor,author)
            } else {
                holder.setText(R.id.square_item_articleauthor,shareUser)
            }
            val gradientDrawable = GradientDrawable()
            gradientDrawable.shape = GradientDrawable.RECTANGLE
            gradientDrawable.setStroke(2, CacheUtils.getThemeColor())
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                holder.getView<View>(R.id.square_item_articlechaptername).background = gradientDrawable
            } else {
                holder.getView<View>(R.id.square_item_articlechaptername).setBackgroundDrawable(gradientDrawable)
            }

            //二级分类
            if (!TextUtils.isEmpty(chapterName)) {
                holder.setVisible(R.id.square_item_articlechaptername,true)
                holder.setText(R.id.square_item_articlechaptername,chapterName)
                holder.setTextColor(R.id.square_item_articlechaptername,CacheUtils.getThemeColor())
            } else {
                holder.setGone(R.id.square_item_articlechaptername,true)
            }

            //时间赋值
            if (!TextUtils.isEmpty(niceDate)) {
                holder.setText(R.id.square_item_articledata,niceDate)
            } else {
                holder.setText(R.id.square_item_articledata,niceShareDate)
            }

            //标题
            holder.setText(R.id.square_tv_articletitle,title.toHtml())
            if (collect) {
                holder.setBackgroundResource(R.id.square_icon_collect, com.knight.kotlin.library_base.R.drawable.base_icon_collect)
            } else {
                holder.setBackgroundResource(R.id.square_icon_collect, com.knight.kotlin.library_base.R.drawable.base_icon_nocollect)
            }

            //是否是新文章
            if (fresh) {
                holder.setVisible(R.id.square_item_articlenew,true)
            } else {
                holder.setGone(R.id.square_item_articlenew,true)
            }
        }

    }
}
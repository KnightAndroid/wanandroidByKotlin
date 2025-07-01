package com.knight.kotlin.module_square.adapter

import android.content.Context
import android.graphics.drawable.GradientDrawable
import android.os.Build
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.chad.library.adapter4.BaseQuickAdapter
import com.core.library_base.ktx.toHtml
import com.core.library_base.util.CacheUtils
import com.knight.kotlin.module_square.databinding.SquareArticlesBinding
import com.knight.kotlin.module_square.entity.SquareShareArticleBean

/**
 * Author:Knight
 * Time:2022/4/26 15:53
 * Description:SquareShareArticleAdapter
 */
class SquareShareArticleAdapter:
    BaseQuickAdapter<SquareShareArticleBean, SquareShareArticleAdapter.VH>() {


    class VH(
        parent: ViewGroup,
        val binding: SquareArticlesBinding = SquareArticlesBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        ),
    ) : RecyclerView.ViewHolder(binding.root)

    override fun onBindViewHolder(holder: VH, position: Int, item: SquareShareArticleBean?) {
        item?.run {
            //作者
            if (!TextUtils.isEmpty(item.author)) {
                holder.binding.squareItemArticleauthor.setText(author)
            } else {
                holder.binding.squareItemArticleauthor.setText(shareUser)
            }
            val gradientDrawable = GradientDrawable()
            gradientDrawable.shape = GradientDrawable.RECTANGLE
            gradientDrawable.setStroke(2, CacheUtils.getThemeColor())
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                holder.binding.squareItemArticlechaptername.background = gradientDrawable
            } else {
                holder.binding.squareItemArticlechaptername.setBackgroundDrawable(gradientDrawable)
            }

            //二级分类
            if (!TextUtils.isEmpty(chapterName)) {
                holder.binding.squareItemArticlechaptername.visibility = View.VISIBLE
                holder.binding.squareItemArticlechaptername.setText(chapterName)
                holder.binding.squareItemArticlechaptername.setTextColor(CacheUtils.getThemeColor())
            } else {
                holder.binding.squareItemArticlechaptername.visibility = View.GONE
            }

            //时间赋值
            if (!TextUtils.isEmpty(niceDate)) {
                holder.binding.squareItemArticledate.setText(niceDate)
            } else {
                holder.binding.squareItemArticledate.setText(niceShareDate)
            }

            //标题
            holder.binding.squareTvArticletitle.setText(title.toHtml())
            if (collect) {
                holder.binding.squareIconCollect.setBackgroundResource(com.knight.kotlin.library_base.R.drawable.base_icon_collect)
            } else {
                holder.binding.squareIconCollect.setBackgroundResource(com.knight.kotlin.library_base.R.drawable.base_icon_nocollect)
            }

            //是否是新文章
            if (fresh) {
                holder.binding.squareItemArticlenew.visibility = View.VISIBLE
            } else {
                holder.binding.squareItemArticlenew.visibility = View.GONE
            }
        }
    }

    override fun onCreateViewHolder(context: Context, parent: ViewGroup, viewType: Int): VH {
        return VH(parent)
    }
}
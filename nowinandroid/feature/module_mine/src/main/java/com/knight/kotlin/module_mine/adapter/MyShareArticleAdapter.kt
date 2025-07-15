package com.knight.kotlin.module_mine.adapter

import android.content.Context
import android.graphics.drawable.GradientDrawable
import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.chad.library.adapter4.BaseQuickAdapter
import com.core.library_base.ktx.toHtml
import com.knight.kotlin.library_common.util.CacheUtils
import com.knight.kotlin.module_mine.databinding.MineSharearticlesItemBinding
import com.knight.kotlin.module_mine.entity.MyArticleEntity

/**
 * Author:Knight
 * Time:2022/5/17 11:17
 * Description:MyShareArticleAdapter
 */
class MyShareArticleAdapter:
    BaseQuickAdapter<MyArticleEntity, MyShareArticleAdapter.VH>() {

    class VH(
        parent: ViewGroup,
        val binding: MineSharearticlesItemBinding = MineSharearticlesItemBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        ),
    ) : RecyclerView.ViewHolder(binding.root)


    override fun onBindViewHolder(holder: VH, position: Int, item: MyArticleEntity?) {
        item?.run {
            //作者
            holder.binding.mineTvShareArticleauthor.setText(shareUser)
            //时间
            holder.binding.mineTvShareArticledate.setText(niceDate)
            //标题
            holder.binding.mineTvShareArticletitle.setText(title.toHtml())
            //文章类别
            if (chapterName.isNullOrEmpty()) {
                holder.binding.mineTvShareArticlechaptername.setText(superChapterName)
            } else {
                holder.binding.mineTvShareArticlechaptername.setText("$superChapterName/$chapterName")

            }
            val gradientDrawable = GradientDrawable()
            gradientDrawable.shape = GradientDrawable.RECTANGLE
            gradientDrawable.setStroke(2, CacheUtils.getThemeColor())
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                holder.binding.mineTvShareArticlechaptername.setBackground(gradientDrawable)
            } else {
                holder.binding.mineTvShareArticlechaptername.setBackgroundDrawable(gradientDrawable)
            }
            holder.binding.mineTvShareArticlechaptername.setTextColor(CacheUtils.getThemeColor())
            //是否是新文章
            if (fresh) {
                holder.binding.mineTvShareArticlenew.visibility = View.VISIBLE
            } else {
                holder.binding.mineTvShareArticlenew.visibility = View.GONE
            }
        }
    }

    override fun onCreateViewHolder(context: Context, parent: ViewGroup, viewType: Int): VH {
        return VH(parent)
    }
}
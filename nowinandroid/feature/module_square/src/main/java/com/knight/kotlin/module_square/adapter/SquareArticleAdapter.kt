package com.knight.kotlin.module_square.adapter

import android.content.Context
import android.graphics.drawable.GradientDrawable
import android.os.Build
import android.text.Html
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.chad.library.adapter4.BaseQuickAdapter
import com.core.library_base.ktx.toHtml
import com.core.library_common.util.CacheUtils
import com.knight.kotlin.module_square.databinding.SquareArticleItemBinding
import com.knight.kotlin.module_square.entity.SquareArticleEntity

/**
 * Author:Knight
 * Time:2022/2/18 15:35
 * Description:SquareArticleAdapter
 */
class SquareArticleAdapter(data:List<SquareArticleEntity>): BaseQuickAdapter<SquareArticleEntity, SquareArticleAdapter.VH>() {

    class VH(
        parent: ViewGroup,
        val binding: SquareArticleItemBinding = SquareArticleItemBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        ),
    ) : RecyclerView.ViewHolder(binding.root)

    override fun onBindViewHolder(holder: VH, position: Int, item: SquareArticleEntity?) {
        item?.run {
            if (!author.isNullOrEmpty()) {
                holder.binding.squareItemArticleAuthor.setText(author)
            } else {
                holder.binding.squareItemArticleAuthor.setText(shareUser)
            }

            //是否是新文章
            if (fresh == true) {
                holder.binding.squareItemArticleNewFlag.visibility = View.VISIBLE
            } else {
                holder.binding.squareItemArticleNewFlag.visibility = View.GONE
            }

            //二级分类
            if (!chapterName.isNullOrEmpty()) {
                holder.binding.squareItemArticleChaptername.visibility = View.VISIBLE
                holder.binding.squareItemArticleChaptername.setText(chapterName)
                val gradientDrawable = GradientDrawable()
                gradientDrawable.shape = GradientDrawable.RECTANGLE
                gradientDrawable.setStroke(2, CacheUtils.getThemeColor())

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                    holder.binding.squareItemArticleChaptername.background = gradientDrawable
                } else {
                    holder.binding.squareItemArticleChaptername.setBackgroundDrawable(gradientDrawable)
                }
                holder.binding.squareItemArticleChaptername.setTextColor(CacheUtils.getThemeColor())
            } else {
                holder.binding.squareItemArticleChaptername.visibility = View.GONE
            }

            //时间赋值
            if (!niceDate.isNullOrEmpty()) {
                holder.binding.squareItemArticledate.setText(niceDate)
            } else {
                holder.binding.squareItemArticledate.setText(niceShareDate)
            }

            //标题
            holder.binding.squareTvArticletitle.setText(title.toHtml())
            //描述
            if (!desc.isNullOrEmpty()) {
                holder.binding.squareLlArticleDesc.visibility = View.VISIBLE
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    holder.binding.squareTvArticledesc.setText(
                        Html.fromHtml(desc,
                            Html.FROM_HTML_MODE_LEGACY))
                } else {
                    holder.binding.squareTvArticledesc.setText(Html.fromHtml(desc))
                }
            } else {
                holder.binding.squareTvArticledesc.visibility = View.GONE
            }

            //一级分类
            if (!superChapterName.isNullOrEmpty()) {
                holder.binding.squareTvArticleSuperchaptername.visibility = View.VISIBLE
                holder.binding.squareTvArticleSuperchaptername.setText(superChapterName)
            } else {
                holder.binding.squareTvArticleSuperchaptername.visibility = View.GONE
            }

            //是否有收藏
            if (collect == true) {
                holder.binding.squareIconCollect.setBackgroundResource(com.core.library_base.R.drawable.base_icon_collect)
            } else {
                holder.binding.squareIconCollect.setBackgroundResource(com.core.library_base.R.drawable.base_icon_nocollect)
            }
        }
    }

    override fun onCreateViewHolder(context: Context, parent: ViewGroup, viewType: Int): VH {
        return VH(parent)
    }
}
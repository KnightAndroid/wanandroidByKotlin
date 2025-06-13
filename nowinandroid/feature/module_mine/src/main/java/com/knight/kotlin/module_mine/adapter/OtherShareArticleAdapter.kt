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
import com.knight.kotlin.library_base.config.Appconfig
import com.knight.kotlin.library_base.databinding.BaseTextItemBinding
import com.knight.kotlin.library_base.ktx.toHtml
import com.knight.kotlin.library_base.util.CacheUtils
import com.knight.kotlin.library_util.StringUtils
import com.knight.kotlin.module_mine.databinding.MineItemBinding
import com.knight.kotlin.module_mine.entity.MyArticleEntity

/**
 * Author:Knight
 * Time:2022/5/7 16:39
 * Description:OtherShareArticleAdapter
 */
class OtherShareArticleAdapter:
    BaseQuickAdapter<MyArticleEntity, OtherShareArticleAdapter.VH>() {

    class VH(
        parent: ViewGroup,
        val binding: BaseTextItemBinding = BaseTextItemBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        ),
    ) : RecyclerView.ViewHolder(binding.root)


    override fun onBindViewHolder(holder: VH, position: Int, item: MyArticleEntity?) {
        item?.run {
            //作者
            if (!TextUtils.isEmpty(author)) {
                holder.binding.baseItemArticleAuthor.setText(StringUtils.getStyle(context,author,Appconfig.search_keyword))
            } else {
                holder.binding.baseItemArticleAuthor.setText(StringUtils.getStyle(context,shareUser,Appconfig.search_keyword))
            }

            //一级分类
            if (!TextUtils.isEmpty(superChapterName) || !TextUtils.isEmpty(chapterName)) {
                holder.binding.baseTvArticleSuperchaptername.visibility = View.VISIBLE
                val gradientDrawable = GradientDrawable()
                gradientDrawable.shape = GradientDrawable.RECTANGLE
                gradientDrawable.setStroke(2, CacheUtils.getThemeColor())
                if (!TextUtils.isEmpty(superChapterName)) {
                    if (!TextUtils.isEmpty(chapterName)) {
                        holder.binding.baseTvArticleSuperchaptername.setText(
                            superChapterName + "/" + chapterName
                        )
                    } else {
                        holder.binding.baseTvArticleSuperchaptername.setText(superChapterName)
                    }
                } else {
                    if (!TextUtils.isEmpty(chapterName)) {holder.binding.baseTvArticleSuperchaptername.setText( chapterName)
                    } else {
                        holder.binding.baseTvArticleSuperchaptername.setText("")
                    }
                }
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                    holder.binding.baseTvArticleSuperchaptername.setBackground(gradientDrawable)
                } else {
                    holder.binding.baseTvArticleSuperchaptername.setBackgroundDrawable(gradientDrawable)
                }
            } else {
                holder.binding.baseTvArticleSuperchaptername.visibility = View.GONE
            }
            //时间赋值
            if (!TextUtils.isEmpty(niceDate)) {
                holder.binding.baseItemArticledate.setText(niceDate)
            } else {
                holder.binding.baseItemArticledate.setText(niceShareDate)
            }
            //标题
            holder.binding.baseTvArticletitle.setText(StringUtils.getStyle(context,title.toHtml().toString(),Appconfig.search_keyword))
            //是否收藏
            if (collect) {
                holder.binding.baseIconCollect.setBackgroundResource(com.knight.kotlin.library_base.R.drawable.base_icon_collect)
            } else {
                holder.binding.baseIconCollect.setBackgroundResource(com.knight.kotlin.library_base.R.drawable.base_icon_nocollect)
            }


        }
    }

    override fun onCreateViewHolder(context: Context, parent: ViewGroup, viewType: Int): VH {
        return VH(parent)
    }
}